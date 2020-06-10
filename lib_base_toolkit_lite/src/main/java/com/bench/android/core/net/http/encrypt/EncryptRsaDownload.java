package com.bench.android.core.net.http.encrypt;

import android.util.Base64;

import com.bench.android.core.net.http.IHttpResponseListener;
import com.bench.android.core.net.http.RequestFormBody;
import com.bench.android.core.net.http.WrapperHttpHelper;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.http.httphelpinterceptor.HttpEncryptInterceptor;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 加密aes密钥上传
 *
 * @author zhouyi
 */
public class EncryptRsaDownload implements IHttpResponseListener {

    public static AtomicBoolean sStartGetUrl = new AtomicBoolean();

    /**
     * 最大重试上传次数
     */
    private final static int MAX_RETRY_COUNT = 5;

    private final static ReentrantLock sLock;
    private final static Condition sCondition;

    private WrapperHttpHelper mHelper;
    private OnAesKeyUploadListener listener;
    private int mRetryCount = 0;

    static {
        sLock = new ReentrantLock();
        sCondition = sLock.newCondition();
    }


    public EncryptRsaDownload(OnAesKeyUploadListener listener) {
        mHelper = new WrapperHttpHelper(this);
        mHelper.setSyncRequest(true);
        this.listener = listener;
    }

    public synchronized boolean startUpload() {
        if (!sStartGetUrl.get()) {
            sStartGetUrl.set(true);
            getRsaKey();
            return true;
        }
        return false;
    }

    public void getRsaKey() {
        RequestFormBody body = new RequestFormBody(EncryptUrlEnum.CLIENT_KEY_DOWNLOAD);
        body.setEncrypt(false);
        mHelper.startRequest(body);
    }

    private void uploadAesKey() {
        byte[] aesData =new byte[1024];
        String s = new String(Base64.encode(aesData, Base64.DEFAULT));
        String data = s.replace("+", "%2b").replace("/", "%2f").replace("?", "%25").replace("#", "%23").replace("&", "%26").replace("=", "%3d").replace("\n", "");

        RequestFormBody body = new RequestFormBody(EncryptUrlEnum.AES_SERCRET_KEY_UPDATE);
        body.setEncrypt(false);
        body.put("ciphertext", data);
        mHelper.startRequest(body);
    }


    @Override
    public void onSuccess(RequestContainer request, Object o) throws JSONException {
        mRetryCount = 0;
        EncryptUrlEnum urlEnum = (EncryptUrlEnum) request.getRequestEnum();
        switch (urlEnum) {
            case AES_SERCRET_KEY_UPDATE: {
                sStartGetUrl.set(false);
                listener.uploadSuccess();
            }
            break;
            case CLIENT_KEY_DOWNLOAD: {
                synchronized (HttpEncryptInterceptor.LOCK){
                    JSONObject jsonObject = (JSONObject) o;
                    String publicKey = jsonObject.getString("publicKey");
                    byte[] bytes = publicKey.getBytes();
                    boolean b = false;
                    if (!b) {
                        listener.rsaKeySetFailed();
                    } else {
                        uploadAesKey();
                    }
                }

            }
            break;
            default:
                break;
        }
    }

    @Override
    public void onFailed(RequestContainer request, JSONObject jsonObject, boolean netFailed) throws Exception {
        EncryptUrlEnum urlEnum = (EncryptUrlEnum) request.getRequestEnum();
        if (mRetryCount < MAX_RETRY_COUNT) {
            switch (urlEnum) {
                case AES_SERCRET_KEY_UPDATE:
                    uploadAesKey();
                    break;
                case CLIENT_KEY_DOWNLOAD:
                    getRsaKey();
                    break;
                default:
                    break;
            }
            mRetryCount++;
        } else {
            listener.uploadFailed();
        }
    }

    public interface OnAesKeyUploadListener {
        void uploadSuccess();

        void uploadFailed();

        void rsaKeySetFailed();
    }
}
