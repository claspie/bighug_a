package com.teleclub.rabbtel.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.taishi.flipprogressdialog.FlipProgressDialog;
import com.teleclub.rabbtel.R;
import com.teleclub.rabbtel.adapter.MessageListAdapter;
import com.teleclub.rabbtel.model.Message;
import com.teleclub.rabbtel.util.AppData;

import java.util.ArrayList;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    private ListView m_lstMessages = null;
    private MessageListAdapter m_listAdapter = null;
    private ArrayList<Message> m_messages = new ArrayList<>();
    private FlipProgressDialog m_indicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        initialize();
    }

    private void initialize() {
        setTitle(getResources().getString(R.string.messages));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        m_lstMessages = findViewById(R.id.lst_notifications);
        findViewById(R.id.btn_new_sms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNewMessage();
            }
        });
        m_listAdapter = new MessageListAdapter(this, m_messages);
        m_lstMessages.setAdapter(m_listAdapter);

        List<Integer> imageList = new ArrayList<>();
        imageList.add(R.drawable.icon_loading);

        m_indicator = new FlipProgressDialog();
        m_indicator.setImageList(imageList);

        m_indicator.setBackgroundColor(getResources().getColor(R.color.colorDisabled));
        m_indicator.setCornerRadius(200);
        m_indicator.setOrientation("rotationY");

        loadMessages();
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

    private void loadMessages() {
        showIndicator(true);
        m_listAdapter.setMessages(m_messages);
        showIndicator(false);
    }

    private void showNewMessage() {
        Intent intent = new Intent(NotificationActivity.this, NewMessageActivity.class);
        startActivity(intent);
    }

    private void showIndicator(boolean visible) {
        if (visible)
            m_indicator.show(getFragmentManager(), "");
        else
            m_indicator.dismiss();
    }
}
