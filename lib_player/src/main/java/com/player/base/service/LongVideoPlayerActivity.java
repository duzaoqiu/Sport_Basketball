package com.player.base.service;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;

import com.bench.android.core.app.activity.BaseActivity;
import com.kk.taurus.playerbase.widget.BaseVideoView;
import com.player.base.R;
import com.player.base.play.VideoPlayHelper;

/************************************************************************
 *@Project: Utils_branch
 *@Package_Name: com.android
 *@Descriptions:长视频播放页
 *@Author: xingjiu
 *@Date: 2019/7/29
 *************************************************************************/
public class LongVideoPlayerActivity extends BaseActivity {

    private VideoPlayHelper videoPlayHelper;
    private static View contentView;
    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_long_video_test);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        path = getIntent().getStringExtra("path");
        initView();

    }

    private void initView() {
        BaseVideoView videoView = findViewById(R.id.baseVideoView);
        FrameLayout flContainer = findViewById(R.id.fl_container);
        flContainer.addView(contentView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        videoPlayHelper = new VideoPlayHelper(this, videoView, path, true);
    }


    @Override
    public void onClick(View v) {
    }

    /**
     * 需要在activity不同生命周期，调用videoHelper的方法
     * - onResume
     * - onPause
     * - onDestroy
     */
    @Override
    protected void onResume() {
        super.onResume();
        videoPlayHelper.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoPlayHelper.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoPlayHelper.onDestroy();
        contentView = null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        videoPlayHelper.onConfigurationChanged(newConfig);
    }

    @Override
    public void onBackPressed() {
        if (!videoPlayHelper.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public static void start(Context context, String path, View contentView) {
        LongVideoPlayerActivity.contentView = contentView;
        Intent intent = new Intent(context, LongVideoPlayerActivity.class);
        intent.putExtra("path", path);
        context.startActivity(intent);
    }

    @Override
    public boolean isLightColor() {
        return false;
    }

    @Override
    public boolean isNeedTopPadding() {
        return false;
    }
}
