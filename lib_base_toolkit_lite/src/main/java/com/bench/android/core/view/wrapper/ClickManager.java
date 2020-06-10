package com.bench.android.core.view.wrapper;

import java.util.concurrent.ConcurrentLinkedQueue;


public class ClickManager {
    public static ConcurrentLinkedQueue<ViewWrapper> list = new ConcurrentLinkedQueue<>();

    public static boolean start = false;

    public static boolean record = false;

    public static void addWidget(ViewWrapper viewWrapper) {
        if (list.size() > 0) {
            for (ViewWrapper wrapper : list) {
                if (wrapper.getViewId() == viewWrapper.getViewId() && wrapper.getContextName().equals(viewWrapper.getContextName())) {
                    return;
                }
            }
        }
        list.add(viewWrapper);
    }


}
