package com.bench.android.core.util;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.annotation.MainThread;

import com.bench.android.core.app.application.BaseApplication;

import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by jun on 2017/9/15.
 */

public class KeyboardUtil {

    private static final Map<Activity, KeyboardLayoutListener> KEYBOARD_LAYOUT_LISTENER_MAP = new WeakHashMap<>();
    private static boolean sInitialized = false;

    /**
     * 监听软键盘状态
     */
    @MainThread
    public static void observeKeyboardChange(Activity activity, OnKeyboardChangeListener onKeyboardChangeListener) {
        if (activity == null || onKeyboardChangeListener == null) {
            return;
        }
        if (!sInitialized) {
            sInitialized = true;
            activity.getApplication().registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
                @Override
                public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

                }

                @Override
                public void onActivityStarted(Activity activity) {

                }

                @Override
                public void onActivityResumed(Activity activity) {

                }

                @Override
                public void onActivityPaused(Activity activity) {

                }

                @Override
                public void onActivityStopped(Activity activity) {

                }

                @Override
                public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

                }

                @Override
                public void onActivityDestroyed(Activity activity) {
                    KeyboardLayoutListener listener = KEYBOARD_LAYOUT_LISTENER_MAP.remove(activity);
                    if (listener != null) {
                        if (activity.getWindow() != null) {
                            activity.getWindow().getDecorView().getViewTreeObserver().removeOnGlobalLayoutListener(listener);
                        }
                    }
                }
            });
        }

        KeyboardLayoutListener listener = new KeyboardLayoutListener(activity, onKeyboardChangeListener);
        KEYBOARD_LAYOUT_LISTENER_MAP.put(activity, listener);
        activity.getWindow().getDecorView().getViewTreeObserver().addOnGlobalLayoutListener(listener);
    }

    /**
     * 显示软键盘
     */
    public static void showSoftInput(Context context, EditText edit) {
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(edit, 0);
        }
    }

    /**
     * 隐藏软键盘
     */
    public static void hideSoftInput(Context context, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public interface OnKeyboardChangeListener {
        void onChanged(boolean isShow, int keyboardHeight);
    }

    public static class KeyboardLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private static int sNavigationBarHeight;
        private static int sStatusBarHeight;

        static {
            Resources resources = BaseApplication.getContext().getResources();
            // navigation bar height
            int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
            if (resourceId > 0) {
                sNavigationBarHeight = resources.getDimensionPixelSize(resourceId);
            }

            // status bar height
            resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                sStatusBarHeight = resources.getDimensionPixelSize(resourceId);
            }
        }

        private Rect mRect = new Rect();
        private Activity mActivity;
        private OnKeyboardChangeListener mOnKeyboardListener;

        public KeyboardLayoutListener(Activity activity, OnKeyboardChangeListener onKeyboardListener) {
            mActivity = activity;
            mOnKeyboardListener = onKeyboardListener;
        }

        @Override
        public void onGlobalLayout() {
            if (mActivity == null || mActivity.isFinishing() || mActivity.isDestroyed() || mOnKeyboardListener == null) {
                return;
            }

            // display window size for the app layout
            mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(mRect);

            // screen height - (user app height + status + nav) ..... if non-zero, then there is a soft keyboard
            int keyboardHeight = mActivity.getWindow().getDecorView().getHeight() - (sStatusBarHeight + sNavigationBarHeight + mRect.height());

            if (keyboardHeight <= 0) {
                mOnKeyboardListener.onChanged(false, -1);
            } else {
                mOnKeyboardListener.onChanged(true, keyboardHeight);
            }
        }
    }
}
