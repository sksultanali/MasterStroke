package com.developerali.masterstroke.Helpers;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.developerali.masterstroke.R;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "meeting_reminder_channel";

    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("title");
        String description = intent.getStringExtra("description");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.notifications_24) // Replace with your app's icon
                .setContentTitle("Meeting Reminder: " + (title != null ? title : "Meeting"))
                .setContentText(description != null ? description : "Your meeting is in 30 minutes!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify((int) System.currentTimeMillis(), builder.build());
        }
    }
}
