package com.bench.android.core.view.emoji.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.IntRange;

import java.util.ArrayList;
import java.util.List;

/************************************************************************
 *@Project: mingliao
 *@Package_Name: com.minglin.android.lib.chat.ui.activity.chooseMember
 *@Descriptions:表情输入法切换工具
 *@Author: xingjiu
 *@Date: 2019/5/7
 *************************************************************************/
public class EmojiKeyboard implements View.OnClickListener {

    /**
     * 默认mode为0的情况下展开表情栏的情况下，再点一次，会隐藏表情栏，显示键盘
     * mode为1时，不展开键盘
     */

    public static final int MODE_SWITCH_SHOW_KEYBOARD = 0;
    public static final int MODE_SWITCH_HIDE_KEYBOARD = 1;

    private Activity activity;

    /**
     * 文本输入框
     */
    private EditText editText;

    /**
     * 底部展开面板
     */
    private ViewGroup panelView;

    /**
     * 点击表情或者更多按钮，如果已经展开，是否隐藏的时候显示键盘
     */
    private int mode;

    /**
     * 内容View,即除了表情布局和输入框布局以外的布局
     * 用于固定输入框一行的高度以防止跳闪
     */

    private View contentView;

    private InputMethodManager inputMethodManager;

    private SharedPreferences sharedPreferences;

    private static final String EMOJI_KEYBOARD = "EmojiKeyboard";

    private static final String KEY_SOFT_KEYBOARD_HEIGHT = "SoftKeyboardHeight";

    private static final int SOFT_KEYBOARD_HEIGHT_DEFAULT = 654;

    private Handler handler;
    private int navBarHeight;
    private List<View> mViewList = new ArrayList<>();

