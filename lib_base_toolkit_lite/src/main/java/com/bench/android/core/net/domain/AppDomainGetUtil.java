package com.bench.android.core.net.domain;

import com.bench.android.core.content.sharePreference.SharedPreferUtil;
import com.bench.android.core.net.domain.base.AppDomainManager;
import com.bench.android.core.net.domain.base.GetDynamicUrlHelper;
import com.bench.android.core.util.LogUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author zaozao
 * @date 2019/3/9
 * 作用：
 */
public class AppDomainGetUtil {

    private boolean isRequesting = false;

    private static AppDomainGetUtil appDomainGetUtil;


    public static AppDomainGetUtil getInstance() {
        if (appDomainGetUtil == null) {
            appDomainGetUtil = new AppDomainGetUtil();
        }
        return appDomainGetUtil;
    }


    public void start(final GetDomainListener getDomainListener) {

        GetDynamicUrlHelper.RequestDoMainSuccessListener requestDoMainSuccessListener = new GetDynamicUrlHelper.RequestDoMainSuccessListener() {

            @Override
            public void requestDoMainSuccess(String response, boolean needUpdate, String domainKey) {
                if (needUpdate) {
                    //发送eventBus 通知更新
//                    EventBus.getDefault().post(new EventBusDominMessage(EventBusDominMessage.APP_DOMAIN_UPDATE));
                }
                isRequesting = false;
                if (getDomainListener != null) {
                    getDomainListener.getDomainResult(true, "");
                }
            }

            @Override
            public void onFailed(String errorMesssage) {
                isRequesting = false;
                if (getDomainListener != null) {
                    getDomainListener.getDomainResult(false, errorMesssage);
                }
            }
        };


        LogUtils.e("kkk", "AppDomainManager.step===" + AppDomainManager.step);

        /**线下或者线上，请求接口地址--------------------这一次分支也配了分支地址*/
//        if (AppDomainManager.step == 0 || AppDomainManager.step == 2) {
        if (isRequesting) {
            return;
        }
        new GetDynamicUrlHelper.Builder()
                .setFirstDomain(AppDomainManager.appDomainRequestArray[0])
                .setSecondDomain(AppDomainManager.appDomainRequestArray[1])
                .setFirstDomainRetryCount(3)
                .setMaxRetryCount(6)
                .setListener(requestDoMainSuccessListener)
                .build().startRequestUrl();
        isRequesting = true;
//        }//分支
//        else if (AppDomainManager.step == 1) {
//            writeBranchUrl();
//        }
    }


    //直接写死分支地址
    public void writeBranchUrl() {
        String url = "http://user1-mapi.caihongtest1.net/";
        try {
            AppDomainManager.host = new URL(url).getHost();
            AppDomainManager.getInstance().setApiDomain(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        AppDomainManager.getInstance().setApiDomain(url);
        SharedPreferUtil.getInstance().putString("APP_DOMAIN", AppDomainManager.getInstance().getApiDomain());
    }


    public interface GetDomainListener {
        void getDomainResult(boolean success, String errorMessage);
    }
}
