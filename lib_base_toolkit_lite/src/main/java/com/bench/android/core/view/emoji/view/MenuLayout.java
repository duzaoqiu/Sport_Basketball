package com.bench.android.core.view.emoji.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.library.R;
import com.bench.android.core.view.emoji.bean.EmojiBean;
import com.bench.android.core.view.emoji.bean.MenuMoreItem;
import com.bench.android.core.view.emoji.gif.OnGifClickListener;
import com.bench.android.core.view.emoji.listener.OnEmojiClickListener;
import com.bench.android.core.view.emoji.listener.OnMenuItemMoreClickListener;
import com.bench.android.core.view.emoji.utils.EmojiKeyboard;

import java.util.List;

/**
 * Created by mrs on 2017/4/10.
 */

public class MenuLayout extends FrameLayout {

    /**
     * 0表示表情按钮
     * 1表示更多按钮
     */
    public static final int INDEX_VIEW_EMOJI = 0;
    public static final int INDEX_VIEW_MORE = 1;
    /**
     * 表情菜单view
     */
    private MenuEmojiView mMenuEmojiView;
    /**
     * 更多菜单view
     */
    private MenuMoreView menuMoreView;


    public MenuLayout(@NonNull Context context) {
        this(context, null);
    }

    public MenuLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MenuLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        boolean hasGifView = true;
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuLayout);
            hasGifView = typedArray.getBoolean(R.styleable.MenuLayout_with_gif_view, false);
            typedArray.recycle();
        }
        View view = LayoutInflater.from(context).inflate(R.layout.emoji_view_menu_layout, this, true);
        mMenuEmojiView = view.findViewById(R.id.menu_emoji);
        menuMoreView = view.findViewById(R.id.menu_more);
        if (hasGifView) {
            mMenuEmojiView.showGifView();
        }
    }

    public void setEmojiList(List<List<EmojiBean>> list) {
        mMenuEmojiView.setEmojiList(list);
    }

    public void setSpanCount(int spanCount) {
        menuMoreView.setSpanCount(spanCount);
    }

    public void updateImg(int position, int imgResId) {
        menuMoreView.updateImg(position, imgResId);
    }


    private void showContentView(View view) {
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            child.setVisibility(GONE);
        }
        if (view instanceof MenuEmojiView) {
            ((MenuEmojiView) view).show();
        } else if (view instanceof MenuMoreView) {
            ((MenuMoreView) view).show();
        }
    }

    public void setEmojiKeyboard(EmojiKeyboard emojiKeyboard) {
        emojiKeyboard.setOnPanelStateChangeListener(new EmojiKeyboard.OnPanelStateChangeListener() {
            @Override
            public void onShowPanel(int index) {
                showContentView(index == INDEX_VIEW_EMOJI ? mMenuEmojiView : menuMoreView);
            }

            @Override
            public void onHidePanel() {
            }

        });
    }

    public void registerSendButtonEvent(final EditText emojiEditText) {
        mMenuEmojiView.registerSendButtonEvent(emojiEditText);
    }

    public void setMenuMoreList(List<MenuMoreItem> menuMoreList) {
        menuMoreView.setMenuMoreList(menuMoreList);
    }

    /**
     * 按钮展开更多 item的点击回调
     *
     * @param onMenuItemMoreClickListener
     */
    public void setOnMenuMoreItemClickListener(OnMenuItemMoreClickListener onMenuItemMoreClickListener) {
        menuMoreView.setOnMenuMoreItemClickListener(onMenuItemMoreClickListener);
    }

    /**
     * 表情的点击回调
     *
     * @param emojiClickListener
     */
    public void setEmojiClickListener(OnEmojiClickListener emojiClickListener) {
        mMenuEmojiView.setEmojiClickListener(emojiClickListener);
    }

    /**
     * gif图片点击回调
     *
     * @param onGifClickListener
     */
    public void setOnGifClickListener(OnGifClickListener onGifClickListener) {
        mMenuEmojiView.setOnGifClickListener(onGifClickListener);
    }
}
