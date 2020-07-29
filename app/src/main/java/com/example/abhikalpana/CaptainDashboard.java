package com.example.abhikalpana;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

public class CaptainDashboard extends AppCompatActivity {

    Button addmemberbtn;
    Button attendancebtn;
    Button databasebtn;
    int globalNest_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_captain_dashboard);

        globalNest_no = getIntent().getIntExtra("nest_no", 1);

        elementsInit();
        addmemberbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddMemberr.class);
                startActivity(intent);
            }
        });

        attendancebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AttendancePage.class);
                intent.putExtra("nest_no", globalNest_no);
                startActivity(intent);
            }
        });

        databasebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), NestDataBase.class);
                intent.putExtra("nest_no", globalNest_no);
                startActivity(intent);
            }
        });

    }


    void elementsInit() {

        addmemberbtn = (Button) findViewById(R.id.addmemberbtn);
        attendancebtn = (Button) findViewById(R.id.attendancebtn);
        databasebtn = (Button) findViewById(R.id.databasebtn);

    }

}