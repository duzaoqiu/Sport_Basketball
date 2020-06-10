package com.bench.android.core.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.core.graphics.ColorUtils;

import java.lang.ref.WeakReference;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Created by qiaomu on 2017/6/1.
 */

public class StatusBarCompat {
    public static int alphaColor(int color, int alpha) {
        color &= ~(0xFF << 24);
        color |= alpha << 24;
        return color;
    }

    public static int color(int color, int alpha) {
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red | green << 8 | blue;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, @ColorInt int color) {
        compat(activity, color, true);
    }

    /**
     * << 16
     * activity全屏，伸入statusbar在下面
     * 需要设置 toolbar paddingTop
     *
     * @param activity
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, @ColorInt int color, boolean isLight) {
        //5.0以上才设置沉浸式
        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            if (color <= 0) {
                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                if (color == Color.TRANSPARENT && SDK_INT < Build.VERSION_CODES.M) {
                    color = Color.parseColor("#999999");
                }
                window.setStatusBarColor(color);
            } else {
                window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            }
            //6.0以上允许改变状态栏图标颜色
            if (SDK_INT >= Build.VERSION_CODES.M) {
                //改变状态栏的颜色 一种白色，一种黑色，根据状态栏颜色决定
                if (isLight) {
                    window.getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
            }
        }
        //4.4 不设置沉浸式，因为如果标题栏是白的话就看不见
//        else if (SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            Window win = activity.getWindow();
//            WindowManager.LayoutParams winParams = win.getAttributes();
//            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//            winParams.flags |= bits;
//            win.setAttributes(winParams);
//        }

    }

    /**
     * 判断颜色深浅(是亮色,还是深色)
     * 来自:https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     *
     * @param color
     * @return
     */
    public static boolean isColorDark(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        return !(darkness < 0.5);
    }

    /**
     * 修改导航栏字体颜色
     * 注:目前还有问题
     *
     * @param window
     * @param isLight
     */
    /*public static void setNavigationBarTextColor(Window window, boolean isLight) {
        if (Build.VERSION.SDK_INT >= 26) {
            View decorView = window.getDecorView();
            if (isLight) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            } else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }*/

