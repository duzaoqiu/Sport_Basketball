package com.bench.android.core.net.imageloader.glide;


import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bench.android.core.app.application.BaseApplication;
import com.bench.android.core.net.imageloader.base.ImageLoaderCallBack;
import com.bench.android.core.net.imageloader.base.ImageLoaderOptions;
import com.bench.android.core.net.imageloader.base.ImageLoaderStrategy;
import com.bench.android.core.net.imageloader.base.SimpleImageView;
import com.bench.android.core.net.imageloader.utils.SubsamplingScaleImageViewUtils;
import com.bench.android.core.util.dimension.PxConverter;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by zaozao on 2019/1/25.
 * 作用：
 */
public class GlideLoader implements ImageLoaderStrategy {

    @Override
    public void loadImage(final ImageLoaderOptions options) {
        if (options.isDownloadOnly) {
            Glide.with(BaseApplication.getContext())
                    .downloadOnly()
                    .load(options.imageUrl)
                    .listener(new LoadRequestListener<File>(options.callBack))
                    .preload();
            return;
        }
        if ((options.targetView == null)) {
            throw new NullPointerException("options.targetView must not be null");
        }

        if (options.isLong) { //----------------------------------大长图

            final SubsamplingScaleImageView imageView;

            if (options.targetView instanceof SimpleImageView) {
                if (options.targetWidth > 0) {
                    ViewGroup.LayoutParams params = options.targetView.getLayoutParams();
                    params.width = options.targetWidth;
                    params.height = options.targetHeight;
                    options.targetView.setLayoutParams(params);
                }
                imageView = (SubsamplingScaleImageView) (((SimpleImageView) options.targetView).getView());
            } else if (options.targetView instanceof SubsamplingScaleImageView) {
                imageView = (SubsamplingScaleImageView) (options.targetView);
            } else {
                throw new NullPointerException("targetView must instanceof SimpleImageView or SubsamplingScaleImageView");
            }

            Glide.with(imageView).downloadOnly().load(options.imageUrl).into(new SimpleTarget<File>() {
                @Override
                public void onResourceReady(@NonNull File resource, @Nullable Transition<? super File> transition) {
                    SubsamplingScaleImageViewUtils.setImage(imageView, resource);
                    if (imageView.getParent() instanceof SimpleImageView) {
                        ((SimpleImageView) imageView.getParent()).removeProgressBar();
                    }
                }
            });

        } else {//----------------------------普通图片，gif图片

            ImageView imageView;

            if (options.targetView instanceof SimpleImageView) {
                ViewGroup.LayoutParams params = options.targetView.getLayoutParams();
                if (options.targetWidth > 0) {

                    params.width = options.targetWidth;
                    params.height = options.targetHeight;
                    options.targetView.setLayoutParams(params);
                } else {
                    options.targetView.setLayoutParams(params);
                }
                imageView = (ImageView) (((SimpleImageView) options.targetView).getView());
            } else if (options.targetView instanceof ImageView) {
                imageView = (ImageView) (options.targetView);
            } else {
                throw new NullPointerException("targetView must instanceof SimpleImageView or ImageView");
            }

            RequestOptions requestOptions = new RequestOptions();
            ArrayList<Transformation<Bitmap>> transformations = new ArrayList<>();
            if (options.placeholder != null) {
                requestOptions.placeholder(options.placeholder);
            }
            if (options.placeholderResId > 0) {
                requestOptions.placeholder(options.placeholderResId);
            }

            if (options.errorResId > 0) {
                requestOptions.error(options.errorResId);
            }
            if (options.isCrop) {
                transformations.add(new CenterCrop());
            }
            if (options.isCenter) {
                transformations.add(new CenterInside());
            }

            if (options.targetHeight > 0 && options.targetWidth > 0) {
                requestOptions.override(options.targetWidth, options.targetHeight);
            }

            if (options.degrees != 0) {
                transformations.add(new RotateTransformation(options.degrees));
            }
            if (options.isCircle) {
                transformations.add(new CircleTransformation(options.strokeColor, PxConverter.dp2px(BaseApplication.getContext(), options.strokeWidth)));
            } else if (options.imageRadius != 0) {
                transformations.add(new RoundCircleTransformation(options.imageRadius, options.roundCircleType));
            }
            if (transformations.size() > 0) {
                requestOptions.transform(new MultiTransformation<Bitmap>(transformations));
            }
            RequestBuilder requestBuilder;
            if (options.isGif) {
                requestBuilder = Glide.with(options.targetView).asGif();
                if (!TextUtils.isEmpty(options.imageUrl)) {
                    requestBuilder.load(options.imageUrl.startsWith("http") || options.imageUrl.startsWith("https") ? options.imageUrl : "file://" + options.imageUrl);
                } else if (options.imageFile != null) {
                    requestBuilder.load(options.imageFile);
                } else if (options.drawableResId != 0) {
                    requestBuilder.load(options.drawableResId);
                } else if (options.uri != null) {
                    requestBuilder.load(options.uri);
                } else {
                    requestBuilder.load("");
                }
                requestBuilder.apply(requestOptions);
                requestBuilder.listener(new LoadRequestListener<GifDrawable>(options.callBack));

            } else {
                requestBuilder = Glide.with(options.targetView).asDrawable();
                if (!TextUtils.isEmpty(options.imageUrl)) {
                    requestBuilder.load(options.imageUrl.startsWith("http") || options.imageUrl.startsWith("https") ? options.imageUrl : "file://" + options.imageUrl);
                } else if (options.imageFile != null) {
                    requestBuilder.load(options.imageFile);
                } else if (options.drawableResId != 0) {
                    requestBuilder.load(options.drawableResId);
                } else if (options.uri != null) {
                    requestBuilder.load(options.uri);
                } else if (options.bitmap != null) {
                    requestBuilder = Glide.with(imageView.getContext()).load(options.bitmap);
                } else {
                    requestBuilder.load("");
                }
                requestBuilder.apply(requestOptions);
                requestBuilder.listener(new LoadRequestListener<Bitmap>(options.callBack));
            }

            //判断预加载
            if (options.isPreLoad) {
                if (options.targetWidth != 0 && options.targetHeight != 0) {
                    requestBuilder.preload(options.targetWidth, options.targetHeight);
                } else {
                    requestBuilder.preload();
                }
            } else {
                requestBuilder.into(imageView);
            }
        }
    }

