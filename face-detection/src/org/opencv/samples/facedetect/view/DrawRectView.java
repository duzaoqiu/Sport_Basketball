package org.opencv.samples.facedetect.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import org.opencv.samples.facedetect.utils.CommonUtils;

/**
 * 画图像检测的方形框
 *
 * @author zhouyi
 */
public class DrawRectView extends View {

    private float[] rectArray = null;
    private Paint mPaint;
    private Paint mTextPaint;

    private int width;
    private int height;

    public DrawRectView(Context context) {
        super(context);
        init();
    }

    public DrawRectView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DrawRectView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setRectArray(float[] array) {
        this.rectArray = array;
        invalidate();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(CommonUtils.dp2px(1));

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.GREEN);
        mTextPaint.setTextSize(CommonUtils.dp2px(12));
    }

//    private String[] labels = {"background",
//            "backboard",
//            "basket",
//            "basketball",
//            "person"};

    public static String[] labels = {"background",
            "backboard",
            "basket",
            "basketball",
            "person"};

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (rectArray == null) {
            return;
        }
        if (width == 0) {
            width = canvas.getWidth();
            height = canvas.getHeight();
        }

//        int left = (width - 1280) / 2;
//        int top = (height - 720) / 2;

        int left = 0;
        int top = 0;

        for (int i = 0; i < rectArray.length / 6; i++) {
            int index = i * 6;
            RectF rect = new RectF();
            rect.left = (left + rectArray[index]);
            rect.top = (top + rectArray[index + 1]);
            rect.right = rectArray[index + 2] + rect.left;
            rect.bottom = rectArray[index + 3] + rect.top;
            canvas.drawRect(rect, mPaint);

            String str = labels[(int) rectArray[index + 4]] + " " + rectArray[index + 5];
            canvas.drawText(str, rect.left, rect.top, mTextPaint);
        }

    }
}
