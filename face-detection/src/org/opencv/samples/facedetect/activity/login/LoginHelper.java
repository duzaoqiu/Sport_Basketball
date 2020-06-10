package org.opencv.samples.facedetect.activity.login;

import com.bench.android.core.content.sharePreference.SharedPreferUtil;
import com.google.gson.Gson;

public class LoginHelper {
    public LoginBean loginInfo;
    private static LoginHelper sInstance;
    private static Gson gson = new Gson();

    public static LoginHelper getInstance() {
        if (sInstance == null) {
            sInstance = new LoginHelper();
            String data = SharedPreferUtil.getInstance().getString("login_data", null);
            if (data != null) {
                sInstance.loginInfo = gson.fromJson(data, LoginBean.class);
            }
        }
        return sInstance;
    }

    public void save() {
        String s = gson.toJson(loginInfo);
        SharedPreferUtil.getInstance().putString("login_data", s);
    }
}
