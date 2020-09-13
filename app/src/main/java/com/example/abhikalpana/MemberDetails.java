package com.example.abhikalpana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MemberDetails extends AppCompatActivity {


    ImageView imageView;
    TextView nametv;
    TextView branchtv;
    TextView agetv;
    TextView nesttv;
    TextView nestcaptaintv;
    TextView schooltv;
    TextView classtv;
    TextView lastattendedDateTV;
    Button laststudiedbtn;
    TextView lastcheckupDateTV;
    TextView lastcheckupTV;
    EditText logedittext;
    EditText nameet;
    EditText branchet;
    EditText ageet;
    EditText nestet;
    EditText nestcaptainet;
    EditText schoolet;
    EditText classet;
    EditText lastattendedDateET;
    EditText lastcheckupDateET;
    RelativeLayout classrelativelayout;
    RelativeLayout schoolrelativelayout;
    LinearLayout laststudiedrelativelayout;
    RelativeLayout lastcheckuprelativelayout;
    CheckBox cb_nestCaptain;
    Button updatebtn;
    Button uploadimagebtn;
    Button editbtn;
    Context context;
    boolean readStatus = false;
    volatile boolean dataFetched = false;
    boolean uploadbuttonPressed = false;
    int tabPosition;
    private static final int SELECT_PHOTO = 100;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;
    String name, class_no, school, last_attended_date, last_studied, last_checkup_date, dpurl, current_name, userName, email;
    String branch, nest_captain, nest_captainCheck = "true";
    static String path;
    int age, nest_no, globalNest_no, id;
    RequestQueue requestQueue;
    String jsonResponse = "";
    String url_post_data = "http://thantrajna.com/AbhiKalpana/insert_log.php";
    String nameofUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_details);
        requestQueue = Volley.newRequestQueue(this);
        tabPosition = getIntent().getIntExtra("tabPosition", 0);
        email = getIntent().getStringExtra("name");
        Log.v("TAG", "intent name: " + name);
        nameofUser = getIntent().getStringExtra("nameofUser");
        globalNest_no = getIntent().getIntExtra("nest_no", 1);
        nest_captainCheck = getIntent().getStringExtra("nest_captain");
        elementsinit();
        readMode();
        if(nest_captainCheck == null)
            editbtn.setVisibility(View.VISIBLE);
        else if (nest_captainCheck.equals("false"))
            editbtn.setVisibility(View.GONE);
        if (tabPosition == 0) {
            path = "Kids";
            childMode();
            getKidsDatafromFirebase();
            editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (readStatus) {
                        editMode();
                        setKidsData();
                    }
                    else
                        updateKidsData(v);
                    childMode();
                }
            });
            uploadimagebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage(v);
                }
            });
            updatebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    last_studied = logedittext.getText().toString();
                    if(last_studied.isEmpty());
                    else
                        insertlog();
                    logedittext.getText().clear();
                    setKidsData();
                }
            });
            laststudiedbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MemberDetails.this, StudyLog.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
        }

        if (tabPosition == 1) {
            path = "Volunteers";
            memberMode();
            getpathName();
            editbtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (readStatus) {
                        editMode();
                        setMembersData();
                    }
                    else
                        updateMembersData(v);
                    memberMode();
                }
            });
            uploadimagebtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectImage(v);
                }
            });
        }


    }

    void elementsinit() {
        imageView = (ImageView) findViewById(R.id.imageView);
        nametv = (TextView) findViewById(R.id.nametv);
        branchtv = (TextView) findViewById(R.id.branchtextView);
        agetv = (TextView) findViewById(R.id.agetextView);
        nesttv = (TextView) findViewById(R.id.nesttv);
        nestcaptaintv = (TextView) findViewById(R.id.nestCaptaintruefalsetv);
        schooltv = (TextView) findViewById(R.id.schooltextView);
        classtv = (TextView) findViewById(R.id.classtv);
        lastattendedDateTV = (TextView) findViewById(R.id.lastattendedtextview);
        laststudiedbtn = (Button) findViewById(R.id.laststudiedbtn);
        lastcheckupDateTV = (TextView) findViewById(R.id.checkuptextView);
        lastcheckupTV = (TextView) findViewById(R.id.checkuptextViewe);
        logedittext = (EditText) findViewById(R.id.logedittext);
        nameet = (EditText) findViewById(R.id.nameeditText);
        branchet = (EditText) findViewById(R.id.branchET);
        ageet = (EditText) findViewById(R.id.ageedittext);
        nestet = (EditText) findViewById(R.id.nestedittext);
        nestcaptainet = (EditText) findViewById(R.id.nestCaptaintruefalseedittext);
        schoolet = (EditText) findViewById(R.id.schooledittext);
        classet = (EditText) findViewById(R.id.classedittext);
        lastattendedDateET = (EditText) findViewById(R.id.lastattendededittext);
        lastcheckupDateET = (EditText) findViewById(R.id.checkupedittext);
        classrelativelayout = (RelativeLayout) findViewById(R.id.classrelativelayout);
        schoolrelativelayout = (RelativeLayout) findViewById(R.id.schoolrelativelayout);
        laststudiedrelativelayout = (LinearLayout) findViewById(R.id.laststudiedrelativelayout);
        lastcheckuprelativelayout = (RelativeLayout) findViewById(R.id.lastcheckuprelativelayout);
        cb_nestCaptain = findViewById(R.id.cb_nestCaptain);
        updatebtn = (Button) findViewById(R.id.updatebtn);
        uploadimagebtn = (Button) findViewById(R.id.uploadimagebtnofmemberdetails);
        editbtn = (Button) findViewById(R.id.editbtn);
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

    }

    void editMode() {

        nametv.setVisibility(View.GONE);
        branchtv.setVisibility(View.GONE);
        agetv.setVisibility(View.GONE);
        nesttv.setVisibility(View.GONE);
//        nestcaptaintv.setVisibility(View.GONE);
        schooltv.setVisibility(View.GONE);
        classtv.setVisibility(View.GONE);
        lastattendedDateTV.setVisibility(View.GONE);
        lastcheckupDateTV.setVisibility(View.GONE);

        nameet.setVisibility(View.VISIBLE);
        branchet.setVisibility(View.VISIBLE);
        ageet.setVisibility(View.VISIBLE);
        nestet.setVisibility(View.VISIBLE);
//        nestcaptainet.setVisibility(View.VISIBLE);
        cb_nestCaptain.setVisibility((View.VISIBLE));
        schoolet.setVisibility(View.VISIBLE);
        classet.setVisibility(View.VISIBLE);
        lastattendedDateET.setVisibility(View.VISIBLE);
        lastcheckupDateET.setVisibility(View.VISIBLE);
        imageView.setVisibility(View.VISIBLE);
        editbtn.setText("Save");
        updatebtn.setVisibility(View.GONE);
        uploadimagebtn.setVisibility(View.VISIBLE);
        readStatus = false;

    }

    void readMode() {

        nameet.setVisibility(View.GONE);
        branchet.setVisibility(View.GONE);
        ageet.setVisibility(View.GONE);
        nestet.setVisibility(View.GONE);
        nestcaptainet.setVisibility(View.GONE);
        schoolet.setVisibility(View.GONE);
        classet.setVisibility(View.GONE);
        lastattendedDateET.setVisibility(View.GONE);
        lastcheckupDateET.setVisibility(View.GONE);
        logedittext.setVisibility(View.VISIBLE);
        logedittext.getText().clear();
        cb_nestCaptain.setVisibility(View.GONE);

        nametv.setVisibility(View.VISIBLE);
        branchtv.setVisibility(View.VISIBLE);
        agetv.setVisibility(View.VISIBLE);
        nesttv.setVisibility(View.VISIBLE);
        nestcaptaintv.setVisibility(View.VISIBLE);
        schooltv.setVisibility(View.VISIBLE);
        laststudiedbtn.setVisibility(View.VISIBLE);
        classtv.setVisibility(View.VISIBLE);
        lastattendedDateTV.setVisibility(View.VISIBLE);
        lastcheckupTV.setVisibility(View.VISIBLE);
        editbtn.setText("Edit");
        updatebtn.setVisibility(View.VISIBLE);
        uploadimagebtn.setVisibility(View.GONE);
        readStatus = true;

    }

    void childMode() {

        if (readStatus) {
            nametv.setVisibility(View.VISIBLE);
            agetv.setVisibility(View.VISIBLE);
            classtv.setVisibility(View.VISIBLE);
            schooltv.setVisibility(View.VISIBLE);
            laststudiedrelativelayout.setVisibility(View.VISIBLE);
            laststudiedbtn.setVisibility(View.VISIBLE);
            lastcheckupTV.setVisibility(View.VISIBLE);
            lastcheckupDateTV.setVisibility(View.VISIBLE);

            branchtv.setVisibility(View.GONE);
            nestcaptaintv.setVisibility(View.GONE);
        } else {
            ageet.setVisibility(View.VISIBLE);
            classet.setVisibility(View.VISIBLE);
            schoolet.setVisibility(View.VISIBLE);
            laststudiedbtn.setVisibility(View.VISIBLE);
            logedittext.setVisibility(View.VISIBLE);
            lastcheckupDateET.setVisibility(View.VISIBLE);

            branchet.setVisibility(View.GONE);
            nestcaptainet.setVisibility(View.GONE);
        }

    }

    void memberMode() {
        updatebtn.setVisibility(View.GONE);
        if (readStatus) {
            nametv.setVisibility(View.VISIBLE);
            agetv.setVisibility(View.GONE);
            classtv.setVisibility(View.GONE);
            schooltv.setVisibility(View.GONE);
            laststudiedbtn.setVisibility(View.GONE);
            lastcheckupTV.setVisibility(View.GONE);
            lastcheckupDateTV.setVisibility(View.GONE);

            classrelativelayout.setVisibility(View.GONE);
            schoolrelativelayout.setVisibility(View.GONE);
            laststudiedrelativelayout.setVisibility(View.GONE);
            lastcheckuprelativelayout.setVisibility(View.GONE);

            branchtv.setVisibility(View.VISIBLE);
            nestcaptaintv.setVisibility(View.VISIBLE);
            nestcaptaintv.setText("Nest Captain");
        } else {
            ageet.setVisibility(View.GONE);
            classet.setVisibility(View.GONE);
            schoolet.setVisibility(View.GONE);
            logedittext.setVisibility(View.GONE);
            lastcheckupDateET.setVisibility(View.GONE);

            classrelativelayout.setVisibility(View.GONE);
            schoolrelativelayout.setVisibility(View.GONE);
            laststudiedrelativelayout.setVisibility(View.GONE);
            lastcheckuprelativelayout.setVisibility(View.GONE);

            branchet.setVisibility(View.VISIBLE);
//            nestcaptainet.setVisibility(View.VISIBLE);
            cb_nestCaptain.setVisibility(View.VISIBLE);
        }
    }

    void getKidsDatafromFirebase () {

        final DatabaseReference childrendb = FirebaseDatabase.getInstance().getReference("Kids");
        childrendb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    class_no = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("Class").getValue(String.class);
                    school = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("School").getValue(String.class);
                    nest_no = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("Nest").getValue(Integer.class);
                    age = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("Age").getValue(Integer.class);
                    last_attended_date = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("Last Attended").getValue(String.class);
                    last_studied = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("Last Studied").getValue(String.class);
                    last_checkup_date = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("Last Checkup").getValue(String.class);
                    dpurl = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("dpUrl").getValue(String.class);
                    id = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("id").getValue(Integer.class);
                    dataFetched = true;
                    setKidsData();
                    Log.v("TAG", "Data Received in Member details");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void getMembersDatafromFirebase () {

        final DatabaseReference memberdb = FirebaseDatabase.getInstance().getReference("Volunteers");
        memberdb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.v("TAG", name);
                    current_name = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("Name").getValue(String.class);
                    branch = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("Branch").getValue(String.class);
                    nest_captain = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("Nest Captain").getValue(String.class);
                    nest_no = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("Nest").getValue(Integer.class);
                    last_attended_date = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("Last Attended").getValue(String.class);
                    dpurl = dataSnapshot.child(String.valueOf(globalNest_no)).child(name).child("dpUrl").getValue(String.class);
                    dataFetched = true;
                    setMembersData();
                    Log.v("TAG", "Data Received in Member details");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    void getpathName() {
        final DatabaseReference memberAuth = FirebaseDatabase.getInstance().getReference("Volunteer Emails");
        memberAuth.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.child(email).child("Name").getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        getMembersDatafromFirebase();
    }

    void setKidsData() {
        if(readStatus) {
            nametv.setText(name);
            agetv.setText("Age: " + Integer.toString(age));
            nesttv.setText("Nest: " + Integer.toString(nest_no));
            classtv.setText("Class: " + class_no);
            schooltv.setText("School: " + school);
            Glide.with(getApplicationContext())
                    .load(dpurl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            if (last_attended_date != null) {
                lastattendedDateTV.setText(last_attended_date);
                lastcheckupDateTV.setText(last_checkup_date);
            } else {
                lastattendedDateTV.setText(" ");
                lastcheckupDateTV.setText(" ");
            }
        }

        if(!readStatus) {

            nameet.setText(name);
            ageet.setText(Integer.toString(age));
            nestet.setText(Integer.toString(nest_no));
            classet.setText(class_no);
            schoolet.setText(school);
            Glide.with(getApplicationContext())
                    .load(dpurl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            if (last_attended_date != null) {
                lastattendedDateET.setText(last_attended_date);
                lastcheckupDateET.setText(last_checkup_date);
            } else {
                lastattendedDateET.getText().clear();
                lastcheckupDateET.getText().clear();
            }

        }

    }

    void setMembersData() {
        if(readStatus ) {
            nametv.setText(current_name);
            nesttv.setText("Nest: " + Integer.toString(nest_no));
            if (nest_captain.equals("false"))
                nestcaptaintv.setText("Nest Captain: No");
            else
                nestcaptaintv.setText("Nest Captain: Yes");
            branchtv.setText("Branch: " + branch);
            Glide.with(getApplicationContext())
                    .load(dpurl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            if (last_attended_date != null)
                lastattendedDateTV.setText(last_attended_date);
            else
                lastattendedDateTV.setText(" ");
        }

        if(!readStatus ) {
            nameet.setText(current_name);
            nestet.setText(Integer.toString(nest_no));
            if (nest_captain.equals("false"))
                nestcaptainet.setText("No");
            else
                nestcaptainet.setText("Yes");
            branchet.setText(branch);
            Glide.with(getApplicationContext())
                    .load(dpurl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
            if (last_attended_date != null)
                lastattendedDateET.setText(last_attended_date);
            else
                lastattendedDateET.getText().clear();
        }

    }

    void updateKidsData(View v) {

        name = nameet.getText().toString();
        age = Integer.parseInt((ageet.getText().toString()));
        class_no = classet.getText().toString();
        school = schoolet.getText().toString();
        nest_no = Integer.parseInt((nestet.getText().toString()));
        last_attended_date = lastattendedDateET.getText().toString();
        last_studied = logedittext.getText().toString();
        last_checkup_date = lastcheckupDateET.getText().toString();
        final DatabaseReference updatekids = FirebaseDatabase.getInstance().getReference("Kids");
        updatekids.child(String.valueOf(globalNest_no)).child(name).child("Name").setValue(name);
        updatekids.child(String.valueOf(globalNest_no)).child(name).child("Age").setValue(age);
        updatekids.child(String.valueOf(globalNest_no)).child(name).child("Class").setValue(class_no);
        updatekids.child(String.valueOf(globalNest_no)).child(name).child("School").setValue(school);
        updatekids.child(String.valueOf(globalNest_no)).child(name).child("Nest").setValue(nest_no);
        updatekids.child(String.valueOf(globalNest_no)).child(name).child("Last Attended").setValue(last_attended_date);
        if(!(last_studied.contains("")))
            updatekids.child(String.valueOf(globalNest_no)).child(name).child("Last Studied").setValue(last_studied);
        updatekids.child(String.valueOf(globalNest_no)).child(name).child("Last Checkup").setValue(last_checkup_date);
        if(uploadbuttonPressed) {
            uploadImage(v);
            uploadbuttonPressed = false;
        }
        Toast.makeText(v.getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
        readMode();
        getKidsDatafromFirebase();
        setKidsData();
    }

    void updateMembersData(View v) {

        String new_name = nameet.getText().toString();
        String new_branch = branchet.getText().toString();
//        String nest_captain = nestcaptainet.getText().toString();
        int new_nest_no = Integer.parseInt((nestet.getText().toString()));
        String new_last_attended_date = lastattendedDateET.getText().toString();
        final DatabaseReference updatemembers = FirebaseDatabase.getInstance().getReference("Volunteers");
        updatemembers.child(String.valueOf(globalNest_no)).child(name).child("Name").setValue(new_name);
        updatemembers.child(String.valueOf(globalNest_no)).child(name).child("Branch").setValue(new_branch);
        if(cb_nestCaptain.isChecked()) {
            updatemembers.child(String.valueOf(globalNest_no)).child(name).child("Nest Captain").setValue("true");
            final DatabaseReference memberAuth = FirebaseDatabase.getInstance().getReference("Volunteer Emails");
            memberAuth.child(email).child("Nest Captain").setValue("true");
        }
        else {
            updatemembers.child(String.valueOf(globalNest_no)).child(name).child("Nest Captain").setValue("false");
            final DatabaseReference memberAuth = FirebaseDatabase.getInstance().getReference("Volunteer Emails");
            memberAuth.child(email).child("Nest Captain").setValue("false");
        }
        updatemembers.child(String.valueOf(globalNest_no)).child(name).child("Nest").setValue(new_nest_no);
        updatemembers.child(String.valueOf(globalNest_no)).child(name).child("Last Attended").setValue(new_last_attended_date);
        if(uploadbuttonPressed) {
            uploadImage(v);
            uploadbuttonPressed = false;
        }
        Toast.makeText(v.getContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
        readMode();
        getMembersDatafromFirebase();
    }


    public void selectImage(View view) {

        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch (requestCode) {
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(getApplicationContext(),"Image selected, click on Save to upload",Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                    uploadbuttonPressed = true;
                }
        }
    }

    public void uploadImage(final View view) {


        //create reference to images folder and assing a name to the file that will be uploaded
        imageRef = storageRef.child(path + "/" + name + "/" + name);

        //creating and showing progress dialog
        progressDialog = new ProgressDialog(MemberDetails.this);
        progressDialog.setMax(100);
        progressDialog.setMessage("Uploading...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.show();
        progressDialog.setCancelable(false);

        //starting upload
        uploadTask = imageRef.putFile(selectedImage);

        // Observe state change events such as progress, pause, and resume
        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                //sets and increments value of progressbar
                progressDialog.incrementProgressBy((int) progress);

            }
        });

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception exception) {

                // Handle unsuccessful uploads
                Toast.makeText(getApplicationContext(),"Error in uploading!",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadPhotoUrl) {
                        dpurl = downloadPhotoUrl.toString();
                        Log.v("TAG", "upload url " + dpurl);
                        final DatabaseReference updatemember = FirebaseDatabase.getInstance().getReference(path);
                        updatemember.child(String.valueOf(globalNest_no)).child(name).child("dpUrl").setValue(dpurl);
                        setKidsData();
                    }
                });
                Toast.makeText(getApplicationContext(),"Upload successful",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();


            }
        });


    }

    void insertlog() {
        try {
            volleyLogin();
            Toast.makeText(getApplicationContext(), "Updated Succesfully", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void volleyLogin() throws JSONException
    {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_post_data,
                new Response.Listener<String>()
                {
                    JSONObject res = null;
                    @Override
                    public void onResponse(String ServerResponse)
                    {
                        try {
                            res = new JSONObject(ServerResponse);

                            if(res.getString("error").length() == 0)
                            {
                                Toast.makeText(getApplicationContext(), "Thank you: "+res.getString("success"), Toast.LENGTH_LONG).show();
                            }

                            else
                            {
                                Toast.makeText(getApplicationContext(), "Sorry!! : "+res.getString("error"), Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (JSONException e)
                        {
                            Toast.makeText(getApplicationContext(), "Error JSON : ", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError volleyError)
                    {
                        Toast.makeText(getApplicationContext(), "ERROR_2 "+volleyError.toString(), Toast.LENGTH_LONG).show();
                    }
                })

        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> params = new HashMap<String, String>();

                params.put("id", String.valueOf(id));
                params.put("log", last_studied);
                params.put("incharge", nameofUser);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}