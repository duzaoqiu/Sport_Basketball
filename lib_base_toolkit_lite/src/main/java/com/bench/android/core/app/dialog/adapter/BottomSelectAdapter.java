package com.bench.android.core.app.dialog.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.android.library.R;


/**
 * 为了解决底部弹窗偶尔会出现文本颜色变为白色的问题
 *
 * @author :  malong    luomingbear@163.com
 * @date :  2019/6/26
 **/
public class BottomSelectAdapter<T> extends ArrayAdapter<T> {
    public BottomSelectAdapter(Context context, T[] objects) {
        super(context, android.R.layout.simple_list_item_1, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(android.R.layout.simple_list_item_1, null);
        TextView textView = view.findViewById(android.R.id.text1);
        textView.setGravity(Gravity.CENTER);
        textView.setTextColor(parent.getResources().getColor(R.color.text_color_black));
        final T item = getItem(position);
        if (item instanceof CharSequence) {
            textView.setText((CharSequence) item);
        } else {
            textView.setText(item.toString());
        }
        return view;
    }
}
