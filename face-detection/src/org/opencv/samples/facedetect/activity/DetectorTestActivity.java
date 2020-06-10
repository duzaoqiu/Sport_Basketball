package org.opencv.samples.facedetect.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.bench.android.core.app.activity.BaseActivity;

import org.opencv.samples.facedetect.R;
import org.opencv.samples.facedetect.view.DrawRectView;


public class DetectorTestActivity extends BaseActivity {

    private ImageView iv;
    private TextView tv;

    private boolean loadSuccess = false;

    private Bitmap mCacheBitmap;

    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private DrawRectView drawRectView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_detector);
        initView();
        new Thread(new Runnable() {
            @Override
            public void run() {
                //DetectorTest.play(iv,tv);
                testVideo();
                //testImage();
            }
        }).start();

    }

    private void testImage() {
        if (!loadSuccess) {
           // NcnnDetector.initYolo();
            loadSuccess = true;
        }

    }

    private void testVideo() {
        if (!loadSuccess) {
           // NcnnDetector.initYolo();
            loadSuccess = true;
        }

    }

    Handler mHandler = new Handler();

//    private void drawObjectRect(final float[] array) {
//        detector(array);
//        if (array != null) {
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    drawRectView.setRectArray(array);
//                }
//            });
//        }
//    }
//
//    private void detector(final float[] array) {
//        if (array != null) {
//            final int d = mFrameCount;
//            mHandler.post(new Runnable() {
//                @Override
//                public void run() {
//                    DetectorModel detector = DetectorUtils.detector(array);
//                    if (detector.getFrameData() != null) {
//                        detector.getFrameData().setFrameCount(d);
//                    }
//
//                }
//            });
//        }
//    }

    private void initView() {
        iv = (ImageView) findViewById(R.id.iv);
        tv = (TextView) findViewById(R.id.tv);
        drawRectView = (DrawRectView) findViewById(R.id.drawRectView);
        drawRectView.setOnClickListener(this);
    }
}
