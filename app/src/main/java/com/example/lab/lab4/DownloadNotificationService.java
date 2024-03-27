package com.example.lab.lab4;
import static androidx.core.content.ContextCompat.getSystemService;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.lab.MainActivity;
import com.example.lab.R;


public class DownloadNotificationService {
    private static final String DOWNLOAD_CHANNEL_ID = "downloadChanel";
    private Context context;
    private NotificationCompat.Builder builder;
    private NotificationManager notificationManager;

    public DownloadNotificationService(Context context) {
        this.context = context;
    }
    private Notification createNotification() {
        Intent intent = new Intent(context, Lab4Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);
        builder = new NotificationCompat.Builder(context, DOWNLOAD_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setContentTitle("Pobieranie pliku")
                .setProgress(100, 0, false)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);
        return builder.build();
    }

    public void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(DOWNLOAD_CHANNEL_ID,
                    "downloadChannel", NotificationManager.IMPORTANCE_LOW);
            notificationManager = getSystemService(context, NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
            notificationManager.notify(0, createNotification());
        }
    }

    public void updateNotificationProgress(int progressStatus) {
        builder.setProgress(100, progressStatus, false);
        notificationManager.notify(0, builder.build());
    }

    public void setAsCompleted() {
        builder.setContentText("Pobieranie ukończone")
                .setProgress(0, 0, false);
        notificationManager.notify(0, builder.build());
    }

}
