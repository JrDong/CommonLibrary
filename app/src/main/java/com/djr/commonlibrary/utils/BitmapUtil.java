package com.djr.commonlibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 */
public class BitmapUtil {
    private BitmapUtil() {
    }


    /**
     * 矩形位图剪切圆形
     *
     * @param bitmap
     * @param radius
     * @return
     */
    public static Bitmap getRoundBitmap(Bitmap bitmap, int radius) {
        //压缩图片大小
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        int r1 = 0;
        if (bitmapWidth > bitmapHeight) {
            r1 = bitmapHeight;
        } else {
            r1 = bitmapWidth;
        }
        float scaled = (float) radius / r1;
        Matrix matrix = new Matrix();
        matrix.postScale(scaled, scaled);
        // 产生缩放后的Bitmap对象
        Bitmap resizeBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, false);

        int width = resizeBitmap.getWidth();
        int height = resizeBitmap.getHeight();
        Bitmap backgroundBmp = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backgroundBmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        RectF rect = new RectF(0, 0, radius, radius);
        canvas.drawRoundRect(rect, radius / 2, radius / 2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(resizeBitmap, null, rect, paint);
        return backgroundBmp;
        /*int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int r = 0;
        if(width > height) {
            r = height;
        } else {
            r = width;
        }
        Bitmap backgroundBmp = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(backgroundBmp);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        RectF rect = new RectF(0, 0, r, r);
        canvas.drawRoundRect(rect, r/2, r/2, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, null, rect, paint);
        return backgroundBmp;*/
    }

    /**
     * @param imagefile
     * @return
     */
    private static Options getBitmapFactoryOpinions(String imagefile) {
        L.e(imagefile);
        Options options = new Options();
        options.inDither = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        options.inSampleSize = 1;
        //如果缩放计算缩放倍数，设置inSampleSize
        int maxHeight = 2048;
        int maxWidth = 2048;
        options.inJustDecodeBounds = true;//不加载bitmap到内存中
        BitmapFactory.decodeFile(imagefile, options);
        int outWidth = options.outWidth;
        int outHeight = options.outHeight;
        if (outWidth != 0 && outHeight != 0 && maxWidth != 0 && maxHeight != 0) {
            int sampleSize = (outWidth / maxWidth + outHeight / maxHeight) / 2;
            L.d("sampleSize = " + sampleSize);
            options.inSampleSize = sampleSize + 1;
        }
        options.inJustDecodeBounds = false;
        return options;
    }

    // 保存bitmap到文件
    public static File saveBitmap(Bitmap bitmap, String filePath) {
        L.e("saveBitmap", filePath);
        FileOutputStream fOut = null;
        File f = new File(filePath);
        try {
            if (f.exists()) {
                f.delete();
            }
            fOut = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return f;
    }

    // 保存uri到文件
    public static void saveToFile(Uri uri, String filePath, Context context) {
        InputStream is = null;
        FileOutputStream fos = null;
        try {
            is = context.getContentResolver().openInputStream(uri);
            File file = new File(filePath);
            //检测Uri和filepath是否为同一文件
            if (file.toURI().getPath().equals(uri.getPath())) {
                return;
            }
            if (file.exists()) {
                file.delete();
            }
            fos = new FileOutputStream(file);
            byte[] buf = new byte[2024];
            int len = 0;

            // 将读取后的数据放置在内存中---ByteArrayOutputStream
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fos)
                    fos.close();
                if (null != is)
                    is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // 根据Uri地址获取bitmap
    public static Bitmap getBitmapFromUri(Uri uri, String tempFilePath, Context context) {
        saveToFile(uri, tempFilePath, context);
        Options options = getBitmapFactoryOpinions(tempFilePath);

        Bitmap bitmap = BitmapFactory.decodeFile(tempFilePath, options);

        int degree = readPictureDegree(tempFilePath);
        if (degree != 0) {
            bitmap = rotaingImageView(degree, bitmap);
        }
        //new File(tempFilePath).delete();
        return bitmap;
    }

    /**
     * 旋转图片
     *
     * @param degree
     * @param bitmap
     * @return Bitmap
     */
    private static Bitmap rotaingImageView(int degree, Bitmap bitmap) {
        //旋转图片 动作     
        Matrix matrix = new Matrix();
        ;
        matrix.postRotate(degree);
        // 创建新的图片     
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 读取图片属性：旋转的角度
     *
     * @param path 图片绝对路径
     * @return degree旋转的角度
     */
    private static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    //使用Bitmap加Matrix来缩放
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h)
    {
        Bitmap BitmapOrg = bitmap;
        int width = BitmapOrg.getWidth();
        int height = BitmapOrg.getHeight();
        int newWidth = w;
        int newHeight = h;

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // if you want to rotate the Bitmap
        // matrix.postRotate(45);
        Bitmap resizedBitmap = Bitmap.createBitmap(BitmapOrg, 0, 0, width,
                height, matrix, true);
        return resizedBitmap;
    }
}
