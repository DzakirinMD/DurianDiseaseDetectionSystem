package com.example.duriandiseasedetectionsystem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duriandiseasedetectionsystem.FarmerActivity.FarmerDashboardActivity;
import com.example.duriandiseasedetectionsystem.model.Farmer;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegistrationActivity extends AppCompatActivity {

    private TextView login_text;
    private EditText email,pass,name,address,notel;
    private ProgressBar mProgressBar;
    private Button btnReg;

    //Image variable
    private CircleImageView profileImage;
    private static final int PICK_IMAGE_REQUEST = 1;

    //uri to point to image and upload to firebase
    private Uri mImageUri, CropImageUri;

    //Firebase
    private FirebaseAuth mAuth;
    private DatabaseReference databaseFarmer;
    private StorageReference mStorageRef;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //initialize FirebaseStorage
        mStorageRef = FirebaseStorage.getInstance().getReference("Farmer");

        //initialize FirebaseDatabase
        databaseFarmer = FirebaseDatabase.getInstance().getReference("Farmer");

        mDialog = new ProgressDialog(this);

        //connect variable ngan interface
        email = findViewById(R.id.email_registration);
        name = findViewById(R.id.name_registration);
        address = findViewById(R.id.address_registration);
        notel= findViewById(R.id.notel_registration);
        pass = findViewById(R.id.password_registration);
        profileImage = findViewById(R.id.profilePic);
        btnReg = findViewById(R.id.registration_btn);
        login_text = findViewById(R.id.login_txt);
        mProgressBar = findViewById(R.id.progress_bar);


        //link ke MainActivity
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }); //END of login_text.setOnClickListener

        //choose image from phone
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
                //This will go to onActivityResult()
            }
        });

        //Button register activity
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
            }
        }); //END of btnReg.setOnClickListener
    } //END of Oncreate



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

            mImageUri = data.getData();//get image data

            //Crop Image ni kena declare kat AndroidManifest.xml under application
            CropImage.activity(mImageUri)//crop image based on preferences
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && data != null) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            CropImageUri = result.getUri();//to get url of image

            if (resultCode == RESULT_OK) {
                Picasso.with(getApplicationContext()).load(CropImageUri).into(profileImage);
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void Register() {
        //ambil data dari field
        final String mEmail = email.getText().toString().trim();
        final String mName = name.getText().toString().trim();
        final String mAddress = address.getText().toString().trim();
        final String mNotel = notel.getText().toString().trim();
        final String mPass = pass.getText().toString().trim();

        // Error Checking
        System.out.println(mEmail);
        System.out.println(mPass);

        if (TextUtils.isEmpty(mEmail)){
            email.setError("Email is required..");
            return;
        }

        if (TextUtils.isEmpty(mName)){
            name.setError("Name is required..");
            return;
        }

        if (TextUtils.isEmpty(mAddress)){
            address.setError("Address is required..");
            return;
        }

        if (TextUtils.isEmpty(mNotel)){
            notel.setError("notel is required..");
            return;
        }

        if (TextUtils.isEmpty(mPass)){
            pass.setError("Password is required..");
            return;
        }

        mDialog.setMessage("Processing..");
        mDialog.show();

        //Create new user
        mAuth.createUserWithEmailAndPassword(mEmail, mPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    createUser();

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(RegistrationActivity.this, "Registration failed. Please Check your Email",
                            Toast.LENGTH_SHORT).show();
                    mDialog.dismiss();
                }
            }
        });
    }//End of Register

    private void createUser() {

        //ambil data dari field
        final String mEmail = email.getText().toString().trim();
        final String mName = name.getText().toString().trim();
        final String mAddress = address.getText().toString().trim();
        final String mNotel = notel.getText().toString().trim();
        final String mPass = pass.getText().toString().trim();
        final String farmerRole = "user";

        //Get current user
        FirebaseUser user = mAuth.getCurrentUser();

        //get user ID
        String userID = user.getUid();

        //get user Image
        if (CropImageUri != null) {
            final StorageReference ref = mStorageRef.child("images/" + userID + "." + getFileExtension(mImageUri));

            ref.putFile(mImageUri)
                    // Inside OnSuccess this to create data
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //for progress bar loading
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                    mDialog.dismiss();
                                }
                            },500);
                            Toast.makeText(getApplicationContext(),"Upload Successfully", Toast.LENGTH_LONG).show();

                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl = uri;

                                    //Put user id into FarmerID and push all info into db
                                    Farmer data = new Farmer(userID,mName,mAddress,mNotel,mEmail,farmerRole,downloadUrl.toString(),mPass);
                                    databaseFarmer.child(userID).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(getApplicationContext(), "Successfuly Register", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(getApplicationContext(), FarmerDashboardActivity.class);
                                                intent.putExtra("farmerRole",farmerRole);
                                                startActivity(intent);
                                                finish();
                                                mDialog.dismiss();
                                            } else {
                                                Toast.makeText(RegistrationActivity.this, "User Create failed.", Toast.LENGTH_SHORT).show();
                                                mDialog.dismiss();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(),"No file selected", Toast.LENGTH_LONG).show();
                            mDialog.dismiss();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            //show progress bar progress
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                            mProgressBar.setProgress((int)progress);
                            mDialog.dismiss();
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(),"No file selected", Toast.LENGTH_LONG).show();
        }
    }
}
