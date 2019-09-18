package com.teleclub.rajatalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.teleclub.rajatalk.R;
import com.teleclub.rajatalk.model.AccessNumber;
import com.teleclub.rajatalk.util.AppData;

import java.util.ArrayList;

public class AccessNumberAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<AccessNumber> accessNumbers;

    public AccessNumberAdapter(Context context, ArrayList<AccessNumber> items) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        accessNumbers = items;
    }

    @Override
    public int getCount() {
        return accessNumbers.size();
    }

    @Override
    public String getItem(int position) {
        return accessNumbers.get(position).getNumber();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();

        View rowView = inflater.inflate(R.layout.list_item_access_number, null);
        holder.txtLabel = rowView.findViewById(R.id.txt_label);
        holder.imgCheckMark = rowView.findViewById(R.id.image_check);

        AccessNumber accessNumber = accessNumbers.get(position);
        String itemText = accessNumber.getAddress() + ", " + accessNumber.getNumber();
        holder.txtLabel.setText(itemText);

        if (position == AppData.accessNumber)
            holder.imgCheckMark.setVisibility(View.VISIBLE);
        else
            holder.imgCheckMark.setVisibility(View.GONE);

        return rowView;
    }

    public void setItemCheckMark(int position) {
        AppData.accessNumber = position;
        notifyDataSetChanged();
    }

    public class Holder {
        TextView txtLabel;
        ImageView imgCheckMark;
    }
}
