package com.bench.android.core.util;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.net.Uri;
import android.text.TextUtils;
import android.view.View;

import com.bench.android.core.cache.CacheManager;
import com.bench.android.core.cache.SdCardCacheManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * 布局工具，用于保存布局到本地
 *
 * @author zhouyi
 */
public class LayoutUtils {

    /**
     * 保存View到相册
     *
     * @param view
     * @return
     */
    public static String saveLayout2Photo(View view) {
        Context context = view.getContext();
        String path = SDCardUtils.getRootPath(context);
        //限制图片的宽度
        int maxWidth = 500;
        if (path != null) {
            Bitmap bitmap = convertViewToBitmap(view, maxWidth);

            SdCardCacheManager cache = (SdCardCacheManager) CacheManager.getCacheService(CacheManager.CACHE_SDCARD);
            cache.put("png",bitmap);

            String savePath = savePhotoToSDCard(bitmap, path, String.valueOf(System.currentTimeMillis()));
            bitmap.recycle();
            if (!TextUtils.isEmpty(savePath)) {
                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(new File(savePath))));
                return "保存成功";
            }
        }
        return "保存失败";
    }


    /**
     * 将View转换为Bitmap
     *
     * @param view
     * @param maxWidth
     * @return
     */
    public static Bitmap convertViewToBitmap(View view, int maxWidth) {
        Bitmap bitmap = null;

        int viewWidth = view.getWidth();
        int viewHeight = view.getHeight();
        bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);

        final Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);

        if (viewWidth > maxWidth) {
            float scaleWidth = ((float) maxWidth) / viewWidth;
            //缩放比例
            Matrix matrix = new Matrix();
            matrix.postScale(scaleWidth, scaleWidth);
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, false);
        } else {
            return bitmap;
        }
    }

    private static String savePhotoToSDCard(Bitmap photoBitmap, String path, String photoName) {
        String savePath = "";
        File dir = new File(path);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File photoFile = new File(path, photoName + ".png");
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(photoFile);
            if (photoBitmap != null) {
                if (photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream)) {
                    fileOutputStream.flush();
                    savePath = photoFile.getAbsolutePath();
                }
            }
        } catch (FileNotFoundException e) {
            photoFile.delete();
            e.printStackTrace();
        } catch (IOException e) {
            photoFile.delete();
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return savePath;
    }
}
