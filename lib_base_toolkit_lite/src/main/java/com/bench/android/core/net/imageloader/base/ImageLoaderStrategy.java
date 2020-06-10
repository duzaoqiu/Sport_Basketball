package com.bench.android.core.net.imageloader.base;

/**
 * Created by zaozao on 2019/1/25.
 * 作用：图片加载框架需要实现的接口
 */
public interface ImageLoaderStrategy {
    /**
     * 加载图片
     * @param options
     */
    void loadImage(ImageLoaderOptions options);
}
