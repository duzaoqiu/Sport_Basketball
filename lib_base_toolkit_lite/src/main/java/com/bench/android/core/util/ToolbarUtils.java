package com.bench.android.core.util;

import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;

import com.bench.android.core.app.activity.BaseActivity;
import com.bench.android.core.view.toolbar.CustomToolbar;

import static android.os.Build.VERSION.SDK_INT;

/************************************************************************
 *@Project: core
 *@Package_Name: com.bench.android.core.util
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/8/23 
 *************************************************************************/
public class ToolbarUtils {

    public static void init(BaseActivity activity, CustomToolbar customToolbar, View topPaddingView) {
        //默认透明色，6.0以下设置不了颜色，这种情形下，可能需要设置颜色
        init(activity, customToolbar, Color.TRANSPARENT, topPaddingView);
    }

    public static void init(BaseActivity activity, CustomToolbar customToolbar, int color, View setPaddingView) {

        //获得页面布局容器，flContent默认第一个child是xml里的布局
        ViewGroup flContent = activity.findViewById(android.R.id.content);
        int statusBarHeight = StatusBarCompat.getStatusBarHeight(activity);
        //如果是5.0及以上，才会设置沉浸式，才会在布局上作一些处理
        if (SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //设置沉浸式
            StatusBarCompat.compat(activity, color, activity.isLightColor());
            //如果有toolbar
            if (customToolbar != null) {
                //计算toolbar的高度
                customToolbar.measure(0, 0);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (activity.isNeedTopPadding()) {
                    params.height = customToolbar.getMeasuredHeight() + statusBarHeight;
                    //因为设置了沉浸式，会从状态栏开始布局，所以设置了高度等于状态栏高度的上padding
                    customToolbar.setPadding(flContent.getPaddingLeft(),
                            flContent.getPaddingTop() + statusBarHeight,
                            flContent.getPaddingRight(),
                            flContent.getPaddingBottom());
                } else {
                    params.height = customToolbar.getMeasuredHeight();
                }
                customToolbar.setLayoutParams(params);

                //插入toolbar
                flContent.addView(customToolbar, 0, params);
                //xml里面的布局给一个高度等于状态栏高度的上margin
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) flContent.getChildAt(1).getLayoutParams();
                layoutParams.topMargin = params.height;
                flContent.getChildAt(1).setLayoutParams(layoutParams);
            } else {
                if (activity.isNeedTopPadding()) {
                    //布局文件的根布局
                    if (setPaddingView == null) {
                        View child = flContent.getChildAt(0);
                        if (child instanceof ViewGroup) {
                            //根布局
                            ViewGroup view = ((ViewGroup) child);
                            if (view.getChildCount() > 0) {
                                //如果根布局是滚动类型的
                                if (view instanceof ScrollView || view instanceof NestedScrollView) {
                                    View childAt;
                                    if (view.getChildCount() > 0) {
                                        //ScrollView的下一级布局
                                        childAt = view.getChildAt(0);
                                        //如果是ViewGroup,并且有child,给他的第一个child设置padding
                                        if (childAt instanceof ViewGroup) {
                                            if (((ViewGroup) childAt).getChildCount() > 0) {
                                                setPaddingView = ((ViewGroup) childAt).getChildAt(0);
                                            }
                                        } else {
                                            //如果是view,直接给他设置padding
                                            setPaddingView = childAt;
                                        }
                                    }
                                } else {
                                    setPaddingView = view.getChildAt(0);
                                }
                            }
                        }
                    }
                    if (setPaddingView != null) {
                        ViewGroup.LayoutParams params = setPaddingView.getLayoutParams();
                        if (params.height != ViewGroup.LayoutParams.MATCH_PARENT && params.height != ViewGroup.LayoutParams.WRAP_CONTENT) {
                            //这边手动测量一次，否则include进来的布局高度拿不到
                            if (params.height == 0) {
                                setPaddingView.measure(0, 0);
                            }
                            params.height = (params.height > 0 ? params.height : setPaddingView.getMeasuredHeight()) + statusBarHeight;
                            setPaddingView.setLayoutParams(params);
                        }
                        setPaddingView.setPadding(flContent.getPaddingLeft(),
                                flContent.getPaddingTop() + statusBarHeight,
                                flContent.getPaddingRight(),
                                flContent.getPaddingBottom());
                    }
                }
            }
        } else {
            if (customToolbar != null) {
                //计算toolbar的高度
                customToolbar.measure(0, 0);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.height = customToolbar.getMeasuredHeight();

                //插入toolbar
                flContent.addView(customToolbar, 0, params);
                //xml里面的布局给一个高度等于状态栏高度的上margin
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) flContent.getChildAt(1).getLayoutParams();
                layoutParams.topMargin = params.height;
                flContent.getChildAt(1).setLayoutParams(layoutParams);
            }
        }
    }
}
