package com.bench.android.core.util;

import android.view.View;

/**
 * 防止重复点击工具类
 *
 * @author danao
 */
public class ClickOnUtil {

    /**
     * 多少毫秒内，double点击无效
     */
    private final static int DEFAULT_CLICK_TIME = 1000;

    private static long lastClickTime = 0;

    /**
     * 是否快速双击
     */
    public static boolean isDoubleClickQuickly() {
        long currentTime = System.currentTimeMillis();
        if ((currentTime - lastClickTime) < DEFAULT_CLICK_TIME) {
            return true;
        }
        lastClickTime = currentTime;
        return false;
    }

    public static void setOnClickListener(View view, final View.OnClickListener listener) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isDoubleClickQuickly()) {
                    listener.onClick(v);
                }
            }
        });
    }
}
