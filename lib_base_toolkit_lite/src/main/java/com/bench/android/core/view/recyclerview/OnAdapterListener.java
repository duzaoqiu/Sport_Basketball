package com.bench.android.core.view.recyclerview;

import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by danao on 2019/2/25.
 */
public interface OnAdapterListener<T> {

    int getItemLayoutId();

    void onBindViewHolder(BaseViewHolder helper, T item);

}
