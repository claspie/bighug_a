package com.teleclub.telesms.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.teleclub.telesms.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CallHistoryFragment extends Fragment {

    public CallHistoryFragment() {
        // Required empty public constructor
    }

    public static CallHistoryFragment newInstance() {
        return new CallHistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_history, container, false);
        ListView listHistory = view.findViewById(R.id.list_history);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getContext() == null) return;
    }
}
