package com.m2team.library.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.m2team.library.R;


public class NotificationUtils {

    public static void showNotification(int iconResId, Context context, int notiId, Class clazz, String content, boolean isVibrate, Uri soundURI) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context, clazz);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(iconResId)
                .setContentText(content)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        if (soundURI != null) {
            builder.setSound(soundURI);
        }

        if (isVibrate) {
            builder.setVibrate(new long[]{0, 300, 100, 300});
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            builder.setContentTitle(context.getString(R.string.app_name));
        }

        Notification notification = builder.build();

        notificationManager.notify(notiId, notification);
    }

    public static void cancelNotification(Context context, int notiId) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(notiId);
    }
}
