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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private TextView signup;
    private EditText email,pass;
    private Button btnLogin;

    private FirebaseAuth mAuth;

    private ProgressDialog mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //kalau dh ada user trus p homeactivity
        if (mAuth.getCurrentUser() != null){
            startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
        }

        mDialog = new ProgressDialog(this);

        signup = findViewById(R.id.signup_txt);
        email = findViewById(R.id.email_login);
        pass = findViewById(R.id.passsword_login);
        btnLogin = findViewById(R.id.login_btn);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ambil data dari field
                String mEmail = email.getText().toString().trim();
                String mPass = pass.getText().toString().trim();

                // Error Checking
                System.out.println(mEmail);
                System.out.println(mPass);

                if (TextUtils.isEmpty(mEmail)){
                    email.setError("Email is required..");
                    return;
                }

                if (TextUtils.isEmpty(mPass)){
                    pass.setError("Password is required..");
                    return;
                }

                mDialog.setMessage("Processing..");
                mDialog.show();

                mAuth.signInWithEmailAndPassword(mEmail, mPass)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                    mDialog.dismiss();

                                    startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(MainActivity.this, "Login failed", Toast.LENGTH_SHORT).show();
                                    mDialog.dismiss();
                                }
                            }
                        });
            }
        }); // END of btnLogin onClick

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegistrationActivity.class));
            }
        }); //End of signup on click



    } //END of oncreate

}
