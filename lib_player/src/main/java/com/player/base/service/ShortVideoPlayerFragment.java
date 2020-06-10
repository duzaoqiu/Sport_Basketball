package com.player.base.service;

import android.os.Bundle;

import com.bench.android.core.app.fragment.BaseFragment;
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
public class ShortVideoPlayerFragment extends BaseFragment {

    private VideoPlayHelper videoPlayHelper;
    private String path;
    private boolean isNeedCloseButton = true;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_short_video_test;
    }

    @Override
    protected void onCreateView() {
        initView();
    }

    private void initView() {
        path = getArguments().getString("path");
        isNeedCloseButton = getArguments().getBoolean("isNeedCloseButton");
        BaseVideoView videoView = rootView.findViewById(R.id.baseVideoView);
        videoPlayHelper = new VideoPlayHelper(getActivity(), videoView, path, false);
        videoPlayHelper.setCloseButtonVisible(isNeedCloseButton);
    }

    /**
     * 需要在activity不同生命周期，调用VideoHelper的方法
     * - onResume
     * - onPause
     * - onDestroy
     */
    @Override
    public void onResume() {
        super.onResume();
        videoPlayHelper.onResume();
//        videoPlayHelper.playVideo(path);
    }

    @Override
    public void onPause() {
        super.onPause();
        videoPlayHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        videoPlayHelper.onDestroy();
    }

    public void onBackPressed() {
        videoPlayHelper.onBackPressed();
    }

    public static ShortVideoPlayerFragment newInstance(String path, boolean isNeedCloseButton) {
        ShortVideoPlayerFragment videoPlayerFragment = new ShortVideoPlayerFragment();
        Bundle bundle = new Bundle();
        bundle.putString("path", path);
        bundle.putBoolean("isNeedCloseButton", isNeedCloseButton);
        videoPlayerFragment.setArguments(bundle);
        return videoPlayerFragment;
    }

}
