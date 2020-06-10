package com.bench.android.core.app.dialog;

import android.content.Context;

import androidx.annotation.LayoutRes;

import com.bench.android.core.app.dialog.holder.ViewHolder;


/**
 * 一个通用的dialog
 *
 * <p>
 * Created by lingjiu on 2018/2/2.
 *
 *          GeneralDialog.init(context)
 *                       .setLayoutId(resid)     //设置dialog布局文件
 *                       .setConvertListener(new ViewConvertListener() {
 *                       @Override
 *                       public void convertView(ViewHolder holder, BaseDialog dialog) {
 *                          //进行相关View操作的回调
 *                       }
 *                       })
 *                       .setDimAmount(0.3f)     //调节灰色背景透明度[0-1]，默认0.5f
 *                       .setShowBottom(true)     //是否在底部显示dialog，默认flase
 *                        .setMargin()     //dialog左右两边到屏幕边缘的距离（单位：dp），默认0dp
 *                        .setWidth()     //dialog宽度（单位：dp），默认为屏幕宽度，-1代表WRAP_CONTENT
 *                       .setHeight()     //dialog高度（单位：dp），默认为WRAP_CONTENT
 *                       .setOutCancel(false)     //点击dialog外是否可取消，默认true
 *                       .setAnimStyle()     //设置dialog进入、退出的动画style(底部显示的dialog有默认动画)
 *                        .show();     //显示dialog
 * </p>
 */

public class GeneralDialog extends BaseDialog {
    private ViewConvertListener convertListener;

    public GeneralDialog(Context context) {
        super(context);
    }

    public static GeneralDialog init(Context context) {
        return new GeneralDialog(context);
    }


    @Override
    public int intLayoutId() {
        return layoutId;
    }

    @Override
    public void convertView(ViewHolder holder, BaseDialog dialog) {
        if (convertListener != null) {
            convertListener.convertView(holder, dialog);
        }

    }

    public GeneralDialog setLayoutId(@LayoutRes int layoutId) {
        this.layoutId = layoutId;
        return this;
    }

    public GeneralDialog setConvertListener(ViewConvertListener convertListener) {
        this.convertListener = convertListener;
        return this;
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        convertListener = null;
    }
}
