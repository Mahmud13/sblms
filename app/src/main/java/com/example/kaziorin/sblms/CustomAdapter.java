package com.example.kaziorin.sblms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Kazi Orin on 12/17/2017.
 */

public class CustomAdapter extends BaseAdapter {
    Context context;
    String[] Name,Pprice,Sprice,Com;
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, String[] Name, String[] Pprice,String[] Sprice,String[] Com) {
        this.context = applicationContext;
        this.Name = Name;
        this.Pprice = Pprice;
        this.Sprice = Sprice;
        this.Com =Com;
        inflter = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return Name.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.custom_spinner, null);
        TextView name = (TextView) view.findViewById(R.id.SpName);
        TextView pprice = (TextView) view.findViewById(R.id.SpPprice);
        TextView sprice = (TextView) view.findViewById(R.id.SpSprice);
        TextView comment = (TextView) view.findViewById(R.id.SpCom);
        name.setText(Name[i]);
        pprice.setText("Purchase Price : "+Pprice[i]);
        sprice.setText("Sale Price : "+Sprice[i]);
        comment.setText(Com[i]);
        return view;
    }
}

