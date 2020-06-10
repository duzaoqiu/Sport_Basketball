package com.bench.android.core.net.http.httphelpinterceptor;

import android.util.Base64;

import com.android.library.R;
import com.bench.android.core.app.application.BaseApplication;
import com.bench.android.core.app.toast.ToastUtils;
import com.bench.android.core.net.http.HttpRequestConfig;
import com.bench.android.core.net.http.HttpRequestInterceptor;
import com.bench.android.core.net.http.RequestFormBody;
import com.bench.android.core.net.http.base.RequestContainer;
import com.bench.android.core.net.http.encrypt.EncryptRsaDownload;
import com.bench.android.core.net.http.encrypt.EncryptUrlFilterManager;
import com.bench.android.core.util.LogUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.concurrent.Callable;

import bolts.Task;



/**
 * 网络请求加密
 *
 * @author zhouyi
 */
public class HttpEncryptInterceptor implements HttpRequestInterceptor {

    /**
     * 主要用于native加解密数据，需要加锁，不然会导致jni崩溃
     */
    public final static String LOCK = "lock";

    /**
     * 加密数据
     *
     * @param jo
     * @return
     */
    @Override
    public JSONObject onResponseSuccess(RequestContainer requestContainer, JSONObject jo) {
        return decrypt(jo);
    }

    @Override
    public JSONObject onResponseFalse(RequestContainer request, JSONObject jo) {
        //如果进行加密了
        if (isEncrypt(jo)) {
            return decrypt(jo);
        } else {
            //判断是否需要上传aes密钥
            JSONObject errorObject = jo.optJSONObject("error");

            //扔给其他拦截器处理
            if (errorObject == null) {
                return jo;
            }

            String code = errorObject.optString("name");
            //服务端解密失败，需要我们重新上传aes密钥
            if ("INTERFACE_DECRYPT_FAILED".equals(code) || "AES_DECRYPT_SYSTEM_ERROR".equals(code)) {
                boolean b = updateAesKey();
                if (b) {
                    return InterceptorConstants.RETRY_JSON;
                } else {
                    return InterceptorConstants.FAILED_JSON;
                }
                //当前接口不加密
            } else if ("PRESENT_ACCESS_INTERFACE_NOT_NEED_ENCRYPT".equals(code)) {
                request.setEncrypt(false);
                //重试
                return InterceptorConstants.RETRY_JSON;
                //全局接口不加密
            } else if ("ACCESS_INTERFACE_NOT_NEED_ENCRYPT".equals(code)) {
                HttpRequestConfig.sUseEncrypt = false;
                //重试
                request.setEncrypt(false);
                return InterceptorConstants.RETRY_JSON;
                //全局接口需要加密
            } else if ("ACCESS_INTERFACE_NEED_ENCRYPT".equals(code)) {
                HttpRequestConfig.sUseEncrypt = true;
                //重试
                request.setEncrypt(true);
                return InterceptorConstants.RETRY_JSON;
            } else {
                //正常的错误，不需要进行处理,扔给其他拦截器
                return jo;
            }
        }
    }

    @Override
    public RequestContainer postRequest(RequestContainer requestContainer) {
        final String requestUrl = requestContainer.getRequestUrl();
        boolean isFilterUrl = false;
        if (requestUrl != null) {
            isFilterUrl = EncryptUrlFilterManager.getInstance().getFilterUrls().contains(requestUrl);
        }

        if (HttpRequestConfig.sUseEncrypt && requestContainer.isEncrypt() && !isFilterUrl) {
            try {
                //如果需要加密，则需要将数据转换为加密的数据
                StringBuilder sb = new StringBuilder();
                HashMap<String, String> filedsMap = requestContainer.getFiledsMap();
                int count = 0;
                for (String key : filedsMap.keySet()) {
                    if (count != 0) {
                        sb.append("&");
                    }
                    sb.append(URLEncoder.encode(key, "utf-8") + "=" + URLEncoder.encode(filedsMap.get(key), "utf-8"));
                    count++;
                }
                LogUtils.e("加密请求", requestUrl + sb.toString());
                byte[] dataBytes = sb.toString().getBytes();
                byte[] base64Data = null;
                synchronized (LOCK) {
                    base64Data = Base64.encode("".getBytes(), Base64.DEFAULT);
                }

                String data = new String(base64Data).replace("+", "%2b").replace("/", "%2f").replace("?", "%25").replace("#", "%23").replace("&", "%26").replace("=", "%3d").replace("\n", "");
                requestContainer.setEncrypt(true);

                //新建一个RequestFormBody用于，用于发起网络请求。主要为了，之后请求还是使用RequestContainer，
                //但是回调回来之后的RequestContainer，还是现有的RequestContainer
                RequestFormBody body = new RequestFormBody(requestContainer.getRequestEnum());
                body.setRequestHeader(requestContainer.getRequestHeader());
                body.setRequestUrl(requestContainer.getRequestUrl());
                body.put("ciphertext", data);
                body.setEncrypt(true);

                return body;
            } catch (UnsupportedEncodingException e) {
                //不会发生
                return null;
            }
        } else {
            requestContainer.setEncrypt(false);
            return requestContainer;
        }
    }

    private boolean isEncrypt(JSONObject jo) {
        if (HttpRequestConfig.sUseEncrypt) {
            boolean hasCipher = jo.has("ciphertext");
            if (hasCipher) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 上传aes密钥
     *
     * @return
     */
    private boolean updateAesKey() {
        EncryptRsaDownload encryptRsaDownload;
        final boolean[] uploadSuccess = new boolean[]{false};
        if (!EncryptRsaDownload.sStartGetUrl.get()) {
            EncryptRsaDownload.OnAesKeyUploadListener onAesKeyUploadListener = new EncryptRsaDownload.OnAesKeyUploadListener() {
                @Override
                public void uploadSuccess() {
                    //aes密钥上传成功，可以执行之后的请求
                    uploadSuccess[0] = true;
                }

                @Override
                public void uploadFailed() {
                    uploadSuccess[0] = false;
                }

                @Override
                public void rsaKeySetFailed() {
                    Task.call(new Callable<Object>() {
                        @Override
                        public Object call() throws Exception {
                            ToastUtils.show(BaseApplication.getContext().getString(R.string.set_rsa_key_tip));
                            return null;
                        }
                    }, Task.UI_THREAD_EXECUTOR);
                }
            };
            encryptRsaDownload = new EncryptRsaDownload(onAesKeyUploadListener);
            encryptRsaDownload.startUpload();
        }
        return uploadSuccess[0];
    }

    /**
     * 处理解密
     *
     * @param
     * @return consumed
     */
    private JSONObject decrypt(JSONObject jo) {
        synchronized (LOCK) {
            if (HttpRequestConfig.sUseEncrypt) {
                try {
                    boolean hasCipher = jo.has("ciphertext");
                    if (hasCipher) {
                        String ciphertext = jo.getString("ciphertext");
                        String timestamp = jo.getString("nowTimestamp");
                        byte[] timestampB = timestamp.getBytes();
                        byte[] cipherData = Base64.decode(ciphertext, Base64.DEFAULT);
                        byte[] bytes1 = new byte[1024];
                        String response = new String(bytes1);
                        LogUtils.e("解密返回", response);
                        return new JSONObject(response);
                    } else {
                        return jo;
                    }
                } catch (JSONException e) {
                    return null;
                }
            } else {
                return jo;
            }
        }

    }
}
