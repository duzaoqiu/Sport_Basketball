package com.bench.android.core.app.dialog;

import android.app.Dialog;

/**
 * Created by lingjiu on 2019/2/18.
 */
public interface OnMyItemClickListener<T> {
    void onClickListener(T t, int position, Dialog dialog);
}
