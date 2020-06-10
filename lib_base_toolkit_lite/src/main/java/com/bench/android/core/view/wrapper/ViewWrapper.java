package com.bench.android.core.view.wrapper;

import androidx.annotation.Nullable;

public class ViewWrapper {

    private int viewId;

    private Object data;

    private String contextName;

    public static class Pos {

        /**
         * ScrollView使用
         */
        int x = 0;
        int y = 0;

        /**
         * Recycler或者ListView使用
         */
        int visiblePos = 0;

    }

    public ViewWrapper(int viewId) {
        this.viewId = viewId;
    }

    public int getViewId() {
        return viewId;
    }

    public void setViewId(int viewId) {
        this.viewId = viewId;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getContextName() {
        return contextName;
    }

    public void setContextName(String contextName) {
        this.contextName = contextName;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        ViewWrapper viewWrapper = (ViewWrapper) obj;
        return viewId == viewWrapper.viewId && contextName.equals(viewWrapper.getContextName());
    }
}
