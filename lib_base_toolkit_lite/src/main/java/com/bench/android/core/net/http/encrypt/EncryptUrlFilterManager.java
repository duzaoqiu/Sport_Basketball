package com.bench.android.core.net.http.encrypt;

import java.util.ArrayList;
import java.util.List;

/**
 * 添加url过滤
 *
 * @author zhouyi 2019/06/19
 */
public class EncryptUrlFilterManager {

    private List<String> filterUrls = new ArrayList<>();

    private static EncryptUrlFilterManager instance;

    private EncryptUrlFilterManager() {
    }

    public static EncryptUrlFilterManager getInstance() {
        if (instance == null) {
            instance = new EncryptUrlFilterManager();
        }
        return instance;
    }

    public void addUrl(String url) {
        if (!filterUrls.contains(url)) {
            filterUrls.add(url);
        }
    }

    public List<String> getFilterUrls() {
        return filterUrls;
    }
}
