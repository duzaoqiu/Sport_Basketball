package org.opencv.samples.facedetect.activity.login;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.bench.android.core.app.activity.BaseActivity;
import com.bench.android.core.app.toast.ToastUtils;
import com.bench.android.core.view.widget.TimerCountButton;

import org.jcodec.common.StringUtils;
import org.opencv.samples.facedetect.R;
import org.opencv.samples.facedetect.activity.MainActivity;


public class LoginActivity extends BaseActivity implements TextWatcher {

    private TimerCountButton btnRegisterVerify;
    private EditText etMobile, etVerify;
    //EditText et_login_name;
    private Button btnRegister;
    private boolean isGetVerify = false;
    private String mSmsId;
    private LoginModel mModel;
    private String mMobile;
//="13646836545"
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        if (LoginHelper.getInstance().loginInfo != null) {
//            MainActivity.startMainActivity(this);
//            finish();
//            return;
//        }
        setContentView(R.layout.login_layout);
        initViewModel();
        initView();
        initEvent();

        btnRegister.setEnabled(true);
    }

    private void initViewModel() {
        mModel = ViewModelProviders.of(this).get(LoginModel.class);
        setHttpErrorAndHttpStatusObservers(this, mModel);
        mModel.getVerifyNumLiveData.observe(this, new Observer<GetVerifyNumBean>() {
            @Override
            public void onChanged(GetVerifyNumBean loginBean) {
                btnRegisterVerify.setTimer(loginBean.getWaitNextPrepareSeconds());
                btnRegisterVerify.startTimeCount();
                etVerify.requestFocus();
                ToastUtils.show("验证码已发送，请注意查收");
                isGetVerify = true;
            }
        });

        mModel.loginLiveData.observe(this, new Observer<LoginBean>() {
            @Override
            public void onChanged(LoginBean loginBean) {
                LoginHelper.getInstance().loginInfo = loginBean;
                LoginHelper.getInstance().save();
                loginSuccess();
            }
        });
    }

    protected void initView() {
        etMobile = findViewById(R.id.et_login_mobile);
        etVerify = findViewById(R.id.et_login_verify);
        btnRegisterVerify = findViewById(R.id.btn_login_verify);
        btnRegister = findViewById(R.id.btn_login);
        etMobile.setText(mMobile);
        if (TextUtils.isEmpty(etMobile.getText().toString())) {
            etMobile.setTextSize(13);
        } else {
            etMobile.setTextSize(24);
        }
    }

    protected void initEvent() {
        btnRegister.setOnClickListener(this);
        btnRegisterVerify.setOnClickListener(this);
        etMobile.addTextChangedListener(this);
        etVerify.addTextChangedListener(this);
    }

    public void loginSuccess() {
//       btnRegisterVerify.closeTimeCount();
//       btnRegisterVerify.reset();
        ToastUtils.show("登录成功");
        MainActivity.startMainActivity(this);
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_login) {
            String account = etMobile.getText().toString().trim();
            String verify = etVerify.getText().toString().trim();
            if (StringUtils.isEmpty(account)) {
                ToastUtils.showTip(this, "请输入手机号");
                return;
            }
            if (!isGetVerify) {
                ToastUtils.showTip(this, "请获取验证码");
                return;
            }
            if (StringUtils.isEmpty(verify)) {
                ToastUtils.showTip(this, "请输入验证码");
                return;
            }
            mModel.login(account, verify);
        } else if (v.getId() == R.id.btn_login_verify) {
            String account = etMobile.getText().toString().trim();
            if (StringUtils.isEmpty(account)) {
                ToastUtils.showTip(this, "请输入手机号");
                return;
            }
            mModel.getVerify(account);
        }
    }

    public static void start(Context context) {

    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (TextUtils.isEmpty(etMobile.getText().toString())
                || TextUtils.isEmpty(etVerify.getText().toString())) {
            btnRegister.setEnabled(false);
        } else {
            btnRegister.setEnabled(true);
        }
        if (TextUtils.isEmpty(etMobile.getText().toString())) {
            etMobile.setTextSize(13);
        } else {
            etMobile.setTextSize(24);
        }
        if (TextUtils.isEmpty(etVerify.getText().toString())) {
            etVerify.setTextSize(13);
        } else {
            etVerify.setTextSize(24);
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

}
