package com.bench.android.core.view.recyclerview;

import com.chad.library.adapter.base.util.MultiTypeDelegate;

/**
 * Created by lingjiu on 2019/3/8.
 */
public abstract class MultiTypeLayout<T> extends MultiTypeDelegate<T> {

    @Override
    protected int getItemType(T o) {
        int itemResId = getItemResId(o);
        registerItemType(itemResId,
                itemResId);
        return itemResId;
    }

    protected abstract int getItemResId(T o);
}
