package com.example.longdinh.tabholder3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.models.StudentItemSpinner;

import java.util.List;


/**
 * Created by long dinh on 14/05/2016.
 */
public class SpinnerAdapter extends ArrayAdapter{
    public List<String> listData;
    private int resource;
    private LayoutInflater inflater;

    public SpinnerAdapter(Context context, int resource, List<String> objects) {
        super(context, resource, objects);
        this.listData = objects;
        this.resource = resource;
        inflater =(LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View layout = inflater.inflate(R.layout.items_children_pinner, parent, false);
        TextView tvName = (TextView) layout.findViewById(R.id.tvName);// tam thoi cho dung chung
        tvName.setText(listData.get(position));
        return layout;
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }
}
