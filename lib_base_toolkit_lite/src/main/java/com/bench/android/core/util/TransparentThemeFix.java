package com.bench.android.core.util;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * android8.0透明主题bug 修复，防止使用透明主题导致app崩溃
 *
 * @author zhouyi
 */
public class TransparentThemeFix {

    public static void fix(Activity activity) {
        //8.0系统，透明主题，会导致崩溃,这是系统bug
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && TransparentThemeFix.isTranslucentOrFloating(activity)) {
            boolean result = TransparentThemeFix.fixOrientation(activity);
            LogUtils.i("BaseActivity", "onCreate fixOrientation when Oreo, result = " + result);
        }
    }

    private static boolean isTranslucentOrFloating(Activity context) {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = context.obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    private static boolean fixOrientation(Activity activity) {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(activity);
            o.screenOrientation = -1;
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
