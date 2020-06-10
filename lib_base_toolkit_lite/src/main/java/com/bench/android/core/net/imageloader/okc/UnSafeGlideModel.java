package com.bench.android.core.net.imageloader.okc;


import android.content.Context;

import androidx.annotation.NonNull;

import com.bench.android.core.net.domain.base.AppDomainManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;

import java.io.InputStream;

public class UnSafeGlideModel implements GlideModule {

    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {

    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {
        if (AppDomainManager.Debug) {
            registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory());
        }
    }
}
