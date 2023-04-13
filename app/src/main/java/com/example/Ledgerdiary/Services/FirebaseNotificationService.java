package com.example.Ledgerdiary.Services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;

import com.example.Ledgerdiary.R;
import com.example.Ledgerdiary.onlinecustomer.customerInterface;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;


public class FirebaseNotificationService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        if (message.getData().size() > 0) {
            Map<String, String> map = message.getData();


                String title = map.get("title");
                String smessage = map.get("message");
                String reciveruid = map.get("id");
                String sImage = map.get("image");
                String rImage = map.get("rimage");
                String chatID = map.get("chatid");
                String rnumber = map.get("number");
                String rname = map.get("rname");



                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    createOreoNotification(title, smessage,reciveruid,rImage,rnumber,rname);
                }

            }


        super.onMessageReceived(message);
    }


    @Override
    public void onNewToken(@NonNull String token) {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            updateToken(token);
        }
        super.onNewToken(token);
    }
    private  void updateToken(String token){
        FirebaseDatabase.getInstance().getReference().child(Objects.requireNonNull(FirebaseAuth.getInstance().getUid()))
                .child("token").setValue(token);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createOreoNotification(String title, String message, String ruid, String rimage, String rnumber,String rname) {

        NotificationChannel channel = new NotificationChannel("1000", "Message", NotificationManager.IMPORTANCE_HIGH);
        channel.setShowBadge(true);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Intent intent = new Intent(this, customerInterface.class);
        intent.putExtra("ruid", ruid);
        intent.putExtra("ciprofile",rimage);
        intent.putExtra("mnumber",rnumber);
        intent.putExtra("ciname",rname);
        intent.putExtra("noti","check");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Notification notification = new Notification.Builder(this, "1000")
                .setContentTitle(title)
                .setContentText(message)
                .setColor(ResourcesCompat.getColor(getResources(), R.color.grey, null))
                .setSmallIcon(R.drawable.profileimage)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .build();
        manager.notify(100, notification);

    }

}
