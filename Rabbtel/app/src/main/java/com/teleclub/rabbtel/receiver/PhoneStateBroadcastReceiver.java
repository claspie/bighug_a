package com.teleclub.rabbtel.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.teleclub.rabbtel.R;
import com.teleclub.rabbtel.listener.CustomPhoneStateListener;
import com.teleclub.rabbtel.network.RabbtelAPI;
import com.teleclub.rabbtel.network.RetrofitBuilder;
import com.teleclub.rabbtel.network.result.GeneralResult;
import com.teleclub.rabbtel.util.AppData;
import com.teleclub.rabbtel.util.Util;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PhoneStateBroadcastReceiver extends BroadcastReceiver {
    public static long start_time, end_time;

    @Override
    public void onReceive(Context context, Intent intent) {

        if (!AppData.called) return;

        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager.listen(new CustomPhoneStateListener(context), PhoneStateListener.LISTEN_CALL_STATE);

        String action = intent.getAction();
        if (action.equalsIgnoreCase("android.intent.action.PHONE_STATE")) {
            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                start_time = System.currentTimeMillis();
            }
//            if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(TelephonyManager.EXTRA_STATE_IDLE)) {
//                end_time = System.currentTimeMillis();
//                long total_time;
//                if (start_time == 0)
//                    total_time = 0;
//                else
//                    total_time = end_time - start_time;
//
//                if (total_time / 1000 >= 60 && !AppData.canTopup) {
//                    setTopupAvailable(context);
//                }
//            }
        }
    }

    private void setTopupAvailable(final Context context) {
        RetrofitBuilder.createRetrofitService(RabbtelAPI.class).setCanTopup(AppData.token, Util.getUniqueIMEI(context),
                Util.getSimCardNumber(context), "android")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GeneralResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(GeneralResult generalResult) {
                        if (generalResult.getStatus()) {
                            AppData.canTopup = true;
                        } else {
                            Toast.makeText(context, "failure to set topup", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}
