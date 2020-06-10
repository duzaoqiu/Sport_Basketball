package com.bench.android.core.view.emoji.adapter;

import com.android.library.R;
import com.bench.android.core.view.emoji.bean.MenuMoreItem;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * @author xingjiu
 */
public class MenuMoreAdapter extends BaseQuickAdapter<MenuMoreItem, BaseViewHolder> {

    private List<MenuMoreItem> menuMoreList;

    public MenuMoreAdapter(List<MenuMoreItem> menuMoreList) {
        super(R.layout.emoji_item_bottom_grid_item,menuMoreList);
        this.menuMoreList = menuMoreList;
    }

    @Override
    protected void convert(BaseViewHolder helper, MenuMoreItem item) {
        helper.setImageResource(R.id.grid_img, item.getImgRes());
        helper.setText(R.id.grid_txt, item.getTxt());
    }

}