package com.example.duriandiseasedetectionsystem.FarmerActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duriandiseasedetectionsystem.R;
import com.example.duriandiseasedetectionsystem.model.Disease;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class FarmerDiseaseActivity extends AppCompatActivity {

    // kena import import androidx.appcompat.widget.Toolbar; sebab guna androidX
    private Toolbar toolbar;

    //Firebase
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    //Recyler..
    private RecyclerView recyclerView;

    //Update input field...
    private EditText updateDiseaseName;
    private SeekBar seekDiseaseSeverity;
    private SeekBar seekBarupdateDiseaseSeverity;
    private ImageView updateDiseaseImage;
    private EditText updateDiseaseSym;
    private Button btnDeleteUp;
    private Button btnUpdateUp;

    //Global Variable
    private String diseaseId;
    private String diseaseName;
    private int diseaseSeverity;
    private String diseaseSymptoms;
    private String diseaseImage;
    private String post_key;

    //uri to point to image and upload to firebase
    private Uri mImageUri;
    private ImageView adddiseaseImage;
    private static int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_disease);

        //Setup Custom toolbar
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Disease Information");

        mAuth = FirebaseAuth.getInstance();

        //ambik current user yg tengah login
        FirebaseUser mUser = mAuth.getCurrentUser();
        //amik user punya id
        String uID = mUser.getUid();
        String identifier = mUser.getEmail();

        //check id sapa tengah login sekarang
        System.out.println("/n/n Current Farmer user is : " + identifier);

        // connect dengan database dan create table name. kalau nama table dh ada dy akan masukkan terus data
        //mDatabase = FirebaseDatabase.getInstance().getReference().child("TaskNote").child(uID);
        //mDatabase.keepSynced(true); //keep synced ni untuk VIEW data recycler.. kalau nk INSERT data xyah letak ni lagi
        //yg bawah ni dy x simpan sapa yg create note tu
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Disease");
        mStorageRef = FirebaseStorage.getInstance().getReference("Disease");
        mDatabase.keepSynced(true); //keep synced ni untuk VIEW data recycler.. kalau nk INSERT data xyah letak ni lagi

        //Recyler.........................
        recyclerView = findViewById(R.id.recyler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


    } // END of Oncreate


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(PICK_IMAGE_REQUEST) {
            case 1:
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

                    mImageUri = data.getData();//get image data

                    Picasso.with(getApplicationContext()).load(mImageUri).into(adddiseaseImage);

                }
                break;
            case 2:
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

                    mImageUri = data.getData();//get image data

                    Picasso.with(this).load(mImageUri).into(updateDiseaseImage);
                }
                break;
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    //START OF VIEW

    //Recycler view punya class dan view holder adapter
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private Context context;
        View myview;

        public MyViewHolder (View itemView){
            super(itemView);
            myview = itemView;
        }

        //Semua R.id. bawah ni ambik dari item_data.xml
        public void setdName(String name){
            TextView mName = myview.findViewById(R.id.disease_name_view);
            //nak tukar texview kepada value dari database
            mName.setText(name);
        }

        public void setdSeverity(int Severity){
            TextView mSeverity = myview.findViewById(R.id.disease_severity_view);
            mSeverity.setText(Integer.toString(Severity));
            //pakai integer.tostring sbb nk convert int to string
        }

        //Semua R.id. bawah ni ambik dari item_data.xml
        public void setdImage(String name){
            ImageView imageView = (ImageView) myview.findViewById(R.id.disease_img_view);
            //nak tukar texview kepada value dari database
            Picasso.with(context).load(name).into(imageView);
        }

        public void setdSymptoms(String Symptoms){
            TextView mSymptoms = myview.findViewById(R.id.disease_symptoms_view);
            mSymptoms.setText(Symptoms);
        }
    }
    // end of recylerview class


    //untuk automatic fetch data dari database
    @Override
    protected void onStart() {
        super.onStart();

        //nak guna firebaserecyleradapter kena letak compile 'com.firebaseui:firebase-ui-database:0.4.0' kat  build.gradle (module:app)
        FirebaseRecyclerAdapter<Disease, FarmerDiseaseActivity.MyViewHolder> adapter = new FirebaseRecyclerAdapter<Disease, FarmerDiseaseActivity.MyViewHolder>(
                Disease.class,  //Name of model class
                R.layout.item_data_disease, //Row layout to show data
                FarmerDiseaseActivity.MyViewHolder.class, //Name of viewholder class
                mDatabase // Database Reference
        ) {
            @Override
            protected void populateViewHolder(FarmerDiseaseActivity.MyViewHolder viewHolder, final Disease model, final int position) {

                //Your Method to load Data
                viewHolder.setdName(model.getDiseaseName());
                viewHolder.setdSeverity(model.getDiseaseSeverity());
                viewHolder.setdSymptoms(model.getDiseaseSymptoms());
                viewHolder.setdImage(model.getDiseaseImage());

            }
        };

        //run recycler view
        recyclerView.setAdapter(adapter);
    }
    //On Start End
}
