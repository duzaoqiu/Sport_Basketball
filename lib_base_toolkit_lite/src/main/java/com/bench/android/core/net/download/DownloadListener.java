package com.bench.android.core.net.download;

/**
 * Author: Aliao
 * Date: 2019/5/17
 * Description: 下载回调
 */
public interface DownloadListener {

    /**
     *
     * @param task       The task
     * @param soFarBytes Already downloaded and reusable bytes stored in the db
     * @param totalBytes Total bytes stored in the db
     */
    void pending(final DownloadTask task, final long soFarBytes,
                          final long totalBytes);

    /**
     * Finish pending, and resume the download runnable.
     *
     * @param task Current task.
     */
    void started(final DownloadTask task);

    /**
     * Already connected to the server, and received the Http-response.
     *
     * @param task       The task
     * @param etag       ETag
     * @param isContinue Is resume from breakpoint
     * @param soFarBytes Number of bytes download so far
     * @param totalBytes Total size of the download in bytes
     */
    void connected(final DownloadTask task, final String etag,
                             final boolean isContinue, final long soFarBytes, final long totalBytes);

    /**
     * Fetching datum from network and Writing to the local disk.
     *
     * @param task       The task
     * @param soFarBytes Number of bytes download so far
     * @param totalBytes Total size of the download in bytes
     */
    void progress(final DownloadTask task, final long soFarBytes,
                                     final long totalBytes);

    /**
     *
     * @param task          The task
     * @param ex            Why retry
     * @param retryingTimes How many times will retry
     * @param soFarBytes    Number of bytes download so far
     */
    void retry(final DownloadTask task, final Throwable ex, final int retryingTimes,
                         final long soFarBytes);

    // ======================= The task is over, if execute below methods =======================

    /**
     * Achieve complete ceremony.
     * <p/>
     * Complete downloading.
     *
     * @param task The task
     */
    void completed(final DownloadTask task);

    /**
     *
     * @param task       The task
     * @param soFarBytes Number of bytes download so far
     * @param totalBytes Total size of the download in bytes
     */
    void paused(final DownloadTask task, final long soFarBytes,
                                   final long totalBytes);

    /**
     * Occur a exception, but don't has any chance to retry.
     *
     * @param task The task
     * @param e    Any throwable on download pipeline
     */
    void error(final DownloadTask task, final Throwable e);

    /**
     * There has already had some same Tasks(Same-URL & Same-SavePath) in Pending-Queue or is
     * running.
     *
     * @param task The task
     */
    void warn(final DownloadTask task);

}
