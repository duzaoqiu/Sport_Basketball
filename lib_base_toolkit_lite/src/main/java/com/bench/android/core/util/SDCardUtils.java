package com.bench.android.core.util;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.File;

/**
 * SD卡帮助类
 *
 * @author zhouyi
 */
public class SDCardUtils {


    /**
     * 获取app存储内容路径
     *
     * @param context
     * @return
     */
    public static synchronized String getRootPath(Context context) {
        String rootPath = null;
        try {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)
                    && hasEnoughSpaceOnSDCard()) {
                rootPath = getRootPathWithSdcard(context);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rootPath;
    }

    /**
     * 是否充足的空间
     *
     * @return
     */
    private static boolean hasEnoughSpaceOnSDCard() {
        /**
         * 目前只检查极限值 容量不为0的
         */
        final long MIN_SPACE = 0;
        File file = Environment.getExternalStorageDirectory();
        if (!file.exists() || !file.canWrite() || !file.canRead()) {
            LogUtils.i("file is not exists");
            return false;
        }
        StatFs stat = new StatFs(file.getAbsolutePath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        LogUtils.i("space : " + blockSize * availableBlocks);
        return blockSize * availableBlocks > MIN_SPACE;
    }

    /**
     * @param context
     * @return
     */
    private static String getRootPathWithSdcard(Context context) {
        StringBuilder builder = new StringBuilder();
        File dirFile = Environment.getExternalStorageDirectory();
        if (dirFile == null || !dirFile.canWrite()) {
            return null;
        }

        builder.append(dirFile.getAbsolutePath())
                .append(File.separator)
                .append(context.getPackageName())
                .append(File.separator)
                .append("download");
        File file = new File(builder.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        if (!file.canWrite()) {
            return null;
        }
        return file.getAbsolutePath();
    }

}
