package com.bench.android.core.net.imageloader.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class CircleTransformation extends BitmapTransformation {

    private static final String ID = "com.android.library.tools.ImageLoader.glide.CircleTransformation";

    private int strokeColor;
    private int strokeWidth;//默认描边宽度

    private Paint paint;
    private Paint paintCircle;

    public CircleTransformation(int strokeColor, int strokeWidth) {
        this.strokeColor = strokeColor;
        this.strokeWidth = strokeWidth;

        paint = new Paint(Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        paintCircle = new Paint(Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap inBitmap, int outWidth, int outHeight) {
        int destMinEdge = Math.min(outWidth, outHeight);
        float radius = destMinEdge / 2f;

        int srcWidth = inBitmap.getWidth();
        int srcHeight = inBitmap.getHeight();

        float scaleX = destMinEdge / (float) srcWidth;
        float scaleY = destMinEdge / (float) srcHeight;
        float maxScale = Math.max(scaleX, scaleY);

        float scaledWidth = maxScale * srcWidth;
        float scaledHeight = maxScale * srcHeight;
        float left = (destMinEdge - scaledWidth) / 2f;
        float top = (destMinEdge - scaledHeight) / 2f;

        RectF destRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);


        // Alpha is required for this transformation.
        Bitmap toTransform = getAlphaSafeBitmap(pool, inBitmap);

        Bitmap.Config outConfig = getAlphaSafeConfig(inBitmap);
        Bitmap result = pool.get(destMinEdge, destMinEdge, outConfig);
        result.setHasAlpha(true);

        Canvas canvas = new Canvas(result);
        // Draw a circle
        canvas.drawCircle(radius, radius, radius, paintCircle);
        // Draw the bitmap in the circle
        canvas.drawBitmap(toTransform, null, destRect, paint);

        if (strokeColor != 0) {
            Paint paintStroke = new Paint(Paint.DITHER_FLAG | Paint.FILTER_BITMAP_FLAG | Paint.ANTI_ALIAS_FLAG);
            paintStroke.setStyle(Paint.Style.STROKE);
            paintStroke.setColor(strokeColor);
            paintStroke.setStrokeWidth(strokeWidth);
            float borderRadius = radius - (strokeWidth >> 1);
            canvas.drawCircle(radius, radius, borderRadius, paintStroke);
        }

        canvas.setBitmap(null);

        if (!toTransform.equals(inBitmap)) {
            pool.put(toTransform);
        }

        return result;
    }

    private Bitmap getAlphaSafeBitmap(
            @NonNull BitmapPool pool, @NonNull Bitmap maybeAlphaSafe) {
        Bitmap.Config safeConfig = getAlphaSafeConfig(maybeAlphaSafe);
        if (safeConfig.equals(maybeAlphaSafe.getConfig())) {
            return maybeAlphaSafe;
        }

        Bitmap argbBitmap =
                pool.get(maybeAlphaSafe.getWidth(), maybeAlphaSafe.getHeight(), safeConfig);
        new Canvas(argbBitmap).drawBitmap(maybeAlphaSafe, 0 /*left*/, 0 /*top*/, null /*paint*/);

        // We now own this Bitmap. It's our responsibility to replace it in the pool outside this method
        // when we're finished with it.
        return argbBitmap;
    }

    @NonNull
    private Bitmap.Config getAlphaSafeConfig(@NonNull Bitmap inBitmap) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Avoid short circuiting the sdk check.
            if (Bitmap.Config.RGBA_F16.equals(inBitmap.getConfig())) { // NOPMD
                return Bitmap.Config.RGBA_F16;
            }
        }

        return Bitmap.Config.ARGB_8888;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + strokeColor * 100 + strokeWidth * 10;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof CircleTransformation) {
            CircleTransformation other = (CircleTransformation) obj;
            return strokeColor == other.strokeColor && strokeWidth == other.strokeWidth;
        }
        return false;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + strokeColor + strokeWidth).getBytes(CHARSET));
    }

}
