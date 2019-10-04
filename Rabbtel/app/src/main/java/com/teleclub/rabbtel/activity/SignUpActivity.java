package com.teleclub.rabbtel.activity;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.taishi.flipprogressdialog.FlipProgressDialog;
import com.teleclub.rabbtel.R;
import com.teleclub.rabbtel.network.RabbtelAPI;
import com.teleclub.rabbtel.network.RetrofitBuilder;
import com.teleclub.rabbtel.network.result.SignUpResult;
import com.teleclub.rabbtel.util.Util;

import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignUpActivity extends AppCompatActivity {
    private EditText m_txtPhoneNumber;
    private Button m_btnSignUp;

    private FlipProgressDialog m_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        initialize();
    }

    private void initialize() {
        setTitle(getResources().getString(R.string.sign_up));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        findViewById(R.id.btn_signin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        m_txtPhoneNumber = findViewById(R.id.txt_phone_number);
        m_btnSignUp = findViewById(R.id.btn_signup);
        m_btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

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

    private void signIn() {
        Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
        startActivity(intent);
    }

    private void signUp() {
        String phoneNumber = Util.getRealPhoneNumber(m_txtPhoneNumber.getText().toString());

        if (phoneNumber.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.alert_fill_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        m_btnSignUp.setEnabled(false);
        m_btnSignUp.setTextColor(getResources().getColor(R.color.colorLightGray));

        showIndicator(true);

        RetrofitBuilder.createRetrofitService(RabbtelAPI.class).signup(phoneNumber, Util.getUniqueIMEI(this),
                Util.getSimCardNumber(this), "android")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<SignUpResult>() {
                    @Override
                    public void onCompleted() {
                        showIndicator(false);
                        m_btnSignUp.setEnabled(true);
                        m_btnSignUp.setTextColor(getResources().getColor(R.color.colorWhite));
                    }

                    @Override
                    public void onError(Throwable e) {
                        showIndicator(false);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_connect_server), Toast.LENGTH_SHORT).show();
                        m_btnSignUp.setEnabled(true);
                        m_btnSignUp.setTextColor(getResources().getColor(R.color.colorWhite));
                    }

                    @Override
                    public void onNext(SignUpResult signUpResult) {
                        if (signUpResult.getStatus()) {
                            if (signUpResult.getMessages().size() > 0) {
                                Toast.makeText(getApplicationContext(), signUpResult.getMessages().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            if (signUpResult.getMessages().size() > 0) {
                                Toast.makeText(getApplicationContext(), signUpResult.getMessages().get(0).getMsg(), Toast.LENGTH_SHORT).show();
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
