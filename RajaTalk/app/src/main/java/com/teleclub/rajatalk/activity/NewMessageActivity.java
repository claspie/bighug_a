package com.teleclub.rajatalk.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.taishi.flipprogressdialog.FlipProgressDialog;
import com.teleclub.rajatalk.R;
import com.teleclub.rajatalk.util.AppData;
import com.teleclub.rajatalk.util.Constants;
import com.teleclub.rajatalk.util.Util;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NewMessageActivity extends AppCompatActivity {
    private EditText m_txtContact;
    private EditText m_txtBody;
    private Context m_Context;
    private FlipProgressDialog m_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        m_Context = this;
        initialize();
    }

    private void initialize() {
        setTitle(getResources().getString(R.string.new_sms));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        m_txtContact = findViewById(R.id.txt_contacts);
        m_txtBody = findViewById(R.id.txt_body);
        findViewById(R.id.btn_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContacts();
            }
        });
        findViewById(R.id.btn_send).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
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

    @Override
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);

        if (reqCode == Constants.REQUEST_PICK_CONTACT && resultCode == Activity.RESULT_OK) {
            Uri contactData = data.getData();
            String[] projection = { ContactsContract.CommonDataKinds.Phone.NUMBER,
                    ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

            String[] segments = contactData.toString().split("/");
            String id = segments[segments.length - 1];

            Cursor c = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    projection, null, null, null);

            c.moveToFirst();
            while (!c.isAfterLast())
            {
                int cid = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
                String contactId = c.getString(cid);

                if (contactId.equals(id)) {
                    // Retrieve the phone number from the NUMBER column
                    int column = c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String number = c.getString(column);
                    m_txtContact.setText(number);
                }

                c.moveToNext();
            }

            c.close();
        }
    }

    private void showContacts() {
        Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(intent, Constants.REQUEST_PICK_CONTACT);
    }

    private void sendMessage() {

    }

    private void showIndicator(boolean visible) {
        if (visible)
            m_indicator.show(getFragmentManager(), "");
        else
            m_indicator.dismiss();
    }
}
