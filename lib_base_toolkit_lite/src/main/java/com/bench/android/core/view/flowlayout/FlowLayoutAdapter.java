package com.bench.android.core.view.flowlayout;

import android.view.View;

import com.bench.android.core.util.LogUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by zaozao on 2018/7/9.
 */
public abstract class FlowLayoutAdapter<T> {

    private List<T> mTagDatas;

    private OnDataChangeListener mOnDataChangedListener;
    @Deprecated
    private HashSet<Integer> mCheckedPosList = new HashSet<Integer>();

    public FlowLayoutAdapter(List<T> datas) {
        mTagDatas = datas;
    }

    @Deprecated
    public FlowLayoutAdapter(T[] datas) {
        mTagDatas = new ArrayList<T>(Arrays.asList(datas));
    }

    public void setOnDataChangedListener(OnDataChangeListener onDataChangedListener) {
        this.mOnDataChangedListener = onDataChangedListener;
    }

    @Deprecated
    public void setSelectedList(int... poses) {
        Set<Integer> set = new HashSet<>();
        for (int pos : poses) {
            set.add(pos);
        }
        setSelectedList(set);
    }

    @Deprecated
    public void setSelectedList(Set<Integer> set) {
        mCheckedPosList.clear();
        if (set != null) {
            mCheckedPosList.addAll(set);
        }
        notifyDataChanged();
    }

    @Deprecated
    HashSet<Integer> getPreCheckedList() {
        return mCheckedPosList;
    }


    public int getCount() {
        return mTagDatas == null ? 0 : mTagDatas.size();
    }

    public void notifyDataChanged(List<T> list) {
        this.mTagDatas = list;
        if (mOnDataChangedListener != null) {
            mOnDataChangedListener.onChanged();
        }
    }


    public void notifyDataChanged() {
        if (mOnDataChangedListener != null) {
            mOnDataChangedListener.onChanged();
        }
    }

    public T getItem(int position) {
        return position < mTagDatas.size() ? mTagDatas.get(position) : null;
    }

    public abstract View getView(TagFlowLayout parent, int position, T t);

    public void onSelected(int position, View view) {
        LogUtils.d("zhy", "onSelected " + position);
    }

    public void unSelected(int position, View view) {
        LogUtils.d("zhy", "unSelected " + position);
    }

    public boolean setSelected(int position, T t) {
        return false;
    }
}
