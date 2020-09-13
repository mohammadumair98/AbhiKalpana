package com.example.abhikalpana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button testbtn;
    private String email;

    EditText usernameet;
    Button loginbtn;
    SharedPreferences sp;
    ProgressBar progressBar;
    String userName;
    String nameofUser;
    boolean logout, userFound;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp = getSharedPreferences("login",MODE_PRIVATE);
        usernameet = (EditText) findViewById(R.id.usernameet);
        loginbtn = (Button) findViewById(R.id.loginbtn);
        progressBar = findViewById(R.id.progressBar);


        testbtn = (Button) findViewById(R.id.testbtn);
        testbtn.setVisibility(View.GONE);
        testbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AddMemberr.class);
                startActivity(intent);
            }
        });


        logout = getIntent().getBooleanExtra("logout", false);
        if(logout) {
            sp.edit().putBoolean("logged", false).apply();
            sp.edit().putString("userName", "").apply();
            userName = "";
        }

        if(sp.getBoolean("logged",false)){
            progressBar.setVisibility(View.VISIBLE);
            findUser(sp.getString("userName", ""));
        }

        findViewById(R.id.loginbtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                userName = usernameet.getText().toString();
                findUser(userName);
            }
        });


    }



    private void findUser(final String userName) {
        final DatabaseReference memberdb = FirebaseDatabase.getInstance().getReference("Volunteer Emails");
        userFound = false;
        memberdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot npsnapshot : dataSnapshot.getChildren()) {
                        try {
                            String mail = npsnapshot.child("Email").getValue(String.class);
                            String name = userName;
                            Log.v("TAG", mail);
                            Log.v("TAG", userName);
                            String nest_captain = npsnapshot.child("Nest Captain").getValue(String.class);
                            Log.v("TAG", nest_captain);
                            if(userName.contains(mail)) {
                                logout = false;
                                userFound = true;
                                nameofUser = npsnapshot.child("Name").getValue(String.class);
                                sp.edit().putString("userName", userName).apply();
                                sp.edit().putBoolean("logged",true).apply();
                                progressBar.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), NestSelect.class);
                                intent.putExtra("nest_captain", nest_captain);
                                intent.putExtra("name", nameofUser);
                                startActivity(intent);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if(!userFound) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Log.v("TAG", "Firebase Else entered");
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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