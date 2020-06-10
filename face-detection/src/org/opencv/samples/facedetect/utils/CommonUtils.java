package org.opencv.samples.facedetect.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import org.opencv.samples.facedetect.LogUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtils {


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        return (int) (0.5f + dpValue * Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static float px2dp(float pxValue) {
        return (pxValue / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     * Get Display
     *
     * @param context Context for get WindowManager
     * @return Display
     */
    public static Display getDisplay(Context context) {
        WindowManager wm = (WindowManager) context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (wm != null) {
            return wm.getDefaultDisplay();
        } else {
            return null;
        }
    }

    //如果指定的视频的宽高都大于了MICRO_KIND的大小，那么你就使用MINI_KIND就可以了
    public static Bitmap getVideoThumbnail(String videoPath, int kind, int width, int height) {
        Bitmap bitmap = null;
        // 获取视频的缩略图
        bitmap = ThumbnailUtils.createVideoThumbnail(videoPath, kind);
        if (width > 0 && height > 0) {
            bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
                    ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        }

        return bitmap;
    }

    public static VideoProperty getVideoFrameRate(String path) {
        VideoProperty videoProperty = new VideoProperty();
        MediaExtractor extractor = new MediaExtractor();
        int frameRate = 24;
        long duration = 0;
        try {
            //Adjust data source as per the requirement if file, URI, etc.
            extractor.setDataSource(path);
            int numTracks = extractor.getTrackCount();
            for (int i = 0; i < numTracks; ++i) {
                MediaFormat format = extractor.getTrackFormat(i);
                String mime = format.getString(MediaFormat.KEY_MIME);
                if (mime.startsWith("video/")) {
                    if (format.containsKey(MediaFormat.KEY_FRAME_RATE)) {
                        frameRate = format.getInteger(MediaFormat.KEY_FRAME_RATE);
                        duration = format.getLong(MediaFormat.KEY_DURATION);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("帧率", e.toString());
        } finally {
            //Release stuff
            extractor.release();
        }
        Log.e("帧率", frameRate + " " + duration);

        videoProperty.setDuration(duration);
        videoProperty.setFrameRate(frameRate);
        return videoProperty;
    }

    /**
     * 计算百分比
     *
     * @param child  分子
     * @param parent 分母
     * @return
     */
    public static String calculateRate(int child, int parent) {
        if (parent == 0) {
            return "0%";
        }
        return (int) ((((float) (child)) / parent) * 100) + "%";
    }

    /**
     * 计算角度
     *
     * @param child  分子
     * @param parent 分母
     * @return
     */
    public static float calculateAngle(int child, int parent) {
        if (parent == 0) {
            return 0f;
        }
        return ((((float) (child)) / parent) * 360);
    }

    public static void showFloatLog(String msg, double[] array) {
        for (double f : array) {
            LogUtils.e(msg + "    " + f);
        }
    }

    public static void showFloatLog(String msg, float[] array) {
        for (Number f : array) {
            LogUtils.e(msg + "    " + f);
        }
    }

    /**
     * Create a File for saving an image or video
     */
    public static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "QiuBao");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("QiuBao", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    /**
     * Create a File for saving an image or video
     */
    public static String getOutputMediaPath(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "QiuBao");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg";
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4";
        } else {
            return null;
        }

        return mediaFile;
    }

    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }


}
