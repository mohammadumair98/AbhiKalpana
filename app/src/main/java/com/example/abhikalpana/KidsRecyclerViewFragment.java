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
import android.widget.SearchView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class KidsRecyclerViewFragment extends Fragment {

    View view;
    int tabPosition, globalNest_no;
    RecyclerView memberListView;
    Context context;
    SearchView searchView;
    RecyclerViewAdapter memberAdapter;
    boolean attendance;
    ArrayList<KidsData> kidsData;
    String nest_captain;
    String nameofUser;

    public KidsRecyclerViewFragment(Context context, int tabposition, boolean attendance, int globalNest_no, String nest_captain,
                                    String nameofUser) {
        this.context = context;
        this.globalNest_no = globalNest_no;
        this.attendance = attendance;
        tabPosition = tabposition;
        this.nest_captain = nest_captain;
        this.nameofUser = nameofUser;
        Log.v("TAG", "Received tabpos in RecyclerView");
    }

    public KidsRecyclerViewFragment() {

    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("TAG", "KidsRecyclerViewFragment entered");

        kidsData = new ArrayList<KidsData>();
        view = inflater.inflate(R.layout.fragment_member_recycler_view, container, false);
        Log.v("TAG", "Reached");
        searchView = (SearchView) view.findViewById(R.id.searchView);
        Log.v("TAG", "Reached");
        memberListView = (RecyclerView) view.findViewById(R.id.memberrv);
        memberAdapter = new RecyclerViewAdapter(context, kidsData, attendance, globalNest_no, nest_captain, nameofUser );
        memberListView.setAdapter(memberAdapter);
        memberListView.setLayoutManager(new LinearLayoutManager(context));
        Log.v("TAG", "Reached");

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
        Log.v("TAG", "Reached");
        final DatabaseReference childrendb = FirebaseDatabase.getInstance().getReference("Kids");
        Log.v("TAG", "Reached");
        childrendb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.v("TAG", "Reached");
                if (dataSnapshot.exists()) {
                    Log.v("TAG", "Reached");
                    for (DataSnapshot npsnapshot : dataSnapshot.child(String.valueOf(globalNest_no)).getChildren()) {
                        try {
                            Log.v("TAG", "Entered Firebase");
                            String name = npsnapshot.child("Name").getValue(String.class);
                            Log.v("TAG", name);
                            String class_no = npsnapshot.child("Class").getValue(String.class);
                            Log.v("TAG", class_no);
                            Integer age = npsnapshot.child("Age").getValue(Integer.class);
                            String dpUrl  = npsnapshot.child("dpUrl").getValue(String.class);
                            kidsData.add(new KidsData(name, class_no, dpUrl, age));
                            memberAdapter.notifyDataSetChanged();
                            Log.v("TAG", "Data Received ");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }


                } else
                    Log.v("TAG", "Firebase Else entered");

                Log.v("TAG", "KidsRecyclerViewFragment exited");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;

    }




}