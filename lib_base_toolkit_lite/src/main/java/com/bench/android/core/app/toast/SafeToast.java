package com.bench.android.core.app.toast;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.StringRes;

import java.lang.reflect.Field;

/**
 * 解决7.1上toast崩溃的问题
 * <p>
 * Created by qiaomu on 2018/1/29.
 */

/**
 * Toast显示与隐藏
 *
 * 首先Toast显示依赖于一个窗口，这个窗口被WMS管理（WindowManagerService），当需要show的时候这个请求会放在WMS请求队列中，
 * 并且会传递一个TN类型的Bider对象给WMS，WMS并生成一个token传递给Android进行显示与隐藏，但是如果UI线程的某个线程发生了阻塞，
 * 并且已经NotificationManager检测已经超时就不删除token记录，此时token已经过期，阻塞结束的时候再显示的时候就发生了异常。
 * ---------------------
 * 作者：icuihai
 * 来源：CSDN
 * 原文：https://blog.csdn.net/icuihai/article/details/81179105
 * 版权声明：本文为博主原创文章，转载请附上博文链接！
 */

public class SafeToast {
    private static Field sField_TN;
    private static Field sField_TN_Handler;
    private static Toast mToast;

    static {
        try {
            sField_TN = Toast.class.getDeclaredField("mTN");
            sField_TN.setAccessible(true);
            sField_TN_Handler = sField_TN.getType().getDeclaredField("mHandler");
            sField_TN_Handler.setAccessible(true);
        } catch (Exception e) {
        }
    }

    private static void hook(Toast toast) {
        //自己添加一个try，catch防止7.1.1上token异常导致崩溃
        try {
            Object tn = sField_TN.get(toast);
            Handler preHandler = (Handler) sField_TN_Handler.get(tn);
            sField_TN_Handler.set(tn, new SafelyHandlerWarpper(preHandler));
        } catch (Exception e) {
        }
    }

    public static Toast newInstance(Context context, CharSequence message, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), message, duration);
        }
        hook(mToast);
        return mToast;
    }

    public static Toast newInstance(Context context, @StringRes int resId, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(context.getApplicationContext(), resId, duration);
        }
        hook(mToast);
        return mToast;
    }

    private static class SafelyHandlerWarpper extends Handler {

        private Handler impl;

        public SafelyHandlerWarpper(Handler impl) {
            this.impl = impl;
        }

        @Override
        public void dispatchMessage(Message msg) {
            try {
                super.dispatchMessage(msg);
            } catch (Exception e) {
            }
        }

        @Override
        public void handleMessage(Message msg) {
            impl.handleMessage(msg);//需要委托给原Handler执行
        }
    }
}
