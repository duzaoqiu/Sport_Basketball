package com.bench.android.core.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.bench.android.core.app.application.BaseApplication;

/**
 * AndroidManifest工具类
 *
 * @author zhouyi 2019/09/12
 */
public class ManifestUtils {

    /**
     * 获取meta里面的key值
     *
     * @param key
     * @return
     */
    public static String getString(String key) {
        String metaStr = "";
        Context context = BaseApplication.getContext();
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            if (applicationInfo != null && applicationInfo.metaData != null) {
                // 这里为对应meta-data的name
                metaStr = (String) applicationInfo.metaData.get(key);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return metaStr;
    }

    /**
     * 获取meta-data里面int值
     *
     * @param key
     * @return
     */
    public static int getInt(String key) {
        return Integer.parseInt(getString(key));
    }
}
