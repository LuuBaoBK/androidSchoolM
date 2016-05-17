package com.example.longdinh.tabholder3.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.models.StudentClass;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by long dinh on 13/05/2016.
 */
public class StudentClassAdapter extends ArrayAdapter {
    public List<StudentClass> studentClassList;
    private int resource;
    private LayoutInflater inflater;

    public StudentClassAdapter(Context context, int resource,List<StudentClass> objects) {
        super(context, resource,  objects);
        this.studentClassList = objects;
        this.resource = resource;
        inflater =(LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_student, null);
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
            holder.tvMaHs = (TextView) convertView.findViewById(R.id.tvMa);
            holder.tvTen = (TextView) convertView.findViewById(R.id.tvTen);
            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
//                ivMovie.setImageIcon(movieModelList.get(position).getImage());
        ImageLoader.getInstance().displayImage(studentClassList.get(position).getImage(), holder.ivAvatar, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
//                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {
//                progressBar.setVisibility(View.GONE);
            }
        }); // Default options will be used
        holder.tvTen.setText(studentClassList.get(position).getName());
        holder.tvMaHs.setText(studentClassList.get(position).getMaHs());

        return convertView;
    }

    class ViewHolder{
        CircleImageView ivAvatar;
        TextView tvTen;
        TextView tvMaHs;
    }
}
