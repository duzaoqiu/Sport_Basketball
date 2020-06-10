package com.bench.android.core.app.dialog.holder;

import android.content.Context;

import com.bench.android.core.app.dialog.DialogHelper;

/************************************************************************
 *@Project: core
 *@Package_Name: com.bench.android.core.app.dialog
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/8/20 
 *************************************************************************/
public class ViewHolders {

    public static ViewHolder getHolder(Context context, DialogHelper helper) {
        ViewHolder holder = null;
        if (helper.holder != null) {
            holder = helper.holder;
        } else if (helper.options instanceof DialogHelper.CommonDialogOptions) {
            holder = new CommonDialogHolder(context);
        } else if (helper.options instanceof DialogHelper.BottomDialogOptions) {
            holder = new BottomDialogHolder(context);
        }
        return holder;
    }
}
