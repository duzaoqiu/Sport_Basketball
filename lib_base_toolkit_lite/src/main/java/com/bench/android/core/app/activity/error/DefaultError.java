package com.bench.android.core.app.activity.error;

import android.app.Activity;
import android.app.Dialog;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.bench.android.core.app.dialog.DialogHelper;
import com.bench.android.core.app.toast.ToastUtils;
import com.bench.android.core.util.bean.ErrorBean;
import com.bench.android.core.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lingjiu on 2019/4/3.
 */
public class DefaultError implements IError {
    protected Dialog dialog;


    /**
     * 处理error事件
     *
     * @param activity
     * @param jo
     * @param showTip
     * @throws JSONException
     */
    @Override
    public void operateErrorResponseMessage(Activity activity, JSONObject jo, boolean showTip) throws JSONException {
        if (jo == null) {
            return;
        }
        if (isActivityFinished(activity)) {
            return;
        }
        ErrorBean bean = new ErrorBean(jo);
        if (!TextUtils.isEmpty(bean.detailMessage)) {
            ToastUtils.showTip(activity, bean.detailMessage);
            return;
        }
        if (bean.getError() == null) {
            return;
        }
        String name = bean.getError().getName();
        String tip = bean.getError().getMessage();
        if (operateCustomError(activity, name, tip)) {
            return;
        }
        if ("USER_LOGIN_EXPIRED".equals(name) || "LOGIN_ELSEWHERE".equals(name)) {
            showReLoginDialog(activity);
        } else if (bean.jumpLogin) {
            toLogin(activity);
            //LibAppUtil.toHalfOpenLogin();//服务端返回需要登录的时候
            //用户不存在或已离职
        } else if ("USER_NOT_EXISTS_OR_DISMISS".equals(name)) {
            toLogin(activity);
            //LibAppUtil.toHalfOpenLogin();//服务端返回需要登录的时候
        }
        else if ("POST_FORBIDDEN".equals(name)) {
            //禁言
            ShowGagDialog(activity, jo);
        } else if ("USER_ID_REQUIRED".equals(name)) {
            ToastUtils.showTip(activity, tip);
        } else if ("LOGIN_FORBIDDEN".equals(name)) {
            //在竞彩之家内账号禁止登录
            ToastUtils.showTip(activity, "该账户已禁止登录~");
        } else if ("ROLE_LOGIN_FORBID".equals(name)) {
            //在竞彩之家内角色禁止登录
            ToastUtils.showTip(activity, "该账户已禁止登录~");
        } else if ("AVAIABLE_AMOUNT_NOT_ENOUGH".equals(name)) {
            //余额不足
        } else if ("IDENTIFY_INFO_IS_NOT_EXITS".equals(name)) {
            //未实名
        } else {
            if (showTip) {
                if (Build.VERSION.SDK_INT >= 28) {//android 9.0
                    Toast.makeText(activity.getApplicationContext(), tip, Toast.LENGTH_SHORT).show();
                } else {
                    ToastUtils.showTip(activity, tip);
                }
            }
        }

    }

    private void ShowGagDialog(Activity activity, JSONObject jo) {
        try {
            if (dialog == null || !dialog.isShowing()) {
                String gmtExpired = null;
                String forbiddenReason = null;
                if (jo.has("gmtExpired")) {
                    gmtExpired = jo.getString("gmtExpired");
                }
                if (jo.has("forbiddenReason")) {
                    forbiddenReason = jo.getString("forbiddenReason");
                }
                dialog = DialogHelper.showMessageDialog(activity, null,
                        "您已被禁言" + "\n" + "禁言原因:" + forbiddenReason + "\n" + "解封时间:" + gmtExpired);
            }
            //ShowMessageDialog dialog = new ShowMessageDialog(BaseActivity.this, R.drawable.icon_dialog_title_warning,)
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void showReLoginDialog(final Activity activity) {
        showReLoginDialogWithMsg(activity, "该账号已在另一处登录,需要重新登录!");
    }

    protected void showReLoginDialogWithMsg(final Activity activity, String message) {
        try {
            if (dialog == null || !dialog.isShowing())
                DialogHelper.showSimpleMessageDialog(activity, "确定", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        toLogin(activity);
                    }
                }, message).show();
        } catch (Exception e) {
            if (e instanceof WindowManager.BadTokenException) {
                LogUtils.e("Error", e.getMessage());
            }
        }
    }

    /**
     * 当前activity是否处于销毁状态
     *
     * @param activity
     * @return
     */
    private boolean isActivityFinished(Activity activity) {
        if (Build.VERSION.SDK_INT >= 17)
            return activity.isFinishing() || activity.isDestroyed();
        return activity.isFinishing();
    }


    @Override
    public void onDetach() {
        if (dialog != null) {
            dialog.dismiss();
            dialog = null;
        }
    }

    /*****************************下面可复写处理Error**********************************************/

    /**
     * 处理不同app里面定制化错误信息
     * 子类可复写
     *
     * @param activity
     * @param name
     * @param tip
     * @return true-->消费此次error事件(一般是name判定成功)
     * false-->则会继续向下判断error事件
     */
    public boolean operateCustomError(Activity activity, String name, String tip) {
        return false;
    }


    /**
     * 登录逻辑处理
     *
     * @param activity
     */
    public void toLogin(Activity activity) {

    }


}
