package com.bench.android.core.view.emoji.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.library.R;
import com.bench.android.core.util.dimension.PxConverter;
import com.bench.android.core.view.emoji.adapter.EmojiAdapter;
import com.bench.android.core.view.emoji.adapter.EmojiPagerAdapter;
import com.bench.android.core.view.emoji.bean.EmojiBean;
import com.bench.android.core.view.emoji.gif.GifView;
import com.bench.android.core.view.emoji.gif.OnGifClickListener;
import com.bench.android.core.view.emoji.listener.OnEmojiClickListener;
import com.bench.android.core.view.emoji.utils.EmojUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;



/************************************************************************
 *@Project: utils
 *@Package_Name: com.android.libEmoj.View.view
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/7/22 
 *************************************************************************/
public class MenuEmojiView extends LinearLayout {

    private ViewPager viewPager;
    private CirclePageIndicator circles;
    private LinearLayout emojiContent;
    private GifView gifEmojiContent;
    private LinearLayout emojiBtn;
    private LinearLayout gifEmojiBtn;
    private TextView tvSend;
    private List<List<EmojiBean>> emojiList;
    private FrameLayout flContent;

    private OnEmojiClickListener emojiClickListener;

    public MenuEmojiView(Context context) {
        super(context);
        initView();
    }

    public MenuEmojiView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MenuEmojiView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    public void initView() {
        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.emoji_view_menu_emoji, this);
        flContent = rootView.findViewById(R.id.fl_content);
        viewPager = rootView.findViewById(R.id.viewPager);
        circles = rootView.findViewById(R.id.circles);
        emojiContent = rootView.findViewById(R.id.ll_emoji_content);
        emojiBtn = rootView.findViewById(R.id.ll_emoji_btn);
        gifEmojiBtn = rootView.findViewById(R.id.ll_gif_emoji_btn);
        tvSend = rootView.findViewById(R.id.tv_send);
        emojiBtn.setSelected(true);

        initEvent();
    }

    public void show() {
        if (viewPager.getAdapter() == null) {
            ArrayList<View> viewList = new ArrayList<>();
            if (emojiList == null) {
                emojiList = EmojUtils.getEmojiList(getContext());
            }
            for (int i = 0; i < emojiList.size(); i++) {
                FrameLayout frameLayout = createEmojiView(emojiList.get(i));
                viewList.add(frameLayout);
            }
            EmojiPagerAdapter adapter = new EmojiPagerAdapter(viewList);
            viewPager.setAdapter(adapter);
            circles.setViewPager(viewPager);
        }
        setVisibility(View.VISIBLE);
    }

    private void initEvent() {
        // 设置切换按钮监听
        emojiBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiBtn.setSelected(true);
                gifEmojiBtn.setSelected(false);
                emojiContent.setVisibility(VISIBLE);
                if (gifEmojiContent != null) {
                    gifEmojiContent.setVisibility(GONE);
                }
            }
        });
        gifEmojiBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiBtn.setSelected(false);
                gifEmojiBtn.setSelected(true);
                emojiContent.setVisibility(GONE);
                gifEmojiContent.setVisibility(VISIBLE);
            }
        });
    }

    private FrameLayout createEmojiView(final List<EmojiBean> list) {
        FrameLayout frameLayout = new FrameLayout(getContext());
        RecyclerView recyclerView = new RecyclerView(getContext());
        recyclerView.setLayoutParams(new FrameLayout.LayoutParams
                (FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER));
        recyclerView.setPadding(PxConverter.dp2px(getContext(), 16), 0, PxConverter.dp2px(getContext(), 16), 0);
        frameLayout.addView(recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 7));
        //删除按钮
        list.add(new EmojiBean());
        EmojiAdapter mAdapter = new EmojiAdapter(list);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (emojiClickListener != null) {
                    emojiClickListener.onEmojiClick(list.get(position));
                }
            }
        });
        return frameLayout;
    }


    public void setEmojiClickListener(OnEmojiClickListener emojiClickListener) {
        this.emojiClickListener = emojiClickListener;
    }

    public void setEmojiList(List<List<EmojiBean>> list) {
        this.emojiList = list;
    }

    public void registerSendButtonEvent(final EditText emojiEditText) {
        tvSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                emojiEditText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_ENTER));
            }
        });
    }

    public void setOnGifClickListener(OnGifClickListener onGifClickListener) {
        if (gifEmojiContent != null) {
            gifEmojiContent.setOnGifClickListener(onGifClickListener);
        }
    }

    public void showGifView() {
        gifEmojiBtn.setVisibility(View.VISIBLE);
        gifEmojiContent = new GifView(getContext());
        gifEmojiContent.setVisibility(View.GONE);
        flContent.addView(gifEmojiContent, flContent.getChildCount());
    }
}
