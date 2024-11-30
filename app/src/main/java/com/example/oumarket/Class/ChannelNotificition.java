package com.example.oumarket.Class;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.example.oumarket.Common.Common;

public class ChannelNotificition extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel("Cart", "Orderes successfull");
    }

    private void createNotificationChannel(String channel_name, String channel_description) {
        // Chỉ tạo kênh cho Android 8.0 trở lên
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel = new NotificationChannel(Common.CHANNEL_CART, channel_name, importance);
            channel.setDescription(channel_description);

            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }



}
