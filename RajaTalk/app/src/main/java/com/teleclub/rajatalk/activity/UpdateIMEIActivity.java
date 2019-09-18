package com.teleclub.rajatalk.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.taishi.flipprogressdialog.FlipProgressDialog;
import com.teleclub.rajatalk.R;
import com.teleclub.rajatalk.network.RajaTalkAPI;
import com.teleclub.rajatalk.network.RetrofitBuilder;
import com.teleclub.rajatalk.network.result.GeneralResult;
import com.teleclub.rajatalk.util.AppData;
import com.teleclub.rajatalk.util.Util;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class UpdateIMEIActivity extends AppCompatActivity {

    private EditText m_txtPhoneNumber;
    private FlipProgressDialog m_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_imei);
        findViewById(R.id.btn_reset_device).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateIMEI();
            }
        });

        setTitle(getResources().getString(R.string.reset_device));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        m_txtPhoneNumber = findViewById(R.id.txt_phone_number);
        m_txtPhoneNumber.setText(AppData.account.getPhone());
        m_txtPhoneNumber.setEnabled(false);

        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.icon_loading);

        m_indicator = new FlipProgressDialog();
        m_indicator.setImageList(imageList);

        m_indicator.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
        m_indicator.setCornerRadius(200);
        m_indicator.setOrientation("rotationY");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void updateIMEI() {
        String strPhoneNumber = m_txtPhoneNumber.getText().toString();
        if (strPhoneNumber.trim().isEmpty()) {
            return;
        }

        showIndicator(true);

        RetrofitBuilder.createRetrofitService(RajaTalkAPI.class).resetDevice(strPhoneNumber, Util.getUniqueIMEI(this), "android")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GeneralResult>() {
                    @Override
                    public void onCompleted() {
                        showIndicator(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        showIndicator(false);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_connect_server), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(GeneralResult generalResult) {
                        if (generalResult.getStatus()) {
                            finish();
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.success_update_imei), Toast.LENGTH_SHORT).show();
                        } else {
                            if (generalResult.getMessages().size() > 0) {
                                Toast.makeText(getApplicationContext(), generalResult.getMessages().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void showIndicator(boolean visible) {
        if (visible)
            m_indicator.show(getFragmentManager(), "");
        else
            m_indicator.dismiss();
    }

}