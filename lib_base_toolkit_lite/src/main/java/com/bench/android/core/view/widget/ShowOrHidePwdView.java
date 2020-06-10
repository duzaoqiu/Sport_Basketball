package com.bench.android.core.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.library.R;


/**
 * @author xingjiu
 * 带x号密码显隐输入框
 */
public class ShowOrHidePwdView extends RelativeLayout implements View.OnClickListener {

    private EditText etPwd;
    private ImageView ivClear;
    private ImageView ivShowPwd;
    private boolean isShowPwd;
    private String textHint;

    public ShowOrHidePwdView(Context context) {
        super(context);
        init();
    }

    public ShowOrHidePwdView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.ShowOrHidePwdView);
        textHint = typedArray.getString(R.styleable.ShowOrHidePwdView_text_hint);
        typedArray.recycle();
        init();
    }

    private void init() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_show_or_hide_pwd, this);
        etPwd = view.findViewById(R.id.et_pwd);
        ivClear = view.findViewById(R.id.iv_clear);
        ivShowPwd = view.findViewById(R.id.iv_show_pwd);
        etPwd.setHint(textHint);
        ivClear.setOnClickListener(this);
        ivShowPwd.setOnClickListener(this);
        etPwd.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ivShowPwd.setVisibility(View.VISIBLE);
                    if (etPwd.getText().toString().trim().length() > 0) {
                        ivClear.setVisibility(View.VISIBLE);
                    }
                } else {
                    ivClear.setVisibility(View.GONE);
                    ivShowPwd.setVisibility(View.GONE);
                }
            }
        });
        etPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            //当输入框有焦点并且有内容时，X才可见
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (etPwd.getText().toString().trim().length() > 0 && etPwd.hasFocus()) {
                    ivClear.setVisibility(View.VISIBLE);
                } else {
                    ivClear.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        etPwd.addTextChangedListener(textWatcher);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.iv_show_pwd) {
            if (isShowPwd) {
                etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                ivShowPwd.setImageResource(R.drawable.eye_unselected);
            } else {
                etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                ivShowPwd.setImageResource(R.drawable.eye_selected);
            }
            isShowPwd = !isShowPwd;
        } else if (v.getId() == R.id.iv_clear) {
            etPwd.setText("");
        }
    }

    public void setTypeface(Typeface typeface) {
        etPwd.setTypeface(typeface);
    }

    public String getText() {
        return etPwd.getText().toString();
    }

    public void setTextSize(int textSize) {
        etPwd.setTextSize(textSize);
    }
}
