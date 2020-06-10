package com.bench.android.core.app.activity;

import android.app.Activity;

import com.bench.android.core.app.activity.error.DefaultError;
import com.bench.android.core.app.activity.error.IError;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by lingjiu on 2019/4/3.
 */
public class ErrorUtils implements IError {
    private static ErrorUtils instance;
    private IError iError;

    private ErrorUtils() {
        //默认DefaultError
        iError = new DefaultError();
    }

    public static ErrorUtils getInstance() {
        if (instance == null) {
            instance = new ErrorUtils();
        }
        return instance;
    }

    /**
     * 设定不同的Error处理
     * 如不设定
     * 默认defaultError
     *
     * @param iError
     */
    public void init(IError iError) {
        this.iError = iError;
    }

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
        if (iError != null) {
            iError.operateErrorResponseMessage(activity, jo, showTip);
        }
    }

    @Override
    public void onDetach() {
        if (iError != null) {
            iError.onDetach();
        }
    }

}