    private EmojiKeyboard(Activity activity, ViewGroup contentView, EditText editText, View ivEmoji, View ivMore, ViewGroup panelView) {
        this.activity = activity;
        this.editText = editText;
        this.panelView = panelView;
        this.contentView = contentView;
        mViewList.add(ivEmoji);
        mViewList.add(ivMore);
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        inputMethodManager = (InputMethodManager) this.activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        sharedPreferences = this.activity.getSharedPreferences(EMOJI_KEYBOARD, Context.MODE_PRIVATE);
        activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        handler = new Handler();
//        如果之前没有保存过键盘高度值
//        则在进入Activity时自动打开键盘，并把高度值保存下来
        if (!sharedPreferences.contains(KEY_SOFT_KEYBOARD_HEIGHT)) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    showSoftKeyboard(true);
                }
            }, 500);
        }
        for (int i = 0; i < mViewList.size(); i++) {
            final int finalI = i;
            View view = mViewList.get(i);
            if (view == null) {
                continue;
            }
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handleClickEvent(finalI);
                }
            });
        }

        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(final View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP && panelView.isShown()) {
                    lockContentViewHeight();
                    hideEmojiPanel(true);
                    unlockContentViewHeight();
                    for (View view : mViewList) {
                        if (view != null) {
                            view.setSelected(false);
                        }
                    }
                }
                return false;
            }
        });
    }

    private void handleClickEvent(int viewIndex) {
        if (panelView.isShown()) {
            //点击按钮时，当前对应的展开栏正在显示
            if (panelView.getChildAt(viewIndex).getVisibility() == View.VISIBLE) {
                if (mode == MODE_SWITCH_SHOW_KEYBOARD) {
                    lockContentViewHeight();
                    hideEmojiPanel(true);
                    unlockContentViewHeight();
                } else {
                    hideEmojiPanel(false);
                }
                mViewList.get(viewIndex).setSelected(false);
            } else {
                for (View view : mViewList) {
                    if (view != null) {
                        view.setSelected(false);
                    }
                }
                mViewList.get(viewIndex).setSelected(true);
                if (onPanelStateChangeListener != null) {
                    onPanelStateChangeListener.onShowPanel(viewIndex);
                }
            }
        } else {
            mViewList.get(viewIndex).setSelected(true);
            if (isSoftKeyboardShown()) {
                lockContentViewHeight();
                showEmojiPanel(viewIndex);
                unlockContentViewHeight();
            } else {
                showEmojiPanel(viewIndex);
            }
        }
    }


    public void getNavigationBarHeight() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                EmojiKeyboard.this.navBarHeight = getNavBarHeight();
            }
        }, 200);
    }

    /**
     * 当点击返回键时需要先隐藏表情面板
     */
    public boolean interceptBackPress() {
        if (panelView.isShown()) {
            hideEmojiPanel(false);
            return true;
        }
        return false;
    }

    /**
     * 释放锁定的内容View
     */
    private void unlockContentViewHeight() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                ((LinearLayout.LayoutParams) contentView.getLayoutParams()).weight = 1;
            }
        }, 200);
    }

    /**
     * 锁定内容View以防止跳闪
     */
    private void lockContentViewHeight() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) contentView.getLayoutParams();
        layoutParams.height = contentView.getHeight();
        layoutParams.weight = 0;
    }

    /**
     * 获取键盘的高度
     */
    private int getSoftKeyboardHeight() {
        Rect rect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rect);
        //屏幕当前可见高度，不包括状态栏
        int displayHeight = rect.bottom - rect.top;
        //屏幕可用高度
        int availableHeight = getScreenHeight();
        //用于计算键盘高度
        int softInputHeight = availableHeight - displayHeight - getStatusBarHeight() - navBarHeight;
        if (softInputHeight != 0) {
            // 因为考虑到用户可能会主动调整键盘高度，所以只能是每次获取到键盘高度时都将其存储起来
            sharedPreferences.edit().putInt(KEY_SOFT_KEYBOARD_HEIGHT, softInputHeight).apply();
        }
        return softInputHeight;
    }

    /**
     * 获取本地存储的键盘高度值或者是返回默认值
     */
    private int getSoftKeyboardHeightLocalValue() {
        return sharedPreferences.getInt(KEY_SOFT_KEYBOARD_HEIGHT, SOFT_KEYBOARD_HEIGHT_DEFAULT);
    }

    /**
     * 判断是否显示了键盘
     */
    private boolean isSoftKeyboardShown() {
        return getSoftKeyboardHeight() != 0;
    }

    /**
     * 令编辑框获取焦点并显示键盘
     */
    private void showSoftKeyboard(boolean saveSoftKeyboardHeight) {
        editText.requestFocus();
        inputMethodManager.showSoftInput(editText, 0);
        if (saveSoftKeyboardHeight) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    getSoftKeyboardHeight();
                }
            }, 200);
        }
    }

    /**
     * 隐藏键盘
     */
    private void hideSoftKeyboard() {
        inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * 显示表情面板
     */
    private void showEmojiPanel(int index) {
        int softKeyboardHeight = getSoftKeyboardHeight();
        if (softKeyboardHeight == 0) {
            softKeyboardHeight = getSoftKeyboardHeightLocalValue();
        } else {
            hideSoftKeyboard();
        }
        panelView.getLayoutParams().height = softKeyboardHeight;
        panelView.setVisibility(View.VISIBLE);
        if (onPanelStateChangeListener != null) {
            onPanelStateChangeListener.onShowPanel(index);
        }
    }

    /**
     * 隐藏表情面板，同时指定是否随后开启键盘
     */
    private void hideEmojiPanel(boolean showSoftKeyboard) {
        if (panelView.isShown()) {
            panelView.setVisibility(View.GONE);
            if (showSoftKeyboard) {
                showSoftKeyboard(false);
            }
            if (onPanelStateChangeListener != null) {
                onPanelStateChangeListener.onHidePanel();
            }
        }
    }

    private void autoHideKeyboard() {
        contentView.setClickable(true);
        contentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (panelView.isShown()) {
                        hideEmojiPanel(false);
                    } else if (isSoftKeyboardShown()) {
                        hideSoftKeyboard();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {

    }

    public static class Builder {

        /**
         * activity对象
         */
        Activity activity;
        /**
         * 除了底部输入框部分的页面，为了锁定高度
         */
        ViewGroup contentView;
        /**
         * 输入框对象
         */
        EditText editText;
        /**
         * 点击展开表情栏的按钮
         */
        View emojiView;
        /**
         * 点击展开更多选项的按钮,比如拍照、选取图片
         */
        View moreView;
        /**
         * 点击后展开的视图
         */
        ViewGroup panelView;

        /**
         * 是否点击空白处自动隐藏键盘或者展开的视图
         */
        private boolean autoHideKeyboard;
        /**
         * 模式
         */
        private int mode;

        public Builder(Activity activity) {
            this.activity = activity;
        }

        public Builder contentView(ViewGroup contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder editText(EditText editText) {
            this.editText = editText;
            return this;
        }

        public Builder emojiView(View emojiView) {
            this.emojiView = emojiView;
            return this;
        }

        public Builder moreView(View moreView) {
            this.moreView = moreView;
            return this;
        }

        public Builder panelView(ViewGroup panelView) {
            this.panelView = panelView;
            return this;
        }

        public Builder autoHideKeyboard(boolean autoHideKeyboard) {
            this.autoHideKeyboard = autoHideKeyboard;
            return this;
        }

        public Builder mode(@IntRange(from = 0, to = 1) int mode) {
            this.mode = mode;
            return this;
        }

        public EmojiKeyboard build() {
            if (activity == null || contentView == null || editText == null || panelView == null || (emojiView == null && moreView == null)) {
                throw new RuntimeException("请传入必须的参数！");
            }
            EmojiKeyboard emojiKeyboard = new EmojiKeyboard(activity, contentView, editText, emojiView, moreView, panelView);
            if (autoHideKeyboard) {
                emojiKeyboard.autoHideKeyboard();
            }
            emojiKeyboard.mode = mode;
            return emojiKeyboard;
        }
    }

    public interface OnPanelStateChangeListener {

        /**
         * 底部展开内容的回调
         *
         * @param index 表示
         */
        void onShowPanel(int index);


        void onHidePanel();
    }

    private OnPanelStateChangeListener onPanelStateChangeListener;

    public void setOnPanelStateChangeListener(OnPanelStateChangeListener onPanelStateChangeListener) {
        this.onPanelStateChangeListener = onPanelStateChangeListener;
    }

    /**
     * 获取状态栏高度
     *
     * @return
     */
    private int getStatusBarHeight() {
        // 获得状态栏高度
        int resourceId = activity.getResources().getIdentifier("status_bar_height", "dimen", "android");
        return activity.getResources().getDimensionPixelSize(resourceId);
    }

    private int getScreenHeight() {
        WindowManager manager = activity.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getRealMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

    /**
     * 判断是否显示了导航栏
     * (说明这里的context 一定要是activity的context 否则类型转换失败)
     *
     * @return
     */
    private int getNavBarHeight() {
        /**
         * 获取应用区域高度
         */
        Rect outRect = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(outRect);
        int activityHeight = outRect.height();
        int remainHeight = getScreenHeight() - getStatusBarHeight();
        /**
         * 剩余高度跟应用区域高度相等 说明导航栏没有显示 否则相反
         */
        return remainHeight - activityHeight;

    }
}