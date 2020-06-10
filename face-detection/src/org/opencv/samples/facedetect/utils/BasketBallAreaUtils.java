package org.opencv.samples.facedetect.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import org.opencv.samples.facedetect.LogUtils;
import org.opencv.samples.facedetect.model.VideoPlayBackModel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 计算球场区域
 *
 * @author zhouyi
 */
public class BasketBallAreaUtils {

    private static HashMap<Integer, Integer> sColorMap = new HashMap<>();
    private static Bitmap sColorBitmap;

    static {
        sColorMap.put(0xFFFF3939, 1);
        sColorMap.put(0xFF36A23E, 2);
        sColorMap.put(0xFF3D21FF, 3);
        sColorMap.put(0xFFA8FFBF, 4);
        sColorMap.put(0xFFA8B4FF, 5);
        sColorMap.put(0xFFFFDEDE, 6);
        sColorMap.put(0xFFAA7D25, 7);
        sColorMap.put(0xFFDFA8FF, 8);
        sColorMap.put(0xFF2C838A, 9);
        sColorMap.put(0xFFFF56B0, 10);
        sColorMap.put(0xFFFFF9A8, 11);
        sColorMap.put(0xFF48F9FF, 12);
        sColorMap.put(0xFF1139FF, 13);
        sColorMap.put(0xFFF9ACFF, 14);
    }

    private static String picsName = "ic_area_point";

    public static void init(Context context) {
        sColorBitmap = getAssetImage(context, picsName);
    }

    public static Bitmap getAssetImage(Context context, String imageName) {
        InputStream open = null;
        try {
            open = context.getAssets().open("basketball/" + imageName + ".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(open);
        return bitmap;
    }

    /**
     * 获取所有区域分数
     *
     * @param list
     * @return
     */
    public static List<VideoPlayBackModel.PointLocationData> getPointLocationScore(List<VideoPlayBackModel.FrameData> list) {
        List<VideoPlayBackModel.PointLocationData> scoreList = new ArrayList<>();
        //初始化数据
        for (int i = 0; i < 14; i++) {
            VideoPlayBackModel.PointLocationData data = new VideoPlayBackModel.PointLocationData();
            data.shootCount = 0;
            data.winCount = 0;
            scoreList.add(data);
        }

        for (VideoPlayBackModel.FrameData data : list) {
            if (data.x >= ImageSizeConstants.TOP_RIGHT_IMAGE_WIDTH) {
                data.x = ImageSizeConstants.TOP_RIGHT_IMAGE_WIDTH - 1;
            }
            if (data.y >= ImageSizeConstants.TOP_RIGHT_IMAGE_HEIGHT) {
                data.y = ImageSizeConstants.TOP_RIGHT_IMAGE_HEIGHT - 1;
            }
            LogUtils.e("pixel " + data.x + " " + data.y);
            int pixel = sColorBitmap.getPixel(data.x, data.y);
            Log.e("pixel", Integer.toHexString(pixel));
            if (sColorMap.get(pixel) != null) {
                int index = sColorMap.get(pixel) - 1;
                scoreList.get(index).shootCount++;
                if (data.win) {
                    scoreList.get(index).winCount++;
                }
            }

        }
        return scoreList;
    }
}
