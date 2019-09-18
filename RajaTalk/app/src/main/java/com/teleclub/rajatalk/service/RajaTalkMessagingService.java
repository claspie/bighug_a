package com.teleclub.rajatalk.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.teleclub.rajatalk.R;
import com.teleclub.rajatalk.util.Constants;
import com.teleclub.rajatalk.util.NotificationUtils;

import org.json.JSONObject;

public class RajaTalkMessagingService extends FirebaseMessagingService {
    private final String TAG = RajaTalkMessagingService.class.getSimpleName();
    private NotificationUtils mNotificationUtils;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationUtils = new NotificationUtils(getApplicationContext());
    }

    @Override
    public void onNewToken(String token) {
        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences(Constants.RAJATALK_PREF, Context.MODE_PRIVATE).edit();
        editor.putString("push_token", token);
        editor.apply();
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage == null) return;

        if (remoteMessage.getNotification() != null) {
            Log.v(TAG, remoteMessage.getNotification().toString());
            handleNotificationPayloadMessage(remoteMessage.getNotification().getTitle(),
                    remoteMessage.getNotification().getBody());
        }

        /*...................................................
            Handle Data Payload
         ..................................................*/

        if (remoteMessage.getData() != null && remoteMessage.getData().size() > 0){
            try {
                JSONObject json = new JSONObject(remoteMessage.getData());
                handleDataPayloadMessage(json);
            } catch (Exception e) {
                Log.d(TAG, e.getMessage());
            }
        }
    }

    private void handleNotificationPayloadMessage(String title, String message){
        /* If app is in background, notification will be handled by fireBase itself.
        We have to show notification manually if app is in foreground. */

        if (!mNotificationUtils.isAppIsInBackground(getApplicationContext())){
            mNotificationUtils.showNotification(R.mipmap.ic_launcher, title, message);
        }
    }


    private void handleDataPayloadMessage(JSONObject notificationJson){
        String title = notificationJson.optString("title");
        String message = notificationJson.optString("message");
        final int smallIcon = R.mipmap.ic_launcher;

        mNotificationUtils.showNotification(smallIcon,
                title,
                message);
    }
}
