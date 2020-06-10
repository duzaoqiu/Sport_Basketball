package com.player.base.service;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.bench.android.core.app.activity.BaseActivity;
import com.kk.taurus.playerbase.widget.BaseVideoView;
import com.player.base.R;
import com.player.base.play.VideoPlayHelper;

/************************************************************************
 *@Project: Utils_branch
 *@Package_Name: com.android
 *@Descriptions:短视频测试类
 *@Author: xingjiu
 *@Date: 2019/7/29
 *************************************************************************/
public class ShortVideoPlayerActivity extends BaseActivity {

    private VideoPlayHelper videoPlayHelper;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_video_test);
        overridePendingTransition(0, 0);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        initView();
    }

    private void initView() {
        String path = getIntent().getStringExtra("path");
        BaseVideoView videoView = findViewById(R.id.baseVideoView);
        videoPlayHelper = new VideoPlayHelper(this, videoView,path,false);
//        videoPlayHelper.playVideo(path);
    }


    /**
     * 需要在activity不同生命周期，调用VideoHelper的方法
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
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        videoPlayHelper.onBackPressed();
    }

    public static void start(Context context, String path) {
        Intent intent = new Intent(context, ShortVideoPlayerActivity.class);
        intent.putExtra("path", path);
        context.startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override
    public boolean isNeedTopPadding() {
        return false;
    }
}
