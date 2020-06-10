package org.opencv.samples.facedetect.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import org.opencv.samples.facedetect.LogUtils;
import org.opencv.samples.facedetect.R;
import org.opencv.samples.facedetect.model.DetectorModel;
import org.opencv.samples.facedetect.model.VideoPlayBackModel;
import org.opencv.samples.facedetect.utils.CommonUtils;
import org.opencv.samples.facedetect.utils.ImageSizeConstants;

import java.util.ArrayList;

/**
 * 设置映射变换矩阵
 *
 * @author zhouyi
 */
public class SetTransformationView extends View {

    private Paint mCurveLinePaint;
    private Paint mPointPaint;

    private Paint mShootWinPaint;
    private Paint mShootLostPaint;


   // private Mat mPerspective;
  //  private List<Point> srcPoint;

    int[] tri1 = new int[]{200, 200};
    int[] tri2 = new int[]{200, 800};
    int[] tri3 = new int[]{600, 800};
    int[] tri4 = new int[]{600, 200};

    /**
     * 初始框的位置
     */
    int[][] initTris = new int[4][2];

    /**
     * 右上角俯视图的位置
     */
    int[][] topRightTris = new int[4][2];


    int[][] tris = new int[][]{tri1, tri2, tri3, tri4};

    /**
     * 投篮的点
     */
    private ArrayList<VideoPlayBackModel.FrameData> mShootList = new ArrayList<>();

    private boolean mGenerate = false;

    private int[] randomPoint = new int[]{50, 50};
    private int[] generatePoint = new int[2];

    private Paint trianglePaint;
    private Paint trianglePaint1;

    /**
     * 四点连成得矩阵
     */
    private Paint mRectPaint;

    /**
     * 右上角的点
     */
    private Paint mRightTopPaint;

    private int width = 0;
    private int height = 0;

    private int minLeft = 0;
    private int minTop = 0;
    private int maxRight = 0;
    private int maxBottom = 0;

    /**
     * 画轨迹的点
     */
    //private List<Point> mCurvePoints;

    private Bitmap mLostIcon;
    private Bitmap mWinIcon;

    public SetTransformationView(Context context) {
        super(context);
        initView();
    }

