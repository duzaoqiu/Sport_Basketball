package com.bench.android.core.net.imageloader.utils;

import android.net.Uri;

import com.bench.android.core.net.imageloader.DisplayOptimizeListener;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.File;

/**
 * Created by danao on 2019/4/28.
 */
public class SubsamplingScaleImageViewUtils {

    public static void setImage(final SubsamplingScaleImageView subsamplingScaleImageView, String filePath) {
        File file = new File(filePath);
        setImage(subsamplingScaleImageView, file);
    }

    public static void setImage(final SubsamplingScaleImageView subsamplingScaleImageView, final File file) {
        subsamplingScaleImageView.post(new Runnable() {
            @Override
            public void run() {
                DisplayOptimizeListener mDisplayOptimizeListener = new DisplayOptimizeListener(subsamplingScaleImageView);
                subsamplingScaleImageView.setOnImageEventListener(mDisplayOptimizeListener);
                subsamplingScaleImageView.setImage(ImageSource.uri(Uri.fromFile(file)));
            }
        });
    }

}
