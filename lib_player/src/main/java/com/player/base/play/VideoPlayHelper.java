package com.player.base.play;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.bench.android.core.util.ScreenUtils;
import com.kk.taurus.playerbase.assist.InterEvent;
import com.kk.taurus.playerbase.assist.OnVideoViewEventHandler;
import com.kk.taurus.playerbase.entity.DataSource;
import com.kk.taurus.playerbase.player.IPlayer;
import com.kk.taurus.playerbase.receiver.ReceiverGroup;
import com.kk.taurus.playerbase.widget.BaseVideoView;
import com.player.base.cover.CloseCover;
import com.player.base.cover.CompleteCover;
import com.player.base.cover.ControllerCover;
import com.player.base.cover.ErrorCover;
import com.player.base.cover.GestureCover;
import com.player.base.cover.LoadingCover;

import static com.player.base.play.DataInter.ReceiverKey.KEY_CLOSE_COVER;
import static com.player.base.play.DataInter.ReceiverKey.KEY_COMPLETE_COVER;
import static com.player.base.play.DataInter.ReceiverKey.KEY_CONTROLLER_COVER;
import static com.player.base.play.DataInter.ReceiverKey.KEY_ERROR_COVER;
import static com.player.base.play.DataInter.ReceiverKey.KEY_GESTURE_COVER;
import static com.player.base.play.DataInter.ReceiverKey.KEY_LOADING_COVER;

/************************************************************************
 *@Project: Utils_branch
 *@Package_Name: com.android
 *@Descriptions:视频helper类，长视频和短视频都能用
 *@Author: xingjiu
 *@Date: 2019/7/31 
 *************************************************************************/
public class VideoPlayHelper {

    private Activity activity;
    private ReceiverGroup mReceiverGroup;
    private BaseVideoView mVideoView;
    /**
     * 是否是长视频
     */
    private boolean isLongVideo;
    private boolean userPause;
    private boolean isLandscape;
    /**
     * 是否已开始
     */
    private boolean hasStart;

    /**
     * 视频路径
     */
    private String path;
    private boolean closeButtonVisible;

    public VideoPlayHelper(Activity activity, BaseVideoView mVideoView, String path, boolean isLongVideo) {
        this.activity = activity;
        this.isLongVideo = true;
        this.mVideoView = mVideoView;
        this.path = path;
        this.isLongVideo = isLongVideo;
        init();
    }

    /**
     * 一些初始化操作
     */
    private void init() {
        //短视频的BaseVideoView是new出来的，长视频是传递过来的
        if (isLongVideo) {
            updateVideo(false);
        }
        initReceiverGroup();
        //一些播放器按钮组件的回调
        mVideoView.setEventHandler(onVideoViewEventHandler);
    }

