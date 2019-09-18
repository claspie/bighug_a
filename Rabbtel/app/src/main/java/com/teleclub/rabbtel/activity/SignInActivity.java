package com.teleclub.rabbtel.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.text.method.SingleLineTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import com.taishi.flipprogressdialog.FlipProgressDialog;
import com.teleclub.rabbtel.R;
import com.teleclub.rabbtel.network.RabbtelAPI;
import com.teleclub.rabbtel.network.RetrofitBuilder;
import com.teleclub.rabbtel.network.result.CanTopupResult;
import com.teleclub.rabbtel.network.result.LoginResult;
import com.teleclub.rabbtel.util.AppData;
import com.teleclub.rabbtel.util.Constants;
import com.teleclub.rabbtel.util.ResourceUtil;
import com.teleclub.rabbtel.util.Util;
import java.util.ArrayList;
import java.util.List;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SignInActivity extends AppCompatActivity {
    private EditText m_txtPhoneNumber;
    private EditText m_txtPassword;
    private Button m_btnSignIn;
//    private Switch m_switchIP;
    private FlipProgressDialog m_indicator;

    private String m_szPhoneNumber = "";
    private String m_szPassword = "";
    private boolean m_bRememberPassword = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initialize();
    }

    private void initialize() {
        setTitle(getResources().getString(R.string.sign_in));

        m_txtPhoneNumber = findViewById(R.id.txt_phone_number);
        m_txtPassword = findViewById(R.id.txt_password);
        m_btnSignIn = findViewById(R.id.btn_signin);
//        m_switchIP = findViewById(R.id.switch_ip);
        m_btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goSignUp();
            }
        });
        findViewById(R.id.btn_reset_password).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword();
            }
        });

        CheckBox chkRemember = findViewById(R.id.chk_remember);
        CheckBox chkShowPassword = findViewById(R.id.chk_show_password);
        chkRemember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkRememberPassword(isChecked);
            }
        });
        chkShowPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                checkShowPassword(isChecked);
            }
        });

        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.icon_loading);

//        m_switchIP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    Constants.BASE_URL = "http://204.244.129.38";
//                } else {
//                    Constants.BASE_URL = "http://bighug.ca";
//                }
//
//                AppData.useDevIp = isChecked;
//                SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.REBTTEL_PREF, Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = pref.edit();
//                editor.putBoolean(Constants.PREF_USE_DEVELOPMENT_IP, isChecked);
//                editor.apply();
//            }
//        });
//        m_switchIP.setChecked(AppData.useDevIp);

        m_indicator = new FlipProgressDialog();
        m_indicator.setImageList(imageList);

        m_indicator.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
        m_indicator.setCornerRadius(200);
        m_indicator.setOrientation("rotationY");

        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE}, Constants.REQUEST_APP_PERMISSION);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.REBTTEL_PREF, Context.MODE_PRIVATE);
        String userName = pref.getString(Constants.PREF_USERNAME, "");
        String password = pref.getString(Constants.PREF_PASSWORD, "");

        if (userName != null && !userName.isEmpty()) {
            m_txtPhoneNumber.setText(userName);
            m_txtPassword.setText(password);
            signIn();
        }
    }

    private void signIn() {
        m_szPhoneNumber = Util.getRealPhoneNumber(m_txtPhoneNumber.getText().toString());
        m_szPassword = m_txtPassword.getText().toString().trim();

        if (m_szPhoneNumber.isEmpty() || m_szPassword.isEmpty()) {
            Toast.makeText(this, getResources().getString(R.string.alert_fill_fields), Toast.LENGTH_SHORT).show();
            return;
        }

        m_btnSignIn.setEnabled(false);
        m_btnSignIn.setTextColor(getResources().getColor(R.color.colorLightGray));

        showIndicator(true);

        RetrofitBuilder.createRetrofitService(RabbtelAPI.class).login(m_szPhoneNumber, m_szPassword, Util.getUniqueIMEI(this),
                Util.getSimCardNumber(this), "android")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResult>() {
                    @Override
                    public void onCompleted() {
                        showIndicator(false);
                        m_btnSignIn.setEnabled(true);
                        m_btnSignIn.setTextColor(getResources().getColor(R.color.colorWhite));
                    }

                    @Override
                    public void onError(Throwable e) {
                        showIndicator(false);
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_connect_server), Toast.LENGTH_SHORT).show();
                        m_btnSignIn.setEnabled(true);
                        m_btnSignIn.setTextColor(getResources().getColor(R.color.colorWhite));
                    }

                    @Override
                    public void onNext(LoginResult loginResult) {
                        if (loginResult.getStatus()) {
                            AppData.token = loginResult.getToken();
                            AppData.account = loginResult.getAccount();
//                            checkTopupAvailable();
                            goMain();
                        } else {
                            if (loginResult.getMessages().size() > 0) {
                                Toast.makeText(getApplicationContext(), loginResult.getMessages().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void goSignUp() {
        Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    private void goMain() {
        Intent intent = new Intent(SignInActivity.this, MainActivity.class);
        intent.putExtra("auto_login", false);
        startActivity(intent);

        // save username and password
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.REBTTEL_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (m_bRememberPassword) {
            // save username and password
            editor.putString(Constants.PREF_USERNAME, m_szPhoneNumber);
            editor.putString(Constants.PREF_PASSWORD, m_szPassword);
        } else {
            editor.putString(Constants.PREF_USERNAME, "");
        }
        editor.apply();
        ResourceUtil.SetLoginDataToFile(m_szPhoneNumber, m_szPassword);
        finish();
    }

    private void checkTopupAvailable() {
        RetrofitBuilder.createRetrofitService(RabbtelAPI.class).getCanTopup(AppData.token, Util.getUniqueIMEI(this),
                Util.getSimCardNumber(this), "android")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<CanTopupResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(CanTopupResult canTopupResult) {
                        if (canTopupResult.getStatus()) {
                            AppData.canTopup = canTopupResult.getCan().equals("1");
                        }
                    }
                });
    }

    private void resetPassword() {
        Intent intent = new Intent(SignInActivity.this, ResetPasswordActivity.class);
        startActivity(intent);
    }

    private void showIndicator(boolean visible) {
        if (visible)
            m_indicator.show(getFragmentManager(), "");
        else
            m_indicator.dismiss();
    }

    private void checkRememberPassword(boolean isChecked) {
        m_bRememberPassword = isChecked;
    }

    private void checkShowPassword(boolean isChecked) {
        if (isChecked) {
            m_txtPassword.setTransformationMethod(SingleLineTransformationMethod.getInstance());
        } else {
            m_txtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }
}
