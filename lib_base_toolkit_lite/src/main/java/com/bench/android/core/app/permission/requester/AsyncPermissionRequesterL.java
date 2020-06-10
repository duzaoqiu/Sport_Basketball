package com.bench.android.core.app.permission.requester;


import com.bench.android.core.app.permission.AsyncPermissionRequest;
import com.bench.android.core.app.permission.checker.AsyncPermissionCheckerFactory;
import com.bench.android.core.app.permission.info.Permission;
import com.bench.android.core.app.permission.info.PermissionEnum;
import com.bench.android.core.os.ansyncchain.AsyncChain;
import com.bench.android.core.os.ansyncchain.core.AsyncChainRunnable;
import com.bench.android.core.os.ansyncchain.core.AsyncChainTask;

import java.util.ArrayList;
import java.util.List;

/**
 * 版本号低于23的权限请求
 *
 * @author :  malong    luomingbear@163.com
 * @date :  2019/8/13
 **/
public class AsyncPermissionRequesterL extends AsyncPermissionRequester {
    public AsyncPermissionRequesterL() {
    }

    @Override
    public void request(final AsyncPermissionRequest request) {
        //延迟执行，让用户添加的监听器能够存进request对象里面
        AsyncChain.delay(200).withMain(new AsyncChainRunnable() {
            @Override
            public void run(AsyncChainTask task) throws Exception {
                List<Permission> grantedList = new ArrayList<>();
                List<Permission> deniedList = new ArrayList<>();
                boolean allGranted = true;
                for (String permission : request.getPermissions()) {
                    if (AsyncPermissionCheckerFactory.getChecker().hasPermission(getContext(), request.isHasTest(), permission)) {
                        grantedList.add(new Permission(permission, PermissionEnum.of(permission).getMessage(), true));
                    } else {
                        deniedList.add(new Permission(permission, PermissionEnum.of(permission).getMessage(), false));
                        allGranted = false;
                    }
                }
                if (allGranted && request.getAllGrantedListener() != null) {
                    request.getAllGrantedListener().onResult(grantedList);
                } else if (request.getGrantedListener() != null) {
                    request.getGrantedListener().onResult(grantedList);
                }
                if (deniedList.size() > 0 && request.getDeniedListener() != null) {
                    request.getDeniedListener().onResult(deniedList);
                }
                task.onComplete();
            }
        }).go(this);
    }
}
