package com.example.longdinh.tabholder3.fragments;

/**
 * Created by long dinh on 12/04/2016.
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.HorizontalScrollView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;

import com.example.longdinh.tabholder3.R;
import com.example.longdinh.tabholder3.activities.MyApplication;
import com.example.longdinh.tabholder3.adapters.ListChildrenSpinnerAdapter;
import com.example.longdinh.tabholder3.adapters.MyFragmentPagerAdapter;
import com.example.longdinh.tabholder3.noticeday.NoticeT7;
import com.example.longdinh.tabholder3.noticeday.NoticeT3;
import com.example.longdinh.tabholder3.noticeday.NoticeT2;
import com.example.longdinh.tabholder3.models.ItemSpinner;
import com.example.longdinh.tabholder3.noticeday.NoticeT5;
import com.example.longdinh.tabholder3.noticeday.NoticeT6;
import com.example.longdinh.tabholder3.noticeday.NoticeT4;


public class NoticeBoardParent extends Fragment implements OnTabChangeListener,
        OnPageChangeListener {

    private TabHost tabHost;
    private ViewPager viewPager;
    private MyFragmentPagerAdapter myViewPagerAdapter;
    List<ItemSpinner> listChildren = new ArrayList<>();
    ListChildrenSpinnerAdapter adapter;
    private MyApplication app;
    int i = 0;
    String mahs = null;
    View v;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.tabs_viewpager_layout, container, false);
        Spinner spinner = (Spinner) v.findViewById(R.id.spChild);
        app = (MyApplication) getActivity().getApplication();
        app.setCurrentchild("nodata");
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position > 0){
                    app.setCurrentchild(listChildren.get(position).getMahs());
                    initializeViewPager();
                    v.post(new Runnable() {
                               @Override
                               public void run() {
                                   setPositionByDay();
                               }
                           }
                    );
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        listChildren = app.getListchildren();
        adapter = new ListChildrenSpinnerAdapter(getContext(), R.layout.items_children_pinner, listChildren);
        spinner.setAdapter(adapter);

        adapter.notifyDataSetChanged();
        this.initializeTabHost(savedInstanceState);
        this.initializeViewPager();
        v.post(new Runnable() {
                   @Override
                   public void run() {
                       setPositionByDay();
                   }
               }
        );
        return v;
    }


    // fake content for tabhost
    class FakeContent implements TabContentFactory {
        private final Context mContext;

        public FakeContent(Context context) {
            mContext = context;
        }

        @Override
        public View createTabContent(String tag) {
            View v = new View(mContext);
            v.setMinimumHeight(0);
            v.setMinimumWidth(0);
            return v;
        }
    }


    private void initializeViewPager() {
        List<Fragment> fragments = new Vector<Fragment>();


        fragments.add(new NoticeT2());
        fragments.add(new NoticeT3());
        fragments.add(new NoticeT4());
        fragments.add(new NoticeT5());
        fragments.add(new NoticeT6());
        fragments.add(new NoticeT7());

        this.myViewPagerAdapter = new MyFragmentPagerAdapter(
                getChildFragmentManager(), fragments);
        this.viewPager = (ViewPager) v.findViewById(R.id.viewPager);
        this.viewPager.setAdapter(this.myViewPagerAdapter);
        this.viewPager.addOnPageChangeListener(this);
    }

    private void initializeTabHost(Bundle args) {

        tabHost = (TabHost) v.findViewById(android.R.id.tabhost);
        tabHost.setup();

        TabHost.TabSpec tabSpec;
        tabSpec = tabHost.newTabSpec(" " + 1);
        tabSpec.setIndicator("Monday");
        tabSpec.setContent(new FakeContent(getActivity()));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(" " + 2);
        tabSpec.setIndicator("Tuesday");
        tabSpec.setContent(new FakeContent(getActivity()));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(" " + 3);
        tabSpec.setIndicator("Wednesday");
        tabSpec.setContent(new FakeContent(getActivity()));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(" " + 4);
        tabSpec.setIndicator("Thursday");
        tabSpec.setContent(new FakeContent(getActivity()));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(" " + 5);
        tabSpec.setIndicator("Friday");
        tabSpec.setContent(new FakeContent(getActivity()));
        tabHost.addTab(tabSpec);

        tabSpec = tabHost.newTabSpec(" " + 6);
        tabSpec.setIndicator("Saturday & Sunday");
        tabSpec.setContent(new FakeContent(getActivity()));
        tabHost.addTab(tabSpec);


        tabHost.setOnTabChangedListener(this);
    }


    void setPositionByDay(){
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case Calendar.MONDAY:
                setPositionByDay(0);
                break;
            case Calendar.TUESDAY:
                setPositionByDay(1);
                break;
            case Calendar.WEDNESDAY:
                setPositionByDay(2);
                break;
            case Calendar.THURSDAY:
                setPositionByDay(3);
                break;
            case Calendar.FRIDAY:
                setPositionByDay(4);
                break;
            case Calendar.SATURDAY :
            case Calendar.SUNDAY:
                setPositionByDay(5);
                break;
        }
    };

    void setPositionByDay(int day){
        this.tabHost.setCurrentTab(day);
        int pos = this.tabHost.getCurrentTab();
        this.viewPager.setCurrentItem(pos);
        HorizontalScrollView hScrollView = (HorizontalScrollView) v
                .findViewById(R.id.hScrollView);
        View tabView = tabHost.getCurrentTabView();
        int scrollPos = tabView.getLeft()
                - (hScrollView.getWidth() - tabView.getWidth()) / 2;

        hScrollView.smoothScrollTo(scrollPos, 0);
    };


    @Override
    public void onTabChanged(String tabId) {
        int pos = this.tabHost.getCurrentTab();

        for(int i=0;i<tabHost.getTabWidget().getChildCount();i++) {
            tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#ffffff")); //unselected
        }
        tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#b93221")); // selected

        this.viewPager.setCurrentItem(pos);
        HorizontalScrollView hScrollView = (HorizontalScrollView) v
                .findViewById(R.id.hScrollView);
        View tabView = tabHost.getCurrentTabView();
        int scrollPos = tabView.getLeft()
                - (hScrollView.getWidth() - tabView.getWidth()) / 2;
        hScrollView.smoothScrollTo(scrollPos, 0);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        this.tabHost.setCurrentTab(position);
    }
}
