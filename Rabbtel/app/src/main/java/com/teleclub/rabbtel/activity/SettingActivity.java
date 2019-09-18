package com.teleclub.rabbtel.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.teleclub.rabbtel.R;
import com.teleclub.rabbtel.adapter.SettingListAdapter;
import com.teleclub.rabbtel.dialog.ItemDialog;
import com.teleclub.rabbtel.model.SettingItem;
import com.teleclub.rabbtel.util.AppData;
import com.teleclub.rabbtel.util.Constants;

import java.util.ArrayList;

public class SettingActivity extends AppCompatActivity implements ItemDialog.OnSetValueListener {

    private SettingListAdapter listAdapter;
    private int m_Position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initialize();
    }

    private void initialize() {
        setTitle(getResources().getString(R.string.option_settings));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initSettings();
    }

    private void initSettings() {
        ListView listView = findViewById(R.id.list_setting);

//        SettingItem item1 = new SettingItem(
//                getResources().getString(R.string.setting_card_no),
//                AppData.cardInfo.getCardName()
//        );
//        SettingItem item2 = new SettingItem(
//                getResources().getString(R.string.country_code),
//                AppData.cardInfo.getCountryCode()
//        );
        SettingItem item3 = new SettingItem(
                getResources().getString(R.string.access_number),
                AppData.cardInfo.getLocalAccessNumber()
        );
//        SettingItem item4 = new SettingItem(
//                getResources().getString(R.string.international_prefix),
//                AppData.cardInfo.getInternationalPrefix()
//        );
//        SettingItem item5 = new SettingItem(
//                getResources().getString(R.string.separating_characters),
//                AppData.cardInfo.getSeparatingCharacters()
//        );
//        SettingItem item6 = new SettingItem(
//                getResources().getString(R.string.pin_number),
//                AppData.cardInfo.getPinNumber()
//        );
//        SettingItem item7 = new SettingItem(
//                getResources().getString(R.string.end_character),
//                AppData.cardInfo.getEndCharacter()
//        );

        ArrayList<SettingItem> arrayList = new ArrayList<>();
//        arrayList.add(item1);
//        arrayList.add(item2);
        arrayList.add(item3);
//        arrayList.add(item4);
//        arrayList.add(item5);
//        arrayList.add(item6);
//        arrayList.add(item7);

        listAdapter = new SettingListAdapter(this, arrayList);
        listView.setAdapter(listAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m_Position = position;
                if (position == 0)
                    showLocalAccessNumberDialog();
                else if (position == 1)
                    showCountryCodeDialog();
                else if (position == 2)
                    showLocalAccessNumberDialog();
                else if (position == 3)
                    showInternationalPrefixDialog();
                else if (position == 4)
                    showSeparatingCharactersDialog();
                else if (position == 5)
                    showPinNumberDialog();
                else
                    showEndCharacterDialog();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        if (AppData.accessNumber != -1) {
            String[] addressArray = getResources().getStringArray(R.array.access_address_array);
            String[] numberArray = getResources().getStringArray(R.array.access_number_array);

            String strLocalAccessItem = addressArray[AppData.accessNumber] + ", " + numberArray[AppData.accessNumber];
            listAdapter.setItemValue(0, strLocalAccessItem);
        }
    }

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == Constants.REQUEST_LOCAL_ACCESS_NUMBER && resultCode == Activity.RESULT_OK) {
            String[] addressArray = getResources().getStringArray(R.array.access_address_array);
            String[] numberArray = getResources().getStringArray(R.array.access_number_array);

            String strLocalAccessItem = addressArray[AppData.accessNumber] + ", " + numberArray[AppData.accessNumber];
            listAdapter.setItemValue(2, strLocalAccessItem);
        }
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

    @Override
    public void onSetValue(String value) {
        listAdapter.setItemValue(m_Position, value);
        savePreference(String.valueOf(value));
    }

    private void showCardNoDialog() {
        ItemDialog itemDialog = new ItemDialog(this, getResources().getString(R.string.setting_card_no));
        itemDialog.setListener(this);
        itemDialog.show();
    }

    private void showCountryCodeDialog() {
    }

    private void showLocalAccessNumberDialog() {
        Intent intent = new Intent(SettingActivity.this, AccessNumberActivity.class);
        startActivityForResult(intent, Constants.REQUEST_LOCAL_ACCESS_NUMBER);
    }

    private void showInternationalPrefixDialog() {
        ItemDialog itemDialog = new ItemDialog(this, getResources().getString(R.string.international_prefix));
        itemDialog.setListener(this);
        itemDialog.show();
    }

    private void showSeparatingCharactersDialog() {
        ItemDialog itemDialog = new ItemDialog(this, getResources().getString(R.string.separating_characters));
        itemDialog.setListener(this);
        itemDialog.show();
    }

    private void showPinNumberDialog() {
        ItemDialog itemDialog = new ItemDialog(this, getResources().getString(R.string.pin_number));
        itemDialog.setListener(this);
        itemDialog.show();
    }

    private void showEndCharacterDialog() {
        ItemDialog itemDialog = new ItemDialog(this, getResources().getString(R.string.end_character));
        itemDialog.setListener(this);
        itemDialog.show();
    }

    private void savePreference(String value) {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.REBTTEL_PREF, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        switch (m_Position) {
            case 0:
                AppData.cardInfo.setCardName(value);
                editor.putString(Constants.PREF_CARD_NAME, value);
                break;
            case 1:
                AppData.cardInfo.setCountryCode(value);
                editor.putString(Constants.PREF_COUNTRY_CODE, value);
                break;
            case 2:
                break;
            case 3:
                AppData.cardInfo.setInternationalPrefix(value);
                editor.putString(Constants.PREF_INTERNATIONAL_PREFIX, value);
                break;
            case 4:
                AppData.cardInfo.setSeparatingCharacters(value);
                editor.putString(Constants.PREF_SEPARATING_CHARACTERS, value);
                break;
            case 5:
                AppData.cardInfo.setPinNumber(value);
                editor.putString(Constants.PREF_PIN_NUMBER, value);
                break;
            case 6:
                AppData.cardInfo.setEndCharacter(value);
                editor.putString(Constants.PREF_END_CHARACTER, value);
                break;
        }

        editor.apply();
    }
}
