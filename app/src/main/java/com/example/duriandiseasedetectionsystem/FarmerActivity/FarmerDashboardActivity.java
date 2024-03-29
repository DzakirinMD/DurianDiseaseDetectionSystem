package com.example.duriandiseasedetectionsystem.FarmerActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.duriandiseasedetectionsystem.DetectorActivity;
import com.example.duriandiseasedetectionsystem.LoginActivity;
import com.example.duriandiseasedetectionsystem.R;
import com.example.duriandiseasedetectionsystem.model.Farmer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class FarmerDashboardActivity extends AppCompatActivity {

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
    private String farmerRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_dashboard);

        Intent intent = getIntent();
        farmerRole = intent.getStringExtra("farmerRole");
        System.out.println("Current Role is : " + farmerRole);

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
        System.out.println("/n/n Current user in Farmer is: " + identifier);
        System.out.println("WELCOME TO FARMER DASHBOARD ACTIVITY");

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

        mDatabase.child("Farmer").child(uID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()){

                    Farmer farmer = dataSnapshot.getValue(Farmer.class);

                    mImageUrl = farmer.getFarmerImage();

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
                startActivity(new Intent(getApplicationContext(), FarmerActivity.class));
            }
        });
        profiledashtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FarmerActivity.class));
            }
        });
        // END of Profile text or img

        //Click link DURIAN text or img
        duriandashboardtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FarmerDurianActivity.class));
            }
        });
        duriandashboardimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FarmerDurianActivity.class));
            }
        });
        // END of DURIAN text or img

        //Click link LEAF text or img
        leafdashboardtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FarmerLeafActivity.class));
            }
        });
        leafdashboardimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FarmerLeafActivity.class));
            }
        });
        // END of LEAFF text or img

        //Click link DISEASE text or img
        diseasedashboardtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FarmerDiseaseActivity.class));
            }
        });
        diseasedashboardimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FarmerDiseaseActivity.class));
            }
        });
        // END of DISEASE text or img

        //Click link CONTROL MEASURE text or img
        cmdasboardimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FarmerControlMeasuresActivity.class));
            }
        });
        cmdasboardtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), FarmerControlMeasuresActivity.class));
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
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    //End of Main Menu
}
