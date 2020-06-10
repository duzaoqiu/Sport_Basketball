package com.bench.android.core.util;


import android.util.Log;

import androidx.annotation.NonNull;

import java.util.HashMap;
import java.util.Locale;

/**
 * @author zhouyi
 */
public class LogUtils {

    public static boolean DEBUG = true;

    public static void e(String tag, String message) {
        if (DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void d(String tag, String message) {
        if (message == null) {
            return;
        }
        if (DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void i(String tag, String message) {
        if (message == null) {
            return;
        }
        if (DEBUG) {
            Log.i(tag, message);
        }
    }


    private static String APP_TAG = "KK_";

    private static HashMap<String, String> sCachedTag = new HashMap<>();

    private LogUtils() {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " cannot be instantiated");
    }

    public static void i(String message) {
        if (DEBUG) {
            Log.i(buildTag(APP_TAG), buildMessage(message));
        }
    }

    public static void d(String message) {
        if (DEBUG) {
            Log.d(buildTag(APP_TAG), buildMessage(message));
        }
    }

    public static void e(String message) {
        if (DEBUG) {
            Log.e(buildTag(APP_TAG), buildMessage(message));
        }
    }

//    public static void i(@NonNull String tag, String message) {
//        Log.i(buildTag(tag), buildMessage(message));
//    }
//
//    public static void d(@NonNull String tag, String message) {
//        Log.d(buildTag(tag), buildMessage(message));
//    }
//
//    public static void e(@NonNull String tag, String message) {
//        Log.e(buildTag(tag), buildMessage(message));
//    }

    private static String buildTag(@NonNull String tag) {
        String key = String.format(Locale.US, "%s@%s", tag, Thread.currentThread().getName());

        if (!sCachedTag.containsKey(key)) {
            if (APP_TAG.equals(tag)) {
                sCachedTag.put(key, String.format(Locale.US, "|%s|",
                        tag
                ));
            } else {
                sCachedTag.put(key, String.format(Locale.US, "|%s_%s|",
                        APP_TAG,
                        tag
                ));
            }
        }
        return sCachedTag.get(key);
    }

    private static String buildMessage(String message) {
        StackTraceElement[] traceElements = Thread.currentThread().getStackTrace();

        if (traceElements == null || traceElements.length < 4) {
            return message;
        }
        StackTraceElement traceElement = traceElements[4];

        return String.format(Locale.US, "%s.%s(%s:%d) %s",
                traceElement.getClassName().substring(traceElement.getClassName().lastIndexOf(".") + 1),
                traceElement.getMethodName(),
                traceElement.getFileName(),
                traceElement.getLineNumber(),
                message
        );
    }
}