    /**
     * 添加视频播放器的一系列组件
     */
    private void initReceiverGroup() {
        mReceiverGroup = new ReceiverGroup(null);
        mReceiverGroup.addReceiver(KEY_LOADING_COVER, new LoadingCover(activity));
        mReceiverGroup.addReceiver(KEY_COMPLETE_COVER, new CompleteCover(activity));
        mReceiverGroup.addReceiver(KEY_ERROR_COVER, new ErrorCover(activity));
        mReceiverGroup.addReceiver(KEY_CONTROLLER_COVER, new ControllerCover(activity));
        if (isLongVideo) {
            mReceiverGroup.addReceiver(KEY_GESTURE_COVER, new GestureCover(activity));
            mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, true);
        } else {
            if (closeButtonVisible) {
                mReceiverGroup.addReceiver(KEY_CLOSE_COVER, new CloseCover(activity));
            }
            //不显示标题
//            mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_TOP_ENABLE, false);
            //不显示全屏切换按钮
            mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_CONTROLLER_SCREEN_SWITCH_ENABLE, false);
        }
        //注册组件
        mVideoView.setReceiverGroup(mReceiverGroup);
    }

    /**
     * 播放器上面的按钮点击回调处理，由cover层传递过来
     */
    private OnVideoViewEventHandler onVideoViewEventHandler = new OnVideoViewEventHandler() {
        @Override
        public void onAssistHandle(BaseVideoView assist, int eventCode, Bundle bundle) {
            super.onAssistHandle(assist, eventCode, bundle);
            switch (eventCode) {
                case InterEvent.CODE_REQUEST_PAUSE:
                    userPause = true;
                    break;

                case DataInter.Event.EVENT_CODE_ERROR_SHOW:
                    mVideoView.stop();
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_CLOSE:
                    //当点击关闭按钮时，停止视频，移除视频容器，添加原来的布局
                    mVideoView.stop();
                    hasStart = false;
                    activity.finish();
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_TOGGLE_SCREEN:
                    activity.setRequestedOrientation(isLandscape ?
                            ActivityInfo.SCREEN_ORIENTATION_PORTRAIT :
                            ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    isLandscape = !isLandscape;
                    mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, isLandscape);
                    break;
                case DataInter.Event.EVENT_CODE_REQUEST_BACK:
                    if (isLandscape) {
                        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    } else {
                        activity.finish();
                    }
                    break;
                default:
                    break;
            }
        }
    };

    /**
     * 播放视频
     *
     * @param path      视频路径
     * @param reference 缩放动画参考点
     */
    public void playVideo(String path, View reference) {
        if (!isLongVideo && reference != null) {
            initAnimator(reference);
        }
        if (!isLongVideo || !hasStart) {
            DataSource dataSource = new DataSource(path);
            mVideoView.setDataSource(dataSource);
            mVideoView.start();
            hasStart = true;
        }
    }

    /**
     * 播放视频
     *
     * @param path 视频路径
     */
    public void playVideo(String path) {
        playVideo(path, null);
    }

    /**
     * 缩放动画
     *
     * @param referenceView 以这个view的中心点展开动画
     */
    private void initAnimator(View referenceView) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(mVideoView, "scaleX", 0, 1);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(mVideoView, "scaleY", 0, 1);
        ObjectAnimator alpha = ObjectAnimator.ofFloat(mVideoView, "alpha", 0, 1);
        if (referenceView != null) {
            int[] location = new int[2];
            referenceView.getLocationOnScreen(location);
            int x = location[0] + referenceView.getWidth() / 2;
            int y = location[1] + referenceView.getHeight() / 2;
            mVideoView.setPivotX(x);
            mVideoView.setPivotY(y);
        }
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(scaleX).with(scaleY).with(alpha);
        animatorSet.setDuration(200);
        animatorSet.start();
    }

    /**
     * 在activity的onPause时调用，如果视频在播放中，就暂停
     */
    public void onPause() {
        int state = mVideoView.getState();
        if (state == IPlayer.STATE_PLAYBACK_COMPLETE) {
            return;
        }
        if (mVideoView.isInPlaybackState()) {
            mVideoView.pause();
        } else {
            mVideoView.stop();
        }
    }

    /**
     * 在activity的onDestroy时调用，销毁释放mVideoView
     */
    public void onDestroy() {
        mVideoView.stopPlayback();
    }

    /**
     * 在activity的onResume时调用，如果视频被暂停了，则继续播放
     */
    public void onResume() {
        int state = mVideoView.getState();
        if (state == IPlayer.STATE_PLAYBACK_COMPLETE) {
            return;
        }
        if (mVideoView.isInPlaybackState()) {
            if (!userPause) {
                mVideoView.resume();
            }
        } else {
            mVideoView.rePlay(0);
        }
        playVideo(path);
    }

    /**
     * 横竖屏切换回调
     *
     * @param newConfig 新的横竖屏参数
     */
    public void onConfigurationChanged(Configuration newConfig) {
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            isLandscape = true;
            updateVideo(true);
        } else {
            isLandscape = false;
            updateVideo(false);
        }
        mReceiverGroup.getGroupValue().putBoolean(DataInter.Key.KEY_IS_LANDSCAPE, isLandscape);
    }

    /**
     * 横竖屏切换更改view的布局
     *
     * @param landscape
     */
    private void updateVideo(boolean landscape) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mVideoView.getLayoutParams();
        if (landscape) {
            layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else {
            layoutParams.width = ScreenUtils.getScreenWidth(activity);
            layoutParams.height = layoutParams.width * 3 / 4;
        }
        mVideoView.setLayoutParams(layoutParams);
    }

    /**
     * 点返回键
     *
     * @return 是否真正返回
     */
    public boolean onBackPressed() {
        if (isLandscape) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            return true;
        }
        return false;
    }


    public void setCloseButtonVisible(boolean closeButtonVisible) {
        this.closeButtonVisible = closeButtonVisible;
    }
}
