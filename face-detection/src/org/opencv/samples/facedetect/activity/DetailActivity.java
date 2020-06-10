package org.opencv.samples.facedetect.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.opencv.samples.facedetect.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailActivity extends AppCompatActivity {

    private TextView titleTv;
    private ImageView backIv;
    private CircleImageView logoIv;
    private TextView nameTv;
    private TextView locationTv;
    private TextView timeTv;
    private ImageView videoView;

    private ItemData mItemData;

    public static void startDetailActivity(Activity activity, ItemData data) {
        Intent intent = new Intent(activity, DetailActivity.class);
        intent.putExtra("data", data);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        mItemData = getIntent().getParcelableExtra("data");
        initView();
    }

    private void initView() {
        titleTv = (TextView) findViewById(R.id.titleTv);
        titleTv.setText("详情");

        View viewById = findViewById(R.id.backIv);
        viewById.setVisibility(View.VISIBLE);
        viewById.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        backIv = (ImageView) findViewById(R.id.backIv);
        logoIv = (CircleImageView) findViewById(R.id.logoIv);
        nameTv = (TextView) findViewById(R.id.nameTv);
        locationTv = (TextView) findViewById(R.id.locationTv);
        timeTv = (TextView) findViewById(R.id.timeTv);
        videoView = (ImageView) findViewById(R.id.videoView);
        logoIv.setImageResource(mItemData.getLogo());
        nameTv.setText(mItemData.getName());
        locationTv.setText(mItemData.getLocation());
        timeTv.setText(mItemData.getTime());

        if (mItemData.getImageSrc() > 0) {
            videoView.setBackgroundResource(mItemData.getImageSrc());
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //PlayBackActivity.startActivity(DetailActivity.this, PlayBackActivity.TYPE_PLAY_VIDEO, mItemData.getVideoPath());
                }
            });
        } else {
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // PlayBackActivity.startActivity(DetailActivity.this, PlayBackActivity.TYPE_DECODER_VIDEO);
                }
            });
        }
    }
}