    public SetTransformationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public SetTransformationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public SetTransformationView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();
    }

    /**
     * @param generate
     */
    public void setGenerate(boolean generate) {
        mGenerate = generate;
        generateMat();
        invalidate();
        //setPerspective();
    }

    /**
     * 设置生成坐标
     */
    public void setPerspective(DetectorModel data) {
//        VideoPlayBackModel.FrameData frameData = data.getFrameData();
//        if (frameData != null) {
//            List<Point> p = new ArrayList<>();
//            int left = (width - 1280) / 2;
//            int top = (height - 720) / 2;
//            p.add(new Point(frameData.x + left, frameData.y + top));
//            Mat mat = calculatePers(p);
//            frameData.x = (int) mat.get(0, 0)[0];
//            frameData.y = (int) mat.get(0, 0)[1];
//            mShootList.add(frameData);
//        }
//        mCurvePoints = data.getPoints();
//        invalidate();
        /*   List<Point> p = new ArrayList<>();
        p.add(new Point(randomPoint[0], randomPoint[1]));

        Mat mat = calculatePers(p);
        generatePoint[0] = (int) mat.get(0, 0)[0];
        generatePoint[1] = (int) mat.get(0, 0)[1];
        invalidate();*/
    }

    public VideoPlayBackModel.FrameData calculateTransformPoint(VideoPlayBackModel.FrameData frameData) {
//        List<Point> p = new ArrayList<>();
//        p.add(new Point(frameData.x, frameData.y));
//
//        Mat mat = calculatePers(p);
//        frameData.x = (int) mat.get(0, 0)[0];
//        frameData.y = (int) mat.get(0, 0)[1];
//
//        int imageWidth = (int) (ImageSizeConstants.TOP_RIGHT_IMAGE_WIDTH + getResources().getDimension(R.dimen.top_iv_margin_right));
//        int left = width - imageWidth;
//        int x = frameData.x;
//        int leftX = Math.abs(x - left);
//        if (leftX > ImageSizeConstants.TOP_RIGHT_IMAGE_WIDTH) {
//            leftX = ImageSizeConstants.TOP_RIGHT_IMAGE_WIDTH;
//        }
//        if (frameData.y > ImageSizeConstants.TOP_RIGHT_IMAGE_HEIGHT) {
//            frameData.y = ImageSizeConstants.TOP_RIGHT_IMAGE_HEIGHT;
//        }
//        frameData.x = leftX;

        return null;
    }

    public ArrayList<VideoPlayBackModel.FrameData> getFrameData() {
        //将映射数据转到(358,229)的画布上
        for (VideoPlayBackModel.FrameData data : mShootList) {
            int imageWidth = (int) (ImageSizeConstants.TOP_RIGHT_IMAGE_WIDTH + getResources().getDimension(R.dimen.top_iv_margin_right));
            int left = width - imageWidth;
            int x = data.x;
            int leftX = Math.abs(x - left);
            if (leftX > ImageSizeConstants.TOP_RIGHT_IMAGE_WIDTH) {
                leftX = ImageSizeConstants.TOP_RIGHT_IMAGE_WIDTH;
            }
            if (data.y > ImageSizeConstants.TOP_RIGHT_IMAGE_HEIGHT) {
                data.y = ImageSizeConstants.TOP_RIGHT_IMAGE_HEIGHT;
            }
            data.x = leftX;
        }

        return mShootList;
    }

    private void initView() {
        mLostIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_shoot_lost);
        mWinIcon = BitmapFactory.decodeResource(getResources(), R.drawable.icon_shoot_win);

        mPointPaint = new Paint();
        mPointPaint.setAntiAlias(true);
        mPointPaint.setColor(Color.parseColor("#ff0000"));
        mPointPaint.setStrokeWidth(20);

        mRightTopPaint = new Paint();
        mRightTopPaint.setAntiAlias(true);
        mRightTopPaint.setColor(Color.parseColor("#ff0000"));
        mRightTopPaint.setStrokeWidth(10);

        mCurveLinePaint = new Paint();
        mCurveLinePaint.setAntiAlias(true);
        mCurveLinePaint.setColor(Color.parseColor("#ff0000"));
        mCurveLinePaint.setStrokeWidth(5);

        trianglePaint = new Paint();
        trianglePaint.setStyle(Paint.Style.FILL);
        trianglePaint.setStrokeJoin(Paint.Join.ROUND);
        trianglePaint.setColor(Color.parseColor("#FF5700"));

        trianglePaint1 = new Paint();
        trianglePaint1.setStyle(Paint.Style.STROKE);
        trianglePaint1.setColor(Color.parseColor("#00ff00"));

        mRectPaint = new Paint();
        mRectPaint.setStyle(Paint.Style.FILL);
        mRectPaint.setStrokeJoin(Paint.Join.ROUND);
        mRectPaint.setAntiAlias(true);
        mRectPaint.setColor(Color.parseColor("#99DBDFE8"));


        mShootWinPaint = new Paint();
        mShootWinPaint.setAntiAlias(true);
        mShootWinPaint.setColor(Color.parseColor("#00FF00"));
        mShootWinPaint.setStrokeWidth(14);

        mShootLostPaint = new Paint();
        mShootLostPaint.setAntiAlias(true);
        mShootLostPaint.setColor(Color.parseColor("#FF0000"));
        mShootLostPaint.setStrokeWidth(14);

        minLeft = CommonUtils.getDisplay(getContext()).getWidth() - (int) (ImageSizeConstants.TOP_RIGHT_IMAGE_WIDTH + getContext().getResources().getDimension(R.dimen.top_iv_margin_right));
        minTop = 0;
        maxRight = minLeft + ImageSizeConstants.TOP_RIGHT_IMAGE_WIDTH;
        maxBottom = ImageSizeConstants.TOP_RIGHT_IMAGE_HEIGHT;
    }

    /**
     * 生成映射矩阵
     */
    private void generateMat() {
//        srcPoint = new ArrayList<>();
//        for (int i = 0; i < tris.length; i++) {
//            int[] t = tris[i];
//            srcPoint.add(new Point(t[0], t[1]));
//        }
//
//        List<Point> dstPoint = new ArrayList<>();
//        dstPoint.add(new Point(topRightTris[0][0], topRightTris[0][1]));
//        dstPoint.add(new Point(topRightTris[1][0], topRightTris[1][1]));
//        dstPoint.add(new Point(topRightTris[2][0], topRightTris[2][1]));
//        dstPoint.add(new Point(topRightTris[3][0], topRightTris[3][1]));
//
//        Mat srcMat = Converters.vector_Point_to_Mat(srcPoint, CvType.CV_32F);
//        Mat dstMat = Converters.vector_Point_to_Mat(dstPoint, CvType.CV_32F);
//
//        mPerspective = Imgproc.getPerspectiveTransform(srcMat, dstMat);
//
//        List<Point> test = new ArrayList<>();
//        test.add(new Point(tris[0][0], tris[0][1]));
//        Mat testMat = Converters.vector_Point_to_Mat(test, CvType.CV_32F);
//
//        Mat dst = new Mat();
//        Core.perspectiveTransform(testMat, dst, mPerspective);
//        showLog("测试" + dst.get(0, 0)[0] + " " + dst.get(0, 0)[1]);
    }

