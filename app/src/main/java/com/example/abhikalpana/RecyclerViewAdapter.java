package com.example.abhikalpana;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable {

    ArrayList<KidsData> kidsData;
    ArrayList<MembersData> membersData;
    ArrayList<KidsData> kidsSearchFull;
    ArrayList<MembersData> membersSearchFull;
    Context ct;
    int tabPosition, globalNest_no;
    String nest_captain;
    String nameofUser;
    boolean kid, volunteer = false, attendance, searchValueEntered = false;

    public RecyclerViewAdapter(Context c, ArrayList<KidsData> kidsData, boolean attendance, int globalNest_no, String nest_captain,
                               String nameofUser) {
        this.attendance = attendance;
        this.nest_captain = nest_captain;
        this.kidsData = kidsData;
        this.globalNest_no = globalNest_no;
        this.nameofUser = nameofUser;
        ct = c;
        kid = true;
        tabPosition = 0;
        kidsSearchFull = new ArrayList<>(kidsData);
    }

    public RecyclerViewAdapter(Context c, ArrayList<MembersData> membersData, boolean attendance, int globalNest_no,
                               String nest_captain,
                               int nothing) {
        ct = c;
        this.nest_captain = nest_captain;
        this.attendance = attendance;
        this.globalNest_no = globalNest_no;
        this.membersData = membersData;
        volunteer = true;
        tabPosition = 1;
        membersSearchFull = new ArrayList<>(membersData);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.v("TAG", "MyAdapter entered");
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_member_list_layout, parent, false);
        if(kid)
            kidsSearchFull = new ArrayList<>(kidsData);
        else if(volunteer)
            membersSearchFull = new ArrayList<>(membersData);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
            if (kid) {
                final KidsData currentKid = kidsData.get(position);
                holder.nametv.setText(currentKid.getName());
                final String name = currentKid.getName();
                holder.classtv.setText(currentKid.getClass_no());
                holder.agetv.setText(Integer.toString(currentKid.getAge()));
                Glide.with(ct)
                        .load(currentKid.getDpurl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.dpimage);
                holder.morebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(attendance){
                            String name = currentKid.getName();
                            final DatabaseReference updatekids = FirebaseDatabase.getInstance().getReference("Kids");
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String last_attended_date = sdf.format(new Date());
                            updatekids.child(String.valueOf(globalNest_no)).child(name).child("Last Attended").setValue(last_attended_date);
                            Toast.makeText(ct, "Attendance Updated", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.v("TAG", "Starting MemberDetails");
                            Intent intent = new Intent(ct, MemberDetails.class);
                            intent.putExtra("name", name);
                            intent.putExtra("nameofUser", nameofUser);
                            intent.putExtra("tabPosition", tabPosition);
                            intent.putExtra("nest_captain", nest_captain);
                            intent.putExtra("nest_no", globalNest_no);
                            ct.startActivity(intent);
                        }
                    }
                });
            }
            if (volunteer) {
                final MembersData currentMember = membersData.get(position);
                final String name = currentMember.getName();
                holder.nametv.setText(currentMember.getName());
                holder.branchtv.setText(currentMember.getBranch());
                holder.nesttv.setText(Integer.toString(currentMember.getNest()));
                Glide.with(ct)
                        .load(currentMember.getDpUrl())
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.dpimage);
                holder.morebtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(attendance){
                            String name = currentMember.getName();
                            final DatabaseReference updatekids = FirebaseDatabase.getInstance().getReference("Volunteers");
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            String last_attended_date = sdf.format(new Date());
                            updatekids.child(String.valueOf(globalNest_no)).child(name).child("Last Attended").setValue(last_attended_date);
                            Toast.makeText(ct, "Attendance Updated", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Log.v("TAG", "Starting MemberDetails");
                            Intent intent = new Intent(ct, MemberDetails.class);
                            intent.putExtra("name", name);
                            intent.putExtra("tabPosition", tabPosition);
//                            Log.v("TAG", nest_captain);
                            intent.putExtra("nest_captain", nest_captain);
                            intent.putExtra("nest_no", globalNest_no);
                            ct.startActivity(intent);
                        }
                    }
                });
            }


            Log.v("TAG", "MyAdapter Complete");

    }

    @Override
    public int getItemCount() {
        int size = 0;
        if(volunteer)
            size = membersData.size();
        if(kid)
            size = kidsData.size();
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView nametv;
        TextView agetv;
        TextView branchtv;
        TextView nesttv;
        TextView classtv;
        Button morebtn;
        Button updatebtn;
        ImageView dpimage;
        CheckBox checkBox;
        public MyViewHolder(@NonNull View v) {
            super(v);
                nametv = (TextView) v.findViewById(R.id.nametv);
                agetv = (TextView) v.findViewById(R.id.agetv);
                classtv = (TextView) v.findViewById(R.id.classtv);
                branchtv = (TextView) v.findViewById(R.id.branchtv);
                nesttv = (TextView) v.findViewById(R.id.nesttv);
                if (volunteer == true) {
                    agetv.setVisibility(View.GONE);
                    classtv.setVisibility(View.GONE);

                }
                if (kid == true) {
                    branchtv.setVisibility(View.GONE);
                    nesttv.setVisibility(View.GONE);
                }
                morebtn = (Button) v.findViewById(R.id.morebtn);
                dpimage = (ImageView) v.findViewById(R.id.dpimage);
                if(attendance) {
                    morebtn.setText("Update");
                }
                Log.v("TAG", "Elements Defined");


        }

    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            if(volunteer) {
                ArrayList<MembersData> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(membersSearchFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    Log.v("TAG", filterPattern);
                    for (MembersData item : membersSearchFull) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }
                results.values = filteredList;
            }

            else if(kid) {
                ArrayList<KidsData> filteredList = new ArrayList<>();
                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(kidsSearchFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();
                    Log.v("TAG", filterPattern);
                    for (KidsData item : kidsSearchFull) {
                        if (item.getName().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                            Log.v("TAG" , filteredList.get(0).getName());
                        }
                    }
                }
                results.values = filteredList;
            }
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(kid) {
                kidsData.clear();
                kidsData.addAll((List) results.values);
                notifyDataSetChanged();
            }

            else if(volunteer){
                membersData.clear();
                membersData.addAll((List) results.values);
                notifyDataSetChanged();
            }
        }
    };

}
