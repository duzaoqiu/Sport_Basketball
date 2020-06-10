package com.bench.android.core.view.wrapper;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatEditText;

public class WrapEditText extends AppCompatEditText {
    public WrapEditText(Context context) {
        super(context);
        init();
    }

    public WrapEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WrapEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (!AutoClickUtils.mAutoClickEnable) {
            return;
        }
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ViewWrapper viewWrapper = new ViewWrapper(WrapEditText.this.getId());
                String name = WrapEditText.this.getContext().getClass().getName();
                viewWrapper.setContextName(name);

                if (!ClickManager.list.contains(viewWrapper)) {
                    ClickManager.list.add(viewWrapper);
                    viewWrapper.setData(s.toString());
                } else {
                    for (ViewWrapper v : ClickManager.list) {
                        if (v.getViewId() == WrapEditText.this.getId() && v.getContextName().equals(name)) {
                            v.setData(s.toString());
                        }
                    }
                }
            }
        });
    }
}
