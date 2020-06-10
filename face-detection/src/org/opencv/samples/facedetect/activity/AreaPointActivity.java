package org.opencv.samples.facedetect.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.samples.facedetect.R;

public class AreaPointActivity extends AppCompatActivity {

    private AreaPointView areaView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_area_point);
        initView();
    }

    private void initView() {
        areaView = (AreaPointView) findViewById(R.id.areaView);
    }
}
