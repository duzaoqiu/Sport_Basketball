package org.opencv.samples.facedetect.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.bench.android.core.net.http.IHttpResponseCallBack;
import com.bench.android.core.net.http.base.RequestContainer;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.samples.facedetect.R;

public class SetActivity extends AppCompatActivity  {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        findViewById(R.id.ideaLayout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SetActivity.this,IdeaActivity.class));
            }
        });

        findViewById(R.id.backIv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });



    }

}
