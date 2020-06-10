package com.bench.android.core.net.domain;

import com.bench.android.core.content.sharePreference.SharedPreferUtil;

import java.util.HashMap;

/**
 * app url管理器
 *
 * @author zhouyi
 */
public class ApiURLManager {

    public static ApiURLManager instance = null;

    private HashMap<String, String> urlMaps = new HashMap<>();

    private ApiURLManager() {
    }

    public static ApiURLManager getInstance() {
        if (instance == null) {
            instance = new ApiURLManager();
        }
        return instance;
    }

    public void putUrl(String key, String url) {
        urlMaps.put(key, url);
        SharedPreferUtil.getInstance().putString(key, url);
    }

    public String getUrl(String key) {
        String url = urlMaps.get(key);
        if (url == null) {
            url = SharedPreferUtil.getInstance().getString(key, null);
            if (url != null) {
                urlMaps.put(key, url);
            }
        }
        return url;
    }
}
