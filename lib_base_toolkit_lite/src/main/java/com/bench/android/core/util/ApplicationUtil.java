package com.bench.android.core.util;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author danao
 */
public class ApplicationUtil {

    public final static List<Activity> sActivities = new ArrayList<>();

    /**
     * 加入Activity
     */
    public static void addActivity(Activity activity) {
        sActivities.add(activity);
    }

    /**
     * 移除Activity
     */
    public static void removeActivity(Activity activity) {
        sActivities.remove(activity);
    }

    /**
     * 销毁指定activity
     */
    public static void removeActivity(Class<?> c) {
        if (sActivities.size() > 0) {
            for (Activity activity : sActivities) {
                if (activity.getClass().getName().equals(c.getName())) {
                    activity.finish();
                }
            }
        }
    }

    public static Activity getTopActivity() {
        if (sActivities.size() > 0) {
            return sActivities.get(sActivities.size() - 1);
        }
        return null;
    }

    /**
     * 获取正在运行中的Activity
     *
     * @param c
     * @return
     */
    public static Activity getActivity(Class<?> c) {
        if (sActivities.size() > 0) {
            for (Activity activity : sActivities) {
                if (activity.getClass().getName().equals(c.getName())) {
                    return activity;
                }
            }
        }
        return null;
    }

}