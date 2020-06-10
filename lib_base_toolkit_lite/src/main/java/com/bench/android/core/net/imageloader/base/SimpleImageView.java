package com.bench.android.core.net.imageloader.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.UiThread;

import com.android.library.R;
import com.bench.android.core.net.imageloader.DisplayOptimizeListener;
import com.bench.android.core.net.imageloader.RoundImageView;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

/**
 * 仅跟全局加载策略有关
 * <p>
 * 根据BaseApplication中设置的图片加载策略使用对应的图片控件
 * 如果全局加载使用FrescoLoader  就会使用对应的  SimpleDraweeView，（大长图）SubsamplingScaleImageView
 * 如果全局加载使用PicassoLoader       对应ImageView
 * <p>
 * 单独设置策略的时候千万不能使用这个，如果和全局不一样，会报错，你懂得
 */
public class SimpleImageView extends FrameLayout {
    private final float DEFAULT_RADIUS = 12;
    private SubsamplingScaleImageView subsamplingScaleImageView;
    private ProgressBar progressBar;
    private ImageView imageView;
    private boolean isLong;     //大长图
    private boolean isRound;    //四个角圆角
    private float cornerRadius;  //圆角的度数
    private String width, height;

    public SimpleImageView(Context context) {
        super(context);
        init(null, 0, 0);
    }

    public SimpleImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0, 0);
    }

    public SimpleImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs, defStyleAttr, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SimpleImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs, defStyleAttr, defStyleRes);

    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        imageView.setVisibility(visibility);
    }

    private void init(AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.SimpleImageView);
            isLong = a.getBoolean(R.styleable.SimpleImageView_isLong, false);
            isRound = a.getBoolean(R.styleable.SimpleImageView_isRound, false);
            cornerRadius = a.getDimension(R.styleable.SimpleImageView_cornerRadius, DEFAULT_RADIUS);
            width = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_width");
            height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
            a.recycle();
        }
        int layoutWidth = LayoutParams.MATCH_PARENT;
        int layoutHeight = LayoutParams.MATCH_PARENT;


        try {
             layoutWidth = Integer.parseInt(this.width);
        }catch (Exception e){
            e.printStackTrace();
        }
        try {
            layoutHeight = Integer.parseInt(this.height);
        }catch (Exception e){
            e.printStackTrace();
        }

        LayoutParams llps = new LayoutParams(layoutWidth, layoutHeight);
        llps.bottomMargin = 0;
        llps.leftMargin = 0;
        llps.topMargin = 0;
        llps.rightMargin = 0;
        if (isLong) {
            subsamplingScaleImageView = new SubsamplingScaleImageView(getContext(), attrs);
            subsamplingScaleImageView.setPadding(0, 0, 0, 0);
            subsamplingScaleImageView.setLayoutParams(llps);
            addView(subsamplingScaleImageView);
            progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
            LayoutParams params = new LayoutParams(150, 150);
            params.gravity = Gravity.CENTER;
            addView(progressBar, params);
        } else {
            if (isRound) {
                imageView = new RoundImageView(getContext(), attrs, defStyleAttr);
                ((RoundImageView) imageView).setCornerRadius(cornerRadius);
            } else {
                imageView = new ImageView(getContext(), attrs, defStyleAttr);
            }
            imageView.setLayoutParams(llps);
            imageView.setPadding(0, 0, 0, 0);
            llps.gravity = Gravity.CENTER;
            imageView.setAdjustViewBounds(true);
            addView(imageView);
        }
    }

    public View getView() {
        if (isLong) {
            return subsamplingScaleImageView;
        } else {
            return imageView;
        }
    }

    @UiThread
    public void setLongImage(final File file) {
        subsamplingScaleImageView.post(new Runnable() {
            @Override
            public void run() {
                DisplayOptimizeListener mDisplayOptimizeListener = new DisplayOptimizeListener(subsamplingScaleImageView);
                subsamplingScaleImageView.setOnImageEventListener(mDisplayOptimizeListener);
                subsamplingScaleImageView.setImage(ImageSource.uri(Uri.fromFile(file)));
            }
        });
    }

    public void removeProgressBar() {
        progressBar.post(new Runnable() {
            @Override
            public void run() {
                removeView(progressBar);
            }
        });
    }


    public void setImageBitmap(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }
}
