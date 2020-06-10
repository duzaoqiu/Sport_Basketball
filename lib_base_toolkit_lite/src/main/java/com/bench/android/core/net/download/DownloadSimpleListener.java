package com.bench.android.core.net.download;

/**
 * Author: Aliao
 * Date: 2019/5/17
 * Description: 下载回调的空实现
 */
public class DownloadSimpleListener implements DownloadListener {

    @Override
    public void pending(DownloadTask task, long soFarBytes, long totalBytes) {

    }

    @Override
    public void started(DownloadTask task) {

    }

    @Override
    public void connected(DownloadTask task, String etag, boolean isContinue, long soFarBytes, long totalBytes) {

    }

    @Override
    public void progress(DownloadTask task, long soFarBytes, long totalBytes) {

    }

    @Override
    public void retry(DownloadTask task, Throwable ex, int retryingTimes, long soFarBytes) {

    }

    @Override
    public void completed(DownloadTask task) {

    }

    @Override
    public void paused(DownloadTask task, long soFarBytes, long totalBytes) {

    }

    @Override
    public void error(DownloadTask task, Throwable e) {

    }

    @Override
    public void warn(DownloadTask task) {

    }
}
