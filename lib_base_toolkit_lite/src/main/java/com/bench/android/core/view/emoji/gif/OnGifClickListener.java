package com.bench.android.core.view.emoji.gif;


import com.bench.android.core.view.emoji.bean.EmojiBean;

/**
 * Created by zhouyi on 2017/3/10.
 */

public interface OnGifClickListener {

    void onEmojAdd(int index, EmojiBean bean);

    void onEmojDelete();
}
