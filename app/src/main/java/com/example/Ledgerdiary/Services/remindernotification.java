package com.example.Ledgerdiary.Services;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.Ledgerdiary.R;
import com.example.Ledgerdiary.reminder.singletimeentry;

public class remindernotification extends BroadcastReceiver {
    private static final String CHANNEL_ID = "reminder_channel";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        Intent notificationIntent = new Intent(context, singletimeentry.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.bell)
                .setContentTitle(title)
                .setContentText(description)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }
}
