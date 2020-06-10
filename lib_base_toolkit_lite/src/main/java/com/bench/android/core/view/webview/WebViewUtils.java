package com.bench.android.core.view.webview;

import android.net.http.SslError;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * webview常用工具
 *
 * @author zhouyi 2019/4/1
 */
public class WebViewUtils {


    /**
     * 强制显示无https证书的网页
     *
     * @param webView
     */
    public static void forceShowNoHttps(WebView webView) {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                super.onReceivedSslError(view, handler, error);

                //如果证书错误，还是强制显示该网页
                handler.proceed();
            }
        });
    }

    /**
     * webview默认设置
     *
     * @param webView
     */
    public static void defaultWebViewSettings(WebView webView) {
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setPluginState(WebSettings.PluginState.OFF);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setUserAgentString("Android Mozilla/5.0 AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Mobile Safari/534.30");
        webView.getSettings().setAllowFileAccess(true);
    }

    /**
     * 返回上一页
     */
    public static void backToLastPage(WebView webView) {
        webView.goBack();
    }

    /**
     * 网页端如果要调用app端的方法直接使用:
     * MyApp.mCallback("xxx")
     * 如果网页通知app绑定成功
     * MyApp.mCallback("bindSuccess")；
     * 如果网页通知app关闭当前界面
     * MyApp.mCallback("exit")；
     * @param webView
     * @param callback
     */
    public static void addJsCallback(WebView webView, JSCallback callback) {
        webView.addJavascriptInterface(new JSObject(callback), "MyApp");
    }

    private static class JSObject {

        JSCallback jsCallback;

        public JSObject(JSCallback jsCallback) {
            this.jsCallback = jsCallback;
        }

        @JavascriptInterface
        public String callback(String str) {
            return jsCallback.callback(str);
        }
    }

    public interface JSCallback {
        String callback(String str);
    }


}
