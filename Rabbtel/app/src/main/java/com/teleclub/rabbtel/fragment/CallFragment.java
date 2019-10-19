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

    private MainActivity m_parent;
    private String m_cardNumber = "";
    private TextView one,two,three, four, five,six,seven,eight,nine,zero,star,hash,dialvalue,clear,delete;



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

        initComponent(view);


        return view;
    }
//
//    void appendDial(final TextView dialpad, TextView textView, final String number){
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialpad.append(number);
//            }
//        });
//
//    }

    public String method(String str) {
        if (str != null && str.length() > 0 && str.charAt(str.length() - 1) == 'x') {
            str = str.substring(0, str.length() - 1);
        }
        return str;
    }

//    void deleteone(final TextView dialpad, TextView textView){
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//               String value =  method(dialpad.getText().toString());
//                dialpad.setText(value);
//            }
//        });
//
//    }

    void clearall(final TextView dialpad, TextView textView){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialpad.setText("");
            }
        });

    }





    void initComponent(View v){
        dialvalue = v.findViewById(R.id.dialvalue);

        one = v.findViewById(R.id.one);
        two = v.findViewById(R.id.two);
        three = v.findViewById(R.id.three);
        four = v.findViewById(R.id.four);
        five = v.findViewById(R.id.five);
        six = v.findViewById(R.id.six);
        seven = v.findViewById(R.id.seven);
        eight = v.findViewById(R.id.eight);
        nine = v.findViewById(R.id.nine);
        star = v.findViewById(R.id.star);
        zero = v.findViewById(R.id.zero);
        hash = v.findViewById(R.id.hash);

        clear = v.findViewById(R.id.clear);
        delete = v.findViewById(R.id.delete);

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialvalue.append("1");
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialvalue.append("2");
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialvalue.append("3");
            }
        });
        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialvalue.append("4");
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialvalue.append("5");
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialvalue.append("6");
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialvalue.append("7");
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialvalue.append("8");
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialvalue.append("9");
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialvalue.append("0");
            }
        });

        star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialvalue.append("*");
            }
        });

        hash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialvalue.append("#");
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String value =  method(dialvalue.getText().toString());
                dialvalue.setText(value);
            }
        });

//        appendDial(dialvalue, one,"1");
//        appendDial(dialvalue, two,"2");
//        appendDial(dialvalue, three,"3");
//        appendDial(dialvalue, four,"4");
//        appendDial(dialvalue, five,"5");
//        appendDial(dialvalue, six,"6");
//        appendDial(dialvalue, seven,"7");
//        appendDial(dialvalue, eight,"8");
//        appendDial(dialvalue, nine,"9");
//        appendDial(dialvalue, zero,"0");
//        appendDial(dialvalue, star,"*");
//        appendDial(dialvalue, hash,"#");
        clearall(dialvalue,clear);
      //  deleteone(dialvalue,delete);




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
