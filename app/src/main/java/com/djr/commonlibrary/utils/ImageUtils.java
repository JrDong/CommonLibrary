package com.djr.commonlibrary.utils;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 */
public class ImageUtils {

    public static final String ACTION_GET_PHOTO_FROM_CAMERA = "android.media.action.IMAGE_CAPTURE";

    public static final int INTENT_REQUEST_CODE_CAMERA = 0;
    public static final int INTENT_REQUEST_CODE_GALLERY = 1;
    public static final int INTENT_REQUEST_CODE_CUT = 2;

    public static final String INTENT_KEY_PHOTO_DATA = "data";
    public static final String GALLERY_DISPLAY_TYPE = "image/*";

    public static Intent getImageFileIntent(String fullPath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Uri uri = Uri.fromFile(new File(fullPath));
        intent.setDataAndType(uri, "image/*");
        return intent;
    }

    public static Intent getImageFileIntent(String dir, String name) {
        if (!dir.endsWith("/")) dir += "/";
        return getImageFileIntent(dir + name);
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static void getPhotoFromGallery(Activity context) {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(intent, INTENT_REQUEST_CODE_GALLERY);
    }

    public static void getPhotoFromCamera(Activity context, File tempFile) {
        Intent i = new Intent();
        i.setAction(ACTION_GET_PHOTO_FROM_CAMERA);
        i.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        context.startActivityForResult(i, INTENT_REQUEST_CODE_CAMERA);
    }

    public static void getPhotoFromCamera(Activity context) {
        Intent i = new Intent();
        i.setAction(ACTION_GET_PHOTO_FROM_CAMERA);
        context.startActivityForResult(i, INTENT_REQUEST_CODE_CAMERA);
    }

    public static void cropForAvatar(Activity context, Uri targetUri, Uri tempUri) {
        if (targetUri == null) return;
        Intent i = new Intent("com.android.camera.action.CROP");
        i.setDataAndType(targetUri, "image/*");
        i.putExtra("cropForAvatar", "true");
        i.putExtra("aspectX", 1);
        i.putExtra("aspectY", 1);
        i.putExtra("outputX", 250);
        i.putExtra("outputY", 250);
        i.putExtra("outputFormat", "JPEG");
        i.putExtra("noFaceDetection", true);
        i.putExtra(MediaStore.EXTRA_OUTPUT, tempUri);
        context.startActivityForResult(i, INTENT_REQUEST_CODE_CUT);
    }

    public static String saveBitmapAsPNG(Bitmap bitmap, String filePath, String fileName) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) L.d("Directory doesn't exist, make directory: " + file.mkdirs());

        file = new File(filePath, fileName + ".png");
        if (file.exists()) L.d("File exists, delete it: " + file.delete());

        FileOutputStream out = new FileOutputStream(file);
        try {
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
                L.d("-->Compress success" + file.getAbsolutePath());
                return file.getAbsolutePath();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
