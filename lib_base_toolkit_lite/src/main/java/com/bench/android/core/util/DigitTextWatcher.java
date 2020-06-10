package com.bench.android.core.util;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * 用于设置EditText不让其输入超过小数点后面XX位的数字
 * 比如设置mDigits=2，则只能输入x.xx 不能输入x.xxx
 * Created by Fanlin on 2019/3/29.
 */
public class DigitTextWatcher implements TextWatcher {
    private EditText mEditText;
    private int mDigits;

    public DigitTextWatcher(EditText editText) {
        this(editText, 2);
    }

    public DigitTextWatcher(EditText editText, int digits) {
        mEditText = editText;
        mDigits = digits;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String input = s.toString().trim();
        String originalInput = input;
        String output = input;
        //如果"."在起始位置,则起始位置自动补0
        if (input.startsWith(".")) {
            output = input = "0" + input;
        }

        //舍弃超过位数限制的部分
        if (input.contains(".")) {
            if (input.length() - 1 - input.indexOf(".") > mDigits) {
                output = input = input.substring(0, input.indexOf(".") + mDigits + 1);
            }
        }

        //如果起始位置为"00xxx.xxx",截取非0部分
        if (input.startsWith("0") && input.length() > 1 && input.charAt(1) != '.') {
            for (int i = 0; i < input.length(); i++) {
                if (input.charAt(i) == '.') {
                    //考虑 00.xxx   i 肯定 > 0
                    output = input.substring(i - 1);
                    break;
                }
                if (input.charAt(i) != '0' || i == input.length() - 1) {
                    // x.xxx 或 000
                    output = input.substring(i);
                    break;
                }
            }
        }
        if (!TextUtils.equals(originalInput, output)) {
            mEditText.removeTextChangedListener(this);
            mEditText.setText(output);
            mEditText.setSelection(mEditText.length());
            mEditText.addTextChangedListener(this);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
