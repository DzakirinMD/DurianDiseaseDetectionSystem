package com.example.duriandiseasedetectionsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duriandiseasedetectionsystem.model.Staff;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistrationActivity extends AppCompatActivity {

    private TextView login_text;
    private EditText email,pass,name,address,notel;
    private Button btnReg;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseStaff;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize Firebase Auth
        databaseStaff = FirebaseDatabase.getInstance().getReference("Staff");
        mAuth = FirebaseAuth.getInstance();

        mDialog = new ProgressDialog(this);

        //connect variable ngan interface
        email = findViewById(R.id.email_registration);
        name = findViewById(R.id.name_registration);
        address = findViewById(R.id.address_registration);
        notel= findViewById(R.id.notel_registration);
        pass = findViewById(R.id.password_registration);
        btnReg = findViewById(R.id.registration_btn);
        login_text = findViewById(R.id.login_txt);



        //link ke MainActivity
        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        }); //END of login_text.setOnClickListener

        //Button register activity
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

                            //Get current user
                            FirebaseUser user = mAuth.getCurrentUser();

                            //get user ID
                            String userID = user.getUid();

                            //Put user id into FarmerID and push all info into db
                            Staff staff = new Staff(userID,mName,mAddress,mNotel,mEmail,mPass);
                            databaseStaff.child(userID).setValue(staff);

                            Toast.makeText(getApplicationContext(),"Succefully Register", Toast.LENGTH_LONG).show();
                            mDialog.dismiss();

                            startActivity(new Intent(getApplicationContext(),DashboardActivity.class));

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegistrationActivity.this, "Registration failed.",
                                    Toast.LENGTH_SHORT).show();
                            mDialog.dismiss();
                        }
                    }
                });
            }
        }); //END of btnReg.setOnClickListener
    } //END of Oncreate
}
