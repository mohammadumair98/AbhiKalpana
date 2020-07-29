package com.example.abhikalpana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;

public class AttendancePage extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    public static Context ct;
    FrameLayout attendanceframeLayout;
    final boolean attendance = true;
    int globalNest_no;
    String nest_captain = "true";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_page);

        viewPager=(ViewPager)findViewById(R.id.viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        attendanceframeLayout = (FrameLayout) findViewById(R.id.attendanceframelayout);
        TabLayout.Tab childrentab = tabLayout.newTab();
        childrentab.setText("Children");
        TabLayout.Tab volunteertab = tabLayout.newTab();
        volunteertab.setText("Volunteers");
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.addTab(childrentab, 0);
        tabLayout.addTab(volunteertab, 1);
        globalNest_no = getIntent().getIntExtra("nest_no", 1);
        final FragmentAdapter adapter = new FragmentAdapter(this, getSupportFragmentManager(), tabLayout.getTabCount(), attendance, globalNest_no, nest_captain);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());                            //Jumps to FragmentAdapter.java class with current  selected tab
                Log.v("TAG", "OnTabSelected Triggered");

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });
    }


}