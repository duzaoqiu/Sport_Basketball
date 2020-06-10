package com.bench.android.core.net.download;

import com.liulishuo.filedownloader.BaseDownloadTask;

/**
 * Author: Aliao
 * Date: 2019/5/17
 * Description: 下载文件的信息，方便替换
 */
public class DownloadTaskProxy implements DownloadTask {

    private BaseDownloadTask task;

    void setTask(BaseDownloadTask task) {
        this.task = task;
    }

    @Override
    public int getId() {
        return task.getId();
    }

    @Override
    public String getUrl() {
        return task.getUrl();
    }

    @Override
    public String getPath() {
        return task.getPath();
    }

    @Override
    public String getFilename() {
        return task.getFilename();
    }

    @Override
    public String getTargetFilePath() {
        return task.getTargetFilePath();
    }

}
