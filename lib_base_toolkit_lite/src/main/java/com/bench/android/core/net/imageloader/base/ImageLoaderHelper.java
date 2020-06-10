package com.bench.android.core.net.imageloader.base;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;

import com.bench.android.core.net.imageloader.glide.GlideLoader;

import java.io.File;

/**
 * @author Created by zaozao on 2019/1/25.
 * 作用：策略者，调度者
 */
public class ImageLoaderHelper {

    private static ImageLoaderHelper imageInstance;

    private static ImageLoaderStrategy imageLoader;

    public static ImageLoaderHelper getInstance() {

        if (imageInstance == null) {
            synchronized (ImageLoaderHelper.class) {
                if (imageInstance == null) {
                    imageInstance = new ImageLoaderHelper();
                }
            }
        }
        return imageInstance;
    }


    /**
     * 提供全局替换图片加载框架的接口，若切换其它框架，可以实现一键全局替换
     */
    @Deprecated
    public void setImageLoaderStrategy(ImageLoaderStrategy imageLoaderType) {
        imageLoader = imageLoaderType;
    }


    public ImageLoaderOptions load(String url) {
        return new ImageLoaderOptions(url);
    }

    public ImageLoaderOptions load(Bitmap bitmap) {
        return new ImageLoaderOptions(bitmap);
    }

    /**
     * 加载圆形图片
     *
     * @param url
     * @return
     */
    public ImageLoaderOptions loadRound(String url) {
        return new ImageLoaderOptions(url).circle(true);
    }

    public ImageLoaderOptions loadRoundWithStroke(String url) {
        return new ImageLoaderOptions(url).circle(true).stroke(Color.WHITE);
    }

    public ImageLoaderOptions load(File file) {
        return new ImageLoaderOptions(file);
    }

    public ImageLoaderOptions load(int drawableResId) {
        return new ImageLoaderOptions(drawableResId);
    }

    public ImageLoaderOptions load(Uri uri) {
        return new ImageLoaderOptions(uri);
    }

    /**
     * 优先使用局部设置的的图片加载框架，其次使用全局图片加载框架
     *
     * @param loaderOptions
     */
    public void toLoadImage(ImageLoaderOptions loaderOptions) {
        if (loaderOptions.imageLoader != null) {
            loaderOptions.imageLoader.loadImage(loaderOptions);
        } else {
            checkImageLoaderNotNull();
            imageLoader.loadImage(loaderOptions);
        }
    }

    private static void checkImageLoaderNotNull() {
        if (imageLoader == null) {
            imageLoader = new GlideLoader();
        }
    }

}



