package com.example.longdinh.tabholder3.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.models.EmailItem;

import java.util.List;

/**
 * Created by long dinh on 30/04/2016.
 */
public class EmailItemAdapter extends ArrayAdapter<EmailItem> {
    Context context;
    int resLayout;
    List<EmailItem> listNavItems;
    private SparseBooleanArray mSelectedItemsId;


    public EmailItemAdapter(Context context, int resLayout, List<EmailItem> listNavItems) {
        super(context, resLayout, listNavItems);

        this.context = context;
        this.resLayout = resLayout;
        this.listNavItems = listNavItems;
        mSelectedItemsId = new SparseBooleanArray();

    }

    @SuppressLint("ViewHolder") @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = View.inflate(context, R.layout.item_email, null);

        TextView tvStand  = (TextView) v.findViewById(R.id.tvStand);
        TextView tvSubject  = (TextView) v.findViewById(R.id.tvSubject);
        TextView tvPreview = (TextView) v.findViewById(R.id.tvPreview);
        TextView tvSender =  (TextView) v.findViewById(R.id.tvSender);
        TextView tvDate =  (TextView) v.findViewById(R.id.tvDate);

        EmailItem navItem = listNavItems.get(position);
        tvStand.setText(Character.toString(Character.toUpperCase(navItem.getSubject().charAt(0))));
        tvSubject.setText(navItem.getSubject());
        tvPreview.setText(navItem.getPreview());
        tvSender.setText(navItem.getSender());
        tvDate.setText(navItem.getDate());
        if(navItem.getIsRead())
            tvSubject.setTypeface(null, Typeface.BOLD);
        else
            tvSubject.setTypeface(null, Typeface.NORMAL);


        return v;
    }

    public void remove(EmailItem object) {
        listNavItems.remove(object);
        notifyDataSetChanged();
    }

    public List<EmailItem> getListNavItems() {
        return listNavItems;
    }

    public void toggleSelection(int position) {
        selectView(position, !mSelectedItemsId.get(position));
    }

    public void removeSelection() {
        mSelectedItemsId = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public void selectView(int position, boolean value) {
        if(value) {
            mSelectedItemsId.put(position, value);
        } else {
            mSelectedItemsId.delete(position);
        }
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsId.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsId;
    }

}
