package com.mousser.myapplication.UI;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.mousser.myapplication.R;

public class MyReceiver extends BroadcastReceiver {
    String channelID = "notify";
    static int notificationID;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, intent.getStringExtra("key"), Toast.LENGTH_LONG).show();

        String channelName = "myChannel";
        String channelDesc = "myDescription";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel nChannel = new NotificationChannel(channelID, channelName,importance);
        nChannel.setDescription(channelDesc);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(nChannel);

        Notification note = new NotificationCompat.Builder(context, channelID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentText(intent.getStringExtra("key"))
                .setContentTitle("Reminder").build();

        notificationManager.notify(notificationID++, note);








    }


}