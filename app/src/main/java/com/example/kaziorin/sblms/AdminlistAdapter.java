package com.example.kaziorin.sblms;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdminlistAdapter extends ArrayAdapter<String> {


    // private final Activity context;
    String[] item_name;
    String[] item_address;
    String[] item_phone;
    String[] item_email;
    String[] item_type;
    Activity context;

    protected LayoutInflater inflater;
    protected int layout;


    public AdminlistAdapter(Activity context, String[] item_name, String[] item_phone, String[] item_address, String[] item_email,String[] item_type) {
        super(context, R.layout.adapter_list, item_name);


        this.context=context;
        this.item_name=item_name;
        this.item_phone=item_phone;
        this.item_address=item_address;
        this.item_email=item_email;
        this.item_type=item_type;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_adminlist_adapter, parent, false);
        TextView name, add, email, phone,type;
        name = (TextView) v.findViewById(R.id.AName);
        add = (TextView) v.findViewById(R.id.AAddress);
        email = (TextView) v.findViewById(R.id.Aemail);
        phone = (TextView) v.findViewById(R.id.Aphone);
        type = (TextView) v.findViewById(R.id.AType);

        name.setText(item_name[position]);
        add.setText(item_address[position]);
        email.setText(item_email[position]);
        phone.setText(item_phone[position]);
        type.setText(item_type[position]);

        return v;
    }

}