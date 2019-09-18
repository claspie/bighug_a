package com.teleclub.rajatalk;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.crashlytics.android.Crashlytics;
import com.teleclub.rajatalk.helper.DatabaseHelper;
import com.teleclub.rajatalk.model.CardInfo;
import com.teleclub.rajatalk.util.AppData;
import com.teleclub.rajatalk.util.Constants;

import io.fabric.sdk.android.Fabric;

public class RajaTalkApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(getApplicationContext(), new Crashlytics());
        loadPreferences();
    }

    private void loadPreferences() {
        AppData.cardInfo = new CardInfo();
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.RAJATALK_PREF, Context.MODE_PRIVATE);
        AppData.cardInfo.setCardName(pref.getString(Constants.PREF_CARD_NAME, ""));
        AppData.cardInfo.setCountryCode(pref.getString(Constants.PREF_COUNTRY_CODE, ""));
        AppData.cardInfo.setInternationalPrefix(pref.getString(Constants.PREF_INTERNATIONAL_PREFIX, "00"));
        AppData.cardInfo.setSeparatingCharacters(pref.getString(Constants.PREF_SEPARATING_CHARACTERS, "\""));
        AppData.cardInfo.setPinNumber(pref.getString(Constants.PREF_PIN_NUMBER, ""));
        AppData.cardInfo.setEndCharacter(pref.getString(Constants.PREF_END_CHARACTER, ""));

        AppData.accessNumber = pref.getInt(Constants.PREF_LOCAL_ACCESS_NUMBER, -1);
        AppData.callCount = pref.getInt(Constants.PREF_CALL_COUNT, 0);

        boolean hasDatabase = pref.getBoolean(Constants.PREF_DATABASE, false);
        AppData.dbHelper = new DatabaseHelper(getApplicationContext());
        if (!hasDatabase) {
            AppData.dbHelper.resetDatabase();

            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean(Constants.PREF_DATABASE, true);
            editor.apply();
        }
    }
}
