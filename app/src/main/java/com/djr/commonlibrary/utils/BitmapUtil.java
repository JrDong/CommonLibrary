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
import android.view.Display;
import android.view.WindowManager;

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
    public static Bitmap resizeImage(Bitmap bitmap, int w, int h) {
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

    /**
     * 通过文件路径 得到适配屏幕宽高的bitmap
     */
    public static Bitmap getScaleBitmap(Context context, String filePath) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        return getScaleBitmap(filePath, screenWidth, screenHeight);
    }

    /**
     * 通过文件路径得到适应宽高的bitmap
     *
     * @param filePath 文件路径
     * @param width    需要适配的宽度
     * @param height   需要适配的高度
     * @return 适配后的bitmap
     */
    public static Bitmap getScaleBitmap(String filePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(filePath, options);

        options.inSampleSize = calculateInSampleSize(options, width, height);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(filePath, options);
    }

    /**
     * 得到适配屏幕的bitmap
     */
    public static Bitmap getScaleBitmap(Context context, Uri uri) {

        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();

        return getScaleBitmap(context, uri, screenWidth, screenHeight);
    }

    /**
     * 通过Uri 得到适配宽高的Bitmap
     * <p>
     * 需要get两次InputStream是因为,第一次取图片尺寸的时候is这个InputStream被使用过了，
     * 再真正取图片的时候又使用了这个InputStream，此时流的起始位置已经被移动过了
     *
     * @param width  需适应的宽度
     * @param height 需适应的高度
     */
    public static Bitmap getScaleBitmap(Context context, Uri uri, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inJustDecodeBounds = true;

        Bitmap bitmap = null;
        InputStream is1 = null;
        InputStream is2 = null;
        try {
            is1 = context.getContentResolver().openInputStream(uri);
            BitmapFactory.decodeStream(is1
                    , null, options);
            options.inSampleSize = calculateInSampleSize(options, width, height);
            options.inJustDecodeBounds = false;
            is2 = context.getContentResolver().openInputStream(uri);
            bitmap = BitmapFactory.decodeStream(is2
                    , null, options);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is1 != null) {
                try {
                    is1.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (is2 != null) {
                try {
                    is2.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return bitmap;
    }

    private static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                //设置inSampleSize为2的幂是因为解码器最终还是会对非2的幂的数进行向下处理，
                // 将inSampleSize进行四舍五入,获取到最接近2的幂的数。详情参考inSampleSize的文档。
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
