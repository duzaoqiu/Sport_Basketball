package com.bench.android.core.net.http.params;


/**
 *    requestBuilder.addHeader("Web-Exterface-SourceVersion", "1.2.20191024.63-SNAPSHOT");
 *         requestBuilder.addHeader("Web-Exterface-ExterfaceVersion", "1");
 *         requestBuilder.addHeader("Web-Exterface-AppCode", "user.account.api.mobile.base");
 *
 * 接口请求需要添加这几个参数，所以，每一个接口都需要相应的版本号
 */
public interface IBaseJsonRequestHttpName extends BaseRequestHttpName {


    String getExterfaceVersion();

    String getSourceVersion();

    String getAppCode();

}
