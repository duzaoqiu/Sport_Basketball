package com.bench.android.core.view.flowlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.LayoutDirection;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.text.TextUtilsCompat;

import com.android.library.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * Created by zaozao on 2018/7/9.
 */
public class MyFlowLayout extends ViewGroup {

    private static final int LEFT = -1;
    private static final int CENTER = 0;
    private static final int RIGHT = 1;

    //所有需要显示的view集合
    protected List<List<View>> mAllViews = new ArrayList<List<View>>();
    //行高
    protected List<Integer> mLineHeight = new ArrayList<Integer>();
    //行宽
    protected List<Integer> mLineWidth = new ArrayList<Integer>();

    private int mGravity;

    private int spanCount = 0;

    private int maxLine = 0;//固定列数的时候没有做限制

    private boolean addLastOne = false;

    private boolean singleLine = false;

    //每一行view的集合的集合
    private List<View> lineViews = new ArrayList<>();

    int width = 0;//屏幕宽度
    int lastChildWidth = 0;//最后一个view的宽度

    public MyFlowLayout(Context context) {
        this(context, null);
    }

    public MyFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TagFlowLayout);
        mGravity = ta.getInt(R.styleable.TagFlowLayout_tag_gravity, LEFT);
        spanCount = ta.getInt(R.styleable.TagFlowLayout_span_count, -1);
        singleLine = ta.getBoolean(R.styleable.TagFlowLayout_single_line, false);
        maxLine = ta.getInt(R.styleable.TagFlowLayout_max_line, 0);
        addLastOne = ta.getBoolean(R.styleable.TagFlowLayout_add_lastOne, false);
        int layoutDirection = TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault());
        if (layoutDirection == LayoutDirection.RTL) {
            if (mGravity == LEFT) {
                mGravity = RIGHT;
            } else {
                mGravity = LEFT;
            }
        }
        ta.recycle();

    }


    public void setAddLastOne(boolean addLastOne) {
        this.addLastOne = addLastOne;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int layoutWidth = MeasureSpec.getSize(widthMeasureSpec);
        int layoutHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        int layoutWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int layoutHeightMode = MeasureSpec.getMode(heightMeasureSpec);

        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) getLayoutParams();

        int needHeight = 0;
        int needWidth = 0;
        int lines = 0;
        int childCount = getChildCount();
        if (childCount == 0) {
            return;
        }

        if (spanCount > 0) {//如果有固定列数-----------------根据列数计算宽度

            int usefulWidth = layoutWidth - marginLayoutParams.rightMargin - marginLayoutParams.leftMargin;//当前屏幕可用的宽度

            View firstView = getChildAt(0);

            measureChild(firstView, widthMeasureSpec, heightMeasureSpec);//测量第一个view，便于拿到param

            MarginLayoutParams lp = (MarginLayoutParams) firstView.getLayoutParams();

            int childWidth = usefulWidth / spanCount - lp.leftMargin - lp.rightMargin;//计算子view的宽度

            int childHeight = firstView.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;//计算子view的高度

            lines = childCount / spanCount + (childCount % spanCount > 0 ? 1 : 0);

            if (singleLine) {
                lines = 1;
            }

            for (int i = 0; i < childCount; i++) {
                LayoutParams lp2 = firstView.getLayoutParams();

                lp2.width = childWidth;

                getChildAt(i).setLayoutParams(lp2);//重新给每个子view设置宽度

                measureChild(getChildAt(i), widthMeasureSpec, heightMeasureSpec);//测量每个子view
            }

            needHeight = lines * childHeight;

            needWidth = layoutWidth;

            setMeasuredDimension(needWidth, needHeight);

        } else {//没有固定列数，宽高不一定，需要计算

            //默认每一行的宽高
            int lineCurrentWidth = 0;
            int childHeight = 0;

            for (int i = 0; i < childCount; i++) {

                View childView = getChildAt(i);

                measureChild(childView, widthMeasureSpec, heightMeasureSpec);

                MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();
                int childWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
                childHeight = childView.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;

                if (lineCurrentWidth + childWidth > layoutWidth) {//换行

                    needWidth = Math.max(lineCurrentWidth, needWidth);
                    lines++;

                    //新的一行重置
                    lineCurrentWidth = childWidth;

                } else {

                    lineCurrentWidth += childWidth;//当前行宽度逐渐增加
                }

                if (i == childCount - 1) {// lineHeight的高度是一行内所有高度的最大值
                    needWidth = Math.max(lineCurrentWidth, needWidth);
                    lines++;
                }

                if (maxLine > 0 && lines > maxLine) {//如果有最大行数限制，跳出循环
                    lines = maxLine;
                    if (addLastOne) {
                        measureChild(getChildAt(getChildCount() - 1), widthMeasureSpec, heightMeasureSpec);
                    }
                    break;
                }
            }

            needHeight = childHeight * lines;//先按照每一行高度是一样的

            if (singleLine) {
                needHeight = childHeight;
            }

            int measuredWidth = layoutWidthMode == MeasureSpec.EXACTLY ? layoutWidth : needWidth + getPaddingRight() + getPaddingLeft();
            int measuredHeight = layoutHeightMode == MeasureSpec.EXACTLY ? layoutHeight : needHeight + getPaddingBottom() + getPaddingTop();
//            Log.e("kk", "onMeasure-------measuredHeight--" + measuredHeight + ":::::::::measuredWidth----" + measuredWidth);
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }

    @Override
    protected void onLayout(boolean b, int a, int i1, int i2, int i3) {

        clear();
        width = getWidth();

        int lineWidth = 0;
        int lineHeight = 0;

        int childCount = getChildCount();

        if (childCount == 0) {
            return;
        }

        if (spanCount > 0) {//如果有固定列数-----------------宽高是固定的
            View childView = getChildAt(0);
            MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();

            int childWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;

            lineHeight = childView.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;

            int lines = childCount / spanCount + (childCount % spanCount > 0 ? 1 : 0);

            for (int i = 0; i < lines; i++) {//6
                int index = 0;
                for (int m = 0; m < spanCount; m++) {
                    index = spanCount * i + m;
                    lineViews.add(getChildAt(index));
                    lineWidth = lineWidth + childWidth;
                    if (index == childCount - 1) {
                        break;
                    }
                }

                if (index < childCount) {
                    mLineHeight.add(lineHeight);
                    mLineWidth.add(lineWidth);
                    lineWidth = 0;
                    mAllViews.add(lineViews);
                    lineViews = new ArrayList<View>();//每一行都对应一个新的行view集合
                }

                if (singleLine) {
                    break;
                }

            }
        } else {//没有固定列数，宽高不一定，需要计算
            int lineCount = 0;

            for (int i = 0; i < childCount; i++) {

                View childView = getChildAt(i);

                MarginLayoutParams lp = (MarginLayoutParams) childView.getLayoutParams();

                int childWidth = childView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
//                LogUtils.e("kk",""+ childView.getMeasuredWidth()+"----------layout");
                if (lineHeight == 0) {//每一行高度固定
                    lineHeight = childView.getMeasuredHeight() + lp.bottomMargin + lp.topMargin;
                }

//                LogUtils.e("kk", "-----------" + lineHeight);

                if (lineWidth + childWidth > width - getPaddingLeft() - getPaddingRight()) {//换行
                    lineCount++;

                    mLineHeight.add(lineHeight);
                    mLineWidth.add(lineWidth);
                    mAllViews.add(lineViews);
//                    //新的一行重置
//                    lineHeight = childHeight;
                    lineWidth = childWidth;

                    lineViews = new ArrayList<View>();//每一行都对应一个新的行view集合

                    if (singleLine) {
                        break;
                    }

                } else {
                    lineWidth += childWidth;//当前行宽度逐渐增加
//                    lineHeight = Math.max(lineHeight, childHeight);//比较当前子view与当前的最大行高取最大值
                }

                lineViews.add(childView);//把每一行的view添加进去

            }

            //for循环结束后----最后一行
            mLineHeight.add(lineHeight);
//            LogUtils.e("kk", "-----------" + lineHeight);
            mLineWidth.add(lineWidth);
            mAllViews.add(lineViews);
            lineCount++;

            //-------------------------------------------------------------------------------------
            if (maxLine > 0 && lineCount > maxLine) {//如果行数大于最大行数限制
                mLineHeight.clear();

                for (int i = 0; i < maxLine; i++) {
                    mLineHeight.add(lineHeight);
                }

                if (addLastOne) {//要把最后一个提到前面---参考详情页
                    lineViews = new ArrayList<View>();//每一行都对应一个新的行view集合

                    lineViews = mAllViews.get(maxLine - 1);

                    lastChildWidth = getViewWidth(getChildAt(getChildCount() - 1));

                    getResultViews(mLineWidth.get(maxLine - 1) + lastChildWidth);
                }
            }
        }

        //开始layout
        int left = getPaddingLeft();
        int top = getPaddingTop();

        int lineNum = mLineHeight.size();

        for (int m = 0; m < lineNum; m++) {
            lineViews = mAllViews.get(m);
            lineHeight = mLineHeight.get(m);

            // set gravity
            int currentLineWidth = this.mLineWidth.get(m);

            switch (this.mGravity) {
                case LEFT:
                    left = getPaddingLeft();
                    break;
                case CENTER:
                    left = (width - currentLineWidth) / 2 + getPaddingLeft();//左边的点，将空白地方一分为二就是左边的
                    break;
                case RIGHT:
                    //  适配了rtl，需要补偿一个padding值
                    left = width - (currentLineWidth + getPaddingLeft()) - getPaddingRight();
                    //  适配了rtl，需要把lineViews里面的数组倒序排
                    Collections.reverse(lineViews);
                    break;
                default:
                    break;
            }
            for (int j = 0; j < lineViews.size(); j++) {
                View child = lineViews.get(j);
                if (child.getVisibility() == View.GONE) {
                    continue;
                }

                MarginLayoutParams lp = (MarginLayoutParams) child
                        .getLayoutParams();

                int lc = left + lp.leftMargin;
                int tc = top + lp.topMargin;

                int rc = lc + child.getMeasuredWidth();
                int bc = tc + child.getMeasuredHeight();

//                LogUtils.e("kk", "lc:" + lc + "     tc:" + tc + "     rc:" + rc + "    bc:" + bc);
                child.layout(lc, tc, rc, bc);

                left += child.getMeasuredWidth() + lp.leftMargin
                        + lp.rightMargin;
            }
            top += lineHeight;
        }
    }

    private List<View> getResultViews(int allWidth) {
        if (allWidth > width - getPaddingLeft() - getPaddingRight()) {
            View v = lineViews.get(lineViews.size() - 1);
            int viewWidth = getViewWidth(v);
            int currentWidth = mLineWidth.get(maxLine - 1) - viewWidth + lastChildWidth;
            lineViews.remove(lineViews.size() - 1);
            mLineWidth.set(maxLine - 1, mLineWidth.get(maxLine - 1) - viewWidth);//最后一行的宽度也在变化
            if (lineViews.size() > 0) {
                return getResultViews(currentWidth);
            } else {
                mLineWidth.set(maxLine - 1, lastChildWidth);
                lineViews.add(getChildAt(getChildCount() - 1));
                return lineViews;
            }
        } else {
            mLineWidth.set(maxLine - 1, mLineWidth.get(maxLine - 1) + lastChildWidth);
            lineViews.add(getChildAt(getChildCount() - 1));
            return lineViews;
        }
    }

    private int getViewWidth(View view) {

        MarginLayoutParams lp = (MarginLayoutParams) view.getLayoutParams();

        return view.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
    }

    /**
     *
     */
    private void clear() {
        mAllViews.clear();
        mLineHeight.clear();
        mLineWidth.clear();
        lineViews.clear();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }
}
