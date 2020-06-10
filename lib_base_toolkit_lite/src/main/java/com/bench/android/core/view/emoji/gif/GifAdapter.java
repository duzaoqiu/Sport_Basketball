package com.bench.android.core.view.emoji.gif;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import com.android.library.R;
import com.bench.android.core.net.imageloader.base.ImageLoaderHelper;
import com.bench.android.core.net.imageloader.base.SimpleImageView;
import com.bench.android.core.view.emoji.bean.EmojiBean;
import com.bench.android.core.view.widget.CustomGestureDetector;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;


/**
 * Created by zhouyi on 2016/1/5.
 */
public class GifAdapter extends BaseQuickAdapter<EmojiBean, BaseViewHolder> {

    private OnGifClickListener mOnEmojClickListener;
    private int page;

    private PopupWindow popupWindow;

    public GifAdapter(List<EmojiBean> list, int finalI, OnGifClickListener onEmojClickListener) {
        super(R.layout.emoji_item_gif, list);
        this.page = finalI;
        this.mOnEmojClickListener = onEmojClickListener;
    }

    private void initPopWindow() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.emoji_pop_view, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    @Override
    protected void convert(final BaseViewHolder helper, final EmojiBean item) {
        final int position = helper.getLayoutPosition();

        GifDrawable gifDrawable;
        try {
            gifDrawable = new GifDrawable(mContext.getResources(), item.getResId());
            int frames = gifDrawable.getNumberOfFrames();
            if (frames > 0) {
                Bitmap bitmap = gifDrawable.seekToFrameAndGet(0);
                ((SimpleImageView) helper.getView(R.id.iv_gif)).setImageBitmap(bitmap);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        final CustomGestureDetector gestureDetector = new CustomGestureDetector(mContext, new CustomGestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                if (mOnEmojClickListener == null) {
                    return false;
                }
                mOnEmojClickListener.onEmojAdd(page * 12 + position, item);
                return false;
            }

            @Override
            public boolean onLongPressTapUp(MotionEvent e) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
                return false;
            }

            @Override
            public void onLongPressCancel() {
                if (popupWindow != null && popupWindow.isShowing()) {
                    popupWindow.dismiss();
                }
            }

            @Override
            public void onLongPress(MotionEvent e) {
                if (popupWindow == null) {
                    initPopWindow();
                }
                if (!popupWindow.isShowing()) {
                    ImageLoaderHelper.getInstance()
                            .load(item.getResId())
                            .asGif(true)
                            .into(popupWindow.getContentView().findViewById(R.id.sv_img));
                    int[] location = new int[2];
                    helper.itemView.getLocationOnScreen(location);
                    popupWindow.getContentView().measure(0, 0);
                    popupWindow.showAtLocation((View) helper.itemView.getParent(), Gravity.NO_GRAVITY,
                            location[0] + (helper.itemView.getWidth() - popupWindow.getContentView().getMeasuredWidth()) / 2,
                            location[1] - popupWindow.getContentView().getMeasuredHeight() - 2);
                }
            }
        });
        helper.itemView.setLongClickable(true);
        helper.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });
    }

}

