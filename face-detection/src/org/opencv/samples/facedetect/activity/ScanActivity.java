package org.opencv.samples.facedetect.activity;

import android.graphics.Bitmap;
import android.os.Bundle;

import com.bench.android.core.app.toast.ToastUtils;
import com.bench.android.core.net.http.IHttpResponseCallBack;
import com.bench.android.core.net.http.IHttpResponseListener;
import com.bench.android.core.net.http.RequestFormBody;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.OnHttpResponseListener;
import com.bench.android.core.net.http.base.RequestContainer;
import com.google.zxing.Result;
import com.google.zxing.client.android.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.samples.facedetect.LogUtils;
import org.opencv.samples.facedetect.R;
import org.opencv.samples.facedetect.bean.DynamicBean;
import org.opencv.samples.facedetect.utils.UrlEnum;
import org.opencv.samples.model.BaseResp;

public class ScanActivity extends CaptureActivity implements IHttpResponseCallBack {
    private WrapperHttpHelper mHttpRequestHelper = new WrapperHttpHelper(this,"ta");

    @Override
    protected void onCreateView(Bundle bundle) {
    }

    @Override
    protected void onScannerSuccess(Result rawResult, Bitmap barcode) {
        LogUtils.e("kkkkkkkkkkkkkkkkkkkkkkkk"+rawResult);

//        6061680407697765401
        scan(rawResult.getText());
    }

    private void scan(String stadiumId) {
        RequestFormBody body = new RequestFormBody(UrlEnum.SCAN);
        body.put("stadiumId", stadiumId);
        body.setUseJsonRequest(true);
        body.setRequestUrl("/joinRecord/add");
        mHttpRequestHelper.startRequest(body);
    }






    @Override
    public void onStartRequest() {

    }

    @Override
    public void onFinishRequest(int stateCode) {

    }

    @Override
    public void onSuccess(RequestContainer request, Object o) throws JSONException {
        LogUtils.e(o.toString());
        ToastUtils.show("扫码成功");
//        {"status":true,"msg":"","code":200,"data":{"stadiumName":"杭行荟"}}
        finish();
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        LogUtils.e(jsonObject.toString());
    }
}
