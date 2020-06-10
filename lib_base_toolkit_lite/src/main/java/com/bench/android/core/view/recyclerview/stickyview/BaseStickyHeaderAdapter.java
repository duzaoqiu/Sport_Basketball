package com.bench.android.core.view.recyclerview.stickyview;


import android.text.TextUtils;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import static com.bench.android.core.view.recyclerview.stickyview.StickyHeaderRecycleListener.FIRST_STICKY_VIEW;
import static com.bench.android.core.view.recyclerview.stickyview.StickyHeaderRecycleListener.HAS_STICKY_VIEW;
import static com.bench.android.core.view.recyclerview.stickyview.StickyHeaderRecycleListener.NONE_STICKY_VIEW;


/************************************************************************
 *@Project: utils
 *@Package_Name: com.android.stickHeader
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/7/4 
 *************************************************************************/
public abstract class BaseStickyHeaderAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {


    public BaseStickyHeaderAdapter(int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public BaseStickyHeaderAdapter(@Nullable List<T> data) {
        super(data);
    }

    public BaseStickyHeaderAdapter(int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(K helper, T item) {
        helper.itemView.setContentDescription(getCompareContent(item));
        int position = helper.getLayoutPosition();
        if (position == 0) {
            showHeader(helper,true);
            helper.itemView.setTag(FIRST_STICKY_VIEW);
        } else {
            //当前Item头部与上一个Item头部相同，则隐藏头部
            if (TextUtils.equals(getCompareContent(item), getCompareContent(mData.get(position - 1)))
                    || TextUtils.isEmpty(getCompareContent(item))) {
                showHeader(helper,false);
                helper.itemView.setTag(NONE_STICKY_VIEW);
            } else {
                showHeader(helper,true);
                helper.itemView.setTag(HAS_STICKY_VIEW);
            }
        }
    }

    /**
     * 是否显示头
     *
     * @param helper
     * @param isShow
     */
    protected abstract void showHeader(K helper, boolean isShow);

    /**
     * 对比的字段，就是属于不同分类的字段
     *
     * @param item
     * @return
     */
    protected abstract String getCompareContent(T item);

}
