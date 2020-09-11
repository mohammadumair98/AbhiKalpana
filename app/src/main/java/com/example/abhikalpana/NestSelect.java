package com.example.abhikalpana;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;

public class NestSelect extends AppCompatActivity {

    Button nest1btn;
    Button nest2btn;
    Button nest3btn;
    Button logoutbtn;
    String nameofUser;
    private String email, nest_captain;
    boolean logout = false;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nest_select);

        elementsinit();
        nest_captain = getIntent().getStringExtra("nest_captain");
        nameofUser = getIntent().getStringExtra("name");

        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout = true;
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("logout", logout);
                startActivity(intent);
            }
        });


        nest1btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(nest_captain.contains("true")) {
                    intent = new Intent(getApplicationContext(), CaptainDashboard.class);
                }
                else {
                    intent = new Intent(getApplicationContext(), NestDataBase.class);
                }
                intent.putExtra("nest_no", 1);
                intent.putExtra("nest_captain", nest_captain);
                intent.putExtra("name", nameofUser);
                startActivity(intent);
            }
        });

        nest2btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(nest_captain.contains("true")) {
                    intent = new Intent(getApplicationContext(), CaptainDashboard.class);
                }
                else {
                    intent = new Intent(getApplicationContext(), NestDataBase.class);
                }
                intent.putExtra("nest_no", 2);
                intent.putExtra("nest_captain", nest_captain);
                intent.putExtra("name", nameofUser);
                startActivity(intent);
            }
        });

        nest3btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(nest_captain.contains("true")) {
                    intent = new Intent(getApplicationContext(), CaptainDashboard.class);
                }
                else {
                    intent = new Intent(getApplicationContext(), NestDataBase.class);
                }
                intent.putExtra("nest_no", 3);
                intent.putExtra("nest_captain", nest_captain);
                intent.putExtra("name", nameofUser);
                startActivity(intent);
            }
        });
    }

    void elementsinit() {

        nest1btn = (Button) findViewById(R.id.nest1btn);
        nest2btn = (Button) findViewById(R.id.nest2btn);
        nest3btn = (Button) findViewById(R.id.nest3btn);
        logoutbtn = (Button) findViewById(R.id.logoutbtn);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            finishAffinity();
            finish();
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);
    }
}