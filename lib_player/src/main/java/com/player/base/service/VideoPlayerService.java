package com.player.base.service;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.fragment.app.FragmentManager;

import com.alibaba.android.arouter.facade.annotation.ServiceImpl;
import com.bench.android.core.content.commonio.FileUtils;
import com.player.base.ijkplayer.IjkPlayer;

import java.io.File;
import java.util.HashMap;

/************************************************************************
 *@Project: packages
 *@Package_Name: com.player.base.service
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/9/4 
 *************************************************************************/
@ServiceImpl
public class VideoPlayerService {

    /**
     * //这是个耗时操作，所以放在线程里面处理
     *
     * @param url     视频路径
     * @param handler 消息处理的handler
     */

    public void getFirstFramePic(final Context context, final String url, final Handler handler) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final MediaMetadataRetriever media = new MediaMetadataRetriever();
                    if (url.startsWith("http")) {
                        media.setDataSource(url, new HashMap<String, String>());
                    } else {
                        media.setDataSource(context, FileUtils.getFileUri(context, new File(url)));
                    }
                    //通过handler第一帧bitmap
                    final Bitmap frameAtTime = media.getFrameAtTime();
                    Message obtain = Message.obtain();
                    obtain.what = 0;
                    obtain.obj = frameAtTime;
                    handler.sendMessage(obtain);

                } catch (Exception e) {
                    e.printStackTrace();
                    //异常
                    handler.sendEmptyMessage(1);
                }
            }

        }).start();
    }


    public void startShortVideoPlayer(Context context, String path) {
        ShortVideoPlayerActivity.start(context, path);
    }


    public void startLongVideoPlayer(Context context, String path, View content) {
        LongVideoPlayerActivity.start(context, path, content);
    }


    public void startShortVideoPlayerFragment(FragmentManager fragmentManager, int contentId, String path,String tag) {
        ShortVideoPlayerFragment shortVideoPlayerFragment = ShortVideoPlayerFragment.newInstance(path,false);
        fragmentManager.beginTransaction().add(contentId, shortVideoPlayerFragment,tag).commit();
    }


//    public void onInit() {
////        IjkPlayer.init(ApplicationAgent.getInstance().getApplication());
//    }



}
