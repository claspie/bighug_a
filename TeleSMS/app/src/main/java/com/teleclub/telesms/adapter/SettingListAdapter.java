package com.teleclub.telesms.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teleclub.telesms.R;
import com.teleclub.telesms.model.SettingItem;

import java.util.ArrayList;

public class SettingListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<SettingItem> settingItems;

    public SettingListAdapter(Context context, ArrayList<SettingItem> items) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        settingItems = items;
    }

    @Override
    public int getCount() {
        return settingItems.size();
    }

    @Override
    public SettingItem getItem(int position) {
        return settingItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();

        View rowView = inflater.inflate(R.layout.list_item_setting, null);
        holder.txtTitle = rowView.findViewById(R.id.txt_title);
        holder.txtValue = rowView.findViewById(R.id.txt_value);

        final SettingItem settingItem = settingItems.get(position);
        holder.txtTitle.setText(settingItem.getTitle());
        holder.txtValue.setText(settingItem.getValue());

        return rowView;
    }

    public void setItemValue(int position, String value) {
        SettingItem item = settingItems.get(position);
        item.setValue(value);
        settingItems.set(position, item);
        notifyDataSetChanged();
    }

    public class Holder {
        TextView txtTitle;
        TextView txtValue;
    }
}
