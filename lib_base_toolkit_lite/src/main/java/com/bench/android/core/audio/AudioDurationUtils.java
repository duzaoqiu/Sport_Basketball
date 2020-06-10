package com.bench.android.core.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.IOException;

/************************************************************************
 *@Project: ml
 *@Package_Name: com.android.library
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/6/27 
 *************************************************************************/
public class AudioDurationUtils {
    public static double getDuration(Context context, Uri uri) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        int duration = 0;
        try {
            mediaPlayer.setDataSource(context, uri);
            mediaPlayer.prepare();
            duration = mediaPlayer.getDuration();
            mediaPlayer.release();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return duration;
    }
}