    /**
     * 设置状态栏背景的颜色
     * 并且相应改变状态栏字体的颜色(背景亮色-->黑色字体,背景暗色--->白色字体)
     *
     * @param window
     * @param color
     */
    public static void setStatusBarColor(Window window, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= 23) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(color);
            setStatusBarTextColor(window, !isColorDark(color));
        }
    }

    /**
     * 设置状态栏字体的颜色
     *
     * @param window
     * @param isLight 字体是否亮色
     */
    public static void setStatusBarTextColor(Window window, boolean isLight) {
        if (Build.VERSION.SDK_INT >= 23) {
            View decorView = window.getDecorView();
            if (isLight) {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }
    }

    /**
     * 功能:隐藏导航栏
     *
     * @param window Activity中的window或者dialog中的window
     */
    public static void hideNavigationBarUI(Window window) {
        View decorView = window.getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE //固定布局,要不然触碰window一下,导航栏会重新出来
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * 显示导航栏
     *
     * @param window
     */
    public static void showNavigationBarUI(Window window) {
        View decorView = window.getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_VISIBLE;
        decorView.setSystemUiVisibility(uiOptions);
    }

    /**
     * 修改导航栏颜色
     *
     * @param window
     * @param Color
     */
    public static void setNavigationBarColor(Window window, @ColorInt int Color) {
        if (Build.VERSION.SDK_INT >= 21) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setNavigationBarColor(Color);
        }
    }

    /*给一个VIew曾加顶部 间距=状态栏高度,使得布局显示正常*/

    /**
     * @param view 记住了  这个 必须在布局里有固定的高度。
     */
    public static void fitsSystemWindows(@NonNull final View view) {
        if (view == null || SDK_INT < 19) {
            return;
        }
        view.post(new Runnable() {
            @Override
            public void run() {
                int statusBarHeight = getStatusBarHeight(view.getContext());
                ViewGroup.LayoutParams params = view.getLayoutParams();
                params.height += statusBarHeight;
                view.setLayoutParams(params);
                view.setPadding(view.getPaddingLeft(),
                        view.getPaddingTop() + statusBarHeight,
                        view.getPaddingRight(),
                        view.getPaddingBottom());
            }
        });
    }

//    /**
//     * @param view 记住了  这个 必须在布局里有固定的高度。
//     */
//    public static void fitsSystemWindows(@NonNull View view, @ColorInt int color) {
//        if (view == null || SDK_INT < 19)
//            return;
//        int statusBarHeight = getStatusBarHeight(view.getContext());
//        ViewGroup.LayoutParams params = view.getLayoutParams();
//        params.height += statusBarHeight;
//        view.setLayoutParams(params);
//        if (color >= 0) {
//            if (view instanceof LinearLayout) {
//                View statusBarView = new View(view.getContext());
//                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
//                statusBarView.setLayoutParams(lp);
//                statusBarView.setBackgroundColor(color);
//                ((LinearLayout) view).addView(statusBarView, 0);
//            } else if (view instanceof FrameLayout || view instanceof RelativeLayout) {
//                view.setPadding(view.getPaddingLeft(),
//                        view.getPaddingTop() + statusBarHeight,
//                        view.getPaddingRight(),
//                        view.getPaddingBottom());
//            }
//        }
//    }


    /*恢复setUpToolbarLayoutParams（）设置的间距*/
    public static void unFitsSystemWindows(@NonNull View view) {
        if (view == null) {
            return;
        }
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height -= getStatusBarHeight(view.getContext());
        view.setLayoutParams(params);
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    //开启全屏模式
    public static void hideSystemUI(Activity activity) {
        View mDecorView = activity.getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    //取消全屏模式
    private static void showSystemUI(Activity activity) {
        View mDecorView = activity.getWindow().getDecorView();
        mDecorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    /*解决 沉浸式状态栏  + 输入框Editext   + adjustResize 输入框不能被弹起的问题*/
    public static void fix4ImmersiveStatusBar(final View rootView) {
        if (SDK_INT < 19)
            return;
        WeakReference<View> mViewReference = new WeakReference<>(rootView);
        if (mViewReference == null || mViewReference.get() == null)
            return;
        mViewReference.get().getViewTreeObserver().addOnGlobalLayoutListener(new GlobalLayoutListener(mViewReference));
    }

    /**
     * Android 6.0 以上设置状态栏颜色
     */
    protected void setStatusBar(Activity activity, @ColorInt int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            // 设置状态栏底色颜色
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(color);

            // 如果亮色，设置状态栏文字为黑色
            if (isLightColor(color)) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
            }
        }

    }

    /**
     * 判断颜色是不是亮色
     *
     * @param color
     * @return
     * @from https://stackoverflow.com/questions/24260853/check-if-color-is-dark-or-light-in-android
     */
    private boolean isLightColor(@ColorInt int color) {
        return ColorUtils.calculateLuminance(color) >= 0.5;
    }

    private static class GlobalLayoutListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private ViewGroup.LayoutParams frameLayoutParams;
        private int usableHeightPrevious;
        private WeakReference<View> mViewReference;

        public GlobalLayoutListener(WeakReference<View> mViewReference) {
            this.mViewReference = mViewReference;
            if (mViewReference == null || mViewReference.get() == null)
                return;
            frameLayoutParams = mViewReference.get().getLayoutParams();
            if (frameLayoutParams == null)
                frameLayoutParams = new ViewGroup.LayoutParams(-1, -1);
        }

        @Override
        public void onGlobalLayout() {
            if (mViewReference == null)
                return;
            View view = mViewReference.get();
            if (view == null)
                return;
            Rect r = new Rect();
            view.getWindowVisibleDisplayFrame(r);
            int usableHeightNow = r.bottom;
            if (usableHeightNow != usableHeightPrevious) {
                //如果两次高度不一致
                //将计算的可视高度设置成视图的高度
                frameLayoutParams.height = usableHeightNow;
                view.requestLayout();//请求重新布局
                usableHeightPrevious = usableHeightNow;
            }
        }
    }
}