    class LoadRequestListener<T> implements RequestListener<T> {

        private ImageLoaderCallBack callback;

        public LoadRequestListener(ImageLoaderCallBack imageLoaderCallBack) {
            callback = imageLoaderCallBack;
        }

        @Override
        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<T> target, boolean isFirstResource) {
            if (callback == null) {
                return false;
            }
            callback.loadFailed(e);
            return false;
        }

        @Override
        public boolean onResourceReady(T resource, Object model, Target<T> target, DataSource dataSource, boolean isFirstResource) {
            if (callback == null) {
                return false;
            }
            callback.loadSuccess(resource);
            return false;
        }
    }


//    class BitmapRequestListener implements RequestListener<Bitmap> {
//
//        private ImageLoaderCallBack callback;
//
//        public BitmapRequestListener(ImageLoaderCallBack imageLoaderCallBack) {
//            callback = imageLoaderCallBack;
//        }
//
//        @Override
//        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
//            if (callback == null) {
//                return false;
//            }
//            callback.loadFailed(e);
//            return false;
//        }
//
//        @Override
//        public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
//            if (callback == null) {
//                return false;
//            }
//            callback.loadSuccess(resource);
//            return false;
//        }
//    }
//
//
//    class GifDrawableRequestListener implements RequestListener<GifDrawable> {
//
//        private ImageLoaderCallBack callback;
//
//        public GifDrawableRequestListener(ImageLoaderCallBack imageLoaderCallBack) {
//            callback = imageLoaderCallBack;
//        }
//
//
//        @Override
//        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
//            if (callback == null) {
//                return false;
//            }
//            callback.loadFailed(e);
//            return false;
//        }
//
//        @Override
//        public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
//            if (callback == null) {
//                return false;
//            }
//            callback.loadSuccess(resource);
//            return false;
//        }
//    }
//
//
//    class FileRequestListener implements RequestListener<File> {
//
//        private ImageLoaderCallBack callback;
//
//        public FileRequestListener(ImageLoaderCallBack imageLoaderCallBack) {
//            callback = imageLoaderCallBack;
//        }
//
//        @Override
//        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<File> target, boolean isFirstResource) {
//            if (callback == null) {
//                return false;
//            }
//            callback.loadFailed(e);
//            return false;
//        }
//
//        @Override
//        public boolean onResourceReady(File resource, Object model, Target<File> target, DataSource dataSource, boolean isFirstResource) {
//            if (callback == null) {
//                return false;
//            }
//            callback.loadSuccess(resource);
//            return false;
//        }
//    }

}
