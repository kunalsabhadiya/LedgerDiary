package com.example.Ledgerdiary.reminder;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.Ledgerdiary.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Objects;

public class ReminderReceiver extends Service {
    private static final String TAG = "ReminderNotificationService";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        FirebaseDatabase.getInstance().getReference().child("singlereminder")
                .child(FirebaseAuth.getInstance().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot snapshot1:snapshot.getChildren()){
                            singletimeentrymodel model=snapshot1.getValue(singletimeentrymodel.class);
                            try {
                                scheduleNotification(model);
                            } catch (ParseException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        return super.onStartCommand(intent, flags, startId);
    }

    private void scheduleNotification(singletimeentrymodel reminder) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a");

        Date reminderDate = dateFormat.parse(reminder.getDate());
        Date reminderTime = timeFormat.parse(reminder.getTime());

        Calendar currentCalendar = Calendar.getInstance();
        currentCalendar.setTime(new Date());

        Calendar reminderCalendar = Calendar.getInstance();
        assert reminderDate != null;
        reminderCalendar.setTime(reminderDate);

        Calendar reminderTimeCalendar = Calendar.getInstance();
        assert reminderTime != null;
        reminderTimeCalendar.setTime(reminderTime);
        reminderCalendar.set(Calendar.HOUR_OF_DAY, reminderTimeCalendar.get(Calendar.HOUR_OF_DAY));
        reminderCalendar.set(Calendar.MINUTE, reminderTimeCalendar.get(Calendar.MINUTE));

        if (currentCalendar.compareTo(reminderCalendar) == 0) {
            createNotificationChannel();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channel_id")
                    .setSmallIcon(R.drawable.bell)
                    .setContentTitle(reminder.getAmount())
                    .setContentText(reminder.getDescription())
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);
             String str=String.valueOf(reminder.getTimestamp());
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
            notificationManager.notify(Integer.parseInt(str.substring(5,11)), builder.build());
        }
    }

    private void createNotificationChannel() {
        String channelId = "channel_id";
        String channelName = "Reminder Channel";
        String channelDescription = "Channel for reminder notifications";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.setDescription(channelDescription);

        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}