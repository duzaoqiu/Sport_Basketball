package org.opencv.samples.facedetect.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.bench.android.core.app.activity.BaseActivity;

import org.opencv.samples.facedetect.R;

public class ModelTestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_test);
    }
}
