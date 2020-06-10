package com.bench.android.core.app.notification;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

/************************************************************************
 *@Project: common_lib
 *@Package_Name: com.bench.android.core.app.notification
 *@Descriptions:
 *@Author: xingjiu
 *@Date: 2019/4/17 
 *************************************************************************/
public class WrappedNotification {
    private Notification notification;

    public WrappedNotification(Builder b) {
        notification = new NotificationCompat.Builder(b.context, b.channelId)
                .setContentTitle(b.title)
                .setContentText(b.text)
                .setWhen(b.when)
                .setSmallIcon(b.smallIcon)
                .setLargeIcon(b.largeIcon)
                .setAutoCancel(b.autoCancel)
                .setContentIntent(b.contentIntent)
                .setDeleteIntent(b.deleteIntent)
                .setStyle(b.style)
                .setCustomContentView(b.remoteViews)
                .setCustomBigContentView(b.bigRemoteViews)
                .setCustomHeadsUpContentView(b.customHeadsUpView)
                .setOnlyAlertOnce(b.onlyAlertOnce)
                .setProgress(b.max, b.progress, b.indeterminate)
                .build();
    }

    public Notification build() {
        return notification;
    }

    public static final class Builder {
        Context context;
        String channelId;
        CharSequence title;//设置通知标题
        CharSequence text;//设置通知内容
        long when;//通知产生的时间
        int smallIcon;
        Bitmap largeIcon;
        boolean autoCancel;//点击后通知栏移除，必须先设置contentIntent才有效果
        PendingIntent contentIntent;// 设置pendingIntent,点击通知时就会用到
        PendingIntent deleteIntent;// 滑动消失行为的Intent
        private boolean onlyAlertOnce;
        private int max, progress;//为通知设置设置一个进度条,indeterminate表示进度是否确定
        private boolean indeterminate;
        private NotificationCompat.Style style;//为通知设置样式
        private RemoteViews remoteViews;//自定义通知样式
        private RemoteViews bigRemoteViews;//自定义通知展开后的样式，有的手机是长按展开
        private RemoteViews customHeadsUpView;//悬挂通知布局

        public Builder(Context context, String channelId) {
            this.context = context;
            this.channelId = channelId;
        }

        public Builder contentTitle(CharSequence title) {
            this.title = title;
            return this;
        }

        public Builder contentText(CharSequence text) {
            this.text = text;
            return this;
        }

        public Builder when(long when) {
            this.when = when;
            return this;
        }

        public Builder smallIcon(int icon) {
            this.smallIcon = icon;
            return this;
        }

        public Builder largeIcon(Bitmap icon) {
            this.largeIcon = icon;
            return this;
        }

        public Builder autoCancel(boolean autoCancel) {
            this.autoCancel = autoCancel;
            return this;
        }

        public Builder contentIntent(PendingIntent intent) {
            this.contentIntent = intent;
            return this;
        }

        public Builder deleteIntent(PendingIntent intent) {
            this.deleteIntent = intent;
            return this;
        }

        public Builder onlyAlertOnce(boolean onlyAlertOnce) {
            this.onlyAlertOnce = onlyAlertOnce;
            return this;
        }

        public Builder progress(int max, int progress, boolean indeterminate) {
            this.max = max;
            this.progress = progress;
            this.indeterminate = indeterminate;
            return this;
        }

        public Builder style(NotificationCompat.Style style) {
            this.style = style;
            return this;
        }

        public Builder customContentView(RemoteViews remoteViews) {
            this.remoteViews = remoteViews;
            return this;
        }

        public Builder customBigContentView(RemoteViews bigRemoteViews) {
            this.bigRemoteViews = bigRemoteViews;
            return this;
        }

        public Builder customHeadsUpContentView(RemoteViews customHeadsUpView) {
            this.customHeadsUpView = customHeadsUpView;
            return this;
        }

        public WrappedNotification build() {
            return new WrappedNotification(this);
        }

    }
}

