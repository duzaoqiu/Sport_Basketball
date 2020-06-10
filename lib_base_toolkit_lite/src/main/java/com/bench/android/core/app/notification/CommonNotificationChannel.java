package com.bench.android.core.app.notification;

import android.app.NotificationManager;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

/************************************************************************
 *@Project: common_lib
 *@Package_Name: com.bench.android.core.app.notification
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/4/17 
 *************************************************************************/
public class CommonNotificationChannel {
    public static final String ChannelA = "chat";
    public static final String ChannelB = "subscribe";
    public static List<ChannelInfoBean> channelList = new ArrayList<>();

    static {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channelList.add(new ChannelInfoBean(ChannelA, "聊天", NotificationManager.IMPORTANCE_HIGH));
            channelList.add(new ChannelInfoBean(ChannelB, "订阅", NotificationManager.IMPORTANCE_DEFAULT));
        }
    }

    static class ChannelInfoBean {
        private String channelId;
        private String channelName;
        private int importance;

        public ChannelInfoBean(String channelId, String channelName, int importance) {
            this.channelId = channelId;
            this.channelName = channelName;
            this.importance = importance;
        }

        public String getChannelId() {
            return channelId;
        }

        public void setChannelId(String channelId) {
            this.channelId = channelId;
        }

        public String getChannelName() {
            return channelName;
        }

        public void setChannelName(String channelName) {
            this.channelName = channelName;
        }

        public int getImportance() {
            return importance;
        }

        public void setImportance(int importance) {
            this.importance = importance;
        }
    }
}
