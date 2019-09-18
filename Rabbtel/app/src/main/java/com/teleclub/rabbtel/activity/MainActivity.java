package com.teleclub.rabbtel.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.taishi.flipprogressdialog.FlipProgressDialog;
import com.teleclub.rabbtel.R;
import com.teleclub.rabbtel.adapter.TabPagerAdapter;
import com.teleclub.rabbtel.model.ContactData;
import com.teleclub.rabbtel.network.RabbtelAPI;
import com.teleclub.rabbtel.network.RetrofitBuilder;
import com.teleclub.rabbtel.network.result.BalanceResult;
import com.teleclub.rabbtel.network.result.CanTopupResult;
import com.teleclub.rabbtel.network.result.GeneralResult;
import com.teleclub.rabbtel.network.result.LoginResult;
import com.teleclub.rabbtel.network.result.TokenResult;
import com.teleclub.rabbtel.util.AppData;
import com.teleclub.rabbtel.util.Constants;
import com.teleclub.rabbtel.util.Util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private Context m_Context;

    private TabPagerAdapter m_pagerAdapter = null;
    private int m_position = 0;
    private TextView m_txtBalance;
    private ViewPager m_viewPager = null;
    private FlipProgressDialog m_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        m_Context = this;
        initialize();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initialize() {
        if (AppData.account != null)
            setTitle();

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabTextColors(getResources().getColor(R.color.colorDarkWhite), getResources().getColor(R.color.colorWhite));

        m_viewPager = findViewById(R.id.view_pager);
        m_pagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        m_viewPager.setAdapter(m_pagerAdapter);
        m_viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                m_position = tab.getPosition();
                m_viewPager.setCurrentItem(tab.getPosition());
                m_pagerAdapter.refresh(m_position);
                hideKeyboard();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
        m_viewPager.setCurrentItem(1);

        m_viewPager.setEnabled(AppData.account != null);

        m_txtBalance = findViewById(R.id.txt_balance);

        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.icon_loading);

        m_indicator = new FlipProgressDialog();
        m_indicator.setImageList(imageList);

        m_indicator.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
        m_indicator.setCornerRadius(200);
        m_indicator.setOrientation("rotationY");

        Intent intent = getIntent();
        boolean isAutoLogin = intent.getBooleanExtra("auto_login", false);
        if (isAutoLogin) {
            login();
        } else {
            checkPermission();
            getBalance();
        }

        generateRootDirectory();
    }

    private void saveToken() {
        final SharedPreferences prefs = getSharedPreferences(Constants.REBTTEL_PREF, Context.MODE_PRIVATE);
        if (!prefs.contains("push_token"))
            return;

        String pushToken = prefs.getString("push_token", "");
        if (pushToken.isEmpty())
            return;

        RetrofitBuilder.createRetrofitService(RabbtelAPI.class).saveToken(AppData.account.getPhone(), "android", Util.getUniqueIMEI(getApplicationContext()), pushToken)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TokenResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(TokenResult result) {

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (m_position == 1 && AppData.accessNumber != -1) {
            String[] numberArray = getResources().getStringArray(R.array.access_number_array);
            m_pagerAdapter.setCardNumber(numberArray[AppData.accessNumber]);
        }

        if (AppData.accessNumber == -1) {
            openLocalAccessNumber();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options, menu);

        // return true so that the menu pop up is opened
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId())
        {
//            case R.id.notifications:
//                showNotifications();
//                break;
            case R.id.reset_device:
                showResetDevice();
                break;
            case R.id.clear_history:
                clearHistory();
                break;
            case R.id.settings:
                openSetting();
                break;
            case R.id.logout:
                finish();
//                logout();
                break;
        }

        return true;
    }

    public void setTitle() {
        String title = getResources().getString(R.string.app_name) + " (" + AppData.account.getPhone() + ")";
        setTitle(title);
    }

    private void showNotifications() {
        Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
        startActivity(intent);
    }

    private void showResetDevice() {
        Intent intent = new Intent(MainActivity.this, UpdateIMEIActivity.class);
        startActivity(intent);
    }

    private void clearHistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.alert_clear_history);
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppData.dbHelper.resetDatabase();
            }
        });
        builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    private void openAbout() {
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    private void openSetting() {
        Intent intent = new Intent(MainActivity.this, SettingActivity.class);
        startActivityForResult(intent, Constants.REQUEST_UPDATE_SETTING);
    }

    private void openLocalAccessNumber() {
        Intent intent = new Intent(MainActivity.this, AccessNumberActivity.class);
        startActivityForResult(intent, Constants.REQUEST_LOCAL_ACCESS_NUMBER);
    }

    private void login() {
        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        String password = intent.getStringExtra("password");

        showIndicator(true);
        RetrofitBuilder.createRetrofitService(RabbtelAPI.class).login(username, password, Util.getUniqueIMEI(this),
                Util.getSimCardNumber(this), "android")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<LoginResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_connect_server), Toast.LENGTH_SHORT).show();
                        showIndicator(false);
                        loginFailure();
                    }

                    @Override
                    public void onNext(LoginResult loginResult) {
                        showIndicator(false);
                        if (loginResult.getStatus()) {
                            AppData.token = loginResult.getToken();
                            AppData.account = loginResult.getAccount();
                            m_viewPager.setEnabled(AppData.account != null);
                            loginSuccess();
                        } else {
                            if (loginResult.getMessages().size() > 0) {
                                Toast.makeText(getApplicationContext(), loginResult.getMessages().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                            }

                            loginFailure();
                        }
                    }
                });
    }

    private void logout() {
        loginFailure();
    }

    private void loginSuccess() {
        getBalance();
//        checkTopupAvailable();
        checkPermission();
        setTitle();
        saveToken();
//        saveContacts();
    }

    private void loginFailure() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent);
        finish();
    }

    private void getBalance() {
        RetrofitBuilder.createRetrofitService(RabbtelAPI.class).getBalance(AppData.token, Util.getUniqueIMEI(this), "android")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BalanceResult>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(BalanceResult balanceResult) {
                        if (balanceResult.getStatus()) {
                            AppData.balance = balanceResult.getBalance();
                            updateBalance();
                        } else {
                            if (balanceResult.getMessages().size() > 0) {
                                Toast.makeText(getApplicationContext(), balanceResult.getMessages().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
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

    private void checkPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE,
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.READ_PHONE_STATE,
                Manifest.permission.CALL_PHONE}, Constants.REQUEST_APP_PERMISSION);
    }

    public void generateRootDirectory() {
        int permission = PermissionChecker.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission == PermissionChecker.PERMISSION_GRANTED) {
            File f = new File(Environment.getExternalStorageDirectory() + "/Rabbtel");
            if (!f.exists()) {
                f.mkdir();
            }
        } else {
            checkPermission();
        }
    }

    private void updateBalance() {
        String balance = "Balance  $" + Util.getDecimalString(AppData.balance, 2);
        m_txtBalance.setText(balance);
    }

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showIndicator(boolean visible) {
        if (visible)
            m_indicator.show(getFragmentManager(), "");
        else
            m_indicator.dismiss();
    }

    private void loadContacts() {
        AppData.contacts.clear();

        Uri uri = ContactsContract.Contacts.CONTENT_URI;
        Cursor contactsCursor = getContentResolver().query(uri, null, null, null,
                ContactsContract.Contacts.DISPLAY_NAME + " ASC ");

        if (contactsCursor.moveToFirst()) {
            do {
                long contactId = contactsCursor.getLong(contactsCursor.getColumnIndex("_ID"));
                Uri dataUri = ContactsContract.Data.CONTENT_URI;
                Cursor dataCursor = getContentResolver().query(dataUri, null,
                        ContactsContract.Data.CONTACT_ID + " = " + contactId, null, null);

                String displayName = "";
                String phoneNumber = "";
                String email = "";

                if (dataCursor.moveToFirst()) {
                    displayName = dataCursor.getString(dataCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

                    do {
                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)) {

                            // In this get All contact numbers like home,
                            // mobile, work, etc and add them to numbers string
                            switch (dataCursor.getInt(dataCursor.getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE:
                                    phoneNumber = dataCursor.getString(dataCursor
                                            .getColumnIndex("data1"));
                                    break;
                            }
                        }

                        String homeEmail = "", workEmail = "", mobileEmail = "", otherEmail = "";
                        if (dataCursor.getString(dataCursor.getColumnIndex("mimetype"))
                                .equals(ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)) {

                            // In this get all Emails like home, work etc and
                            // add them to email string
                            switch (dataCursor.getInt(dataCursor.getColumnIndex("data2"))) {
                                case ContactsContract.CommonDataKinds.Email.TYPE_HOME:
                                    homeEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                    break;
                                case ContactsContract.CommonDataKinds.Email.TYPE_WORK:
                                    workEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                    break;
                                case ContactsContract.CommonDataKinds.Email.TYPE_MOBILE:
                                    mobileEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                    break;
                                case ContactsContract.CommonDataKinds.Email.TYPE_OTHER:
                                    otherEmail = dataCursor.getString(dataCursor.getColumnIndex("data1"));
                                    break;
                            }
                        }

                        if (!homeEmail.isEmpty())
                            email = homeEmail;
                        else if (!workEmail.isEmpty())
                            email = workEmail;
                        else if (!mobileEmail.isEmpty())
                            email = mobileEmail;
                        else
                            email = otherEmail;

                        ContactData data = new ContactData();
                        data.setEmail(email);
                        data.setName(displayName);
                        data.setPhone(phoneNumber);

                        boolean repeat = false;
                        for (ContactData contact : AppData.contacts) {
                            if (contact.getName().equalsIgnoreCase(data.getName()) &&
                                    contact.getEmail().equalsIgnoreCase(data.getEmail()) &&
                                    contact.getPhone().equalsIgnoreCase(data.getPhone())) {
                                repeat = true;
                                break;
                            }
                        }

                        if (!repeat && !data.getPhone().isEmpty())
                            AppData.contacts.add(data);

                    } while (dataCursor.moveToNext());
                }
            } while (contactsCursor.moveToNext());
        }
    }

    public void saveContacts() {
        loadContacts();

        JSONArray array = new JSONArray();

        for (ContactData data : AppData.contacts) {
            Gson gson = new Gson();
            String json = gson.toJson(data);

            try {
                JSONObject object = new JSONObject(json);
                array.put(object);
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

        if (array.length() > 0) {
            RetrofitBuilder.createRetrofitService(RabbtelAPI.class).setContacts(AppData.token, array)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Observer<GeneralResult>() {
                        @Override
                        public void onCompleted() {
                        }

                        @Override
                        public void onError(Throwable e) {
                            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_connect_server), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(GeneralResult generalResult) {
                            if (generalResult.getStatus()) {

                            } else {
                                if (generalResult.getMessages().size() > 0) {
                                    Toast.makeText(getApplicationContext(), generalResult.getMessages().get(0).getMsg(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    });
        }
    }
}
