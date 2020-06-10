package com.bench.android.core.app.notification;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.bench.android.core.app.application.BaseApplication;


import java.util.List;

/************************************************************************
 *@Project: common_lib
 *@Package_Name: com.bench.android.core.app.notification
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/4/17 
 *************************************************************************/
public class NotificationUtils {
    private static NotificationUtils notificationUtils;
    private NotificationManager notificationManager;

    public NotificationUtils() {
        notificationManager = (NotificationManager) BaseApplication.getContext().getSystemService(
                Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            initNotification();
        }
    }

    public static NotificationUtils getDefault(){
        if(notificationUtils==null){
            notificationUtils = new NotificationUtils();
        }
        return notificationUtils;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initNotification() {
        List<CommonNotificationChannel.ChannelInfoBean> channelList = CommonNotificationChannel.channelList;
        if(channelList.size()>0){
            for(CommonNotificationChannel.ChannelInfoBean channelInfoBean:channelList){
                createNotificationChannel(channelInfoBean.getChannelId(), channelInfoBean.getChannelName(), channelInfoBean.getImportance());
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel(String channelId, String channelName, int importance) {
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        notificationManager.createNotificationChannel(channel);
    }
    public void sendChatNotice(int identifier,String channel, WrappedNotification notification){
        checkNotificationInService(channel);
        notificationManager.notify(identifier, notification.build());
    }

    /**
     * 检查通知是否可用
     */
    private void checkNotificationInService(String channelName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(channelName);
            if (channel.getImportance() == NotificationManager.IMPORTANCE_NONE) {
                Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                intent.putExtra(Settings.EXTRA_APP_PACKAGE, BaseApplication.getContext().getPackageName());
                intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Toast.makeText(BaseApplication.getContext(), "请手动将通知打开", Toast.LENGTH_SHORT).show();
                BaseApplication.getContext().startActivity(intent);
            }
        }
    }
}
