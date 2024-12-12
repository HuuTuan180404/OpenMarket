package com.example.oumarket.Class;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.example.oumarket.Common.Common;
import com.example.oumarket.MyOrder;
import com.example.oumarket.R;
import com.example.oumarket.Signin;

import io.paperdb.Paper;

public class Notification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, MyOrder.class);

        Paper.init(context);
        String user = Paper.book().read(Common.USERNAME_KEY);
        String password = Paper.book().read(Common.PASSWORD_KEY);

        if (user == null || password == null) {
            intent1 = new Intent(context, Signin.class);
        }

        String idRequest = intent.getStringExtra("idRequest");

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent1);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(Common.NOTIFICATION_ID, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Common.CHANNEL_CART)
                .setContentTitle(context.getResources().getString(R.string.titleChannel1))
                .setContentText(context.getResources().getString(R.string.contentChannel1))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getResources().getString(R.string.contentChannel1)))
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(Common.NOTIFICATION_ID, builder.build());
            Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(idRequest).child("status").setValue("1");
        }
    }

}
