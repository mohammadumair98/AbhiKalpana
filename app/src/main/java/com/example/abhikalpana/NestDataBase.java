package com.example.abhikalpana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.InflateException;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class NestDataBase extends AppCompatActivity{


    /*Create tablayout with two tabs for kids and volunteers. It has an appbar on top and recyclerview listing the details will overlay
    * there's a viewpager inside which a frame layout is sitting. The frame layout will be replaced by the recyclerview fragment */



    TabLayout tabLayout;
    int tabposition, globalNest_no;
    String nest_captain;
    TextView appbarnametv;
    ViewPager viewPager;
    public static Context ct;
    final boolean attendance = false;
    public FrameLayout nestframelayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest_data_base);

        Log.v("TAG", "Entered NestDataBAse");


        /* Creating the layouts  */


        appbarnametv = (TextView) findViewById(R.id.appbarnametv);
        tabLayout = (TabLayout) findViewById(R.id.tab);
        viewPager=(ViewPager)findViewById(R.id.viewPager);
        nestframelayout = (FrameLayout) findViewById(R.id.nestframelayout);


        //---------------------------------------------------------------------------------------------------------------



        /* Creating two tabs for kids and volunteer  */

        globalNest_no = getIntent().getIntExtra("nest_no", 1);
        nest_captain = getIntent().getStringExtra("nest_captain");
        TabLayout.Tab childrentab = tabLayout.newTab();
        appbarnametv.setText("Nest " + globalNest_no);
        childrentab.setText("Children");
        TabLayout.Tab volunteertab = tabLayout.newTab();
        volunteertab.setText("Volunteers");
        tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        tabLayout.addTab(childrentab, 0);
        tabLayout.addTab(volunteertab, 1);
        Log.v("TAG", "Set Tab Layout");


        /*Instantiating a fragmentadapter because viewpager needs it along with tablayout    */


        final FragmentAdapter adapter = new FragmentAdapter(this,
                getSupportFragmentManager(),
                tabLayout.getTabCount(),
                attendance,
                globalNest_no,
                nest_captain);
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));   /*When tab is slid in app this detects it   */




        //When a tab is selected below functions takes action


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());                            //Jumps to FragmentAdapter.java class with current  selected tab
                Log.v("TAG", "OnTabSelected Triggered");

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//                Log.v("TAG", "OnTabSelected Triggered");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }

        });

    }



}