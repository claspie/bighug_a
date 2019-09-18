package com.teleclub.rajatalk.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.teleclub.rajatalk.R;
import com.teleclub.rajatalk.model.Message;
import com.teleclub.rajatalk.util.Constants;

import java.util.ArrayList;

public class MessageListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private Context m_context;
    private ArrayList<Message> m_Messages;

    public MessageListAdapter(Context context, ArrayList<Message> messages) {
        m_context = context;
        inflater = (LayoutInflater)m_context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        m_Messages = messages;
    }

    @Override
    public int getCount() {
        return m_Messages.size();
    }

    @Override
    public Message getItem(int position) {
        return m_Messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Holder holder = new Holder();

        View rowView = inflater.inflate(R.layout.list_item_message, null);
        holder.txtPhone = rowView.findViewById(R.id.txt_msg_number);
        holder.txtMsgType = rowView.findViewById(R.id.txt_msg_type);
        holder.txtMsgBody = rowView.findViewById(R.id.txt_msg_body);

        Message message = m_Messages.get(position);

        holder.txtPhone.setText(message.getPhone());

        if (message.getType() == Constants.MESSAGE_TYPE_SENT) {
            holder.txtMsgType.setText(m_context.getResources().getString(R.string.sent_sms));
        } else {
            holder.txtMsgType.setText(m_context.getResources().getString(R.string.received_sms));
        }

        holder.txtMsgBody.setText(message.getBody());

        return rowView;
    }

    public void setMessages(ArrayList<Message> messages) {
        m_Messages = messages;
        notifyDataSetChanged();
    }

    public class Holder {
        TextView txtPhone;
        TextView txtMsgType;
        TextView txtMsgBody;
    }
}
