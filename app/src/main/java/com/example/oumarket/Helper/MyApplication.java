package com.example.oumarket.Helper;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.cloudinary.android.MediaManager;
import com.example.oumarket.Common.Common;

import java.util.HashMap;
import java.util.Map;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannel("Cart", "Orderes successfull");
        initConfig();
    }
    private void initConfig() {
        try {
            MediaManager.get(); // Kiểm tra nếu đã được khởi tạo
        } catch (IllegalStateException e) {
            // Chưa được khởi tạo, tiến hành khởi tạo
            Map<String, Object> config = new HashMap<>();
            config.put("cloud_name", "dv2zyrxsv");
            config.put("api_key", "564718357889346");
            config.put("api_secret", "TfxQE6I3edoX7yeQrglD0avshDQ");
            MediaManager.init(this, config);
        }
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
