package com.bench.android.core.view.emoji.utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;

import com.android.library.R;
import com.bench.android.core.view.emoji.bean.EmojiBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhouyi on 2016/1/6 12:11.
 */
public class EmojUtils {

    public static List<List<EmojiBean>> getEmojiList(Context context) {
        List<List<EmojiBean>> multipleList = new ArrayList<>();
        String[] emojiNames = null;
        int emojiId = context.getResources().getIdentifier("emoji_name", "array", context.getPackageName());
        if (emojiId != 0) {
            emojiNames = context.getResources().getStringArray(emojiId);
        }
        int iconId = context.getResources().getIdentifier("emoji_icon", "array", context.getPackageName());
        TypedArray typedArray = null;
        if (iconId != 0) {
            typedArray = context.getResources().obtainTypedArray(iconId);
        }
        if (emojiNames == null || typedArray == null) {
            return multipleList;
        }
        if (typedArray.length() != emojiNames.length) {
            throw new RuntimeException("数据错误");
        }

        List<EmojiBean> emojiList = new ArrayList<>();
        for (int i = 0; i < emojiNames.length; i++) {
            EmojiBean emojiBean = new EmojiBean();
            emojiBean.setResId(typedArray.getResourceId(i, 0));
            emojiBean.setDrawableName(emojiNames[i]);
            emojiBean.setUploadMessage(getReplaceMessage(emojiNames[i]));
            emojiList.add(emojiBean);
        }

        int pageCount = emojiList.size() / 20 + (emojiList.size() % 20 == 0 ? 0 : 1);
        for (int i = 0; i < pageCount; i++) {
            List<EmojiBean> tempList = new ArrayList<>();

            for (int j = i * 20; j < (i != pageCount - 1 ? 20 * (i + 1) : emojiList.size()); j++) {
                tempList.add(emojiList.get(j));
            }
            multipleList.add(tempList);
        }
        typedArray.recycle();
        return multipleList;
    }

    private static String getReplaceMessage(String message, String name) {

        String str = "<emotion image_name=\"[NAME]\" image_alt=\"[MESSAGE]\">[MESSAGE]</emotion>";

        return str.replace("[NAME]", name).replace("[MESSAGE]", message);

    }

    private static String getReplaceMessage(String name) {
        return "[" + name + "]";
    }

    public static int findResIdByName(Context context, String emojiName) {
        String[] emojiNames = context.getResources().getStringArray(context.getResources().getIdentifier("emoji_name", "array", context.getPackageName()));
        TypedArray typedArray = context.getResources().obtainTypedArray(context.getResources().getIdentifier("emoji_icon", "array", context.getPackageName()));
        if (typedArray.length() != emojiNames.length) {
            return 0;
        }
        for (int i = 0; i < emojiNames.length; i++) {
            if (TextUtils.equals(emojiNames[i], emojiName)) {
                int resourceId = typedArray.getResourceId(i, 0);
                typedArray.recycle();
                return resourceId;
            }
        }
        return 0;
    }
}
