package org.opencv.samples.facedetect.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

import org.opencv.samples.facedetect.model.VideoPlayBackModel;

import java.util.ArrayList;

/**
 * 右上角映射图
 *
 * @author zhouyi
 */
public class TopRightTransView extends AppCompatImageView {

    private Paint mShootWinPaint;
    private Paint mShootLostPaint;

    /**
     * 投篮的点
     */
    private ArrayList<VideoPlayBackModel.FrameData> mShootList = new ArrayList<>();

    //private Mat transformMat;

    public TopRightTransView(Context context) {
        super(context);
        init();
    }

    public TopRightTransView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TopRightTransView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mShootWinPaint = new Paint();
        mShootWinPaint.setAntiAlias(true);
        mShootWinPaint.setColor(Color.parseColor("#00FF00"));
        mShootWinPaint.setStrokeWidth(14);

        mShootLostPaint = new Paint();
        mShootLostPaint.setAntiAlias(true);
        mShootLostPaint.setColor(Color.parseColor("#FF0000"));
        mShootLostPaint.setStrokeWidth(14);
    }

    /**
     * 添加一个点
     *
     * @param data
     */
    public void addPoint(VideoPlayBackModel.FrameData data) {
        mShootList.add(data);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (VideoPlayBackModel.FrameData d : mShootList) {
            boolean win = d.win;
            if (win) {
                canvas.drawPoint(d.x, d.y, mShootWinPaint);
            } else {
                canvas.drawPoint(d.x, d.y, mShootLostPaint);
            }
        }
    }
}
