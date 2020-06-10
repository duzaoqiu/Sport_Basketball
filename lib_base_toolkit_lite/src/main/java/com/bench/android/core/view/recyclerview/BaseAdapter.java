package com.bench.android.core.view.recyclerview;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.library.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created on 2019/2/25.
 *
 * @author danao
 */
public abstract class BaseAdapter<E> extends BaseQuickAdapter<E, BaseViewHolder> {

    /**
     * 单布局条目
     *
     * @param layoutId
     */
    public BaseAdapter(int layoutId) {
        super(layoutId);
    }

    /**
     * 设置多布局条目
     *
     * @param multiTypeLayout
     */
    public BaseAdapter(MultiTypeLayout<E> multiTypeLayout) {
        super(null);
        setMultiTypeDelegate(multiTypeLayout);
    }

    @Override
    protected void convert(BaseViewHolder helper, E item) {
        onBindViewHolder(helper, item);
    }

    public abstract void onBindViewHolder(BaseViewHolder helper, E item);

    /**
     * 默认的emptyView
     *
     * @param charSequence
     * @param imgResId
     * @param viewGroup    recyclerView
     */
    public void setEmptyView(CharSequence charSequence, int imgResId, ViewGroup viewGroup) {
        View defaultEmptyView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.base_widget_empty_layout,
                viewGroup, false);
        ImageView imageView = defaultEmptyView.findViewById(R.id.imageView);
        TextView emptyTipTv = defaultEmptyView.findViewById(R.id.emptyTipTv);
        if (imageView != null && imgResId > 0) {
            imageView.setImageResource(imgResId);
        }
        if (emptyTipTv != null&& !TextUtils.isEmpty(charSequence)) {
            emptyTipTv.setText(charSequence);
        }
        setEmptyView(defaultEmptyView);
//        isUseEmpty(false);
    }
}