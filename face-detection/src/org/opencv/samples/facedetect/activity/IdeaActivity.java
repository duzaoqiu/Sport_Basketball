package org.opencv.samples.facedetect.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.bench.android.core.app.toast.ToastUtils;
import com.bench.android.core.net.http.IHttpResponseCallBack;
import com.bench.android.core.net.http.RequestFormBody;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.RequestContainer;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.samples.facedetect.R;
import org.opencv.samples.facedetect.bean.StarInfoBean;
import org.opencv.samples.facedetect.utils.UrlEnum;

public class IdeaActivity extends AppCompatActivity implements IHttpResponseCallBack {
    private WrapperHttpHelper mHttpRequestHelper = new WrapperHttpHelper(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_idea);
        EditText editText = findViewById(R.id.inputEt);
        findViewById(R.id.okTv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editText.getText())){
                    ToastUtils.show("请输入内容");
                    return;
                }

                ideaRequest(editText.getText().toString());
            }
        });
    }


    /**
     * 获取投篮总数汇总
     */
    private void ideaRequest(String content) {
        RequestFormBody body = new RequestFormBody(UrlEnum.IDEA);
        body.setRequestUrl("/feedback/add");
        body.put("title","用户意见反馈");
        body.put("content",content);
        body.setUsePost(true);
        body.setUseJsonRequest(true);
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
        ToastUtils.show("提交成功");
        finish();
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {

    }
}
