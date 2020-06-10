package com.bench.android.core.cache;

import com.bench.android.core.content.sharePreference.SharedPreferUtil;

import java.util.HashMap;

/************************************************************************
 *@Project: packages
 *@Package_Name: com.bench.android.core.util
 *@Descriptions: 缓存管理器
 *@Author: xingjiu
 *@Date: 2019/8/5 
 *************************************************************************/
public class CacheManager {

    /**
     * 文件缓存
     */
    public static final String CACHE_INTERNAL = "file";
    /**
     * SharePresence缓存
     */
    public static final String CACHE_SP = "sp";
    /**
     * sd卡缓存
     */
    public static final String CACHE_SDCARD = "sdcard";
    /**
     * ICacheServiceFetcher 集合
     */
    private static final HashMap<String, ICacheServiceFetcher<?>> CACHE_SERVICE_FETCHERS =
            new HashMap<>();

    private static int mCacheSize = 0;
    private static Object[] cache;

    static {

        registerCacheService(CACHE_INTERNAL,
                new AbstractCachedFetcher<InternalCacheManager>() {
                    @Override
                    public InternalCacheManager createCacheService() {
                        return InternalCacheManager.get();
                    }
                });

        registerCacheService(CACHE_SP,
                new AbstractCachedFetcher<SharedPreferUtil>() {
                    @Override
                    public SharedPreferUtil createCacheService() {
                        return SharedPreferUtil.getInstance();
                    }
                });

        registerCacheService(CACHE_SDCARD,
                new AbstractCachedFetcher<SdCardCacheManager>() {
                    @Override
                    public SdCardCacheManager createCacheService() {
                        return SdCardCacheManager.get();
                    }
                });
        /*
          存放ICacheServiceFetcher
         */
        cache = new Object[mCacheSize];
    }

    /**
     * 注册缓存服务
     *
     * @param cacheServiceName 缓存服务的名字
     * @param serviceFetcher   缓存服务访问者
     * @param <T>              缓存服务
     */
    private static <T> void registerCacheService(String cacheServiceName,
                                                 AbstractCachedFetcher<T> serviceFetcher) {
        CACHE_SERVICE_FETCHERS.put(cacheServiceName, serviceFetcher);
    }

    /**
     * 获取缓存服务
     *
     * @param name 缓存服务名字,{@link #CACHE_INTERNAL}或者{@link #CACHE_SDCARD} 或者{@link #CACHE_SP}
     * @return 缓存服务 与上面的name对应的对象是 {@link InternalCacheManager}，{@link SdCardCacheManager},{@link SharedPreferUtil}
     */
    public static Object getCacheService(String name) {
        ICacheServiceFetcher<?> fetcher = CACHE_SERVICE_FETCHERS.get(name);
        return fetcher != null ? fetcher.getCacheService() : null;
    }

    static abstract class AbstractCachedFetcher<T> implements ICacheServiceFetcher<T> {
        private final int mCacheIndex;

        AbstractCachedFetcher() {
            mCacheIndex = mCacheSize++;
        }

        @Override
        public T getCacheService() {
            T service = (T) cache[mCacheIndex];
            if (service != null) {
                return service;
            }
            service = createCacheService();
            cache[mCacheIndex] = service;
            return service;
        }

        /**
         * 创建缓存服务
         *
         * @return 缓存服务
         */
        public abstract T createCacheService();
    }


    interface ICacheServiceFetcher<T> {
        /**
         * 获取缓存服务
         *
         * @return 缓存服务
         */
        T getCacheService();
    }
}
