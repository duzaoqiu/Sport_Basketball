package com.bench.android.core.util;

import android.content.Context;
import android.text.TextUtils;

import com.bench.android.core.app.activity.BaseActivity;
import com.bench.android.core.app.toast.ToastUtils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 电话号码，密码，身份证，昵称等业务判断
 */
public final class ValidateUtil {

    public static boolean validatePassword(Context context, String password) {
        if (StringUtils.isBlank(password)) {
            ToastUtils.showTip(context, "请输入您的密码");
            return false;
        }
        if (password.length() < 6) {
            ToastUtils.showTip(context, "密码最少为6个字符");
            return false;
        }
        Pattern pattern = Pattern.compile("^[a-z|A-Z|0-9|!@#$%^&*()]{6,20}$");
        Matcher matcher = pattern.matcher(password);
        if (!matcher.find()) {
            ToastUtils.showTip(context, "密码只能包括字母、数字和特殊字符");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 新设置的密码长度8-20位，须包含数字、字母、符号至少2种或以上元素
     *
     * @param context
     * @param password
     * @return
     */
    public static boolean validatePasswordNew(Context context, String password) {
        if (StringUtils.isBlank(password)) {
            ToastUtils.showTip(context, "请输入您的密码");
            return false;
        }
        if (password.length() < 8||password.length() > 20) {
            ToastUtils.showTip(context, "请输入8-20位密码");
            return false;
        }
        Pattern pattern = Pattern.compile("^[a-z|A-Z|0-9|!@#$%^&*()]{8,20}$");
        Matcher matcher = pattern.matcher(password);
        Pattern patternS = Pattern.compile("^(?![0-9]+$)(?![a-zA-Z]+$)(?![@#$%^&*()]+$)");
        Matcher matcherS = patternS.matcher(password);
        if (!matcher.find()) {
            ToastUtils.showTip(context, "请输入正确的密码");
            return false;
        }else if(!matcherS.find()){
            ToastUtils.showTip(context, "请输入正确的密码");
            return false;
        } else {
            return true;
        }
    }

    public static boolean validateVerifyNum(Context context, String verifyNum) {
        if (TextUtils.isEmpty(verifyNum)) {
            ToastUtils.showTip(context, "请输入验证码");
            return false;
        }
        return true;
    }

    public static boolean validateMobile(String mobileNo) {
        return validateMobile(null, mobileNo);
    }

    public static boolean validateMobile(Context context, String mobileNo) {
        if (TextUtils.isEmpty(mobileNo)) {
            if (context != null) {
                ToastUtils.showTip(context, "请输入手机号");
            }
            return false;
        }
        String regEx = "^[1][0-9]{10}$";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(mobileNo);
        boolean rs = mat.find();
        if (!rs) {
            if (context != null) {
                ToastUtils.showTip(context, "请输入正确的手机号");
            }
            return false;
        }
        return true;
    }

    /**
     * 验证身份证号码
     */
    public static boolean validateIDCard(Context context, String cardNo) {
        if (TextUtils.isEmpty(cardNo)) {
            ToastUtils.showTip(context, "请输入你的身份证号码");
            return false;
        }
        String regEx = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(cardNo);
        boolean rs = mat.find();
        if (!rs) {
            ToastUtils.showTip(context, "请输入有效的身份证号码");
            return false;
        }
        return true;
    }

    /**
     * 验证昵称
     */
    public static boolean validateNickname(Context context, String nickName) {
        if (nickName.length() < 2) {
            ToastUtils.showTip(context, "请输入2-12位的昵称");
            return false;
        }
        String regEx = "^[a-z|A-Z|0-9|\\u4e00-\\u9fa5_]{2,12}$";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(nickName);
        if (!mat.matches()) {
            ToastUtils.showTip(context, "用户名只能由字母、数字、下划线和汉字组成，区分大小写");
            return false;
        }
        return true;
    }

    /**
     * 验证真实姓名
     */
    public static boolean validateIdentityName(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showTip(context, "姓名不能为空");
            return false;
        }
        String regEx = "^([\\u4e00-\\u9fa5]+[．.·]{0,1}[\\u4e00-\\u9fa5]+)+$";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(name);
        boolean rs = mat.find();
        if (!rs) {
            ToastUtils.showTip(context, "请输入有效姓名");
            return false;
        }
        return true;
    }

    /**
     * 是否是正确的金额格式
     *
     * @param value
     * @return
     */
    public static boolean isMoney(String value) {
        String regEx = "^[0-9]+(.[0-9]{0,2})?$";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(value);
        return mat.find();
    }

    public static boolean validatePayPassword(Context context, String password) {
        if (StringUtils.isBlank(password)) {
            ToastUtils.showTip(context, "请输入支付密码");
            return false;
        }
        if (password.length() < 8) {
            ToastUtils.showTip(context, "支付密码至少8位");
            return false;
        }
        if (password.length() > 20) {
            ToastUtils.showTip(context, "支付密码最多20位");
            return false;
        }
        String regEx = "^[0-9a-zA-Z!@#$%^&*()]{8,20}$";
        Pattern pat = Pattern.compile(regEx);
        Matcher mat = pat.matcher(password);
        boolean rs = mat.find();
        if (!rs) {
            ToastUtils.showTip(context, "密码只支持字母、数字和特殊字符（仅限!@#$%^&*()）");
            return false;
        }
        return true;
    }

    /**
     * @param context
     * @param password
     * @param password2
     * @return
     */
    public static boolean validateConfirmPassword(BaseActivity context, String password, String password2) {
        if (StringUtils.isBlank(password)) {
            context.showToast("请输入密码");
            return false;
        }
        if (StringUtils.isBlank(password2)) {
            context.showToast("请输入确认密码");
            return false;
        }
        if (!password.equals(password2)) {
            context.showToast("密码和确认密码不一致");
            return false;
        }
        return true;
    }

    public static boolean validateEmail(String email) {

        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
