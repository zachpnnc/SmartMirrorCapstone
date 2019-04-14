package com.android.candz.smartmirrorcapstone;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomCalendarListAdapter extends BaseAdapter {
    private ArrayList<EventView> listData;
    private LayoutInflater layoutInflater;

    public CustomCalendarListAdapter(Context aContext, ArrayList<EventView> listData) {
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_row_layout, null);
            holder = new ViewHolder();

            holder.eventTitle = convertView.findViewById(R.id.event_title);
            holder.eventTime = convertView.findViewById(R.id.event_time);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.eventTitle.setText(listData.get(position).getTitle());
        holder.eventTime.setText(listData.get(position).getTime());

        return convertView;
    }

    static class ViewHolder {
        TextView eventTitle;
        TextView eventTime;
    }
}
