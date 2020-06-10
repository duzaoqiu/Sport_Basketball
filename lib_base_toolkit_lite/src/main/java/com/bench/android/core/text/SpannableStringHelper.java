package com.bench.android.core.text;

import android.graphics.Color;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;

/**
 * SpannableString帮助类
 *
 * @author zhouyi
 */
public class SpannableStringHelper {

    public static SpannableString changeTextColor(String str, int color, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(color), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static SpannableString changeTextSize(String str, float size, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new RelativeSizeSpan(size), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static SpannableString changeTextColor(String str, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static SpannableString changeTextColor(String str) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ec4432")), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static SpannableString changeTextColor(String str, int color) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;

    }

    public static SpannableString changeTextColorAndSize(String str, int color, int size) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new AbsoluteSizeSpan(size, true), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static SpannableString changeTextSize(String str, int size) {
        return changeTextSize(str, size, 0, str.length());
    }

    public static SpannableString changeTextSize(String str, int size, int start, int end) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new AbsoluteSizeSpan(size, true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;
    }

    public static SpannableString changeBackgroundAndTextColor(String str, int color) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new ForegroundColorSpan(Color.RED), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new BackgroundColorSpan(color), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;

    }

    public static SpannableString changeBackground(String str, int color) {
        SpannableString ss = new SpannableString(str);
        ss.setSpan(new BackgroundColorSpan(color), 0, str.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss;

    }

    public static SpannableStringBuilder customSpan(String str, String[] str2, int color) {

        String[] strArray = str.split("x");

        SpannableStringBuilder ssb = new SpannableStringBuilder();

        for (int i = 0; i < str2.length; i++) {

            SpannableString ss = new SpannableString(str2[i]);

            ss.setSpan(new ForegroundColorSpan(color), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            ssb.append(strArray[i]);

            ssb.append(ss);

        }

        ssb.append(strArray[strArray.length - 1]);

        return ssb;

    }
}
