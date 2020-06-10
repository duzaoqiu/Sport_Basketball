package com.bench.android.core.util.dimension;

import android.text.TextPaint;
import android.widget.TextView;

/**
 *
 * 计算文字宽度，高度工具类
 * @author zhouyi
 */
public class TextCalculator {

    public static float measureTextSize(String content, TextView tv) {

        TextPaint newPaint = new TextPaint();

        newPaint.setTextSize(tv.getTextSize());

        float newPaintWidth = newPaint.measureText(content);

        return newPaintWidth;

    }

}
