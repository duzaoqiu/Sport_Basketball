package com.bench.android.core.net.imageloader.base;

/**
 * Created by zaozao on 2019/1/25.
 * 作用：图片加载回调
 */
public interface ImageLoaderCallBack<T> {

    /**
     * 返回bitmap
     * GifDrawable
     * File file
     *
     * @param bitmap
     */
    void loadSuccess(T bitmap);
//
//    /**
//     * 返回file，用于SubsamplingScaleImageView
//     *
//     * @param file
//     */
//    void loadFileSuccess(File file);
//
//    /**
//     * GifDrawable，用于gif
//     *
//     * @param drawable
//     */
//    void loadGifSuccess(GifDrawable drawable);

    /**
     * 加载失败
     */
    void loadFailed(Exception e);

}
