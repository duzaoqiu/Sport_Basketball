package com.bench.android.core.util;

/**
 * 模仿kotlin的Pair,可以返回一对数据。
 *
 * @author zhouyi created 2019/06/03
 */
public class BasePair<T, M> {
    public T first;
    public M second;

    public BasePair(T first, M second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public M getSecond() {
        return second;
    }
}
