package com.example.longdinh.tabholder3.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.models.ItemClassDate;

import java.util.List;

/**
 * Created by long dinh on 13/05/2016.
 */
public class ClassReceiveAdapter extends ArrayAdapter {
    public List<ItemClassDate> ClassList;
    private int resource;
    private LayoutInflater inflater;

    public ClassReceiveAdapter(Context context, int resource, List<ItemClassDate> objects) {
        super(context, resource,  objects);
        this.ClassList = objects;
        this.resource = resource;
        inflater =(LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_class_date, null);
            holder.classname = (TextView) convertView.findViewById(R.id.tvClassName);
            holder.date = (TextView) convertView.findViewById(R.id.tvDateReceive);
            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        holder.classname.setText(ClassList.get(position).getClassname());
        holder.date.setText(ClassList.get(position).getDate());

        return convertView;
    }

    class ViewHolder{
        TextView classname;
        TextView date;
    }
}
