package com.bench.android.core.net.http.okhttp;

import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import okio.BufferedSink;
import okio.Okio;
import okio.Source;

/**
 * Created by mrs on 2017/5/18.
 */

public class ProgressRequestBody extends RequestBody {

    private final ProgressListener progressListener;
    private File mFile;
    private Handler uiHandler;
    private Long remaining;

    private byte[] mData;

    public ProgressRequestBody(String filePath, ProgressListener progressListener) {
        this.mFile = new File(filePath);
        this.progressListener = progressListener;

        if (progressListener != null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }
    }

    public ProgressRequestBody(byte[] data, ProgressListener progressListener) {
        this.mData = data;
        this.progressListener = progressListener;
        if (progressListener != null) {
            uiHandler = new Handler(Looper.getMainLooper());
        }
    }

    @Override
    public MediaType contentType() {
        return MediaType.parse("application/octet-stream");
    }

    @Override
    public long contentLength() {
        if (mFile == null) {
            remaining = Long.valueOf(mData.length);
        } else {
            remaining = mFile.length();
        }

        return remaining;
    }

    @Override
    public void writeTo(BufferedSink sink) throws IOException {
        if (mData != null) {
            uploadData(sink);
        } else {
            uploadFile(sink);
        }
    }

    private void uploadData(BufferedSink sink) {
        try {
            sink.write(mData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadFile(BufferedSink sink) {
        Source source = null;
        try {
            source = Okio.source(mFile);
            long remaining = contentLength();
            for (long readCount; (readCount = source.read(sink.buffer(), 2048)) != -1; ) {
                remaining -= readCount;
                sink.flush();
                progressListener.update(contentLength(), remaining, remaining == 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            Util.closeQuietly(source);
        }
    }

    public interface ProgressListener {
        void update(long totalLenth, long progress, boolean ok);
    }
}