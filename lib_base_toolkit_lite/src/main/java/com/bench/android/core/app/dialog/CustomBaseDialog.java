package com.bench.android.core.app.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.WindowManager;

import com.android.library.R;

/************************************************************************
 *@Project: ml
 *@Package_Name: com.bench.android.core.app.Dialog
 *@Descriptions:BaseDialog如果有底部导航栏 dialog弹出时 页面会抖动 这个不会
 *@Author: xingjiu
 *@Date: 2019/7/2 
 *************************************************************************/
public class CustomBaseDialog extends Dialog {


    private CustomBaseDialog(Context context) {
        super(context, R.style.dialog_base);
    }


    public static class Builder {
        private Context context;
        private int width;
        private int height;
        private int contentViewLayoutId;
        private DialogInterface.OnDismissListener onDismissListener;
        private float dimAmount = -1;
        private int gravity = -1;
        private boolean canceledOnTouchOutside = true;
        private int windowAnimations = -1;

        public Builder with(Context context) {
            this.context = context;
            return this;
        }

        public Builder width(int width) {
            this.width = width;
            return this;
        }

        public Builder height(int height) {
            this.height = height;
            return this;
        }

        public Builder dimAmount(float dimAmount) {
            this.dimAmount = dimAmount;
            return this;
        }

        public Builder gravity(int gravity) {
            this.gravity = gravity;
            return this;
        }

        public Builder contentViewLayoutId(int contentViewLayoutId) {
            this.contentViewLayoutId = contentViewLayoutId;
            return this;
        }

        public Builder windowAnimations(int windowAnimations) {
            this.windowAnimations = windowAnimations;
            return this;
        }

        public Builder canceledOnTouchOutside(boolean canceledOnTouchOutside) {
            this.canceledOnTouchOutside = canceledOnTouchOutside;
            return this;
        }

        public Builder onDismissListener(DialogInterface.OnDismissListener onDismissListener) {
            this.onDismissListener = onDismissListener;
            return this;
        }

        public CustomBaseDialog build() {
            CustomBaseDialog customBaseDialog = new CustomBaseDialog(context);
            customBaseDialog.setContentView(contentViewLayoutId);
            WindowManager.LayoutParams params = customBaseDialog.getWindow().getAttributes();
            params.width = width == 0 ? WindowManager.LayoutParams.WRAP_CONTENT : width;
            params.width = height == 0 ? WindowManager.LayoutParams.WRAP_CONTENT : height;
            if (onDismissListener != null) {
                customBaseDialog.setOnDismissListener(onDismissListener);
            }
            if (dimAmount != -1) {
                params.dimAmount = dimAmount;
            }
            if (gravity != -1) {
                params.gravity = gravity;
            }
            if (windowAnimations != -1) {
                params.windowAnimations = windowAnimations;
            }
            customBaseDialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
            return customBaseDialog;
        }
    }

}
