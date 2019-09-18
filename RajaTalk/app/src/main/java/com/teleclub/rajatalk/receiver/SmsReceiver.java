package com.teleclub.rajatalk.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class SmsReceiver extends BroadcastReceiver {
    private int notificationId = 0;

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
//        if (bundle != null && bundle.getInt(PushConsts.CMD_ACTION) == PushConsts.GET_MSG_DATA) {
//            byte[] payload = bundle.getByteArray("payload");
//
//            if (payload != null) {
//                String notification = new String(payload);
//
//
//            }
//        }
    }
}
