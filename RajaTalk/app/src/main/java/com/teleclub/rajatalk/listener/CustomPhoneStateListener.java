package com.teleclub.rajatalk.listener;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.teleclub.rajatalk.R;
import com.teleclub.rajatalk.network.RajaTalkAPI;
import com.teleclub.rajatalk.network.RetrofitBuilder;
import com.teleclub.rajatalk.network.result.BlockAccountResult;
import com.teleclub.rajatalk.util.AppData;
import com.teleclub.rajatalk.util.Util;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CustomPhoneStateListener extends PhoneStateListener {
    private  Context context; //Context to make Toast if required

    public CustomPhoneStateListener(Context context) {
        super();
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        super.onCallStateChanged(state, incomingNumber);

        if (!AppData.called) return;

        switch (state) {
            case TelephonyManager.CALL_STATE_IDLE:
                blockUser();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK:
                unblockUser();
                break;
            case TelephonyManager.CALL_STATE_RINGING:
                break;
            default:
                break;
        }
    }

    private void blockUser() {
//        Toast.makeText(context, "Called Blocking API", Toast.LENGTH_SHORT).show();
        RetrofitBuilder.createRetrofitService(RajaTalkAPI.class).blockAccount(AppData.token, AppData.account.getPhone(),
                Util.getUniqueIMEI(this.context), "android")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BlockAccountResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(BlockAccountResult accountResult) {
//                        if (!accountResult.getStatus()) {
                            if (accountResult.getMessages().size() > 0) {
                                Toast.makeText(context, accountResult.getMessages().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                            }
//                        }
                    }
                });
        AppData.called = false;
    }

    private void unblockUser() {
//        Toast.makeText(context, "Called Un-Blocking API", Toast.LENGTH_SHORT).show();
        RetrofitBuilder.createRetrofitService(RajaTalkAPI.class).unblockAccount(AppData.token, AppData.account.getPhone(),
                Util.getUniqueIMEI(this.context), "android")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BlockAccountResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(BlockAccountResult accountResult) {
//                        if (!accountResult.getStatus()) {
                            if (accountResult.getMessages().size() > 0) {
                                Toast.makeText(context, accountResult.getMessages().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                            }
//                        }
                    }
                });
    }
}
