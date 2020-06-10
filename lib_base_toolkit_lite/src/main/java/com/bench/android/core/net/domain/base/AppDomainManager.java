package com.bench.android.core.net.domain.base;

import android.content.Context;
import android.text.TextUtils;

import com.android.library.R;
import com.bench.android.core.app.toast.ToastUtils;
import com.bench.android.core.util.LogUtils;
import com.bench.android.core.net.http.params.AppParam;
import com.bench.android.core.net.http.processor.OkHttpHelper;
import com.bench.android.core.content.sharePreference.SharedPreferUtil;


/**
 * @author Created by zaozao on 2018/10/31.
 */
public class AppDomainManager {

    //是否使用多域名
    public static boolean userDynamicDomain = false;

    private static Context mContext;

    public static int step = 0;

    public static boolean Debug = true;

    //是否是纯净版
    public static boolean PURE = false;

    //主域名
    private String API_DOMAIN = "http://mapi.kkbuluo.net";// 默认给一个  ,省的网络请求demo报错

    public static String[] appDomainRequestArray;
    public static String[] ftDomainRequestArray;

    /**
     * 主机名，用于判断域名地址是否符合规范
     */
    public static String host;

    private static AppDomainManager appDomainManager;


    public static void init(Context context) {
        mContext = context;
        LogUtils.DEBUG = Debug;
        OkHttpHelper.DEBUG = Debug;
    }

    public static AppDomainManager getInstance() {
        if (appDomainManager == null) {
            appDomainManager = new AppDomainManager();
        }
        return appDomainManager;
    }

    public String getApiDomain() {
        if (TextUtils.isEmpty(API_DOMAIN)) {
            return SharedPreferUtil.getInstance().getString("APP_DOMAIN", null);
        }
        return API_DOMAIN;
    }


    public void setApiDomain(String apiDomain) {
        //如果是分支，就直接使用分支地址，不使用动态域名地址
        if (step == 3) {
            API_DOMAIN = mContext.getResources().getStringArray(R.array.appDomainBranchTest)[0];
        } else {
            API_DOMAIN = apiDomain;
        }
    }


    private static void getDebugLastUrl() {

        if (step == 2) {
            //线上地址
            appDomainRequestArray = mContext.getResources().getStringArray(R.array.appDomainRequestUrlOnLine);
            ftDomainRequestArray = mContext.getResources().getStringArray(R.array.ftDomainRequestUrlOnline);
        } else {
            //线下地址
            appDomainRequestArray = mContext.getResources().getStringArray(R.array.appDomainRequestUrlOffLine);
            ftDomainRequestArray = mContext.getResources().getStringArray(R.array.ftDomainRequestUrlOffLine);
        }
    }


    private static void setUrl() {
        String[] ftImageDomainArray = mContext.getResources().getStringArray(R.array.FT_API_IMAGE_DOMAIN_ON_LINE);
        String[] btImageDomainArray = mContext.getResources().getStringArray(R.array.BT_API_IMAGE_DOMAIN_ON_LINE);

        switch (step) {
            case 0: //线下
                AppParam.FT_API_DOMAIN = ftImageDomainArray[0];
                AppParam.FT_IMAGE_DOWNLOAD_URL = ftImageDomainArray[3];

                AppParam.BT_API_DOMAIN = btImageDomainArray[0];
                AppParam.BT_IMAGE_DOWNLOAD_URL = btImageDomainArray[3];
                break;

            case 1://分支
                AppParam.FT_API_DOMAIN = ftImageDomainArray[1];
                AppParam.FT_IMAGE_DOWNLOAD_URL = ftImageDomainArray[3];

                AppParam.BT_API_DOMAIN = btImageDomainArray[1];
                AppParam.BT_IMAGE_DOWNLOAD_URL = btImageDomainArray[3];
                break;

            case 2://线上发布地址
                AppParam.FT_API_DOMAIN = ftImageDomainArray[2];
                AppParam.FT_IMAGE_DOWNLOAD_URL = ftImageDomainArray[4];

                AppParam.BT_API_DOMAIN = btImageDomainArray[2];
                AppParam.BT_IMAGE_DOWNLOAD_URL = btImageDomainArray[4];
                break;

            default:
                break;
        }
    }

    public void initDomainUrl() {
        if (step == 0) {//DEBUG  情况下先取本地保存的，不然之前切换过得就没用
            step = SharedPreferUtil.getInstance().getInt("step", 0);
        }
        getDebugLastUrl();
        setUrl();
    }


    public void changeToOffline() {
//        if(true){  //测多域名，先不用切换
//            return;
//        }

        int localStep = SharedPreferUtil.getInstance().getInt("step", step);

        /**
         * 线下---》线上----》分支
         */
        localStep = localStep == 0 ? 2 : (localStep == 2 ? 1 : 0);

        changeDebugDomain(localStep,1);
    }

    public void changeDebugDomain(int localStep, int branchNum) {
        SharedPreferUtil.getInstance().putInt("step", localStep);

        SharedPreferUtil.getInstance().remove("APP_DOMAIN");

        if (localStep == 1) {
            API_DOMAIN = "http://mapi" + branchNum + ".kkbuluo.net";
            ToastUtils.showTip(mContext, "已切换为分支\n如地址有问题,请退出app重进再试~");
        } else if (localStep == 2) {
            API_DOMAIN = "http://mapi.kkbuluo.com";
            ToastUtils.showTip(mContext, "已切换为线上\n如地址有问题,请退出app重进再试~");
        } else if (localStep == 0) {
            API_DOMAIN = "http://mapi.kkbuluo.net";
            ToastUtils.showTip(mContext, "已切换为线下\n如地址有问题,请退出app重进再试~");
        }

        step = localStep;

        setUrl();

        SharedPreferUtil.getInstance().putString("APP_DOMAIN", API_DOMAIN);
    }


}
