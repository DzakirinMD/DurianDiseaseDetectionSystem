package com.example.duriandiseasedetectionsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

public class DashboardActivity extends AppCompatActivity {

    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private ImageView duriandashboardimg, leafdashboardimg, diseasedashboardimg, cmdasboardimg, profiledashimg;
    private TextView duriandashboardtxt, leafdashboardtxt, diseasedashboardtxt, cmdasboardtxt, profiledashtxt;

    private Button scanBtn, usrListBtn, search_something;

    // kena import import androidx.appcompat.widget.Toolbar; sebab guna androidX
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Setup Custom toolbar
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Dashboard");

        mAuth = FirebaseAuth.getInstance();

        //ambik current user yg tengah login
        FirebaseUser mUser = mAuth.getCurrentUser();
        //amik user punya id
        String uID = mUser.getUid();
        String identifier = mUser.getEmail();

        //check id sapa tengah login sekarang
        System.out.println("/n/n Current user is : " + identifier);

        profiledashimg = findViewById(R.id.profiledashimg);
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

        //Click link Profile text or img
        profiledashimg.setOnClickListener(new View.OnClickListener() {
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
