package com.bench.android.core.arch.presenter;

import org.json.JSONObject;

public interface IBaseView {

    void onStartRequest();

    void showProgressDialog(String loadingTxt);

    void hideProgressDialog();

    void showErrorMsg(JSONObject jsonObject);

    void showToast(String message);
}
