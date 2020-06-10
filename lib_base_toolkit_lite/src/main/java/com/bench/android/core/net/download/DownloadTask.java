package com.bench.android.core.net.download;

/**
 * Author: Aliao
 * Date: 2019/5/17
 * Description: 下载文件的信息
 */
public interface DownloadTask {

    /**
     *
     * @return 任务 id
     */
    int getId();

    /**
     *
     * @return 下载 url
     */
    String getUrl();

    /**
     *
     * @return 文件目录
     */
    String getPath();

    /**
     *
     * @return 文件名
     */
    String getFilename();

    /**
     *
     * @return 目标文件的存储路径
     */
    String getTargetFilePath();

}
