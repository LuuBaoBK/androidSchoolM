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
import com.example.longdinh.tabholder3.models.StudentInClass;
import com.example.longdinh.tabholder3.models.TeacherInClass;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by long dinh on 13/05/2016.
 */
public class TeacherClassAdapter extends ArrayAdapter {
    public List<TeacherInClass> teacherClassList;
    private int resource;
    private LayoutInflater inflater;

    public TeacherClassAdapter(Context context, int resource, List<TeacherInClass> objects) {
        super(context, resource,  objects);
        this.teacherClassList = objects;
        this.resource = resource;
        inflater =(LayoutInflater)context.getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder ;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_teacher_subject, null);
            holder.ivAvatar = (CircleImageView) convertView.findViewById(R.id.ivAvatar);
            holder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            holder.tvSubject = (TextView) convertView.findViewById(R.id.tvSubject);
            holder.tvEmail = (TextView) convertView.findViewById(R.id.tvEmail);
            convertView.setTag(holder);
        }else{
            holder =(ViewHolder) convertView.getTag();
        }
        final ProgressBar progressBar = (ProgressBar) convertView.findViewById(R.id.progressBar);
//                ivMovie.setImageIcon(movieModelList.get(position).getImage());
        ImageLoader.getInstance().displayImage(teacherClassList.get(position).getImage(), holder.ivAvatar, new ImageLoadingListener() {
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

        System.out.println("test error:---" + holder.tvName + teacherClassList.get(position).getName());

        holder.tvName.setText(teacherClassList.get(position).getName());

        holder.tvSubject.setText(teacherClassList.get(position).getSubject());
        holder.tvEmail.setText(teacherClassList.get(position).getEmail());
        return convertView;
    }

    class ViewHolder{
        CircleImageView ivAvatar;
        TextView tvName;
        TextView tvSubject;
        TextView tvEmail;
    }
}
