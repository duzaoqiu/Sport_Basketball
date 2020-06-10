package com.bench.android.core.app.popupwindow;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/************************************************************************
 *@Project: common_lib
 *@Package_Name: com.android.library.tools.Utils
 *@Descriptions:popupwindow工具类
 *@Author: xingjiu
 *@Date: 2019/6/13 
 *************************************************************************/
public class PopupWindowHelper {
    public static final int SHOW_LEFT = 0;
    public static final int SHOW_RIGHT = 1;
    public static final int SHOW_TOP = 2;
    public static final int SHOW_BOTTOM = 3;
    private Builder builder;
    private PopupWindow mPopupWindow;

    private PopupWindowHelper(Builder builder) {
        this.builder = builder;
        mPopupWindow = new PopupWindow(builder.contentView, builder.width, builder.height);
        mPopupWindow.setOutsideTouchable(builder.outsideTouchable);
        mPopupWindow.setBackgroundDrawable(builder.backgroundDrawable);
        if (builder.dismissListener != null) {
            mPopupWindow.setOnDismissListener(builder.dismissListener);
        }
    }

    public void show() {
        int[] location = new int[2];
        builder.targetView.getLocationOnScreen(location);
        builder.contentView.measure(0, 0);
        switch (builder.orientation) {
            case SHOW_LEFT:
                mPopupWindow.showAtLocation(builder.targetView, Gravity.NO_GRAVITY, location[0] - builder.contentView.getMeasuredWidth(),
                        location[1] - (builder.contentView.getMeasuredHeight() + builder.targetView.getMeasuredHeight()) / 2);
                break;
            case SHOW_RIGHT:
                mPopupWindow.showAtLocation(builder.targetView, Gravity.NO_GRAVITY, location[0] + builder.targetView.getMeasuredWidth(),
                        location[1] - (builder.contentView.getMeasuredHeight() + builder.targetView.getMeasuredHeight()) / 2);
                break;
            case SHOW_TOP:
                mPopupWindow.showAtLocation(builder.targetView, Gravity.NO_GRAVITY, location[0] +
                        (builder.targetView.getMeasuredWidth() - builder.contentView.getMeasuredWidth()) / 2, location[1] - builder.contentView.getMeasuredHeight());
                break;
            case SHOW_BOTTOM:
                mPopupWindow.showAtLocation(builder.targetView, Gravity.NO_GRAVITY, location[0] +
                        (builder.targetView.getMeasuredWidth() - builder.contentView.getMeasuredWidth()) / 2, location[1] + builder.targetView.getMeasuredHeight());
                break;
            default:
                break;
        }
    }

    public boolean isShowing(){
        return mPopupWindow.isShowing();
    }

    public void dismiss() {
        mPopupWindow.dismiss();
    }

    public static class Builder {

        /**
         * 宽度
         */
        private int width = ViewGroup.LayoutParams.WRAP_CONTENT;

        /**
         * 高度
         */
        private int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        /**
         * popupWindow的view
         */
        private View contentView;
        /**
         * 参考坐标的view
         */
        private View targetView;
        /**
         * 是否外部点击消失
         */
        private boolean outsideTouchable = true;
        /**
         * 背景
         */
        private Drawable backgroundDrawable = new ColorDrawable(Color.TRANSPARENT);
        /**
         * 方向
         */
        private int orientation;
        /**
         * popupWindow消失回调
         */
        private PopupWindow.OnDismissListener dismissListener;
        /**
         * 展开动画
         */
        private int animIn;

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder contentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder targetView(View targetView) {
            this.targetView = targetView;
            return this;
        }

        public Builder outsideTouchable(boolean outsideTouchable) {
            this.outsideTouchable = outsideTouchable;
            return this;
        }

        public Builder backgroundDrawable(Drawable backgroundDrawable) {
            this.backgroundDrawable = backgroundDrawable;
            return this;
        }

        public Builder orientation(int orientation) {
            this.orientation = orientation;
            return this;
        }

        public Builder dismissListener(PopupWindow.OnDismissListener dismissListener) {
            this.dismissListener = dismissListener;
            return this;
        }
//      public Builder animIn(int animIn) {
//            this.animIn = animIn;
//            return this;
//        }
//        public Builder animIn(int animIn) {
//            this.animIn = animIn;
//            return this;
//        }

        public PopupWindowHelper build() {
            return new PopupWindowHelper(this);
        }
    }
}
