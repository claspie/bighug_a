package com.teleclub.telesms.util;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.teleclub.telesms.activity.MainActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationUtils {
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;
    private String NOTIFICATION_CHANNEL_ID;
    private String NOTIFICATION_CHANNEL_NAME;
    private Context context;


    public NotificationUtils(Context context) {
        this.context = context;
        NOTIFICATION_CHANNEL_ID = context.getApplicationContext().getPackageName();
        NOTIFICATION_CHANNEL_NAME = context.getApplicationContext().getPackageName();
    }

    /*--------------------------------------------------
        BEFORE STARTED
    -------------------------------------------------------*/

    private void buildNotification(int NOTIFICATION_ID) {
        getNotificationManager().notify(NOTIFICATION_ID, mBuilder.build());
    }

    public  NotificationManager getNotificationManager(){
        if (mNotificationManager == null) {
            mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel mChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
                mChannel.enableLights(true);
                mChannel.enableVibration(true);
                mNotificationManager.createNotificationChannel(mChannel);
            }
        }
        return mNotificationManager;
    }

    private int getNotificationId() {
        return (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
    }

    private PendingIntent getLaunchIntent(int notificationId) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("notificationId", notificationId);
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    /*--------------------------------------------------
        BIG PICTURE STYLE NOTIFICATION
      -------------------------------------------------------*/

    private void bigPictureStyleNotification(String title,
                                             Bitmap bitmap) {

        int NOTIFICATION_ID = getNotificationId();

        mBuilder.setContentTitle(title);
        mBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(bitmap));
        mBuilder.setContentIntent(getLaunchIntent(NOTIFICATION_ID));

        buildNotification(NOTIFICATION_ID);
    }


    /*--------------------------------------------------
        BIG TEXT STYLE NOTIFICATION
      -------------------------------------------------------*/

    private void bigTextStyleNotification(String title,
                                          String message) {

        int NOTIFICATION_ID = getNotificationId();

        mBuilder.setContentTitle(title);
        mBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(message));
        mBuilder.setContentIntent(getLaunchIntent(NOTIFICATION_ID));

        buildNotification(NOTIFICATION_ID);
    }

    /*--------------------------------------------------
        INBOX STYLE NOTIFICATION
      -------------------------------------------------------*/

    private void inboxStyleNotification(String title,
                                        String summeryText,
                                        ArrayList<String> messageList) {

        if (messageList == null || messageList.isEmpty())
            return;

        int NOTIFICATION_ID = getNotificationId();

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setSummaryText(summeryText);
        for (String singleMsg : messageList) {
            inboxStyle.addLine(singleMsg);
        }
        mBuilder.setContentTitle(title);
        mBuilder.setStyle(inboxStyle);
        mBuilder.setContentIntent(getLaunchIntent(NOTIFICATION_ID));

        buildNotification(NOTIFICATION_ID);
    }

    /*--------------------------------------------------
        SHOW NOTIFICATION PUBLIC
     -------------------------------------------------------*/
    public void showNotification(final int smallIcon,
                          final String title,
                          final String message) {

//        if (TextUtils.isEmpty(message))
//            return;

        mBuilder = new NotificationCompat.Builder(context.getApplicationContext(), NOTIFICATION_CHANNEL_ID);
        mBuilder.setSmallIcon(smallIcon);
        mBuilder.setAutoCancel(true);

        bigTextStyleNotification(title, message);
    }

    public boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }
}
