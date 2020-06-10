package com.bench.android.core.app.dialog;

import android.app.Dialog;
import android.content.Context;

import com.android.library.R;
import com.bench.android.core.app.dialog.holder.ViewHolder;


/************************************************************************
 *@Project: core
 *@Package_Name: com.bench.android.core.app.dialog
 *@Descriptions: 自定义dialogHolder模板
 *@Author: xingjiu
 *@Date: 2019/8/22 
 *************************************************************************/
public class CustomDialogHolder extends ViewHolder {

    public CustomDialogHolder(Context context) {
        super(context, R.layout.holder_custom);
    }

    public CustomDialogHolder(Context context,int redId) {
        super(context, redId);
    }

    @Override
    public void init(Dialog dialog, DialogHelper helper) {

    }
}
