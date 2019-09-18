package com.teleclub.rabbtel.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;
import com.teleclub.rabbtel.R;
import com.teleclub.rabbtel.util.Constants;
import com.teleclub.rabbtel.util.ResourceUtil;
import com.teleclub.rabbtel.util.Util;

public class SplashActivity extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 1;
    private static String[] PERMISSIONS = {
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        verifyStoragePermissions(this);
    }

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission0 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int permission1 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET);
        int permission2 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE);
        int permission3 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CONTACTS);
        int permission4 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_WIFI_STATE);
        int permission5 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_PHONE_STATE);
        int permission6 = ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE);

        if (permission0 != PackageManager.PERMISSION_GRANTED
                || permission1 != PackageManager.PERMISSION_GRANTED
                || permission2 != PackageManager.PERMISSION_GRANTED
                || permission3 != PackageManager.PERMISSION_GRANTED
                || permission4 != PackageManager.PERMISSION_GRANTED
                || permission5 != PackageManager.PERMISSION_GRANTED
                || permission6 != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS,
                    REQUEST_PERMISSION
            );
        } else {
            Util.deleteCache(this);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startApp();
                }
            }, 3000);
        }
    }

    private void startApp() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.REBTTEL_PREF, Context.MODE_PRIVATE);
        String userName = pref.getString(Constants.PREF_USERNAME, "");
        String password = pref.getString(Constants.PREF_PASSWORD, "");
        if (userName.isEmpty()) {
            String[] loginData = ResourceUtil.GetLoginDataFromFile();
            userName = loginData[0];
            password = loginData[1];
            if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(password)) {
                SharedPreferences.Editor editor = pref.edit();
                editor.putString(Constants.PREF_USERNAME, userName);
                editor.putString(Constants.PREF_PASSWORD, password);
                editor.apply();
            }
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("auto_login", true);
            intent.putExtra("username", userName);
            intent.putExtra("password", password);
            startActivity(intent);
        }
        finish();
    }
}
