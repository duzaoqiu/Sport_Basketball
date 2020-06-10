package com.bench.android.core.text;

import android.text.InputFilter;
import android.text.Spanned;

import com.bench.android.core.util.PingYinUtil;
import com.bench.android.core.app.toast.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 常用输入框过滤器
 * EditText text=new EditText(context);
 * text.setFilters(new Filters[]{maxLengthFilter,unicodeEmojFilter});
 * @author zhouyi 2019/5/22
 */
public class InputFilterHelper {

    /**
     * emoji 表情校验器
     */
    private static Pattern mEmojiPattern = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    /**
     * 最大字符数过滤器
     * @param maxLength
     * @return
     */
    public static InputFilter maxLengthFilter(int maxLength) {
        return new InputFilter.LengthFilter(maxLength);
    }

    /**
     * 中文名字输入框过滤器,对于新疆的姓名里面包含.
     * @return
     */
    public static InputFilter chineseNameInputFilter(){
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                for (int i = start; i < end; i++) {
                    if (!PingYinUtil.isChinese(source.charAt(i)) && '·' != source.charAt(i)) {
                        return "";
                    }
                }
                return source;
            }
        };
    }

    /**
     * unicode表情过滤器，主要适用于输入法内的表情
     * @return
     */
    public static InputFilter unicodeEmojFilter() {
        return new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                Matcher result = mEmojiPattern.matcher(source);
                if (result.find()) {
                    ToastUtils.showTip("暂不支持表情输入");
                    return "";
                } else {
                    //不是emoji表情内容,交给系统自带的filter进行过滤
                    return source;
                }
            }
        };
    }
}
