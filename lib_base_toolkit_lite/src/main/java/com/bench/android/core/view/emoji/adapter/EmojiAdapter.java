package com.bench.android.core.view.emoji.adapter;

import com.android.library.R;
import com.bench.android.core.view.emoji.bean.EmojiBean;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;


/**
 * Created by zhouyi on 2016/1/5.
 */
public class EmojiAdapter extends BaseQuickAdapter<EmojiBean, BaseViewHolder> {

    public EmojiAdapter(List<EmojiBean> list) {
        super(R.layout.emoj_lib_item_emoj, list);
    }

    @Override
    protected void convert(BaseViewHolder helper, EmojiBean item) {
        int position = helper.getAdapterPosition();
        if (position == mData.size() - 1) {
            helper.setImageResource(R.id.imageView, R.drawable.icon_emoj_delete);
        } else {
            helper.setImageResource(R.id.imageView, item.getResId());
        }
    }
}
