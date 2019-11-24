package com.teleclub.rabbtel.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teleclub.rabbtel.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class VoipFragment extends Fragment {
    private TextView one,two,three, four, five,six,seven,eight,nine,zero,star,hash,dialvalue,clear,delete,status;
    private ImageView btn_call_2;

    public VoipFragment() {
        // Required empty public constructor
    }

    public static VoipFragment newInstance() {
        return new VoipFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
      View v =  inflater.inflate(R.layout.fragment_voip, container, false);
        initComponent(v);
        return v;
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
        btn_call_2 = v.findViewById(R.id.btn_call_2);

        clear = v.findViewById(R.id.clear);
        delete = v.findViewById(R.id.delete);
        status = v.findViewById(R.id.status);

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
                Log.i("deler",value+"text");
                dialvalue.setText(value);
            }
        });

        btn_call_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"You need to register to SIP server to make calls ", Toast.LENGTH_SHORT).show();
            }
        });

//        try
//        {
//            Thread.sleep(10000);
//            status.setText("Registration failed");
//        }
//        catch(InterruptedException ex)
//        {
//            Thread.currentThread().interrupt();
//        }


        clearall(dialvalue,clear);






    }
    void clearall(final TextView dialpad, TextView textView){
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialpad.setText("");
            }
        });

    }

    public String method(String str) {
        String result = null;
        if ((str != null) && (str.length() > 0)) {
          return str.substring(0, str.length() - 1);
        }
        else{
            return "";
        }
    }






}
