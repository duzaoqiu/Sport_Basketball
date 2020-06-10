package org.opencv.samples.facedetect.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.RequiresApi;

import org.opencv.samples.facedetect.R;
import org.opencv.samples.facedetect.bean.DynamicBean;
import org.opencv.samples.facedetect.model.VideoPlayBackModel;
import org.opencv.samples.facedetect.utils.CommonUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 显示投篮每个位置百分比
 *
 * @author zhouyi
 */
public class PointLocationScoreView extends View {

    private final static List<Bitmap> reds = new ArrayList<>();
    private final static List<Bitmap> orgs = new ArrayList<>();
    private final static List<Bitmap> blues = new ArrayList<>();
    private final static List<Bitmap> blanks = new ArrayList<>();

    /**
     * 0-5 底部数据
     * 6-10 中间数据
     * 11-13 底部数据
     */
    private List<DynamicBean.StatsBean.DetailBean> mDataList = new ArrayList<>();
    private Paint mPaint;
    private Paint mSmallTextPaint;
    private Paint mGreyTextPaint;
    private Paint mSmallGreyTextPaint;

    public PointLocationScoreView(Context context) {
        super(context);
        init();
    }

    public PointLocationScoreView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointLocationScoreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public PointLocationScoreView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setDataList(List<DynamicBean.StatsBean.DetailBean> dataList) {
        mDataList = dataList;
        invalidate();
    }

    public int sp2px(float spValue) {
        final float fontScale = getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(sp2px(16));
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);

        mGreyTextPaint = new Paint();
        mGreyTextPaint.setTextSize(sp2px(16));
        mGreyTextPaint.setColor(Color.parseColor("#A1A1A1"));
        mGreyTextPaint.setAntiAlias(true);

        mSmallGreyTextPaint = new Paint();
        mSmallGreyTextPaint.setTextSize(sp2px(12));
        mSmallGreyTextPaint.setColor(Color.parseColor("#A1A1A1"));
        mSmallGreyTextPaint.setAntiAlias(true);

        mSmallTextPaint = new Paint();
        mSmallTextPaint.setTextSize(sp2px(12));
        mSmallTextPaint.setColor(Color.WHITE);
        mSmallTextPaint.setAntiAlias(true);

        //如果图片为空，则需要初始加载数据
        if (reds.size() == 0) {
            int[] res = new int[]{R.mipmap.b1, R.mipmap.b2, R.mipmap.b3, R.mipmap.b4, R.mipmap.b5, R.mipmap.b6,
                    R.mipmap.c1, R.mipmap.c2, R.mipmap.c3, R.mipmap.c4, R.mipmap.c5,
                    R.mipmap.t1, R.mipmap.t2, R.mipmap.t3};

            for (int i = 0; i < res.length; i++) {
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), res[i]);
                Bitmap red = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap orange = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap blue = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Bitmap blank = bitmap.copy(Bitmap.Config.ARGB_8888, true);

                for (int j = 0; j < bitmap.getWidth(); j++) {
                    for (int z = 0; z < bitmap.getHeight(); z++) {
                        int alpha = Color.alpha(bitmap.getPixel(j, z));
                        if (alpha != 0x00) {
                            alpha = alpha << 24;
                            red.setPixel(j, z, Color.parseColor("#FF5700") + alpha);
                            orange.setPixel(j, z, Color.parseColor("#FF794A") + alpha);
                            blue.setPixel(j, z, Color.parseColor("#5F77B3") + alpha);
                            blank.setPixel(j, z, Color.parseColor("#F5F6FA") + alpha);
                        }
                    }
                }

