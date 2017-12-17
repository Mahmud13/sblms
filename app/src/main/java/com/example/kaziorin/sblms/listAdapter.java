package com.example.kaziorin.sblms;

/**
 * Created by Kazi Orin on 12/13/2017.
 */

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class listAdapter extends ArrayAdapter<String> {


    // private final Activity context;
    String[] item_name;
    String[] item_address;
    String[] item_time;
    String[] item_zone;
    Activity context;

    protected LayoutInflater inflater;
    protected int layout;


    public listAdapter(Activity context, String[] item_name, String[] item_zone, String[] item_address, String[] item_time) {
        super(context, R.layout.adapter_list, item_name);


        this.context=context;
        this.item_name=item_name;
        this.item_zone=item_zone;
        this.item_address=item_address;
        this.item_time=item_time;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.adapter_list, parent, false);
        TextView name, add, date, zone;
        name = (TextView) v.findViewById(R.id.LName);
        add = (TextView) v.findViewById(R.id.LAddress);
        date = (TextView) v.findViewById(R.id.DTime);
        zone = (TextView) v.findViewById(R.id.Lzone);

        name.setText(item_name[position]);
        add.setText(item_address[position]);
        date.setText(item_time[position]);
        zone.setText(item_zone[position]);
        return v;
    }

}

