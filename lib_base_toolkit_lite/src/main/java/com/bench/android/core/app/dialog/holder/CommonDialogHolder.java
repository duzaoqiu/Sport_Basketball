package com.bench.android.core.app.dialog.holder;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import com.android.library.R;
import com.bench.android.core.app.dialog.DialogHelper;

/************************************************************************
 *@Project: core
 *@Package_Name: com.bench.android.core.app.dialog
 *@Descriptions: 通用的一种dialog的holder
 *@Author: xingjiu
 *@Date: 2019/8/20 
 *************************************************************************/
public class CommonDialogHolder extends ViewHolder {

    protected CommonDialogHolder(Context context) {
        super(context, R.layout.dialog_layout);
    }

    @Override
    public void init(final Dialog dialog, DialogHelper helper) {
        final DialogHelper.CommonDialogOptions options = (DialogHelper.CommonDialogOptions) helper.options;
        setOnClickListener(R.id.okBtn, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (options.onPositiveListener != null) {
                    options.onPositiveListener.onClick(v);
                }
            }
        });
        if (options.singleButton) {
            setViewVisibility(R.id.cancelBtn, View.GONE);
        } else {
            if (!TextUtils.isEmpty(options.negativeMsg)) {
                setText(R.id.cancelBtn, options.negativeMsg);
            }
            setOnClickListener(R.id.cancelBtn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    if(options.onNegativeListener!=null){
                        options.onNegativeListener.onClick(v);
                    }
                }
            });
        }
        setText(R.id.messageTv, options.content);
        if (!TextUtils.isEmpty(options.positiveMsg)) {
            setText(R.id.okBtn, options.positiveMsg);
        }
        if(options.showTitle){
            setViewVisibility(R.id.titleTv,View.VISIBLE);
        }else{
            setViewVisibility(R.id.titleTv,View.GONE);
        }

        if (!TextUtils.isEmpty(options.title)) {
            setText(R.id.titleTv, options.title);
        }
        if (options.contentGravity > 0) {
            setTextGravity(R.id.messageTv, options.contentGravity);
        }
        if (options.titleGravity > 0) {
            setTextGravity(R.id.titleTv, options.titleGravity);
        }
    }

}
