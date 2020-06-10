package com.bench.android.core.view.emoji.gif;

import android.content.Context;
import android.content.res.TypedArray;

import com.android.library.R;
import com.bench.android.core.view.emoji.bean.EmojiBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by danao on 2019/3/27.
 */
public class GifUtils {

    public static List<List<EmojiBean>> getGifList(Context context) {
        List<List<EmojiBean>> list = new ArrayList<>();
        String[] label = null;
        int labeId = context.getResources().getIdentifier("gif_label", "array", context.getPackageName());
        if (labeId != 0) {
            label = context.getResources().getStringArray(labeId);
        }
        TypedArray array = null;
        int iconId = context.getResources().getIdentifier("gif_icon", "array", context.getPackageName());
        if (iconId != 0) {
            array = context.getResources().obtainTypedArray(iconId);
        }
        if (label == null || array == null) {
            return list;
        }
        if (array.length() != label.length) {
            throw new RuntimeException("数据错误");
        }

        int pageSize = array.length() % 12 == 0 ? (array.length() / 12) : (array.length() / 12 + 1);
        for (int i = 0; i < pageSize; i++) {
            List<EmojiBean> smallList = new ArrayList<>();
            for (int j = 0; j < 12; j++) {
                int position = i * 12 + j;
                if (position < array.length()) {
                    EmojiBean bean = new EmojiBean();
                    bean.setResId(array.getResourceId(position, 0));
                    bean.setName(label[position]);
                    bean.setUploadMessage("[" + label[position] + "]");
                    smallList.add(bean);
                }
            }
            list.add(smallList);
        }
        return list;
    }

    /**
     * 通过下标获取
     *
     * @param context
     * @param index   此处下标从1开始
     * @return
     */
    public static int getResIdByIndex(Context context, int index) {
        TypedArray array = context.getResources().obtainTypedArray(context.getResources().getIdentifier("gif_icon", "array", context.getPackageName()));
        return array.getResourceId(index - 1, 0);
    }
}
