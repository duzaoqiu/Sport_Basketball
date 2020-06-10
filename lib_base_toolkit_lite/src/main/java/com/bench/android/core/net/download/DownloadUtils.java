package com.bench.android.core.net.download;

import android.content.Context;

import androidx.annotation.NonNull;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

/**
 * Author: Aliao
 * Date: 2019/5/17
 * Description: 下载工具类，简单封装
 */
public class DownloadUtils {

    /**
     *
     * @param context 使用前先初始化
     */
    public static void setup(Context context) {
        FileDownloader.setup(context);
    }

    /**
     *
     * @param url 下载链接
     * @param path 保存路径
     */
    public static void download(String url, String path) {
        FileDownloader.getImpl().create(url).setPath(path, false).start();
    }

    /**
     *
     * @param url 下载链接
     * @param path 保存路径
     * @param listener 下载监听
     */
    public static void download(String url, String path, @NonNull final DownloadListener listener) {
        final DownloadTaskProxy taskProxy = new DownloadTaskProxy();
        BaseDownloadTask task = FileDownloader.getImpl().create(url).setPath(path, true).setListener(new FileDownloadListener() {
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                listener.pending(taskProxy, soFarBytes, totalBytes);
            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                listener.progress(taskProxy, soFarBytes, totalBytes);
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                listener.completed(taskProxy);
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                listener.paused(taskProxy, soFarBytes, totalBytes);
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                listener.error(taskProxy, e);
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                listener.warn(taskProxy);
            }
        });
        taskProxy.setTask(task);
        task.start();
    }

}
