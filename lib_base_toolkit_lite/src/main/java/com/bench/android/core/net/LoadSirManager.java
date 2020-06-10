package com.bench.android.core.net;

import com.kingja.loadsir.callback.Callback;
import com.kingja.loadsir.callback.NetErrorCallback;
import com.kingja.loadsir.callback.ServerErrorCallback;
import com.kingja.loadsir.callback.SuccessCallback;
import com.kingja.loadsir.core.Convertor;
import com.kingja.loadsir.core.LoadService;
import com.kingja.loadsir.core.LoadSir;

import java.util.HashMap;
import java.util.Map;

import static com.bench.android.core.net.http.HttpRequestHelper.CODE_NET_ERROR;
import static com.bench.android.core.net.http.HttpRequestHelper.CODE_SERVER_ERROR;
import static com.bench.android.core.net.http.HttpRequestHelper.CODE_SUCCESS;

/************************************************************************
 *@Project: core
 *@Package_Name: com.bench.android.core.net
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/9/6 
 *************************************************************************/
public class LoadSirManager {
    private Map<String, LoadService> mLoadServiceMap = new HashMap<>();
    private static LoadSirManager loadSirManager;

    public synchronized static LoadSirManager getInstance() {
        if (loadSirManager == null) {
            loadSirManager = new LoadSirManager();
        }
        return loadSirManager;
    }

    /**
     * 注册loadsir服务
     *
     * @param view     注册的对象，可以是activity,view
     * @param listener 实现了OnReloadListener的类
     */
    public LoadService register(Object view, Callback.OnReloadListener listener) {
        LoadService loadService = LoadSir.getDefault().register(view, listener, new Convertor<Integer>() {
            @Override
            public Class<? extends Callback> map(Integer code) {
                switch (code) {
                    case CODE_NET_ERROR:
                        return NetErrorCallback.class;
                    case CODE_SERVER_ERROR:
                        return ServerErrorCallback.class;
                    default:
                        return SuccessCallback.class;
                }
            }
        });
        mLoadServiceMap.put(String.valueOf(listener.hashCode()), loadService);
        return loadService;
    }


    public void onFinishRequest(String tag, int statusCode) {
        LoadService loadService = mLoadServiceMap.get(tag);
        if (loadService == null) {
            return;
        }
        boolean isFirstLoad = true;
        if (loadService.getLoadLayout().getTag() != null) {
            isFirstLoad = (boolean) loadService.getLoadLayout().getTag();
        }
        //如果请求成功，那么展示成功页面
        if (statusCode == CODE_SUCCESS) {
            loadService.showWithConvertor(statusCode);
        } else {
            //如果是第一次加载，才显示错误页面相关视图
            //不然触发列表加载更多的时候，突然没有网了，整个页面变成了失败图，不合适
            if (isFirstLoad) {
                loadService.showWithConvertor(statusCode);
            }
        }
        if (isFirstLoad) {
            loadService.getLoadLayout().setTag(false);
        }
    }

    public void removeLoadService(Object object) {
        mLoadServiceMap.remove(String.valueOf(object.hashCode()));
    }
}
