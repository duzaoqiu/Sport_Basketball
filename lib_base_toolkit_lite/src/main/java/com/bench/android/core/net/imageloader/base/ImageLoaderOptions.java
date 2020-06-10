package com.bench.android.core.net.imageloader.base;

import android.graphics.Bitmap;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.View;

import java.io.File;
import java.util.HashMap;

/**
 * Created by zaozao on 2019/1/25.
 * 作用：图片加载涉及到的一些选项配置，
 * 以及可以设置图片加载框架imageLoadStrategy
 * <p>
 * 设置具体的参数，设计为 Builder 模式，方便拆分功能
 */
public class ImageLoaderOptions {
    public ImageLoaderStrategy imageLoader;
    public View targetView;

    public String imageUrl;
    public File imageFile;
    public int drawableResId;
    public Uri uri;
    public Bitmap bitmap;

    //占位图
    public Drawable placeholder;
    public int placeholderResId;

    /**
     * 加载失败的时候的图片
     */
    public int errorResId;

    public boolean isCrop;
    public boolean isCenter;

    public boolean isGif;

    //宽高
    public int targetWidth;
    public int targetHeight;

    /**
     * 圆形
     */
    public boolean isCircle;
    /**
     * 圆形描边
     */
    public int strokeColor = 0;
    public int strokeWidth = 2;  //圆形描边fresco和picasso都支持， 非圆形描边fresco支持，picasso不支持
    /**
     * 圆角
     * 默认四角圆角
     */
    public int roundCircleType = RoundRadiusType.CORNER_TOP;
    /**
     * 圆角角度
     */
    public int imageRadius;

    /**
     * /旋转角度.注意:picasso针对三星等本地图片，
     * 默认旋转回0度，即正常位置。此时不需要自己rotate
     */
    public float degrees = 0;

//    //是否缓存到本地
//    public boolean skipLocalCache;
//    public boolean skipNetCache;

    public PointF frescoScalePoint;

    public Bitmap.Config config = Bitmap.Config.RGB_565;

    /**
     * 是否长图
     */
    public boolean isLong;
    /**
     * 图片加载回调
     */
    public ImageLoaderCallBack callBack;

    private boolean autoAnimation = true;

    public boolean isDownloadOnly = false;
    /**
     * 预加载的判断
     */
    public boolean isPreLoad = false;
    //预留
    private HashMap<Object, Object> mMap = new HashMap<>();

    public ImageLoaderOptions(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ImageLoaderOptions(File imageFile) {
        this.imageFile = imageFile;
    }

    public ImageLoaderOptions(int drawableResId) {
        this.drawableResId = drawableResId;
    }

    public ImageLoaderOptions(Uri uri) {
        this.uri = uri;
    }

    public ImageLoaderOptions(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public ImageLoaderOptions put(Object key, Object value) {
        mMap.put(key, value);
        return this;
    }

    public Object get(Object key) {
        if (mMap.containsKey(key)) {
            return mMap.get(key);
        } else {
            return null;
        }
    }

    public ImageLoaderOptions setTargetView(View targetView) {
        this.targetView = targetView;
        return this;
    }

    /**
     * 现在默认使用GlideLoder了，不需要使用这个了，后期稳定后会删掉
     *
     * @return
     */
    @Deprecated
    public ImageLoaderOptions setImageLoaderStrategy(ImageLoaderStrategy imageLoader) {
        this.imageLoader = imageLoader;
        return this;
    }

    /**
     * 显示view
     *
     * @param targetView
     */
    public void into(View targetView) {
        this.targetView = targetView;
        ImageLoaderHelper.getInstance().toLoadImage(this);
        return;
    }

    /**
     * 显示view带回调
     */
    public void intoWithCallBack(ImageLoaderCallBack callBack, View targetView) {
        this.targetView = targetView;
        callback(callBack, targetView);
        ImageLoaderHelper.getInstance().toLoadImage(this);
        return;
    }

    /**
     * 下载显示，并保存到本地
     *
     * @param targetView
     */
    public void intoLocalSave(View targetView) {
        this.targetView = targetView;
        ImageLoaderHelper.getInstance().toLoadImage(this);
        return;
    }

    /**
     * 回调回去 并且显示
     *
     * @param callBack
     */
    public void callback(ImageLoaderCallBack callBack, View targetView) {
        this.callBack = callBack;
        this.targetView = targetView;
        ImageLoaderHelper.getInstance().toLoadImage(this);
        return;
    }

    /**
     * 回调回去
     *
     * @param callBack
     */
    public void callback(ImageLoaderCallBack callBack) {
        this.callBack = callBack;
        ImageLoaderHelper.getInstance().toLoadImage(this);
        return;
    }

    /**
     * ----------------------------下面是一些图片加载选项的设置
     */
    public ImageLoaderOptions placeholder(Drawable placeholder) {
        this.placeholder = placeholder;
        return this;
    }

    public ImageLoaderOptions placeholderResId(int placeholderResId) {
        this.placeholderResId = placeholderResId;
        return this;
    }

    public ImageLoaderOptions errorResId(int errorResId) {
        this.errorResId = errorResId;
        return this;
    }

    public ImageLoaderOptions centerCrop(boolean isCrop) {
        this.isCrop = isCrop;
        return this;
    }

    public ImageLoaderOptions centerCrop(boolean isCrop, PointF frescoScalePoint) {
        this.isCrop = isCrop;
        this.frescoScalePoint = frescoScalePoint;
        return this;
    }

    public ImageLoaderOptions centerInside(boolean isCenter) {
        this.isCenter = isCenter;
        return this;
    }

    public ImageLoaderOptions circle(boolean isCircle) {
        this.isCircle = isCircle;
        return this;
    }

    public ImageLoaderOptions asGif(boolean isGif) {
        this.isGif = isGif;
        return this;
    }

    public ImageLoaderOptions roundCircleRadius(int roundCircleType, int imageRadius) {
        this.roundCircleType = roundCircleType;
        this.imageRadius = imageRadius;
        return this;
    }

    public ImageLoaderOptions roundCircleRadius(int imageRadius) {
        this.imageRadius = imageRadius;
        return this;
    }

    public ImageLoaderOptions stroke(int strokeColor) {
        this.strokeColor = strokeColor;
        return this;
    }

    public ImageLoaderOptions stroke(int strokeColor, int strokeWidthDp) {
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidthDp;
        return this;
    }

    public ImageLoaderOptions resize(int targetWidth, int targetHeight) {
        this.targetHeight = targetHeight;
        this.targetWidth = targetWidth;
        return this;
    }


    public ImageLoaderOptions rotate(float degrees) {
        this.degrees = degrees;
        return this;
    }

    public ImageLoaderOptions setLong(boolean aLong) {
        isLong = aLong;
        return this;
    }

    public boolean isAutoAnimation() {
        return autoAnimation;
    }

    public ImageLoaderOptions autoAnimation(boolean autoAnimation) {
        this.autoAnimation = autoAnimation;
        return this;
    }

    /**
     * 图片下载，或者预加载
     *
     * @param downloadOnly
     * @return
     */
    public ImageLoaderOptions downloadOnly(boolean downloadOnly) {
        this.isDownloadOnly = downloadOnly;
        return this;
    }


    public ImageLoaderOptions config(Bitmap.Config config) {
        this.config = config;
        return this;
    }

    /**
     * 设置预加载
     *
     * @return
     */
    public ImageLoaderOptions preload(View targetView) {
        this.isPreLoad = true;
        this.targetView = targetView;
        return this;
    }
}
