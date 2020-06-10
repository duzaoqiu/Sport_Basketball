package com.bench.android.core.view.recyclerview;

/**
 * 刷新回调
 * Created by lingjiu on 2019/3/9.
 */
public interface OnRefreshListener {

    /**
     * @param refresh true 下拉刷新,false 上啦加载
     */
    void onRefresh(boolean refresh);
}
