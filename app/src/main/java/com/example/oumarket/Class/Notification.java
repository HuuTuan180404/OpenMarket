package com.example.oumarket.Class;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.example.oumarket.Common.Common;
import com.example.oumarket.MyOrder;
import com.example.oumarket.R;

public class Notification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        Intent intent1 = new Intent(context, MyOrder.class);

        String idRequest = intent.getStringExtra("idRequest");

        Log.d("ZZZZZ",idRequest);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntentWithParentStack(intent1);

        PendingIntent pendingIntent = stackBuilder.getPendingIntent(Common.NOTIFICATION_ID, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, Common.CHANNEL_CART)
                .setContentTitle(context.getResources().getString(R.string.titleChannel1))
                .setContentText(context.getResources().getString(R.string.contentChannel1))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(context.getResources().getString(R.string.contentChannel1)))
                .setSmallIcon(R.drawable.baseline_edit_notifications_24)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(Common.NOTIFICATION_ID, builder.build());
            Common.FIREBASE_DATABASE.getReference(Common.REF_REQUESTS).child(idRequest).child("status").setValue("1");
        }
    }

}
