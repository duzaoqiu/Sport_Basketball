package com.bench.android.core.view.webview;

import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.bench.android.core.app.application.BaseApplication;
import com.bench.android.core.util.LibAppUtil;
import com.bench.android.core.util.LogUtils;
import com.bench.android.core.util.dimension.PxConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

/**
 * Created by zaozao on 2017/12/5.
 */

public class DetailsWebViewShowUtils {

    /**
     * webview.setTag(RESUME) Activity onResume状态
     */
    public static final int RESUME = 1;

    /**
     * Activity onPause状态
     */
    public static final int PAUSE = 2;

    /**
     * Activity onDestroy状态
     */
    public static final int DESTROY = 3;

    private static String mHtmlContent = null;

    private static Gson gson = new Gson();

    /**
     * 默认设置WebView适用于一个界面只有一个WebView，不用计算内容高度
     *
     * @param webView
     */
    public static void setWebView(WebView webView) {
        setWebView(webView, false, null, false);
    }

    /**
     * 设置webview参数
     *
     * @param keepOriginalLayoutParam 设置是否用WebView的LayoutParams
     *                                true：使用自己WebView的LayoutParmas,高度为网页的高度
     *                                false:设置宽度为MATCH_PARENT,高度为计算网页的高度
     * @param useJsCalculateHeight    true:使用js计算高度,并且要设置WebView的tag，以此来防止循环调用计算高度.
     *                                false:不使用js计算高度
     *
     */
    public static void setWebView(final WebView webView,
                                  boolean keepOriginalLayoutParam,
                                  final LoadFinishListener loadFinishListener,
                                  final boolean useJsCalculateHeight) {

        webView.getSettings().setJavaScriptEnabled(true);

        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);

        webView.getSettings().setBlockNetworkImage(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webView.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {

                if (useJsCalculateHeight) {
                    webView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (webView != null && webView.getParent() != null) {
                                int statusType = (int) webView.getTag();
                                if (statusType == RESUME) {
                                    webView.loadUrl("javascript:MyApp.resize(document.body.offsetHeight)");
                                }
                                webView.postDelayed(this, 500);
                            }
                        }
                    });
                }


                webView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (loadFinishListener != null) {
                            loadFinishListener.loadFinish();
                        }
                    }
                }, 600);

                super.onPageFinished(view, url);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                handler.proceed();
            }
        });

        webView.addJavascriptInterface(new JSObject(webView, keepOriginalLayoutParam), "MyApp");

    }

    public static void loadContent(WebView webView, String content) {
        String s = gson.toJson(new SubjectDetailWebShowModel(content));
        webView.loadDataWithBaseURL(null, getFormatHtmlContent(s), "text/html", "utf-8", null);
    }

    private static String getFormatHtmlContent(String content) {
        return getHtmlContent(BaseApplication.getContext()).replace("[SUBJECT_CONTENT]", content);
    }

    /**
     * 获取替换内容
     *
     * @param context
     * @return
     */
    private static String getHtmlContent(Context context) {
        if (mHtmlContent == null) {
            mHtmlContent = LibAppUtil.getFromRaw(context, "detail_show.htm");
            return mHtmlContent;
        } else {
            return mHtmlContent;
        }
    }


    private static class JSObject {

        private WebView webView;

        private float initHeight = 0;

        private boolean keepOriginalLayoutParam;

        public JSObject(WebView webView, boolean keepOriginalLayoutParam) {
            this.webView = webView;
            this.keepOriginalLayoutParam = keepOriginalLayoutParam;
        }

        @JavascriptInterface
        public void resize(final float height) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    if (height != 0 && initHeight != height) {
                        int layoutHeight = PxConverter.dp2px(BaseApplication.getContext(), height + 20);
                        if (keepOriginalLayoutParam && webView.getLayoutParams() != null) {
                            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) webView.getLayoutParams();
                            lp.height = layoutHeight;
                            webView.setLayoutParams(lp);
                        } else {
                            webView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (int) height));
                        }

                        initHeight = height;

                    }
                }
            });
        }

        @JavascriptInterface
        public void showImage(final int index, final String imageUrl) {
            webView.post(new Runnable() {
                @Override
                public void run() {
                    LogUtils.e("kkk", "点击图片----" + (imageUrl == null));
                    ArrayList<String> strArray = new Gson().fromJson(imageUrl, new TypeToken<ArrayList<String>>() {
                    }.getType());
                }
            });
        }

        @JavascriptInterface
        public void showLog(String log) {
            LogUtils.e("webView", log);
        }

        @JavascriptInterface
        public String sign(String id) {
            //todo 之后有需求，添加进去
            return null;
        }
    }

    public interface LoadFinishListener {
        void loadFinish();
    }

}