                reds.add(red);
                orgs.add(orange);
                blues.add(blue);
                blanks.add(blank);
            }
        }

        //初始化数据
        for (int i = 0; i < 14; i++) {
            DynamicBean.StatsBean.DetailBean data = new DynamicBean.StatsBean.DetailBean();
            data.tryCount = 1;
            data.sucCount = 0;
            mDataList.add(data);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mDataList == null) {
            return;
        }

        int divider = CommonUtils.dp2px(3);

        int imageWidth = 0;
        //顶层
        for (int i = 0; i < 6; i++) {
            Bitmap bitmap = reds.get(i);

            imageWidth += (bitmap.getWidth() + divider);
        }
        int baseLeft = (getMeasuredWidth() - imageWidth) / 2;

        int width = baseLeft;

        DynamicBean.StatsBean.DetailBean data;
        int winRate;
        String winRateStr;
        String tip;

        //顶层
        for (int i = 0; i < 6; i++) {
            data = mDataList.get(i);
            winRate = getWinRate(i);
            winRateStr = winRate + "%";
            tip = data.sucCount + "-" + data.tryCount;

            Bitmap bitmap = getBitmaps(winRate, i);
            canvas.drawBitmap(bitmap, width, 0, mPaint);
            float v = mPaint.measureText(winRateStr);
            canvas.drawText(winRateStr, width + (bitmap.getWidth() - v) / 2, (bitmap.getHeight() + sp2px(16)) / 2, getTextPaint(winRate));
            float smallWidth = mSmallTextPaint.measureText(tip);
            float sw = width + (bitmap.getWidth() - smallWidth) / 2;
            float sy = (bitmap.getHeight() / 2) + sp2px(20);
            canvas.drawText(tip, sw, sy, getSmallTextPaint(winRate));
            width += (bitmap.getWidth() + divider);
        }

        int left = baseLeft + reds.get(0).getWidth() + divider;
        int top = reds.get(1).getHeight() + divider;

        //中间层
        width = left;

        for (int i = 6; i < 10; i++) {
            data = mDataList.get(i);
            winRate = getWinRate(i);
            winRateStr = winRate + "%";
            tip = data.sucCount + "-" + data.tryCount;

            Bitmap bitmap = getBitmaps(winRate, i);
            canvas.drawBitmap(bitmap, width, top, mPaint);
            float v = mPaint.measureText(winRateStr);
            canvas.drawText(winRateStr, width + (bitmap.getWidth() - v) / 2, top + (bitmap.getHeight() - sp2px(16)) / 2, getTextPaint(winRate));

            float smallWidth = mSmallTextPaint.measureText(tip);
            float sw = width + (bitmap.getWidth() - smallWidth) / 2;
            float sy = top + (bitmap.getHeight() / 2) + sp2px(8);
            canvas.drawText(tip, sw, sy, getSmallTextPaint(winRate));

            width += (bitmap.getWidth() + divider);
        }


        data = mDataList.get(10);
        winRate = getWinRate(10);
        winRateStr = winRate + "%";
        tip = data.sucCount + "-" + data.tryCount;

        //c5单独放
        left = left + reds.get(6).getWidth() + divider;
        top = top + reds.get(7).getHeight() + divider;
        Bitmap b = getBitmaps(winRate, 10);
        canvas.drawBitmap(b, left, top, mPaint);
        float v = mPaint.measureText(winRateStr);
        canvas.drawText(winRateStr, left + (b.getWidth() - v) / 2, top + (b.getHeight() / 2 + sp2px(16) / 2), getTextPaint(winRate));

        float smallWidth = mSmallTextPaint.measureText(tip);
        float sw = left + (b.getWidth() - smallWidth) / 2;
        float sy = top + (b.getHeight() / 2) + sp2px(20);
        canvas.drawText(tip, sw, sy, getSmallTextPaint(winRate));

        top = reds.get(0).getHeight() + (divider - CommonUtils.dp2px(1));
        int top1 = top + (reds.get(11).getHeight() - reds.get(12).getHeight()) + divider;
        width = baseLeft;

        //底层
        for (int i = 11; i < 14; i++) {
            data = mDataList.get(i);
            winRate = getWinRate(i);
            winRateStr = winRate + "%";
            tip = data.sucCount + "-" + data.tryCount;
            if (i == 12) {
                Bitmap bitmap = getBitmaps(winRate, i);
                canvas.drawBitmap(bitmap, width, top1, mPaint);
                v = mPaint.measureText(winRateStr);
                canvas.drawText(winRateStr, width + (bitmap.getWidth() - v) / 2, top1 + (bitmap.getHeight() + sp2px(16)) / 2, getTextPaint(winRate));

                smallWidth = mSmallTextPaint.measureText(tip);
                sw = width + (bitmap.getWidth() - smallWidth) / 2;
                sy = top1 + (bitmap.getHeight() / 2) + sp2px(20);
                canvas.drawText(tip, sw, sy, getSmallTextPaint(winRate));

                width += (bitmap.getWidth() + (divider * 2));
            } else {
                Bitmap bitmap = getBitmaps(winRate, i);
                canvas.drawBitmap(bitmap, width, top + 10, mPaint);
                v = mPaint.measureText(winRateStr);
                canvas.drawText(winRateStr, width + (bitmap.getWidth() - v) / 2, top + (bitmap.getHeight() + sp2px(16)) / 2, getTextPaint(winRate));

                smallWidth = mSmallTextPaint.measureText(tip);
                sw = width + (bitmap.getWidth() - smallWidth) / 2;
                sy = top + (bitmap.getHeight() / 2) + sp2px(20);
                canvas.drawText(tip, sw, sy, getSmallTextPaint(winRate));

                width += (bitmap.getWidth() + divider);
            }
        }
    }

    /**
     * 获取胜率
     *
     * @param pos
     * @return
     */
    private int getWinRate(int pos) {
        float shootCount = mDataList.get(pos).tryCount;
        float winCount = mDataList.get(pos).sucCount;
        if (shootCount == 0) {
            return 0;
        }
        return (int) ((winCount / shootCount) * 100);
    }

    private Paint getSmallTextPaint(int rate) {
        if (rate >= 0 && rate <= 10) {
            return mSmallGreyTextPaint;
        }
        return mSmallTextPaint;
    }

    private Paint getTextPaint(int rate) {
        if (rate >= 0 && rate <= 10) {
            return mGreyTextPaint;
        }
        return mPaint;
    }

    private Bitmap getBitmaps(int rate, int pos) {
        if (rate > 70 && rate <= 100) {
            return reds.get(pos);
        } else if (rate > 40 && rate <= 70) {
            return orgs.get(pos);
        } else if (rate > 10 && rate <= 40) {
            return blues.get(pos);
        } else if (rate >= 0 && rate <= 10) {
            return blanks.get(pos);
        }
        return null;
    }


}
