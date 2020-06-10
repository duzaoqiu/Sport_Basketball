package org.opencv.samples.facedetect.utils;

import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.util.Log;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Mp4ParserUtils {

    public void start() throws IOException {
        MediaExtractor mediaExtractor = new MediaExtractor();
        //设置MP4文件存放的位置
        mediaExtractor.setDataSource("");
        Log.d("mp4", "getTrackCount: " + mediaExtractor.getTrackCount());
        for (int i = 0; i < mediaExtractor.getTrackCount(); i++) {
            MediaFormat format = mediaExtractor.getTrackFormat(i);
            String mime = format.getString(MediaFormat.KEY_MIME);
            //如果是video格式
            if (mime.startsWith("video")) {
                ByteBuffer allocate = ByteBuffer.allocate(1920 * 1080);
                int i1 = mediaExtractor.readSampleData(allocate, i);
                mediaExtractor.selectTrack(i);
            }
        }
    }

    public void mediaPlayer() {

    }

}
