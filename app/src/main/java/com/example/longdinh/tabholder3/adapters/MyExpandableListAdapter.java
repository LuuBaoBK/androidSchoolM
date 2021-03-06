package com.example.longdinh.tabholder3.adapters;

/**
 * Created by long dinh on 21/04/2016.
 */
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.models.NavItem;
import com.example.longdinh.tabholder3.models.NavItemChild;

import java.util.HashMap;
import java.util.List;

/**
 * Created by long dinh on 21/04/2016.
 */
public class MyExpandableListAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private List<NavItem> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<NavItemChild>> _listDataChild;

    public MyExpandableListAdapter(Context context, List<NavItem> listDataHeader,
                                   HashMap<String, List<NavItemChild>> listChildData) {
        this._context = context;
        this._listDataHeader = listDataHeader;
        this._listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        List<NavItemChild> listchild = this._listDataChild.get(this._listDataHeader.get(groupPosition).getTitle());
        if(listchild != null)
            return listchild.get(childPosititon);
        else
            return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {



        final NavItemChild childItem = (NavItemChild) getChild(groupPosition, childPosition);

        if(childItem == null)
            return null;

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_nav_child_list, null);
        }

        TextView tvTitle  = (TextView) convertView.findViewById(R.id.title);
        ImageView navIcon =  (ImageView) convertView.findViewById(R.id.nav_icon);
        TextView tvNum =  (TextView) convertView.findViewById(R.id.tvNum);

        tvTitle.setText(childItem.getTitle());
        navIcon.setImageResource(childItem.getResIcon());
        tvNum.setText((childItem.getNum() <= 0)?"":(childItem.getNum()+""));

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<NavItemChild> listchild = this._listDataChild.get(this._listDataHeader.get(groupPosition).getTitle());
        if(listchild != null)
            return this._listDataChild.get(this._listDataHeader.get(groupPosition).getTitle())
                    .size();
        else
            return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {


        NavItem navItem  = (NavItem) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_nav_list, null);
        }

        TextView tvTitle  = (TextView) convertView.findViewById(R.id.title);
        ImageView navIcon =  (ImageView) convertView.findViewById(R.id.nav_icon);
        TextView tvNum =  (TextView) convertView.findViewById(R.id.tvNum);

        tvTitle.setText(navItem.getTitle());
        navIcon.setImageResource(navItem.getResIcon());
        tvNum.setText((navItem.getNum() <= 0) ? "" : (navItem.getNum() + ""));

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

