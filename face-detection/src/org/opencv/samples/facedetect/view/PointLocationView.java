package org.opencv.samples.facedetect.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import org.opencv.samples.facedetect.LogUtils;
import org.opencv.samples.facedetect.R;
import org.opencv.samples.facedetect.model.VideoPlayBackModel;
import org.opencv.samples.facedetect.utils.CommonUtils;
import org.opencv.samples.facedetect.utils.ImageSizeConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * 点位图
 */
public class PointLocationView extends AppCompatImageView {

    /**
     *
     */
    private List<VideoPlayBackModel.FrameData> mList = new ArrayList<>();

    private Bitmap mBg;
    private Paint mPaint;
    private Paint mTextPaint;

    private int width = 0;
    private int height = 0;

    /**
     * 原始坐标基于这个宽高，需要对这个宽高进行等比放大
     */
    private final int BASE_WIDTH = ImageSizeConstants.TOP_RIGHT_IMAGE_WIDTH;
    private final int BASE_HEIGHT = ImageSizeConstants.TOP_RIGHT_IMAGE_HEIGHT;
    /**
     * 原理比例宽高放大倍数
     */
    private float multiply = 0;

    private Bitmap mLostIcon;
    private Bitmap mWinIcon;

    public PointLocationView(Context context) {
        super(context);
        init();
    }

    public PointLocationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointLocationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mTextPaint = new Paint();
        mTextPaint.setTextSize(CommonUtils.dp2px(10));
        mTextPaint.setColor(Color.RED);

        mLostIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_shoot_lost);
        mWinIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_shoot_win);
    }

    public void setList(List<VideoPlayBackModel.FrameData> list) {
        mList = list;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (VideoPlayBackModel.FrameData d : mList) {
            LogUtils.e((d.x * multiply - mWinIcon.getWidth())+" "+(d.y * multiply - mWinIcon.getHeight()));
            if (d.win) {
                canvas.drawBitmap(mWinIcon,Math.abs(d.x * multiply - mWinIcon.getWidth()), Math.abs(d.y * multiply - mWinIcon.getHeight()), mPaint);
            } else {
                canvas.drawBitmap(mLostIcon, Math.abs((d.x * multiply) - mLostIcon.getWidth()), Math.abs(d.y * multiply - mLostIcon.getHeight()), mPaint);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = getMeasuredWidth();
        height = getMeasuredHeight();
        multiply = (float) width / BASE_WIDTH;
    }


}
