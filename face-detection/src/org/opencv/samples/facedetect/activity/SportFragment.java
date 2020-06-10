package org.opencv.samples.facedetect.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.opencv.samples.facedetect.R;

public class SportFragment extends Fragment {
    private TextView titleTv;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.activity_detail, null);
        initView(inflate);
        titleTv.setText("运动");
        return inflate;
    }

    private void initView(View inflate) {
        titleTv = (TextView) inflate.findViewById(R.id.titleTv);
        inflate.findViewById(R.id.backIv).setVisibility(View.GONE);
        inflate.findViewById(R.id.videoView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //PlayBackActivity.startActivity(getActivity(), PlayBackActivity.TYPE_DECODER_VIDEO);
            }
        });

    }
}
