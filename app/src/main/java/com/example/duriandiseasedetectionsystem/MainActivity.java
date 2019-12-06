package com.example.duriandiseasedetectionsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.duriandiseasedetectionsystem.FarmerActivity.FarmerDashboardActivity;
import com.example.duriandiseasedetectionsystem.model.Farmer;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private String farmerRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        FirebaseUser mUser = mAuth.getCurrentUser();
        System.out.println("Current user is : " + mUser);

        if (mUser != null){

            //******TO GET USER TYPE********
            mDatabase.child("Farmer").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        Farmer data = dataSnapshot.getValue(Farmer.class);
                        farmerRole = data.getFarmerRole();
                        System.out.println("Login Role : " + farmerRole);

                        Toast.makeText(MainActivity.this,"Retrieving Data",Toast.LENGTH_LONG).show();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (farmerRole.equalsIgnoreCase("admin")){

                                    //All file for farmer is inside FarmerActivity
                                    Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                                    intent.putExtra("farmerRole",farmerRole);
                                    startActivity(intent);

                                } else {

                                    //All file for farmer is inside FarmerActivity
                                    Intent intent = new Intent(getApplicationContext(), FarmerDashboardActivity.class);
                                    intent.putExtra("farmerRole",farmerRole);
                                    startActivity(intent);
                                }
                            }
                        }, 2500);


                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        } else {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        }



    } //END of oncreate

}
