package org.opencv.samples.facedetect.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import org.opencv.samples.facedetect.utils.CommonUtils;

/**
 * 圆形比率图
 * @author zhouyi
 */
public class CircleView extends View {

    private Paint whitePaint;
    private Paint colorPaint;
    private Paint greyPaint;
    private float radius;
    private float littleCircleRadius;

    /**
     * 圆扫过的弧度
     */
    private float sweepAngle;

    public CircleView(Context context) {
        super(context);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public CircleView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.FILL);

        colorPaint = new Paint();
        colorPaint.setAntiAlias(true);
        colorPaint.setColor(Color.parseColor("#ff0000"));
        colorPaint.setStyle(Paint.Style.FILL);

        greyPaint = new Paint();
        greyPaint.setAntiAlias(true);
        greyPaint.setColor(Color.parseColor("#F5F6FA"));
        greyPaint.setStyle(Paint.Style.FILL);

        radius = CommonUtils.dp2px(40);
        littleCircleRadius = CommonUtils.dp2px(35);
    }

    public void setCircleColor(int color) {
        colorPaint.setColor(color);
    }

    public void setSweepAngle(float angle) {
        sweepAngle = angle;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(radius, radius, radius, greyPaint);
        canvas.drawArc(0, 0, radius * 2, radius * 2, -90, sweepAngle, true, colorPaint);
        canvas.drawCircle(radius, radius, littleCircleRadius, whitePaint);
    }
}
