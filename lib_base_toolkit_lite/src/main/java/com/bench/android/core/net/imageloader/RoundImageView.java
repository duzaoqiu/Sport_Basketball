package com.bench.android.core.net.imageloader;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;

import com.android.library.R;


/**
 * author: wanshi
 * created on: 2019-05-29 10:46
 * description:
 */
public class RoundImageView extends AppCompatImageView {

    private float cornerRadius;
    private final float DEFAULT_RADIUS = 12;
    private Path mPath;
    private RectF mRectF;

    /**
     * 利用clip剪切的四个角半径，八个数据分别代表左上角（x轴半径，y轴半径），右上角（**），右下角（**），左下角（**）
     */
    private float[] rids = new float[8];
    private PaintFlagsDrawFilter paintFlagsDrawFilter;

    public RoundImageView(Context context) {
        this(context, null);
    }

    public RoundImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RoundImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView, 0, defStyleAttr);
        cornerRadius = array.getDimension(R.styleable.RoundImageView_riv_corner_radius, DEFAULT_RADIUS);
        setCornerRadius(cornerRadius);
        array.recycle();

        mPath = new Path();
        paintFlagsDrawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
        setLayerType(View.LAYER_TYPE_HARDWARE, null);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mRectF == null) {
            int w = MeasureSpec.getSize(widthMeasureSpec);
            int h = MeasureSpec.getSize(heightMeasureSpec);
            mRectF = new RectF(0, 0, w, h);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectF = new RectF(0, 0, w, h);
    }

    public void setCornerRadius(float cornerRadius) {
        rids[0] = cornerRadius;
        rids[1] = cornerRadius;
        rids[2] = cornerRadius;
        rids[3] = cornerRadius;
        rids[4] = cornerRadius;
        rids[5] = cornerRadius;
        rids[6] = cornerRadius;
        rids[7] = cornerRadius;
        if (mRectF != null) {
            invalidate();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        mPath.reset();
        mPath.addRoundRect(mRectF, rids, Path.Direction.CW);
        canvas.setDrawFilter(paintFlagsDrawFilter);
        canvas.save();
        canvas.clipPath(mPath);
        super.onDraw(canvas);
        canvas.restore();
    }
}
