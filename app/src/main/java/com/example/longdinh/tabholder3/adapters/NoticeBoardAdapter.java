package com.example.longdinh.tabholder3.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.models.NoticeBoardItem;
import com.example.longdinh.tabholder3.models.StudentClass;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

/**
 * Created by long dinh on 13/05/2016.
 */
public class NoticeBoardAdapter extends ArrayAdapter {
    public List<NoticeBoardItem> noticeBoardItemList;
    private int resource;

    public NoticeBoardAdapter(Context context, int resource,List<NoticeBoardItem> objects) {
        super(context, resource,  objects);
        this.noticeBoardItemList = objects;
        this.resource = resource;
    }


    @Override
    public int getCount() {
        return noticeBoardItemList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(getContext(),resource , null);
            holder.tvSubject = (TextView) convertView.findViewById(R.id.tvSubject);
            holder.tvNotice = (TextView) convertView.findViewById(R.id.tvNotice);
            holder.tvLevel = (TextView) convertView.findViewById(R.id.tvLevel);
            holder.tvDeadline = (TextView) convertView.findViewById(R.id.tvDeadline);
            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        holder.tvSubject.setText(noticeBoardItemList.get(position).getSubject());
        holder.tvNotice.setText(noticeBoardItemList.get(position).getNotice());
        String level = noticeBoardItemList.get(position).getLevel();
        if(level.equals("1"))
            holder.tvLevel.setTextColor(Color.parseColor("#da4336"));
        else  if(level.equals("2"))
            holder.tvLevel.setTextColor(Color.parseColor("#f39c12"));
        else
            holder.tvLevel.setTextColor(Color.parseColor("#00a65a"));
        holder.tvLevel.setText(level);
        holder.tvDeadline.setText(noticeBoardItemList.get(position).getDeadline());

        return convertView;
    }

    class ViewHolder{
        TextView tvSubject;
        TextView tvNotice;
        TextView tvLevel;
        TextView tvDeadline;
    }
}