//    /**
//     * 计算映射的点
//     *
//     * @param originPoint
//     * @return
//     */
//    private Mat calculatePers(List<Point> originPoint) {
//        Mat origin = Converters.vector_Point_to_Mat(originPoint, CvType.CV_32F);
//        Mat dst = new Mat();
//        Core.perspectiveTransform(origin, dst, mPerspective);
//        return dst;
//    }

    private void showLog(String msg) {
        Log.e("数据", msg);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (width == 0) {
            width = canvas.getWidth();
            height = canvas.getHeight();
            LogUtils.e("画布宽度 " + width);

            int left = width / 2 - CommonUtils.dp2px(80);
            int right = width / 2 + CommonUtils.dp2px(80);

            int top = height / 2 - CommonUtils.dp2px(100);
            int bottom = height / 2 + CommonUtils.dp2px(100);

            tri1 = new int[]{left, top};
            tri2 = new int[]{left, bottom};
            tri3 = new int[]{right, bottom};
            tri4 = new int[]{right, top};

            tris[0] = tri1;
            tris[1] = tri2;
            tris[2] = tri3;
            tris[3] = tri4;

            for (int i = 0; i < tris.length; i++) {
                for (int j = 0; j < tris[i].length; j++) {
                    initTris[i][j] = tris[i][j];
                }
            }

            left = width - CommonUtils.dp2px(20) - 214;
            right = width - CommonUtils.dp2px(20) - 134;
            top = 2;
            bottom = 155;

            topRightTris[0] = new int[]{left, top};
            topRightTris[1] = new int[]{left, bottom};
            topRightTris[2] = new int[]{right, top};
            topRightTris[3] = new int[]{right, bottom};
        }

        //初始方框
        for (int i = 0; i < initTris.length; i++) {
            //如果还没生成映射矩阵，需要绘制初始点
            if (!mGenerate) {
                canvas.drawPoint(initTris[i][0], initTris[i][1], mPointPaint);
            }
            //右上角点
            canvas.drawPoint(topRightTris[i][0], topRightTris[i][1], mRightTopPaint);
        }

        //如果未生成映射矩阵，则画出标线图
        if (!mGenerate) {
            Path path;
            path = new Path();
            path.moveTo(tris[0][0], tris[0][1]);
            path.lineTo(tris[1][0], tris[1][1]);
            path.lineTo(tris[2][0], tris[2][1]);
            path.lineTo(tris[3][0], tris[3][1]);

            path.close();

            canvas.drawPath(path, mRectPaint);
            //canvas.drawPath(path, trianglePaint1);

            //画三角形
            for (int i = 0; i < tris.length; i++) {
                int[] t = tris[i];
                path = new Path();
                path.moveTo(t[0] - 20, t[1] - 40);//此点为多边形的起点
                path.lineTo(t[0], t[1]);
                path.lineTo(t[0] + 20, t[1] - 40);
                path.lineTo(t[0] - 20, t[1] - 40);
                path.close();         //使这些点构成封闭的多边形
                canvas.drawPath(path, trianglePaint);
                //canvas.drawPath(path, trianglePaint1);
            }
        } else {
            /*Path path;
            int[] t;
            t = randomPoint;
            path = new Path();
            path.moveTo(t[0], t[1]);//此点为多边形的起点
            path.lineTo(t[0] + 40, t[1]);
            path.lineTo(t[0] + 20, t[1] + 20);
            path.lineTo(t[0], t[1]);
            path.close();         //使这些点构成封闭的多边形
            canvas.drawPath(path, trianglePaint);
            //canvas.drawPath(path, trianglePaint1);

            t = generatePoint;
            path = new Path();
            path.moveTo(t[0], t[1]);//此点为多边形的起点
            path.lineTo(t[0] + 40, t[1]);
            path.lineTo(t[0] + 20, t[1] + 20);
            path.lineTo(t[0], t[1]);
            path.close();         //使这些点构成封闭的多边形
            canvas.drawPath(path, trianglePaint);
            //canvas.drawPath(path, trianglePaint1);*/

            for (VideoPlayBackModel.FrameData d : mShootList) {
                boolean win = d.win;
                //纠正数据，防止数据超过偏移值
                if (d.x < minLeft) {
                    d.x = minLeft;
                }
                if (d.x > maxRight) {
                    d.x = maxRight;
                }
                if (d.y < minTop) {
                    d.y = minTop;
                }
                if (d.y > maxBottom) {
                    d.y = maxBottom;
                }
                int x = d.x;
                int y = d.y;

                if (win) {
                    canvas.drawBitmap(mWinIcon, x, y, mShootWinPaint);
                } else {
                    canvas.drawBitmap(mLostIcon, x, y, mShootLostPaint);
                }
            }
        }

        drawCurve(canvas);
    }

    private void drawCurve(Canvas canvas) {
//        if (mCurvePoints != null) {
//            float left = (width - 1280) / 2;
//            float top = (height - 720) / 2;
//            for (int i = 1; i < mCurvePoints.size(); i++) {
//                if (mCurvePoints.get(i - 1) == null || mCurvePoints.get(i) == null) {
//                    continue;
//                }
//                //canvas.drawLine((float) mCurvePoints.get(i - 1).x + left, (float) mCurvePoints.get(i - 1).y + top, (float) mCurvePoints.get(i).x + left, (float) mCurvePoints.get(i).y + top, mCurveLinePaint);
//            }
//        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //return super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                calculatePoint(event);
                //calculateRandomPoint(event);
            }
            break;
            case MotionEvent.ACTION_MOVE: {
                calculatePoint(event);
                //calculateRandomPoint(event);
            }
            break;
            case MotionEvent.ACTION_UP: {
                calculatePoint(event);
                //calculateRandomPoint(event);
            }
            break;
        }
        return true;
    }

    private void calculateRandomPoint(MotionEvent event) {
        if (!mGenerate) {
            return;
        }
        int[] t = randomPoint;

        int x = (int) Math.abs(event.getX() - t[0]);
        int y = (int) Math.abs(event.getY() - t[1]);

        if (x < 50 && y < 50) {
            t[0] = (int) event.getX();
            t[1] = (int) event.getY();
            //setPerspective();
        }
    }

    private void calculatePoint(MotionEvent event) {
        if (mGenerate) {
            return;
        }
        for (int i = 0; i < tris.length; i++) {
            int[] t = tris[i];

            int x = (int) Math.abs(event.getX() - t[0]);
            int y = (int) Math.abs(event.getY() - t[1]);

            if (x < 50 && y < 50) {
                t[0] = (int) event.getX();
                t[1] = (int) event.getY();
                invalidate();
                break;
            }
        }
    }


}
