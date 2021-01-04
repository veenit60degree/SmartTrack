package com.adapter.smarttrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.models.SubLocationModel;
import com.tracking.smarttrack.R;

import java.util.ArrayList;
import java.util.List;

public class LegTypeAdapter extends BaseAdapter {

    Context context;
    LayoutInflater mInflater;
    LayoutInflater inflater;
    List<SubLocationModel> legList;

    public LegTypeAdapter(Context context, List<SubLocationModel> legList) {
        this.context = context;
        this.legList = legList;
        mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return legList.size();
    }


    @Override
    public Object getItem(int arg0) {
        return legList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return arg0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_leg_type, null);

            holder.legTypeTV = (TextView) convertView.findViewById(R.id.legTypeTV);

            holder.legTypeTV.setText(legList.get(position).getLocationName());


        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        return convertView;
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }




    public class ViewHolder {
        TextView legTypeTV;
    }

}
