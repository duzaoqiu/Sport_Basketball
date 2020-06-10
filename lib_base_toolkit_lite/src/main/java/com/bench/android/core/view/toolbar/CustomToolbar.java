package com.bench.android.core.view.toolbar;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.ColorRes;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.android.library.R;

/************************************************************************
 *@Project: core
 *@Package_Name: com.bench.android.core.view.toolbar
 *@Descriptions: 默认的Toolbar布局，最左边是一个图标，中间文字，右边按钮文字默认隐藏
 *@Author: xingjiu
 *@Date: 2019/8/19 
 *************************************************************************/
public class CustomToolbar extends Toolbar {

    private View mContentView;

    private CustomToolbar(Context context, Builder builder) {
        super(context, null, 0);
        initView(builder);
    }

    /**
     * 初始化
     *
     * @param builder 配置的属性builder
     */
    private void initView(CustomToolbar.Builder builder) {
        if (mContentView == null) {
            //如果使用的是自定义的布局，那么逻辑就由这个布局自己去实现
            //不然默认布局里的一些控件就会找不到而报错
            if (builder.contentView != null) {
                mContentView = builder.contentView;
            } else if (builder.contentViewId != 0) {
                mContentView = LayoutInflater.from(getContext()).inflate(builder.contentViewId, null);
            } else {
                //使用默认的布局，最左边是一个图标，中间文字，右边按钮文字，默认隐藏
                mContentView = LayoutInflater.from(getContext()).inflate(R.layout.custom_toolbar, null);
                ImageView toolbarLeftButton = mContentView.findViewById(R.id.toolbar_leftButton);
                View toolbarLeftLayout = mContentView.findViewById(R.id.toolbar_leftLayout);
                ImageView toolbarRightButton = mContentView.findViewById(R.id.toolbar_rightButton);
                View toolbarRightLayout = mContentView.findViewById(R.id.toolbar_rightLayout);
                TextView toolbarTitle = mContentView.findViewById(R.id.toolbar_title);
                TextView toolbarRightText = mContentView.findViewById(R.id.toolbar_right_text);
                //背景色
                if (builder.backGroundColor != 0) {
                    setBackgroundColor(builder.backGroundColor);
                } else {
                    setBackgroundColor(Color.WHITE);
                }
                if (!builder.showLeftIcon) {
                    toolbarLeftButton.setVisibility(View.GONE);
                } else {
                    //设置左边的icon
                    if (builder.leftIconDrawable != null) {
                        toolbarLeftButton.setImageDrawable(builder.leftIconDrawable);
                    } else if (builder.leftIconRes != 0) {
                        toolbarLeftButton.setImageResource(builder.leftIconRes);
                    } else {
                        //使用默认的按钮
                        toolbarLeftButton.setImageResource(R.drawable.icon_back_blue);
                    }
                }
                //设置右边的icon,默认隐藏,没有默认按钮
                if (builder.rightIconDrawable != null) {
                    toolbarRightButton.setVisibility(View.VISIBLE);
                    toolbarRightButton.setImageDrawable(builder.rightIconDrawable);
                } else if (builder.rightIconRes != 0) {
                    toolbarRightButton.setVisibility(View.VISIBLE);
                    toolbarRightButton.setImageResource(builder.rightIconRes);
                }
                if (!TextUtils.isEmpty(builder.rightText)) {
                    toolbarRightText.setVisibility(View.VISIBLE);
                    toolbarRightText.setText(builder.rightText);
                    if (builder.rightTextColor != 0) {
                        toolbarRightText.setTextColor(builder.rightTextColor);
                    } else if (builder.rightTextColorRes != 0) {
                        toolbarRightText.setTextColor(ContextCompat.getColor(builder.context, builder.rightTextColorRes));
                    }
                    if (builder.rightTextSize != 0) {
                        toolbarRightText.setTextSize(builder.rightTextSize);
                    }
                }

                //设置右边的文字,默认隐藏
                if (!TextUtils.isEmpty(builder.title)) {
                    toolbarTitle.setText(builder.title);
                    if (builder.titleColor != 0) {
                        toolbarTitle.setTextColor(builder.titleColor);
                    } else if (builder.titleColorRes != 0) {
                        toolbarTitle.setTextColor(ContextCompat.getColor(builder.context, builder.titleColorRes));
                    }
                    if (builder.titleSize != 0) {
                        toolbarTitle.setTextSize(builder.titleSize);
                    }
                }
                if (builder.leftIconOnClickListener != null) {
                    toolbarLeftLayout.setOnClickListener(builder.leftIconOnClickListener);
                } else {
                    toolbarLeftLayout.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (getContext() instanceof Activity) {
                                ((Activity) getContext()).finish();
                            }
                        }
                    });
                }
                if (builder.rightIconOnClickListener != null) {
                    toolbarRightLayout.setOnClickListener(builder.rightIconOnClickListener);
                }
                if (builder.rightTextOnClickListener != null) {
                    toolbarRightLayout.setOnClickListener(builder.rightTextOnClickListener);
                }
            }
            addView(mContentView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }

    }

    /**
     * 更新标题
     *
     * @param title 新的title
     */
    public void updateTitle(String title) {
        TextView toolbarTitle = mContentView.findViewById(R.id.toolbar_title);
        if (toolbarTitle != null) {
            toolbarTitle.setText(title);
        }
    }

    /**
     * 获取view
     *
     * @return view
     */
    public View getContentView() {
        return mContentView;
    }


    public static class Builder {
        /**
         * context
         */
        private Context context;
        /**
         * 中间的标题
         */
        String title;
        /**
         * 中间的标题颜色
         */
        int titleColor;
        /**
         * 中间的标题颜色res
         */
        int titleColorRes;
        /**
         * 标题字的大小
         */
        int titleSize;

        /**
         * 是否显示左边的图标
         */
        boolean showLeftIcon = true;
        /**
         * 左边的图标
         */
        Drawable leftIconDrawable;
        /**
         * 左边的图标
         */
        int leftIconRes;
        /**
         * 左边的图标
         */
        Drawable rightIconDrawable;
        /**
         * 右边的图标
         */
        int rightIconRes;
        /**
         * 右边的文字
         */
        String rightText;
        /**
         * 右边的文字颜色
         */
        int rightTextColor;
        /**
         * 右边的文字颜色
         */
        int rightTextColorRes;
        /**
         * 右边的文字
         */
        int rightTextSize;
        /**
         * toolbar的布局id
         */
        int contentViewId;
        /**
         * toolbar的布局View
         */
        View contentView;

        /**
         * 背景色
         */
        int backGroundColor;

        /**
         * 左边图标的点击事件
         */
        View.OnClickListener leftIconOnClickListener;
        /**
         * 右边图标的点击事件
         */
        View.OnClickListener rightIconOnClickListener;
        /**
         * 右边文字的点击事件
         */
        View.OnClickListener rightTextOnClickListener;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder title(String title) {
            this.title = title;
            return this;
        }

        public Builder titleColor(int titleColor) {
            this.titleColor = titleColor;
            return this;
        }

        public Builder titleColorRes(int titleColorRes) {
            this.titleColorRes = titleColorRes;
            return this;
        }

        public Builder titleSize(int titleSize) {
            this.titleSize = titleSize;
            return this;
        }

        public Builder leftIconDrawable(Drawable leftIconDrawable) {
            this.leftIconDrawable = leftIconDrawable;
            return this;
        }

        public Builder leftIconRes(int leftIconRes) {
            this.leftIconRes = leftIconRes;
            return this;
        }

        public Builder leftIconRes(boolean showLeftIcon) {
            this.showLeftIcon = showLeftIcon;
            return this;
        }


        public Builder rightIconDrawable(Drawable rightIconDrawable) {
            this.rightIconDrawable = rightIconDrawable;
            return this;
        }

        public Builder rightIconRes(int rightIconRes) {
            this.rightIconRes = rightIconRes;
            return this;
        }

        public Builder rightText(String rightText) {
            this.rightText = rightText;
            return this;
        }

        public Builder rightTextColor(@ColorInt int rightTextColor) {
            this.rightTextColor = rightTextColor;
            return this;
        }

        public Builder rightTextColorRes(@ColorRes int rightTextColorRes) {
            this.rightTextColorRes = rightTextColorRes;
            return this;
        }

        public Builder rightTextSize(int rightTextSize) {
            this.rightTextSize = rightTextSize;
            return this;
        }

        public Builder contentViewId(int contentViewId) {
            this.contentViewId = contentViewId;
            return this;
        }

        public Builder contentView(View contentView) {
            this.contentView = contentView;
            return this;
        }

        public Builder backGroundColor(int backGroundColor) {
            this.backGroundColor = backGroundColor;
            return this;
        }

        public Builder leftIconOnClickListener(View.OnClickListener leftIconOnClickListener) {
            this.leftIconOnClickListener = leftIconOnClickListener;
            return this;
        }

        public Builder rightIconOnClickListener(View.OnClickListener rightIconOnClickListener) {
            this.rightIconOnClickListener = rightIconOnClickListener;
            return this;
        }

        public Builder rightTextOnClickListener(View.OnClickListener rightTextOnClickListener) {
            this.rightTextOnClickListener = rightTextOnClickListener;
            return this;
        }

        public CustomToolbar build() {
            return new CustomToolbar(context, this);
        }
    }
}

