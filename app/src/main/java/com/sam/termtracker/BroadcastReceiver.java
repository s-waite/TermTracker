package com.sam.termtracker;

import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class BroadcastReceiver extends android.content.BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context.getApplicationContext(), "MY_CHANNEL")
                .setSmallIcon(R.drawable.ic_baseline_add_24)
                .setContentTitle("Term Information")
                .setContentText("Your term is starting/ending tomorrow")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

        notificationManager.notify(200, builder.build());
    }
}
