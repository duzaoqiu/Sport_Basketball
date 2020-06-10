package com.bench.android.core.app.activity.error;

import android.app.Activity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lingjiu on 2019/4/3.
 */
public interface IError {

    /**
     * 处理error事件
     *
     * @param activity
     * @param jo
     * @param showTip
     * @throws JSONException
     */
    void operateErrorResponseMessage(Activity activity, JSONObject jo, boolean showTip) throws JSONException;

    void onDetach();
}
