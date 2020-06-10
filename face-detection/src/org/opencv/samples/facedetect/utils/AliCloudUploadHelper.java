package org.opencv.samples.facedetect.utils;

import android.content.Context;

import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.vod.upload.VODUploadCallback;
import com.alibaba.sdk.android.vod.upload.VODUploadClient;
import com.alibaba.sdk.android.vod.upload.VODUploadClientImpl;
import com.alibaba.sdk.android.vod.upload.model.UploadFileInfo;
import com.alibaba.sdk.android.vod.upload.model.VodInfo;
import com.bench.android.core.app.application.BaseApplication;
import com.bench.android.core.net.http.IHttpResponseListener;
import com.bench.android.core.net.http.RequestFormBody;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.RequestContainer;

import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.samples.facedetect.LogUtils;

/**
 * 阿里云上传文件
 *
 * @author zhouyi
 */
public class AliCloudUploadHelper implements IHttpResponseListener {

    String auth;
    String url;
    // create VODUploadClient
    VODUploadClient uploader;
    // setup callback
    VODUploadCallback callback = new VODUploadCallback() {
        @Override
        public void onUploadSucceed(UploadFileInfo info) {
            String coverUrl = info.getVodInfo().getCoverUrl();
            uploadComplete();

            LogUtils.e("onUploadSucceed" + coverUrl);
            OSSLog.logDebug("onsucceed ------------------" + coverUrl);
        }

        @Override
        public void onUploadFailed(UploadFileInfo info, String code, String message) {
            LogUtils.e("onUploadFailed");
            mListener.onFailed("onUploadFailed");
            OSSLog.logError("onfailed ------------------ " + info.getFilePath() + " " + code + " " + message);
        }

        @Override
        public void onUploadProgress(UploadFileInfo info, long uploadedSize, long totalSize) {
            LogUtils.e("onUploadProgress");
            OSSLog.logDebug("onProgress ------------------ " + info.getFilePath() + " " + uploadedSize + " " + totalSize);
        }


        @Override
        public void onUploadTokenExpired() {
            mListener.onFailed("onUploadTokenExpired");
            LogUtils.e("onUploadTokenExpired");
            OSSLog.logError("onExpired ------------- ");
            // 重新刷新上传凭证:RefreshUploadVideo
            //uploadAuth = "此处需要设置重新刷新凭证之后的值";
            //uploader.resumeWithAuth(uploadAuth);
        }

        @Override
        public void onUploadRetry(String code, String message) {
            LogUtils.e("onUploadRetry");
            OSSLog.logError("onUploadRetry ------------- ");
        }

        @Override
        public void onUploadRetryResume() {
            LogUtils.e("onUploadRetryResume");
            OSSLog.logError("onUploadRetryResume ------------- ");
        }

        @Override
        public void onUploadStarted(UploadFileInfo uploadFileInfo) {
            LogUtils.e("onUploadStarted "+auth+" "+url);
            OSSLog.logError("onUploadStarted ------------- ");
            uploader.setUploadAuthAndAddress(uploadFileInfo, auth, url);
        }
    };

    public void init(Context context) {
        LogUtils.e("开始上传 "+mVideoPath);
        uploader = new VODUploadClientImpl(context);
        //上传初始化
        uploader.init(callback);

        String filePath = mVideoPath;
        VodInfo vodInfo = new VodInfo();

        vodInfo.setTitle("测试1");
        vodInfo.setDesc("描述1");
        vodInfo.setCateId(19);

        //vodInfo.setTags(CollectionUtils.);
        uploader.addFile(filePath, vodInfo);
        uploader.start();
    }

    private WrapperHttpHelper httpHelper = new WrapperHttpHelper(this);
    private String mModelData;
    private String mVideoPath;
    private String mTrainRecordId;

    private OnUploadSuccessListener mListener;

    public void uploadHttp(String modelData, String videoPath, OnUploadSuccessListener listener) {
        mModelData = modelData;
        mVideoPath = videoPath;
        mListener = listener;
        RequestFormBody body = new RequestFormBody(UrlEnum.TRAIN_RECORD_CREATE);
        httpHelper.startRequest(body);
    }

    public void uploadComplete() {
        RequestFormBody body = new RequestFormBody(UrlEnum.TRAIN_RECORD_COMPLETE);
        body.put("trainRecordId", mTrainRecordId);
        body.put("trainData", mModelData);
        httpHelper.startRequest(body);
    }

    public void shareVideo() {
        RequestFormBody body = new RequestFormBody(UrlEnum.TRAIN_RECORD_SHARE);
        body.put("trainRecordId", mTrainRecordId);
        httpHelper.startRequest(body);
    }


    @Override
    public void onSuccess(RequestContainer request, Object o) throws JSONException {
        UrlEnum urlEnum = (UrlEnum) request.getRequestEnum();
        switch (urlEnum) {
            case TRAIN_RECORD_CREATE: {
                JSONObject jo = (JSONObject) o;
                String thirdUploadAuth = jo.getString("thirdUploadAuth");
                String thirdUploadAddress = jo.getString("thirdUploadAddress");
                mTrainRecordId = jo.getString("trainRecordId");
                auth = thirdUploadAuth;
                url = thirdUploadAddress;
                init(BaseApplication.getContext());
            }
            break;
            case TRAIN_RECORD_COMPLETE: {
                shareVideo();
            }
            break;
            case TRAIN_RECORD_SHARE: {
                mListener.onSuccess();
            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        mListener.onFailed("网络返回错误" + jsonObject.toString());
    }

    public static interface OnUploadSuccessListener {
        void onSuccess();

        void onFailed(String msg);
    }
}
