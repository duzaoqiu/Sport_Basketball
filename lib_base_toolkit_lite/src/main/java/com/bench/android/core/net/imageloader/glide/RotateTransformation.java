package com.bench.android.core.net.imageloader.glide;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;

import java.security.MessageDigest;

public class RotateTransformation extends BitmapTransformation {

    private static final String ID = "com.android.library.tools.ImageLoader.glide.RotateTransformation";

    private float degrees;

    public RotateTransformation(float degrees) {
        this.degrees = degrees;
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        return TransformationUtils.rotateImage(toTransform, (int) degrees);
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + (int) degrees * 10;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof RotateTransformation) {
            return ((RotateTransformation) obj).degrees == degrees;
        }
        return false;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + degrees).getBytes(CHARSET));
    }
}
