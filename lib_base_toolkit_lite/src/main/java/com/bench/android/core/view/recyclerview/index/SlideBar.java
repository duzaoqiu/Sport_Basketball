package com.bench.android.core.view.recyclerview.index;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.android.library.R;

import java.util.List;

/**
 * Edit by zhoushen 2019/07/05
 * 1、修改点击没有背景色，
 * 2、修改默认索引字母居中
 *
 * @author wanshi
 * @date 2019/3/14
 * A-Z索引bar
 */
public class SlideBar extends View {

    // 触摸事件
    private OnIndexListener onIndexListener;

    // 26个字母
    private String[] b = null;

    private int choose = -1;// 选中

    private Paint paint = new Paint();

    private TextView mTextDialog;

    private int height; //获取对应高度

    private int width;  //获取对应宽度

    private int singleAlphabetHeight = 0;

    //总共包含28个字符
    private int mAllSize = 28;

    private int alphabetLength;

    private int initHeight;

    public void setTextView(TextView mTextDialog) {
        this.mTextDialog = mTextDialog;
    }

    public SlideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SlideBar(Context context) {
        super(context);
    }

    public void setIndexText(List<String> list) {

        String[] strs = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            strs[i] = list.get(i);
        }
        b = strs;

        invalidate();

    }

    /**
     * 重写这个方法
     */
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if (b == null) {
            return;
        }

        alphabetLength = b.length * singleAlphabetHeight;


        initHeight = height / 2 - alphabetLength / 2 + (int) getResources().getDimension(R.dimen.padding_10dp);

        for (int i = 0; i < b.length; i++) {
            paint.setColor(Color.parseColor("#8e8e93"));
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setAntiAlias(true);
            paint.setTextSize(getContext().getResources().getDimension(R.dimen.text_size_small));

            // 选中的状态
            if (i == choose) {
                paint.setColor(Color.parseColor("#3399ff"));
                paint.setFakeBoldText(true);
            }

            // x坐标等于中间-字符串宽度的一半.
            float xPos = width / 2 - paint.measureText(b[i]) / 2;

            float yPos = initHeight + i * singleAlphabetHeight;

            canvas.drawText(b[i], xPos, yPos, paint);

            paint.reset();// 重置画笔

        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (b == null) {
            return true;
        }

        final int action = event.getAction();

        final float y = event.getY();// 点击y坐标

        final int oldChoose = choose;

        final OnIndexListener listener = onIndexListener;

        final int c = (int) ((y - initHeight + getResources().getDimension(R.dimen.padding_10dp)) / alphabetLength * b.length);

        switch (action) {
            case MotionEvent.ACTION_UP:
                setBackgroundColor(Color.parseColor("#00000000"));
                choose = -1;//
                invalidate();
                if (mTextDialog != null) {
                    mTextDialog.setVisibility(View.INVISIBLE);
                }
                break;
            default:
                // 设置右侧字母列表[A,B,C,D,E....]的背景颜色
                setBackgroundColor(Color.parseColor("#d5d5d5"));
                if (oldChoose != c) {
                    if (c >= 0 && c < b.length) {
                        if (listener != null) {
                            listener.onIndex(b[c]);
                        }
                        if (mTextDialog != null) {
                            mTextDialog.setText(b[c]);
                            mTextDialog.setVisibility(View.VISIBLE);
                        }
                        choose = c;
                        invalidate();
                    }
                }
                break;
        }
        return true;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        height = getMeasuredHeight();

        width = getMeasuredWidth();

        singleAlphabetHeight = height / mAllSize;

    }

    /**
     * 向外公开的方法
     */
    public void setOnIndexListener(OnIndexListener onIndexListener) {
        this.onIndexListener = onIndexListener;
    }

    /**
     * 接口
     *
     * @author coder
     */
    public interface OnIndexListener {
        void onIndex(String s);
    }

}
