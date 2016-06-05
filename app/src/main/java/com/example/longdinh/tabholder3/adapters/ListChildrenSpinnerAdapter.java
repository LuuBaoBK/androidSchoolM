package com.example.longdinh.tabholder3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.models.ItemSpinner;
import java.util.List;


/**
 * Created by long dinh on 14/05/2016.
 */
public class ListChildrenSpinnerAdapter extends ArrayAdapter{
    public List<ItemSpinner> childrenList;
    private int resource;
    private LayoutInflater inflater;

    public ListChildrenSpinnerAdapter(Context context, int resource, List<ItemSpinner> objects) {
        super(context, resource, objects);
        this.childrenList = objects;
        this.resource = resource;
        inflater =(LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public View getCustomView(int position, View convertView, ViewGroup parent) {
        View layout = inflater.inflate(R.layout.items_children_pinner, parent, false);
        TextView tvName = (TextView) layout.findViewById(R.id.tvName);
        tvName.setText(childrenList.get(position).getTen());
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
