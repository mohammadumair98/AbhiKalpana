package com.example.abhikalpana;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class VolunteerRecyclerViewFragment extends Fragment {

    View view;
    RecyclerView memberListView;
    Context context;
    SearchView searchView;
    RecyclerViewAdapter memberAdapter;
    boolean attendance;
    ArrayList<MembersData> membersData;
    int globalNest_no;
    String nest_captain;

    public VolunteerRecyclerViewFragment(Context context, boolean attendance, int globalNest_no, String nest_captain) {
        this.attendance = attendance;
        this.globalNest_no = globalNest_no;
        this.context = context;
        this.nest_captain = nest_captain;
    }

    public VolunteerRecyclerViewFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        membersData = new ArrayList<MembersData>();

        view = inflater.inflate(R.layout.fragment_member_recycler_view, container, false);
        searchView = (SearchView) view.findViewById(R.id.searchView);

        memberListView = (RecyclerView) view.findViewById(R.id.memberrv);
        memberAdapter = new RecyclerViewAdapter(context, membersData, attendance, globalNest_no,nest_captain, 0);
        memberListView.setAdapter(memberAdapter);
        memberListView.setLayoutManager(new LinearLayoutManager(context));

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                memberAdapter.getFilter().filter(newText);
                return false;
            }
        });

        final DatabaseReference memberdb = FirebaseDatabase.getInstance().getReference("Volunteers");
        memberdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.child(String.valueOf(globalNest_no)).getChildren()) {
                        try {
                            String name = npsnapshot.child("Name").getValue(String.class);
                            Log.v("TAG", name);
                            String branch = npsnapshot.child("Branch").getValue(String.class);
                            Integer nest = npsnapshot.child("Nest").getValue(Integer.class);
                            String dpUrl  = npsnapshot.child("dpUrl").getValue(String.class);
                            membersData.add(new MembersData(name, branch, dpUrl, nest));
                            memberAdapter.notifyDataSetChanged();
                            Log.v("TAG", "Data Received ");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } else
                    Log.v("TAG", "Firebase Else entered");

                Log.v("TAG", "VolunteerRecyclerViewFragment exited");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

}
