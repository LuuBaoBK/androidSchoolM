package com.example.longdinh.tabholder3.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.longdinh.tabholder3.R;

/**
 * Created by long dinh on 05/05/2016.
 */
public class MyProfile extends Fragment{
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragmennt_admininfo, container, false);
        return v;
    }
}
