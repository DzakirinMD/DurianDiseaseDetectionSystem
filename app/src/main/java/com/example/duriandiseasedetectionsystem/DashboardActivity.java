package com.example.duriandiseasedetectionsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duriandiseasedetectionsystem.model.Staff;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class DashboardActivity extends AppCompatActivity {

    //Firebase
    private DatabaseReference mDatabase,mProfileRef;
    private FirebaseAuth mAuth;

    private ImageView duriandashboardimg, leafdashboardimg, diseasedashboardimg, cmdasboardimg;
    private TextView duriandashboardtxt, leafdashboardtxt, diseasedashboardtxt, cmdasboardtxt, profiledashtxt;

    private Button scanBtn, usrListBtn, search_something;

    // kena import import androidx.appcompat.widget.Toolbar; sebab guna androidX
    private Toolbar toolbar;

    //Image variable
    private CircleImageView profileImage;
    private static final int PICK_IMAGE_REQUEST = 1;

    //uri to point to image and upload to firebase
    private Uri mImageUri, CropImageUri;

    private String mImageUrl;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Setup Custom toolbar
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        mAuth = FirebaseAuth.getInstance();

        //Connect to DB
        mProfileRef = FirebaseDatabase.getInstance().getReference();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        //ambik current user yg tengah login
        FirebaseUser mUser = mAuth.getCurrentUser();
        //amik user punya id
        String uID = mUser.getUid();
        String identifier = mUser.getEmail();

        //check id sapa tengah login sekarang
        System.out.println("/n/n Current user is : " + identifier);

        profileImage = findViewById(R.id.profilePicDash);
        profiledashtxt = findViewById(R.id.profiledashtxt);

        duriandashboardtxt = findViewById(R.id.duriandashtxt);
        duriandashboardimg = findViewById(R.id.duriandashimg);

        leafdashboardtxt = findViewById(R.id.leafdashtxt);
        leafdashboardimg = findViewById(R.id.leafdashimg);

        diseasedashboardtxt = findViewById(R.id.diseasedashboardtxt);
        diseasedashboardimg = findViewById(R.id.diseasedashboardimg);

        cmdasboardtxt = findViewById(R.id.cmdasboardtxt);
        cmdasboardimg = findViewById(R.id.cmdasboardimg);

        scanBtn = findViewById(R.id.scan_disease);
        usrListBtn = findViewById(R.id.user_list_btn);

        search_something = findViewById(R.id.search_something);

        mDatabase.child("Staff").child(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()){

                    Staff staff = dataSnapshot.getValue(Staff.class);

                    mImageUrl = staff.getImgurl();

                    System.out.println("Image uri " + mImageUrl);

                    if(mImageUrl != null){
                        Picasso.with(getApplicationContext()).load(mImageUrl).into(profileImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        //Click link Profile text or img
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StaffViewProfile.class));
            }
        });
        profiledashtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), StaffViewProfile.class));
            }
        });
        // END of Profile text or img

        //Click link DURIAN text or img
        duriandashboardtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DurianActivity.class));
            }
        });
        duriandashboardimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DurianActivity.class));
            }
        });
        // END of DURIAN text or img

        //Click link LEAF text or img
        leafdashboardtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LeafActivity.class));
            }
        });
        leafdashboardimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LeafActivity.class));
            }
        });
        // END of LEAFF text or img

        //Click link DISEASE text or img
        diseasedashboardtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DiseaseActivity.class));
            }
        });
        diseasedashboardimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DiseaseActivity.class));
            }
        });
        // END of DISEASE text or img

        //Click link CONTROL MEASURE text or img
        cmdasboardimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ControlMeasuresActivity.class));
            }
        });
        cmdasboardtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ControlMeasuresActivity.class));
            }
        });
        // END of CONTROL MEASURE text or img


        //Scan Disease button
        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), DetectorActivity.class));
            }
        });

        usrListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), UserListActivity.class));
            }
        });

        search_something.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), Search.class));
            }
        });

    } // END of Oncreate



    //Logo menu dekat section 12.. nak buat menu tepi tu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){

            case R.id.logout:
                //function untuk signed out
                mAuth.signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    //End of Main Menu


}
