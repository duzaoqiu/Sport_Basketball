package org.opencv.samples;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.bench.android.core.app.application.BaseApplication;
import com.bench.android.core.content.sharePreference.SharedPreferUtil;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.http.interceptor.HttpParamsInterceptor;
import com.bench.android.core.net.http.processor.HttpUtil;
import com.bench.android.core.net.http.processor.OkHttpHelper;

import org.opencv.samples.facedetect.activity.login.LoginHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MyApplication extends Application {


    public static boolean sInit = false;

    public static Application instance = null;

//    static {
//        System.loadLibrary("detection_based_tracker");
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        BaseApplication.onCreate(this);
      //  BasketBallAreaUtils.init(getApplicationContext());
        SharedPreferUtil.init(this,null);
        ARouter.init(this);
        OkHttpHelper httpProcessor = new OkHttpHelper();
        httpProcessor.addInterceptors(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request.Builder mRequest = chain.request().newBuilder();
                if (LoginHelper.getInstance().loginInfo != null) {
                    mRequest.addHeader("token", LoginHelper.getInstance().loginInfo.data.token);
                }
                return chain.proceed(mRequest.build());
            }
        });

        HttpUtil.getDefault().init(httpProcessor);

        HttpUtil.getDefault().addHttpInterceptors(new HttpParamsInterceptor() {
            @Override
            public RequestContainer intercept(RequestContainer map) {
                map.setRequestUrl("http://183.136.222.58:18080/basketball-app" + map.getRequestUrl());
                return map;
            }
        });

       // CommonUtils.getOutputMediaFile(CommonUtils.MEDIA_TYPE_VIDEO);

        //new AliCloudUploadHelper().uploadHttp();


    }
}
