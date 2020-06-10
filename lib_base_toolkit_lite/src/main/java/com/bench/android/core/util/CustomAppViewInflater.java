package com.bench.android.core.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatViewInflater;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;

import com.bench.android.core.view.wrapper.WrapButton;
import com.bench.android.core.view.wrapper.WrapConstraintLayout;
import com.bench.android.core.view.wrapper.WrapEditText;
import com.bench.android.core.view.wrapper.WrapImageView;
import com.bench.android.core.view.wrapper.WrapLinearLayout;
import com.bench.android.core.view.wrapper.WrapRadioButton;
import com.bench.android.core.view.wrapper.WrapRelativeLayout;
import com.bench.android.core.view.wrapper.WrapScrollView;
import com.bench.android.core.view.wrapper.WrapTextView;

/************************************************************************
 *@Project: core
 *@Package_Name: com.bench.android.core.test
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/9/5 
 *************************************************************************/
//@Keep
public class CustomAppViewInflater extends AppCompatViewInflater {
    @NonNull
    @Override
    protected AppCompatTextView createTextView(Context context, AttributeSet attrs) {
        return new WrapTextView(context, attrs);
    }

    @NonNull
    @Override
    protected AppCompatImageView createImageView(Context context, AttributeSet attrs) {
        return new WrapImageView(context, attrs);
    }

    @NonNull
    @Override
    protected AppCompatButton createButton(Context context, AttributeSet attrs) {
        return new WrapButton(context, attrs);
    }

    @NonNull
    @Override
    protected AppCompatRadioButton createRadioButton(Context context, AttributeSet attrs) {
        return new WrapRadioButton(context, attrs);
    }

    @NonNull
    @Override
    protected AppCompatEditText createEditText(Context context, AttributeSet attrs) {
        return new WrapEditText(context, attrs);
    }

    @Nullable
    @Override
    protected View createView(Context context, String name, AttributeSet attrs) {
        View view = null;

        // We need to 'inject' our tint aware Views in place of the standard framework versions
        switch (name) {
            case "ConstraintLayout":
                view = new WrapConstraintLayout(context, attrs);
                break;
            case "LinearLayout":
                view = new WrapLinearLayout(context, attrs);
                break;
            case "RelativeLayout":
                view = new WrapRelativeLayout(context, attrs);
                break;
            case "ScrollView":
                view = new WrapScrollView(context, attrs);
                break;
            default:
                break;
        }
        return view;
    }
}
