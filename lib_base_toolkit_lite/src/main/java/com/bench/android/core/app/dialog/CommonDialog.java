package com.bench.android.core.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import com.android.library.R;
import com.bench.android.core.app.dialog.holder.ViewHolder;
import com.bench.android.core.app.dialog.holder.ViewHolders;
import com.bench.android.core.util.dimension.PxConverter;

/************************************************************************
 *@Project: core
 *@Package_Name: com.bench.android.core.app.dialog
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/8/23 
 *************************************************************************/
public class CommonDialog extends Dialog {

    /**
     * 将控件封装在ViewHolder里面
     */
    private ViewHolder holder;
    private final DialogHelper helper;


    public CommonDialog(@NonNull Context context, DialogHelper helper) {
        super(context, R.style.dialog_base);
        this.helper = helper;
        initView();
        initParams();
    }


    private void initView() {
        holder = ViewHolders.getHolder(getContext(), helper);
        setContentView(holder.getConvertView());
    }

    private void initParams() {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams lp = window.getAttributes();

        //设置全屏
        {

            window.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            // 设置宽度
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            window.setAttributes(layoutParams);
        }

        //调节灰色背景透明度[0-1]，默认0.3f
        lp.dimAmount = helper.dimAmount;

//        设置dialog宽高
        lp.width = helper.width < 0 ? helper.width : PxConverter.dp2px(helper.width);
        lp.width = helper.height < 0 ? helper.width : PxConverter.dp2px(helper.height);
        lp.gravity = helper.gravity;

        if (helper.x != 0) {
            lp.x = helper.x;
            lp.gravity = lp.gravity | Gravity.START;
        }
        if (helper.y != 0) {
            lp.y = helper.y;
            lp.gravity = lp.gravity | Gravity.TOP;
        }

        if (helper.onDismissListener != null) {
            setOnDismissListener(helper.onDismissListener);
        }

        //设置dialog进入、退出的动画
        if (helper.windowAnimations != -1) {
            window.setWindowAnimations(helper.windowAnimations);
        }

        //如果是底部显示的，可能builder模式构建的时候没有设置，特殊设置一下动画和位置
        if (helper.options != null && helper.options instanceof DialogHelper.BottomDialogOptions) {
            if (helper.windowAnimations == -1) {
                window.setWindowAnimations(R.style.dialog_anim_bottom);
            }
            lp.gravity = Gravity.BOTTOM;
        }

        window.setAttributes(lp);

        if(helper.cancelable){
            setCancelable(true);
        }else {
            setCanceledOnTouchOutside(helper.canceledOnTouchOutside);
        }
        holder.init(this, helper);

    }


    public void updateY(int y) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.y = y;
        onWindowAttributesChanged(lp);
    }

    public void updateX(int x) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.x = x;
        onWindowAttributesChanged(lp);
    }

}
