package com.bench.android.core.app.dialog.adapter;

import android.content.Context;

import com.android.library.R;
import com.bench.android.core.app.dialog.holder.ViewHolder;
import com.bench.android.core.view.listview.ListBaseAdapter;

import java.util.List;
import java.util.Map;

/**
 * Created by lingjiu on 2019/2/19.
 */
public class PopupDialogAdapter extends ListBaseAdapter<Map<Integer, Object>> {

    private static int TEXT_TYPE = 0;
    public static int IMG_TYPE = 1;

    /**
     * @param context 上下文
     * @param data    数据源
     */
    public PopupDialogAdapter(Context context, List data) {
        super(context, data, R.layout.simple_menu_list_item);
    }

    @Override
    public void convert(ViewHolder holder, Map<Integer, Object> item, int position) {
        Object text = item.get(TEXT_TYPE);
        Object image = item.get(IMG_TYPE);
        if (text != null && text instanceof CharSequence) {
            holder.setText(R.id.textView, (CharSequence) text);
        }
        if (image != null && image instanceof Integer) {
            holder.setImageResource(R.id.textView, (Integer) image);
        }
    }
}
