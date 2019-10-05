package com.teleclub.rabbtel.fragment;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.teleclub.rabbtel.R;
import com.teleclub.rabbtel.activity.AccessNumberActivity;
import com.teleclub.rabbtel.activity.MainActivity;
import com.teleclub.rabbtel.adapter.CallHistoryAdapter;
import com.teleclub.rabbtel.model.CallRecord;
import com.teleclub.rabbtel.util.AppData;
import com.teleclub.rabbtel.util.Constants;
import com.teleclub.rabbtel.util.Util;

import java.util.ArrayList;
import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallFragment extends Fragment {
    private EditText m_txtContacts;
    private TextView m_txtCardNumber;
   // private ListView m_lvCallHistory;
   // private CallHistoryAdapter m_callHistoryAdapter;

    private MainActivity m_parent;
    private String m_cardNumber = "";

    public CallFragment() {
        // Required empty public constructor
    }

    public static CallFragment newInstance() {
        return new CallFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        m_parent = (MainActivity)getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call, container, false);
        view.findViewById(R.id.btn_contacts).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                startActivityForResult(intent, Constants.REQUEST_PICK_CONTACT);
            }
        });
        view.findViewById(R.id.btn_card_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AccessNumberActivity.class);
                startActivityForResult(intent, Constants.REQUEST_LOCAL_ACCESS_NUMBER);
            }
        });
        m_txtCardNumber = view.findViewById(R.id.txt_card_no);
        m_txtCardNumber.setText(m_cardNumber);
//        m_lvCallHistory = view.findViewById(R.id.lst_call_history);
        view.findViewById(R.id.btn_call).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callNumber();
            }
        });
        m_txtContacts = view.findViewById(R.id.txt_contacts);
        updateCallHistoryTable();

        return view;
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

            Cursor c = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
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
                    m_txtContacts.setText(number);
                }

                c.moveToNext();
            }

            c.close();
        } else if (reqCode == Constants.REQUEST_CALL) {

        }
    }

    public void setCardNumber(String cardNumber) {
        m_cardNumber = cardNumber;
        if (m_txtCardNumber != null)
            m_txtCardNumber.setText(cardNumber);
    }

    private void callNumber() {
        m_parent.generateRootDirectory();
        if (m_txtContacts.getText().toString().trim().isEmpty()) {
            Toast.makeText(getContext(), getResources().getString(R.string.empty_phone_number), Toast.LENGTH_SHORT).show();
        } else {
            String phoneNumber = m_txtContacts.getText().toString().trim();
            call(phoneNumber, m_txtCardNumber.getText().toString());
        }
    }

    public void call(String number, String accessNumber) {
        String strTelNumber = "tel:" + Util.getRealPhoneNumber(accessNumber) + "," +
                Util.getRealPhoneNumber(number);
        if (AppData.dbHelper.isExistRecord(number)) {
            AppData.dbHelper.updateTimeCallRecord(number, Util.getSqlDateString(new Date()));
        } else {
            AppData.dbHelper.addCallRecord(
                    number,
                    accessNumber,
                    Util.getSqlDateString(new Date()));
        }

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse(strTelNumber));
        startActivityForResult(callIntent, Constants.REQUEST_CALL);

        AppData.called = true;
        updateCallHistoryTable();
    }

    private void updateCallHistoryTable() {
        ArrayList<CallRecord> callRecords = AppData.dbHelper.getCallHistory();

      //  m_callHistoryAdapter = new CallHistoryAdapter(this, getContext(), callRecords);
      //  m_lvCallHistory.setAdapter(m_callHistoryAdapter);
    }
}
