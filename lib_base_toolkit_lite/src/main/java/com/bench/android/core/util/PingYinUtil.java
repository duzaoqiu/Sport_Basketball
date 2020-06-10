package com.bench.android.core.util;


import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

import org.apache.commons.lang3.StringUtils;

/**
 常用的中文工具类，判断是否是中文，获取拼音首字母
 */
public class PingYinUtil {
    private static final String TAG = "HanziToPinyin";

    private static PingYinUtil sInstance;

    protected PingYinUtil() { }

    public static PingYinUtil getInstance() {
        synchronized (PingYinUtil.class) {

            if (sInstance != null) {
                return sInstance;
            }

            sInstance = new PingYinUtil();
            return sInstance;
        }
    }

    public String getPinyin(String input) {
        char[] charArray = input.trim().toCharArray();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < charArray.length; i++) {
            char firstChar = charArray[i];

            if (firstChar >= 65 && firstChar <= 90 || firstChar >= 97 && firstChar <= 122) {
                return String.valueOf(firstChar);
            }

            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

            // UPPERCASE：大写  (ZHONG)
            // LOWERCASE：小写  (zhong)
            format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

            // WITHOUT_TONE：无音标  (zhong)
            // WITH_TONE_NUMBER：1-4数字表示英标  (zhong4)
            // WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常）  (zhòng)
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

            // WITH_V：用v表示ü  (nv)
            // WITH_U_AND_COLON：用"u:"表示ü  (nu:)
            // WITH_U_UNICODE：直接用ü (nü)
            format.setVCharType(null);

            try {
                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(firstChar, format);
                if (pinyin == null) {
                    return "#";
                } else {
                    //将首字母大写
                    String first = pinyin[0].substring(0, 1).toUpperCase();
                    sb.append(first);
                    String left = pinyin[0].substring(1);
                    sb.append(left);
                }
            } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
                badHanyuPinyinOutputFormatCombination.printStackTrace();
            }
        }


        return sb.toString();
    }

    /**
     * 获取首字母
     * 例:侯大芮 ---> h
     *
     * @param input
     * @return
     */
    public String getLetter(String input) {
        if (StringUtils.isBlank(input)) {
            return "#";
        }

        char[] charArray = input.trim().toCharArray();

        char firstChar = charArray[0];

        if (firstChar >= 65 && firstChar <= 90 || firstChar >= 97 && firstChar <= 122) {
            return String.valueOf(firstChar);
        }

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

        // UPPERCASE：大写  (ZHONG)
        // LOWERCASE：小写  (zhong)
        format.setCaseType(HanyuPinyinCaseType.UPPERCASE);

        // WITHOUT_TONE：无音标  (zhong)
        // WITH_TONE_NUMBER：1-4数字表示英标  (zhong4)
        // WITH_TONE_MARK：直接用音标符（必须WITH_U_UNICODE否则异常）  (zhòng)
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        // WITH_V：用v表示ü  (nv)
        // WITH_U_AND_COLON：用"u:"表示ü  (nu:)
        // WITH_U_UNICODE：直接用ü (nü)
        format.setVCharType(null);

        try {
            String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(firstChar, format);
            if (pinyin == null) {
                return "#";
            } else {
                return pinyin[0].substring(0, 1);
            }
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }

        return "#";
    }

    // GENERAL_PUNCTUATION 判断中文的“号
    // CJK_SYMBOLS_AND_PUNCTUATION 判断中文的。号
    // HALFWIDTH_AND_FULLWIDTH_FORMS 判断中文的，号
    public static final boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS;
    }

    public static final boolean isChinese(String strName) {
        char[] ch = strName.toCharArray();
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否为中文
     *
     * @return true=中文
     */
    public boolean isChinaString(String input) {
        return isChinese(input);
    }
}