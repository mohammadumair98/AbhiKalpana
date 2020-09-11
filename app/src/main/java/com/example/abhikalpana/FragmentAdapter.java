package com.example.abhikalpana;

import android.content.Context;
import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.appcompat.app.AppCompatActivity;

public class FragmentAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    boolean attendance = false;
    int globalNest_no;
    String nest_captain;
    String nameofUser;

    public FragmentAdapter(Context context, FragmentManager fm, int totalTabs) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        myContext = context;
        this.totalTabs = totalTabs;
        Log.v("TAG", "Entered FragmentAdapter");
    }

    public FragmentAdapter(Context context,
                           FragmentManager fm,
                           int totalTabs,
                           boolean attendance,
                           int globalNest_no,
                           String nest_captain,
                           String nameofUser) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        myContext = context;
        this.totalTabs = totalTabs;
        this.attendance = attendance;
        this.globalNest_no = globalNest_no;
        this.nest_captain = nest_captain;
        this.nameofUser = nameofUser;
        Log.v("TAG", "Entered FragmentAdapter");
    }

    /*Using switch case to check which tab is selected
    * Here Kids tab is 0 and volunteer tab is 1*/
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                /*If Kids tab is selected then instantiate the KidsRecyclerViewFragment class and execute it */
                KidsRecyclerViewFragment  kids= new KidsRecyclerViewFragment(myContext, position, attendance, globalNest_no, nest_captain, nameofUser);   //Sending context through myContext
                Log.v("TAG", "KidsRecyclerViewFragment switch case entered");
                return kids;

            case 1:
                /*If Volunteer tab is selected then instantiate the VolunteerRecyclerViewFragment class and execute it */
                VolunteerRecyclerViewFragment volunteer = new VolunteerRecyclerViewFragment(myContext, attendance, globalNest_no, nest_captain);             //Sending context through myContext
                Log.v("TAG", "VolunteerRecyclerViewFragment switch case entered");
                return volunteer;
            default:
                return null;
        }

    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}
