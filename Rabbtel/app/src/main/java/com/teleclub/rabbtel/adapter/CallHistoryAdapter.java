package com.teleclub.rabbtel.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.teleclub.rabbtel.R;
import com.teleclub.rabbtel.fragment.CallFragment;
import com.teleclub.rabbtel.model.CallRecord;
import com.teleclub.rabbtel.util.AppData;
import com.teleclub.rabbtel.util.Util;

import java.util.ArrayList;
import java.util.Date;

public class CallHistoryAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private CallFragment m_parent;
    private Context m_context;
    private ArrayList<CallRecord> m_historyItems;

    public CallHistoryAdapter(CallFragment fragment, Context context, ArrayList<CallRecord> items) {
        m_parent = fragment;
        m_context = context;
        inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_historyItems = items;
    }

    @Override
    public int getCount() {
        return m_historyItems.size();
    }

    @Override
    public CallRecord getItem(int position) {
        return m_historyItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();

        View rowView = inflater.inflate(R.layout.list_item_history, null);
        holder.txtPhone = rowView.findViewById(R.id.txt_phone_number);
        holder.txtTime = rowView.findViewById(R.id.txt_time);
        holder.btnCall = rowView.findViewById(R.id.btn_call);
        holder.btnDelete = rowView.findViewById(R.id.btn_delete);

        CallRecord history = m_historyItems.get(position);

        holder.txtPhone.setText(history.getPhoneNumber());
        String szDate = history.getCalledTime();
        String[] date = szDate.split("#");
        if (date.length == 2) {
            holder.txtTime.setText(date[0] + "     " + date[1]);
        }

        holder.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                call(position);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(position);
            }
        });

        return rowView;
    }

    public void setListData(ArrayList<CallRecord> data) {
        m_historyItems.clear();
        m_historyItems.addAll(data);
        notifyDataSetChanged();
    }

    private void call(int position) {
        CallRecord record = m_historyItems.get(position);
        m_parent.call(record.getPhoneNumber(), record.getAccessNumber());
        notifyDataSetChanged();
    }

    private void delete(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(m_context);
        alertDialog.setTitle("");
        alertDialog.setMessage("Are you sure you want to delete selected call history?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                AppData.dbHelper.removeCallRecord(m_historyItems.get(position).getId());
                m_historyItems.remove(position);
                notifyDataSetChanged();
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public class Holder {
        TextView txtPhone;
        TextView txtTime;
        ImageButton btnCall;
        ImageView btnDelete;
    }
}
