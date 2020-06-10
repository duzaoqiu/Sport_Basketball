package org.opencv.samples.facedetect.activity;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.opencv.samples.facedetect.R;

public class DiscoverFragment extends Fragment {
    private TextView titleTv;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_discovery, null);
        initView(inflate);
        titleTv.setText("发现");
        return inflate;
    }


    private void initView(View inflate) {
        titleTv = (TextView) inflate.findViewById(R.id.titleTv);
    }


}
