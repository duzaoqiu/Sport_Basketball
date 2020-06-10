package com.bench.android.core.audio;

import android.media.MediaRecorder;

import com.bench.android.core.cache.CacheManager;
import com.bench.android.core.cache.InternalCacheManager;

import java.io.File;
import java.io.IOException;

/************************************************************************
 *@Project: ml
 *@Package_Name: com.android.library.tools.Utils
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/6/27 
 *************************************************************************/
public class MediaRecordUtils {
    private static MediaRecordUtils recordUtils;
    private MediaRecorder mr = null;
    public static String soundDir = "sounds";
    InternalCacheManager fm;

    public static MediaRecordUtils getInstance() {
        if (recordUtils == null) {
            recordUtils = new MediaRecordUtils();
        }
        return recordUtils;
    }

    private MediaRecordUtils() {
        fm = (InternalCacheManager) CacheManager.getCacheService(CacheManager.CACHE_INTERNAL);
    }

    private void init() {
        mr = new MediaRecorder();
        //音频输入源
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        //设置输出格式
        mr.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
        //设置编码格式
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
    }

    /**
     * 开始录制
     *
     * @param fileName
     */
    public void startRecord(String fileName) {
        init();
//        File dir = new File(context.getCacheDir(), soundDir);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
        File soundFile = fm.getInternalFilePath(fileName);
        if (!soundFile.exists()) {
            try {
                soundFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        mr.setOutputFile(soundFile.getAbsolutePath());
        try {
            mr.prepare();
            mr.start();  //开始录制
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 停止录制
     */

    public void stopRecord() {
        if (mr != null) {
            try {
                mr.stop();
                mr.release();
                mr = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
