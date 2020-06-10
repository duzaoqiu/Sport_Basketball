package com.bench.android.core.net.http.encrypt;


import com.bench.android.core.net.http.params.AppParam;
import com.bench.android.core.net.http.params.BaseRequestHttpName;

public enum EncryptUrlEnum implements BaseRequestHttpName {


    CLIENT_KEY_DOWNLOAD("CLIENT_KEY_DOWNLOAD", AppParam.S_URL),

    AES_SERCRET_KEY_UPDATE("AES_SERCRET_KEY_UPDATE", AppParam.S_URL);

    private String name;
    private String url;

    EncryptUrlEnum(String name, String url) {
        this.name = name;
        this.url = url;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getUrl() {
        return url;
    }
}
