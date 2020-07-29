package com.example.abhikalpana;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import static android.app.Activity.RESULT_OK;

public class AddMember extends Fragment{

        public AddMember() {
        // Required empty public constructor
    }

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
    String name, branch, class_no, school, nestCaptain,path,email;
    String dpUrl = null;
    int age,nest_no;
    private static final int SELECT_PHOTO = 100;
    Uri selectedImage;
    FirebaseStorage storage;
    StorageReference storageRef,imageRef;
    ProgressDialog progressDialog;
    UploadTask uploadTask;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_member, container, false);

        elementsInit();
        radiobuttonlistener();
        // Inflate the layout for this fragment

        return view;
    }

    void elementsInit() {

        childradioButoon = (RadioButton) view.findViewById(R.id.childradioButton);
        memberradioButoon = (RadioButton) view.findViewById(R.id.memberradioButton);
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        nameeditText = (EditText) view.findViewById(R.id.nameeditText);
        ageeditText = (EditText) view.findViewById(R.id.ageedittext);
        brancheditText = (EditText) view.findViewById(R.id.branchedittext);
        classeditText = (EditText) view.findViewById(R.id.classedittext);
        schooleditText = (EditText) view.findViewById(R.id.schooledittext);
        uploadimagebtn = (Button) view.findViewById(R.id.uploadimagebtn);
        nestCaptaintv = (TextView) view.findViewById(R.id.nestCaptaintv);
        nestCaptaincheckBox = (CheckBox) view.findViewById(R.id.nestCaptaincheckBox);
        emaileditText = (EditText) view.findViewById(R.id.emailedittext);
        nestnoeditText = (EditText) view.findViewById(R.id.nestnoedittext);
        brancheditText.setVisibility(View.GONE);
        nestCaptaintv.setVisibility(View.GONE);
        nestCaptaincheckBox.setVisibility(View.GONE);
        emaileditText.setVisibility(View.GONE);
        addbtn = (Button) view.findViewById(R.id.addbtn);


        //accessing the firebase storage
        storage = FirebaseStorage.getInstance();

        //creates a storage reference
        storageRef = storage.getReference();



    }

    void radiobuttonlistener() {

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (childradioButoon.isChecked()) {
                    childRadioButtonSelected();
                }

                else if (memberradioButoon.isChecked()) {
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
                age = Integer.parseInt((ageeditText.getText().toString()));
                class_no = classeditText.getText().toString();
                school = schooleditText.getText().toString();
                nest_no = Integer.parseInt((nestnoeditText.getText().toString()));
                uploadImage(v);
                final DatabaseReference addkids = FirebaseDatabase.getInstance().getReference("Kids");
                addkids.child(name).child("Name").setValue(name);
                addkids.child(name).child("Age").setValue(age);
                addkids.child(name).child("Class").setValue(class_no);
                addkids.child(name).child("School").setValue(school);
                addkids.child(name).child("Nest").setValue(nest_no);
                Toast.makeText(v.getContext(), "Child Added Successfully", Toast.LENGTH_SHORT).show();
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
                email = emaileditText.getText().toString();
                nest_no = Integer.parseInt((nestnoeditText.getText().toString()));
                if(nestCaptaincheckBox.isChecked())
                    nestCaptain = "true";
                else
                    nestCaptain = "false";
                uploadImage(v);
                final DatabaseReference addmember = FirebaseDatabase.getInstance().getReference("Volunteers");
                addmember.child(name).child("Name").setValue(name);
                addmember.child(name).child("Branch").setValue(branch);
                addmember.child(name).child("Nest Captain").setValue(nestCaptain);
                addmember.child(name).child("Nest").setValue(nest_no);
                addmember.child(name).child("Email").setValue(email);
                Toast.makeText(v.getContext(), "Member Added Successfully", Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(getContext(),"Image selected, click on Add Member button",Toast.LENGTH_SHORT).show();
                    selectedImage = imageReturnedIntent.getData();
                }
        }
    }

    public void uploadImage(View view) {

        //create reference to images folder and assing a name to the file that will be uploaded
        imageRef = storageRef.child(path + "/"+selectedImage.getLastPathSegment());

        //creating and showing progress dialog
        progressDialog = new ProgressDialog(getContext());
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
                Toast.makeText(getContext(),"Error in uploading!",Toast.LENGTH_SHORT).show();
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
                        addmember.child(name).child("dpUrl").setValue(dpUrl);
                    }
                });
                Toast.makeText(getContext(),"Upload successful",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();


            }
        });


    }

}
