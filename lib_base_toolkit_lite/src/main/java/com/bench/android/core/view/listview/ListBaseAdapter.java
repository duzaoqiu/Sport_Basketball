package com.bench.android.core.view.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;

import com.bench.android.core.app.dialog.holder.ViewHolder;

import java.util.List;

/**
 * Created by lingjiu on 2019/2/19.
 */
public abstract class ListBaseAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected List<T> data;
    protected final int layoutId;
    private AbsListView listView;

    /**
     * @param context  上下文
     * @param data     数据源
     * @param layoutId 布局id
     */
    public ListBaseAdapter(Context context, List<T> data, int layoutId) {
        this.layoutId = layoutId;
        this.mContext = context;
        this.data = data;
    }

    /**
     * @param list 添加新的数据源，刷新
     */
    public void addView(List<T> list) {
        if (this.data != null && list != null) {
            this.data.addAll(list);
            this.notifyDataSetChanged();
        }
    }

    /**
     * @param data 填充新的数据刷新
     */
    public void updateView(List<T> data) {
        if (this.data != null) {
            this.data.clear();
            this.data.addAll(data);
            notifyDataSetChanged();
        }
    }

    /**
     * 刷新指定单行数据
     *
     * @param position 位置
     * @param t        数据源
     */
    public void updateView(int position, T t) {
        if (t != null && listView != null) {
            //更新数据
            this.data.set(position, t);
            if (position >= listView.getFirstVisiblePosition() &&
                    position <= listView.getLastVisiblePosition()) {
                getView(position,
                        listView.getChildAt(position - listView.getFirstVisiblePosition()), listView);
            }
        }
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public T getItem(int position) {
        return data == null ? null : data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (listView == null) {
            listView = (AbsListView) parent;
        }
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            holder = ViewHolder.create(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        convert(holder, getItem(position), position);
        return convertView;
    }

    public AbsListView getAbsListView() {
        return listView;
    }

    public abstract void convert(ViewHolder holder, T item, int position);

}
