package com.djr.commonlibrary.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * 文件操作工具类
 */
public class FileUtils {

    public static final String TAG = "FileUtils";

    /**
     * 计算某一file的大小，单位为MB
     *
     * @param file 文件
     * @return 文件或文件夹大小，单位为byte
     */
    public static long getSize(File file) {
        if (file.exists()) {
            if (!file.isFile()) {
                File[] fl = file.listFiles();
                long ss = 0;
                for (File f : fl)
                    ss += getSize(f);
                return ss;
            } else {
                return file.length();
            }
        } else {
            L.e(TAG, "File Not Exist");
            return 0;
        }
    }

    /**
     * 计算某一路径下的文件大小
     *
     * @param path 某文件的绝对路径
     * @return 文件大小，单位为Byte
     */
    public static long getSize(String path) {
        File file = new File(path);
        return getSize(file);
    }

    /**
     * 得到某一文件的大小字符串
     *
     * @param file 文件
     * @return 大小字符串
     */
    public static String getSizeString(File file) {
        long size = getSize(file);
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) size / (float) (1024 * 1024));
        if (f < 1.0) {
            float f2 = ((float) size / (float) (1024));
            return df.format(Float.valueOf(f2).doubleValue()) + "KB";
        } else {
            return df.format(Float.valueOf(f).doubleValue()) + "M";
        }

    }

    /**
     * 删除某一文件
     *
     * @param file 文件
     */
    public static void deleteFile(File file) {

        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }
            for (File childFile : childFiles) {
                deleteFile(childFile);
            }
            file.delete();
        }
    }

    /**
     * 删除某一路径下的文件或文件夹
     *
     * @param path 待删除路径
     */
    public static void deleteFile(String path) {
        File file = new File(path);
        deleteFile(file);
    }

    public static String getNameFromUrl(String url) {
        return url.substring(url.lastIndexOf('/') + 1, url.length());
    }

    public static boolean isLocalExistFile(String dir, String fileName) {
        File f = new File(dir + fileName);
        return f.exists() && !f.isDirectory();
    }

    public static File getFile(String fileName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + "commonlibrary");
            if (!file.exists()) {
                file.mkdir();
            }
            File tempFile = new File(file, fileName);
            try {
                tempFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tempFile;
        }
        return null;
    }
}