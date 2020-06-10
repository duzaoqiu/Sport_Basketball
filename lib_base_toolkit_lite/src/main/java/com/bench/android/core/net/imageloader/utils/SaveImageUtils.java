package com.bench.android.core.net.imageloader.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.bench.android.core.app.application.BaseApplication;
import com.bench.android.core.graphics.BitmapUtils;
import com.bench.android.core.util.LogUtils;

import java.io.File;
import java.io.IOException;

/**
 * @author Created by zaozao on 2019/2/21.
 * 作用：保存图片到本地
 * <p>
 * <p>
 * .Android开发:filePath放在哪个文件夹
 * Environment.getDataDirectory() = /data
 * Environment.getDownloadCacheDirectory() = /cache
 * Environment.getExternalStorageDirectory() = /mnt/sdcard
 * Environment.getExternalStoragePublicDirectory(“test”) = /mnt/sdcard/test
 * Environment.getRootDirectory() = /system
 * getPackageCodePath() = /data/app/com.my.app-1.apk
 * getPackageResourcePath() = /data/app/com.my.app-1.apk
 * getCacheDir() = /data/data/com.my.app/cache
 * getDatabasePath(“test”) = /data/data/com.my.app/databases/test
 * getDir(“test”, Context.MODE_PRIVATE) = /data/data/com.my.app/app_test
 * getExternalCacheDir() = /mnt/sdcard/Android/data/com.my.app/cache
 * getExternalFilesDir(“test”) = /mnt/sdcard/Android/data/com.my.app/files/test
 * getExternalFilesDir(null) = /mnt/sdcard/Android/data/com.my.app/files
 * getFilesDir() = /data/data/com.my.app/files
 */

/**
 * 需要修改
 */
public class SaveImageUtils {
    /**
     * 拍照路径
     */
    private static String FILE_NAME = "duzaoqiu";


    private static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /**
     *  应用程序在运行的过程中如果需要向手机上保存数据，一般是把数据保存在SDcard中的。
     * 大部分应用是直接在SDCard的根目录下创建一个文件夹，然后把数据保存在该文件夹中。
     * 这样当该应用被卸载后，这些数据还保留在SDCard中，留下了垃圾数据。
     * 如果你想让你的应用被卸载后，与该应用相关的数据也清除掉，该怎么办呢？
     * 通过Context.getExternalFilesDir()方法可以获取到 SDCard/Android/data/你的应用的包名/files/ 目录，
     * 一般放一些长时间保存的数据
     * 通过Context.getExternalCacheDir()方法可以获取到 SDCard/android/data/你的应用包名/cache/目录，
     * 一般存放临时缓存数据.如果使用上面的方法，当你的应用在被用户卸载后，
     * SDCard/Android/data/你的应用的包名/ 这个目录下的所有文件都会被删除，不会留下垃圾信息。
     * 而且上面二个目录分别对应 设置->应用->应用详情里面的”清除数据“与”清除缓存“选项,如果要保存下载的内容，就不要放在以上目录下
     * getCacheDir()方法用于获取/data/data//cache目录
     * getFilesDir()方法用于获取/data/data//files目录
     *
     * @return
     */
    public static File getAvailableCacheDir() {
        if (isExternalStorageWritable()) {
            return BaseApplication.getContext().getExternalCacheDir();
        } else {
            return BaseApplication.getContext().getCacheDir();
        }
    }

    /**
     * 较优秀的程序都会专门写一个方法来获取缓存地址，如下所示：
     * 自己加的  皂皂
     *  可以看到，当SD卡存在或者SD卡不可被移除的时候，就调用getExternalCacheDir()方法来获取缓存路径，
     * 否则就调用getCacheDir()方法来获取缓存路径。前者获取到的就是 /sdcard/Android/data//cache 这个路径，
     * 而后者获取到的是 /data/data//cache 这个路径。
     *  注意：这两种方式的缓存都会在卸载app的时候被系统清理到，而开发者自己在sd卡上建立的缓存文件夹，是不会跟随着app的卸载而被清除掉的。
     */
    public static String getDiskCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
            LogUtils.e("kkkkkk---getExternalCacheDir", cachePath);
        } else {
            cachePath = context.getCacheDir().getPath();
            LogUtils.e("kkkkkk---getCacheDir", cachePath);
        }


        return cachePath;
    }


    public static String getAvatarCropPath() {
        return new File(getAvailableCacheDir(), FILE_NAME).getAbsolutePath();
    }

    public static String getPublishPath(String fileName) {
        return new File(getAvailableCacheDir(), fileName).getAbsolutePath();
    }


    public static void deleteDir(File directory) {
        if (directory != null) {
            if (directory.isFile()) {
                directory.delete();
                return;
            }

            if (directory.isDirectory()) {
                File[] childFiles = directory.listFiles();
                if (childFiles == null || childFiles.length == 0) {
                    directory.delete();
                    return;
                }

                for (int i = 0; i < childFiles.length; i++) {
                    deleteDir(childFiles[i]);
                }
                directory.delete();
            }
        }
    }


    public static File getDCIMFile(String filePath, String imageName) {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) { // 文件可用
            File dirs = new File(Environment.getExternalStorageDirectory(), "DCIM" + filePath);
            if (!dirs.exists()) {
                dirs.mkdirs();
            }
            File file = new File(Environment.getExternalStorageDirectory(), "DCIM" + filePath + imageName);
            if (!file.exists()) {
                try {
                    //在指定的文件夹中创建文件
                    file.createNewFile();
                } catch (Exception e) {

                }
            }
            return file;
        } else {
            return null;
        }

    }

    /**
     * 保存图片
     *
     * @param bitmap
     */
    public static void saveBitmap(Bitmap bitmap, File imgFile) {
        BitmapUtils.saveBitmap(bitmap,imgFile);
    }

    public static File getBaseFile(String filePath) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) { // 文件可用
            File f = new File(Environment.getExternalStorageDirectory(),
                    filePath);
            if (!f.exists()) {
                f.mkdirs();
            }
            return f;
        } else {
            return null;
        }
    }

    public static String getFileName() {
        String fileName = FILE_NAME;
        return fileName;
    }

    /**
     * 由指定的路径和文件名创建文件
     */
    public static File createFile(String path, String name) throws IOException {
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdirs();
        }
        File file = new File(path + name);
        if (!file.exists()) {
            file.createNewFile();
        }
        return file;
    }


    public interface Callback {
        void callback();
    }
}
