package org.opencv.samples.facedetect.activity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.samples.facedetect.R;
import org.opencv.samples.facedetect.view.SetTransformationView;

public class LocationActivity extends AppCompatActivity implements View.OnClickListener {

    private SetTransformationView locationView;
    private Button generateMat;
    private Button randomBtn;
    private Button perspectiveBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_location);
        initView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    private void initView() {
        locationView = (SetTransformationView) findViewById(R.id.locationView);
        generateMat = (Button) findViewById(R.id.generateMat);
        randomBtn = (Button) findViewById(R.id.randomBtn);
        perspectiveBtn = (Button) findViewById(R.id.perspectiveBtn);

        generateMat.setOnClickListener(this);
        randomBtn.setOnClickListener(this);
        perspectiveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.generateMat:
                locationView.setGenerate(true);
                break;
            case R.id.randomBtn:

                break;
            case R.id.perspectiveBtn:

                break;
        }
    }
}
