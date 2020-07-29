package com.example.abhikalpana;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class AddMemberr extends AppCompatActivity {

    View view;
    RadioButton childradioButoon;
    RadioGroup radioGroup;
    RadioButton memberradioButoon;
    EditText nameeditText;
    EditText ageeditText;
    EditText brancheditText;
    EditText classeditText;
    EditText schooleditText;
    EditText emaileditText;
    EditText nestnoeditText;
    TextView nestCaptaintv;
    CheckBox nestCaptaincheckBox;
    Button uploadimagebtn;
    Button addbtn;
    String name, branch, class_no, school, nestCaptain, userName;
    static String path;
    String dpUrl = null;
    int age = 0, nest_no = 0, globalNest_no;
    boolean imageUploaded;
    private static final int SELECT_PHOTO = 100;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference storageRef, imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_memberr);

        elementsInit();
        radiobuttonlistener();
    }

    void elementsInit() {

        childradioButoon = (RadioButton) findViewById(R.id.childradioButton);
        memberradioButoon = (RadioButton) findViewById(R.id.memberradioButton);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        nameeditText = (EditText) findViewById(R.id.nameeditText);
        ageeditText = (EditText) findViewById(R.id.ageedittext);
        brancheditText = (EditText) findViewById(R.id.branchedittext);
        classeditText = (EditText) findViewById(R.id.classedittext);
        schooleditText = (EditText) findViewById(R.id.schooledittext);
        uploadimagebtn = (Button) findViewById(R.id.uploadimagebtn);
        nestCaptaintv = (TextView) findViewById(R.id.nestCaptaintv);
        nestCaptaincheckBox = (CheckBox) findViewById(R.id.nestCaptaincheckBox);
        emaileditText = (EditText) findViewById(R.id.emailedittext);
        nestnoeditText = (EditText) findViewById(R.id.nestnoedittext);
        brancheditText.setVisibility(View.GONE);
        nestCaptaintv.setVisibility(View.GONE);
        nestCaptaincheckBox.setVisibility(View.GONE);
        emaileditText.setVisibility(View.GONE);
        addbtn = (Button) findViewById(R.id.addbtn);


        //accessing the firebase storage
        storage = FirebaseStorage.getInstance();

        //creates a storage reference
        storageRef = storage.getReference();

        globalNest_no = getIntent().getIntExtra("nest_no", 1);
    }

    void radiobuttonlistener() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (childradioButoon.isChecked()) {
                    childRadioButtonSelected();
                } else if (memberradioButoon.isChecked()) {
                    memberRadioButtonSelected();
                }
            }
        });

    }

    void childRadioButtonSelected() {

        brancheditText.setVisibility(View.GONE);
        nestCaptaintv.setVisibility(View.GONE);
        nestCaptaincheckBox.setVisibility(View.GONE);
        emaileditText.setVisibility(View.GONE);
        ageeditText.setVisibility(View.VISIBLE);
        classeditText.setVisibility(View.VISIBLE);
        schooleditText.setVisibility(View.VISIBLE);
        path = "Kids";
        uploadimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });
        Log.v("TAG", "Entered child radio button");
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameeditText.getText().toString();
                String ageCheck = ageeditText.getText().toString();
                age = Integer.parseInt(ageCheck);
                class_no = classeditText.getText().toString();
                school = schooleditText.getText().toString();
                String nest_noCheck = nestnoeditText.getText().toString();
                if (!imageUploaded || (ageCheck.length() == 0) || (nest_noCheck.length() == 0) || (name.length() == 0) || (class_no.length() == 0) || (school.length() == 0))
                    Toast.makeText(getApplicationContext(), "Enter all the details!", Toast.LENGTH_SHORT).show();
                else
                    userCheck(name, nest_noCheck,userName, v);
            }
        });

    }

    void memberRadioButtonSelected() {

        ageeditText.setVisibility(View.GONE);
        classeditText.setVisibility(View.GONE);
        schooleditText.setVisibility(View.GONE);
        brancheditText.setVisibility(View.VISIBLE);
        nestCaptaincheckBox.setVisibility(View.VISIBLE);
        emaileditText.setVisibility(View.VISIBLE);
        nestCaptaintv.setVisibility(View.VISIBLE);
        path = "Volunteers";
        uploadimagebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });
        Log.v("TAG", "Entered member radio button");
        addbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameeditText.getText().toString();
                branch = brancheditText.getText().toString();
                userName = emaileditText.getText().toString();
                nest_no = Integer.parseInt((nestnoeditText.getText().toString()));
                String nest_noCheck = nestnoeditText.getText().toString();
                if (nestCaptaincheckBox.isChecked())
                    nestCaptain = "true";
                else
                    nestCaptain = "false";
                if (!imageUploaded || (branch.length() == 0) || (nest_noCheck.length() == 0) || (name.length() == 0) || (userName.length() == 0))
                    Toast.makeText(getApplicationContext(), "Enter all the details!", Toast.LENGTH_SHORT).show();
                else
                    userCheck(name, nest_noCheck, userName, v);
            }
        });

    }

    void usernameCheck(final String userName, final View v) {
        if (userName.contains(".") || userName.contains("[") || userName.contains("]") || userName.contains("$") || userName.contains("#") || userName.contains("/"))
            Toast.makeText(getApplicationContext(), "Username taken or contains symbols or spaces!", Toast.LENGTH_SHORT).show();
        else {
            final DatabaseReference addmember = FirebaseDatabase.getInstance().getReference("Volunteers");
            addmember.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        if (dataSnapshot.child(userName).child("Email").exists())
                            Toast.makeText(getApplicationContext(), "Username taken or contains symbols or spaces!", Toast.LENGTH_SHORT).show();
                        else {
                            // this is a comment 2
                            uploadImage(v);
                            addmember.child(String.valueOf(nest_no)).child(name).child("Name").setValue(name);
                            addmember.child(String.valueOf(nest_no)).child(name).child("Branch").setValue(branch);
                            addmember.child(String.valueOf(nest_no)).child(name).child("Nest Captain").setValue(nestCaptain);
                            addmember.child(String.valueOf(nest_no)).child(name).child("Nest").setValue(nest_no);
                            addmember.child(String.valueOf(nest_no)).child(name).child("Email").setValue(userName);
                            final DatabaseReference memberAuth = FirebaseDatabase.getInstance().getReference("Volunteer Emails");
                            memberAuth.child(userName).child("Name").setValue(name);
                            memberAuth.child(userName).child("Nest Captain").setValue(nestCaptain);
                            memberAuth.child(userName).child("Nest").setValue(nest_no);
                            memberAuth.child(userName).child("Email").setValue(userName);
                            Toast.makeText(v.getContext() , "Member Added Successfully", Toast.LENGTH_SHORT).show();
                            imageUploaded = false;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    void userCheck(final String name, final String nest_noCheck, final String userName, final View v) {
        final DatabaseReference addmember = FirebaseDatabase.getInstance().getReference(path);
        addmember.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Log.v("Extracted namecheck", name);
                    Log.v("Nest no", nest_noCheck);
                    if (dataSnapshot.child(nest_noCheck).child(name).child("Name").exists())
                        Toast.makeText(getApplicationContext(), "Member already exists!", Toast.LENGTH_SHORT).show();
                    else if (path.equals("Volunteers"))
                        usernameCheck(userName, v);
                    else if (path.equals("Kids")){
                        nest_no = Integer.parseInt((nest_noCheck));
                        uploadImage(v);
                        addmember.child(String.valueOf(nest_no)).child(name).child("Name").setValue(name);
                        addmember.child(String.valueOf(nest_no)).child(name).child("Age").setValue(age);
                        addmember.child(String.valueOf(nest_no)).child(name).child("Class").setValue(class_no);
                        addmember.child(String.valueOf(nest_no)).child(name).child("School").setValue(school);
                        addmember.child(String.valueOf(nest_no)).child(name).child("Nest").setValue(nest_no);
                        imageUploaded = false;
                        Toast.makeText(v.getContext(), "Child Added Successfully", Toast.LENGTH_SHORT).show();
                    }
                } else
                    Log.v("TAG", "Else entered");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v("TAG", "Error");
            }
        });
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
                    Toast.makeText(getApplicationContext(), "Image selected, click on Add Member button", Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                    if (selectedImage.getLastPathSegment() == null)
                        imageUploaded = false;
                    else
                        imageUploaded = true;
                }
        }
    }

    public void uploadImage(final View view) {

        //create reference to images folder and assing a name to the file that will be uploaded
        imageRef = storageRef.child(path + "/" + name + "/" + name);

        //creating and showing progress dialog
        progressDialog = new ProgressDialog(AddMemberr.this);
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
                Toast.makeText(getApplicationContext(), "Error in uploading!", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();

            }
        });
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {

            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                imageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri downloadPhotoUrl) {
                        dpUrl = downloadPhotoUrl.toString();
                        Log.v("TAG", "upload url " + dpUrl);
                        final DatabaseReference addmember = FirebaseDatabase.getInstance().getReference(path);
                        addmember.child(String.valueOf(nest_no)).child(name).child("dpUrl").setValue(dpUrl);
                    }
                });
                Toast.makeText(getApplicationContext(), "Upload successful", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();


            }
        });


    }

}