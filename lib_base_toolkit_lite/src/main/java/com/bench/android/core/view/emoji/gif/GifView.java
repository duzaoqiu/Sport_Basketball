package com.bench.android.core.view.emoji.gif;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.library.R;
import com.bench.android.core.view.emoji.bean.EmojiBean;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyi on 2017/3/10.
 */
public class GifView extends LinearLayout {

    private ViewPager viewPager;
    private CirclePageIndicator circles;
    private OnGifClickListener mOnGifClickListener;

    public GifView(Context context) {
        super(context);
    }

    public GifView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public GifView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        LayoutInflater.from(getContext()).inflate(R.layout.emoj_lib_layout_emoj_show, this);
        viewPager = findViewById(R.id.viewPager);
        circles = findViewById(R.id.circles);
        setPagerAdapter();
    }

    private void setPagerAdapter() {
        final List<View> viewList = getViewList();
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                View view = viewList.get(position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                View view = viewList.get(position);
                container.removeView(view);
            }
        });
        circles.setViewPager(viewPager);
    }

    private List<View> getViewList() {
        List<View> viewList = new ArrayList<>();
        List<List<EmojiBean>> list = GifUtils.getGifList(getContext());
        for (int i = 0; i < list.size(); i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.emoji_view_menu_gif, null);
            RecyclerView rcvGif = view.findViewById(R.id.rcv_gif);
            rcvGif.setLayoutManager(new GridLayoutManager(getContext(), 4));
            final List<EmojiBean> emojiList = list.get(i);
            rcvGif.setAdapter(new GifAdapter(emojiList, i, mOnGifClickListener));
            viewList.add(view);
        }
        return viewList;
    }

    @Override
    protected void onDetachedFromWindow() {
        removeAllViews();
        super.onDetachedFromWindow();
    }

    public void setOnGifClickListener(OnGifClickListener onGifClickListener) {
        this.mOnGifClickListener = onGifClickListener;
    }
}
