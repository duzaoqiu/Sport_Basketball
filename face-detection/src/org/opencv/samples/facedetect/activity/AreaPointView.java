package org.opencv.samples.facedetect.activity;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * 区域分数图
 */
public class AreaPointView extends View {

    private Paint mPaint;

    public AreaPointView(Context context) {
        super(context);
        initView();
    }

    public AreaPointView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public AreaPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public AreaPointView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    private void initView() {
        mPaint = new Paint();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#F5F6FA"));
        int bottomRectWidth = mWidth / 6;

        int bottomHeight = bottomRectWidth * 2 - 20;

        //画底部
        for (int i = 0; i < 6; i++) {
            Rect rect = new Rect();
            rect.bottom = bottomRectWidth + 30;
            int divider = 10;
            if (i == 0) divider = 0;
            switch (i) {
                case 0:
                case 5:
                    mPaint.setColor(Color.parseColor("#FFD303"));
                    rect.bottom = bottomHeight;
                    break;
                case 1:
                case 4:
                    mPaint.setColor(Color.parseColor("#FF7838"));
                    break;
                case 2:
                    mPaint.setColor(Color.parseColor("#F5F6FA"));
                    break;
                case 3:
                    mPaint.setColor(Color.parseColor("#58CDE1"));
                    break;
            }
            rect.top = 0;
            rect.left = i * bottomRectWidth + divider;
            rect.right = rect.left + bottomRectWidth - divider;
            canvas.drawRect(rect, mPaint);
        }


        //画底部
        bottomRectWidth = (mWidth - 20) / 3;
        int divider = 10;
        for (int i = 0; i < 3; i++) {
            Rect rect = new Rect();
            rect.top = bottomHeight + divider;
            if (i == 0) {
                rect.left = bottomRectWidth * i;
            } else {
                rect.left = bottomRectWidth * i + divider * (i);
            }
            rect.bottom = rect.top + bottomRectWidth;
            rect.right = rect.left + bottomRectWidth;
            if (i == 0) {
                mPaint.setColor(Color.parseColor("#FF794A"));
            } else if (i == 1) {
                mPaint.setColor(Color.parseColor("#FF5700"));
            } else if (i == 2) {
                mPaint.setColor(Color.parseColor("#FF794A"));
            }
            canvas.drawRect(rect, mPaint);
        }

        //----------------------------画中间弧度----------------------------------------
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setStyle(Paint.Style.FILL);
        p.setStrokeJoin(Paint.Join.ROUND);
        p.setAntiAlias(true);
        p.setStrokeWidth(10);

        p.setColor(Color.parseColor("#F5F6FA"));
        bottomHeight = bottomHeight + 10;
        RectF rectF = new RectF();
        rectF.top = -170;
        rectF.left = bottomHeight / 2 - 15;
        rectF.right = rectF.left + bottomHeight * 2.2f;
        rectF.bottom = rectF.top + bottomHeight * 2.2f;
        canvas.drawArc(rectF, 20f, 140f, false, p);//20 140

        int baseWidth = 10;
        p.setColor(Color.parseColor("#ff0000"));
        rectF = new RectF();
        rectF.top = -170 + baseWidth;
        rectF.left = bottomHeight / 2 - 15 + baseWidth;
        rectF.right = rectF.left + bottomHeight * 2.2f - baseWidth * 2;
        rectF.bottom = rectF.top + bottomHeight * 2.2f - baseWidth * 2;
        canvas.drawArc(rectF, 120f, 40f, false, p);

        p.setColor(Color.parseColor("#00ff00"));
        rectF = new RectF();
        rectF.top = -170 + baseWidth;
        rectF.left = bottomHeight / 2 - 15 + baseWidth;
        rectF.right = rectF.left + bottomHeight * 2.2f - baseWidth * 2;
        rectF.bottom = rectF.top + bottomHeight * 2.2f - baseWidth * 2;
        canvas.drawArc(rectF, 64f, 54f, false, p);

        p.setColor(Color.parseColor("#0000ff"));
        rectF = new RectF();
        rectF.top = -170 + baseWidth;
        rectF.left = bottomHeight / 2 - 15 + baseWidth;
        rectF.right = rectF.left + bottomHeight * 2.2f - baseWidth * 2;
        rectF.bottom = rectF.top + bottomHeight * 2.2f - baseWidth * 2;
        canvas.drawArc(rectF, 21f, 40f, false, p);

        //----------------------------画中间矩形------------------------------------
        bottomRectWidth = mWidth / 6;

        Paint pathPaint = new Paint();
        pathPaint.setStyle(Paint.Style.FILL);
        //pathPaint.setStrokeWidth(20);
        pathPaint.setStrokeJoin(Paint.Join.ROUND);
        pathPaint.setAntiAlias(true);

        Path path;
        pathPaint.setColor(Color.parseColor("#ff0000"));
        path = new Path();
        path.moveTo(bottomRectWidth + 10 + 3, bottomRectWidth + 42);
        path.lineTo(bottomRectWidth + 10 + 3, bottomRectWidth * 2 - 16);
        path.lineTo(bottomRectWidth * 2, bottomRectWidth * 3 + 4);
        path.lineTo(bottomRectWidth * 2, bottomRectWidth + 42);
        path.close();
        canvas.drawPath(path, pathPaint);

        pathPaint.setColor(Color.parseColor("#0000ff"));
        path = new Path();
        path.moveTo(bottomRectWidth + 10 + 1 + 3 * bottomRectWidth, bottomRectWidth + 42);
        path.lineTo(bottomRectWidth + 10 + 1 + 3 * bottomRectWidth, bottomRectWidth * 3 + 4);
        path.lineTo(bottomRectWidth * 2 + 3 * bottomRectWidth, bottomRectWidth * 2 - 16);
        path.lineTo(bottomRectWidth * 2 + 3 * bottomRectWidth, bottomRectWidth + 42);
        path.close();
        canvas.drawPath(path, pathPaint);


    }

    private int mWidth = 0;
    private int mHeight = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }
}
