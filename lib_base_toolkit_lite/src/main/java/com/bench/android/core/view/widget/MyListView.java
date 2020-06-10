package com.bench.android.core.view.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @author leslie
 * @version $Id: MyListView.java, v 0.1 2014年9月11日 下午1:59:07 leslie Exp $
 */
public class MyListView extends ListView {

    private boolean mShowExpandHeight = true;

    public MyListView(Context context) {
        super(context);
    }

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

    public void setShowExpandHeight(boolean showExpandHeight) {
        if (mShowExpandHeight != showExpandHeight) {
            mShowExpandHeight = showExpandHeight;
            requestLayout();
        }
    }
}
