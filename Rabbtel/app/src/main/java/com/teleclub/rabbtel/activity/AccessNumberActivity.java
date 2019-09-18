package com.teleclub.rabbtel.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.teleclub.rabbtel.R;
import com.teleclub.rabbtel.adapter.AccessNumberAdapter;
import com.teleclub.rabbtel.model.AccessNumber;
import com.teleclub.rabbtel.util.AppData;
import com.teleclub.rabbtel.util.Constants;

import java.util.ArrayList;

public class AccessNumberActivity extends AppCompatActivity {

    private AccessNumberAdapter m_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_access_number);
        initialize();
    }

    private void initialize() {
        setTitle(getResources().getString(R.string.access_number));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ListView listView = findViewById(R.id.list_access_number);

        ArrayList<AccessNumber> accessNumbers = new ArrayList<>();

        String[] addressArray = getResources().getStringArray(R.array.access_address_array);
        String[] numberArray = getResources().getStringArray(R.array.access_number_array);

        for (int i = 0; i < addressArray.length; i ++) {
            accessNumbers.add(new AccessNumber(addressArray[i], numberArray[i]));
        }

        m_adapter = new AccessNumberAdapter(this, accessNumbers);
        listView.setAdapter(m_adapter);

        m_adapter.setItemCheckMark(AppData.accessNumber);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                m_adapter.setItemCheckMark(position);

                SharedPreferences pref = getApplicationContext().getSharedPreferences(Constants.REBTTEL_PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putInt(Constants.PREF_LOCAL_ACCESS_NUMBER, position);
                editor.apply();

                finish();
            }
        });
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

}
