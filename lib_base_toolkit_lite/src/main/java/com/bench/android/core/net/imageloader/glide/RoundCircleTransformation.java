package com.bench.android.core.net.imageloader.glide;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bench.android.core.net.imageloader.base.RoundRadiusType;
import com.bench.android.core.util.LogUtils;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;

import java.security.MessageDigest;

public class RoundCircleTransformation extends BitmapTransformation {

    private static final String ID = "com.android.library.tools.ImageLoader.glide.RoundCircleTransformation";

    private int radius;
    private int cornersType;

    public RoundCircleTransformation(int roundPx, int cornersType) {
        this.radius = roundPx;
        this.cornersType = cornersType;
    }

    /**
     * 左上角变直角
     *
     * @param canvas
     * @param paint
     * @param offset
     * @param width
     * @param height
     */
    private void clipTopLeft(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(0, 0, offset, offset);
        canvas.drawRect(block, paint);
    }

    /**
     * 右上角变直角
     *
     * @param canvas
     * @param paint
     * @param offset
     * @param width
     * @param height
     */
    private void clipTopRight(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(width - offset, 0, width, offset);
        canvas.drawRect(block, paint);
    }

    /**
     * 左下角变直角
     *
     * @param canvas
     * @param paint
     * @param offset
     * @param width
     * @param height
     */
    private void clipBottomLeft(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(0, height - offset, offset, height);
        canvas.drawRect(block, paint);
    }

    /**
     * 右下角变直角
     *
     * @param canvas
     * @param paint
     * @param offset
     * @param width
     * @param height
     */
    private void clipBottomRight(final Canvas canvas, final Paint paint, int offset, int width, int height) {
        final Rect block = new Rect(width - offset, height - offset, width, height);
        canvas.drawRect(block, paint);
    }

    @Override
    protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform, int outWidth, int outHeight) {
        final int width = toTransform.getWidth();
        final int height = toTransform.getHeight();

        Bitmap paintingBoard = pool.get(width, height, Bitmap.Config.ARGB_8888);
        paintingBoard.setHasAlpha(true);
        Canvas canvas = new Canvas(paintingBoard);
        canvas.drawARGB(Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT, Color.TRANSPARENT);

        final Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.BLACK);

        //画出4个圆角
        final RectF rectF = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rectF, radius, radius, paint);

        switch (cornersType) {
            case RoundRadiusType.CORNER_ALL:
                break;
            case RoundRadiusType.CORNER_TOP:
                clipBottomLeft(canvas,paint,radius,width,height);
                clipBottomRight(canvas, paint, radius, width, height);
                break;
            case RoundRadiusType.CORNER_BOTTOM:
                clipTopLeft(canvas,paint,radius,width,height);
                clipTopRight(canvas, paint, radius, width, height);
                break;
            case RoundRadiusType.CORNER_LEFT:
                clipBottomRight(canvas, paint, radius, width, height);
                clipTopRight(canvas, paint, radius, width, height);
                break;
            case RoundRadiusType.CORNER_RIGHT:
                clipTopLeft(canvas,paint,radius,width,height);
                clipBottomLeft(canvas,paint,radius,width,height);
                break;
            default:
                break;
        }
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        //帖子图
        final Rect src = new Rect(0, 0, width, height);
        final Rect dst = src;
        canvas.drawBitmap(toTransform, src, dst, paint);
        return paintingBoard;
    }

    @Override
    public int hashCode() {
        return ID.hashCode() + radius * 100 + cornersType * 10;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof RoundCircleTransformation) {
            RoundCircleTransformation other = (RoundCircleTransformation) obj;
            return other.radius == radius && other.cornersType == cornersType;
        }
        return false;
    }

    @Override
    public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {
        messageDigest.update((ID + radius + cornersType).getBytes(CHARSET));
        LogUtils.e("kkk","updateDiskCacheKey---"+messageDigest.toString());
    }
}
