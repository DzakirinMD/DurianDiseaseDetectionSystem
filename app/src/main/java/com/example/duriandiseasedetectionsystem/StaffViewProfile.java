package com.example.duriandiseasedetectionsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duriandiseasedetectionsystem.model.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

public class StaffViewProfile extends AppCompatActivity {

    private TextView mstaffID, mstaffName, mstaffAddress, mstaffNoTel, mstaffEmail, mstaffPassword, muserType, mpatientType;
    private FirebaseAuth mAuth;
    private DatabaseReference mProfileRef, mProfileRefUpdate, mProfileViewUpdate;
    private CircleImageView profilePic;
    private String mImageUrl;
    private Button mEditButton;
    private Context context;

    //Update input field...
    private EditText updateSEmail;
    private EditText updateSPass;
    private EditText updateSNotel;
    private EditText updateSAdd;
    private Button btnDeleteUp;
    private Button btnUpdateUp;

    private String email, emailCurrent, passCurrent, pass, notel, add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_view_profile);

        mAuth = FirebaseAuth.getInstance();
        mProfileRef = FirebaseDatabase.getInstance().getReference();
        mProfileRefUpdate = FirebaseDatabase.getInstance().getReference().child("Staff");
        mProfileViewUpdate = FirebaseDatabase.getInstance().getReference();

        //ambik current user yg tengah login
        FirebaseUser mUser = mAuth.getCurrentUser();
        //amik user punya id/email
        final String uID = mUser.getUid();
        String identifier = mUser.getEmail();

        //check id sapa tengah login sekarang
        System.out.println("/n/n Current ID is : " + uID);
        System.out.println("/n/n Current user is : " + identifier);
        mProfileRef.child("Staff").child(mAuth.getUid());

        //link with xml
        mstaffEmail = findViewById(R.id.pEmail);
        mstaffPassword = findViewById(R.id.pPassword);
        mstaffName = findViewById(R.id.pName);
        mstaffNoTel = findViewById(R.id.pPhone);
        mstaffAddress = findViewById(R.id.pAddress);
        profilePic = findViewById(R.id.profiledashPic);

        mEditButton = findViewById(R.id.updateBtn);

        //Inflate updatedeleteinputfield_staff
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Edit Staff
                AlertDialog.Builder mydialog = new AlertDialog.Builder(StaffViewProfile.this);
                LayoutInflater inflater = LayoutInflater.from(StaffViewProfile.this);

                View myview = inflater.inflate(R.layout.updatedeleteinputfield_staff, null);
                mydialog.setView(myview);

                final AlertDialog dialog = mydialog.create();

                //Ni untuk update punya input field tarik
                mProfileRef.child("Staff").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChildren()){

                            Staff staff = dataSnapshot.getValue(Staff.class);

                            String pEmail = staff.getStaffEmail();
                            String pPassword = staff.getStaffPassword();
                            String pName = staff.getStaffName();
                            String pPhoneNo = staff.getStaffNoTel();
                            String pAddress = staff.getStaffAddress();

                            updateSEmail.setText(pEmail);
                            updateSPass.setText(pPassword);
                            updateSNotel.setText(pPhoneNo);
                            updateSAdd.setText(pAddress);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Failed to read value.", Toast.LENGTH_SHORT).show();
                    }
                });


                //collect data from field of updatedeleteinputfield
                updateSEmail = myview.findViewById(R.id.upd_staff_email);
                updateSPass = myview.findViewById(R.id.upd_staff_pass);
                updateSNotel = myview.findViewById(R.id.upd_staff_notel);
                updateSAdd = myview.findViewById(R.id.upd_staff_address);

                //instantiate button
                btnDeleteUp = myview.findViewById(R.id.btn_delete_upd);
                btnUpdateUp = myview.findViewById(R.id.btn_update_upd);

                //Update profile
                mProfileRef.child("Staff").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChildren()){

                            Staff staff = dataSnapshot.getValue(Staff.class);

                            final String pName = staff.getStaffName();
                            final String amImageUrl = staff.getImgurl();

                               btnUpdateUp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    //variable ni dari global variable
                                    emailCurrent = mstaffEmail.getText().toString().trim();
                                    passCurrent = mstaffPassword.getText().toString().trim();
                                    email = updateSEmail.getText().toString().trim();
                                    pass = updateSPass.getText().toString().trim();
                                    notel = updateSNotel.getText().toString().trim();
                                    add = updateSAdd.getText().toString().trim();

                                    System.out.println("Current User ID is : " + uID);
                                    System.out.println("Email is : " + email);
                                    System.out.println("New password :" + pass);
                                    System.out.println(notel);
                                    System.out.println(add);

                                    System.out.println("Current email is : " + emailCurrent);
                                    System.out.println("Current password is : " + passCurrent);

                                    //Call model constructor
                                    Staff data = new Staff(uID,pName,add,notel,email,pass,amImageUrl);

                                    //masuk kan value dalam id tu (position dy)
                                    mProfileRefUpdate.child(uID).setValue(data);


                                    //Update Password
                                    FirebaseUser updPass = FirebaseAuth.getInstance().getCurrentUser();
                                    String newPassword = pass;

                                    updPass.updatePassword(newPassword)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        System.out.println("Update password jadi");
                                                    }
                                                }
                                            });

                                    ////////////////////////////

                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    // Get auth credentials from the user for re-authentication //User Sekarang punya email
                                    AuthCredential credential = EmailAuthProvider.getCredential(emailCurrent, passCurrent); // Current Login Credentials \\
                                    // Prompt the user to re-provide their sign-in credentials
                                    user.reauthenticate(credential)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "User re-authenticated.", Toast.LENGTH_SHORT).show();
                                                        //Now change your email address \\
                                                        //----------------Code for Changing Email Address----------\\
                                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                                        user.updateEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        if (task.isSuccessful()) {
                                                                            Toast.makeText(getApplicationContext(), "Email Updated.. Signing out.. Please Re-Login", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                });
                                                        //----------------------------------------------------------\\
                                                    } // End of isSuccessful
                                                } // End of onComplete
                                            });

                                    ///////////////////////

                                    Toast.makeText(getApplicationContext(),"Data Updated", Toast.LENGTH_SHORT).show();

                                    dialog.dismiss();

                                    mAuth.signOut();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getApplicationContext(), "Failed to read value.", Toast.LENGTH_SHORT).show();
                    }
                });



                dialog.show();
            }
        });


        mProfileRef.child("Staff").child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()){

                    Staff staff = dataSnapshot.getValue(Staff.class);

                    String pEmail = staff.getStaffEmail();
                    String pPassword = staff.getStaffPassword();
                    String pName = staff.getStaffName();
                    String pPhoneNo = staff.getStaffNoTel();
                    String pAddress = staff.getStaffAddress();

                    mstaffEmail.setText(pEmail);
                    mstaffPassword.setText(pPassword);
                    mstaffName.setText(pName);
                    mstaffNoTel.setText(pPhoneNo);
                    mstaffAddress.setText(pAddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
