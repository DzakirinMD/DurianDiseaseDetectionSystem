package com.example.duriandiseasedetectionsystem.FarmerActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duriandiseasedetectionsystem.R;
import com.example.duriandiseasedetectionsystem.model.Durian;
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

public class FarmerDurianActivity extends AppCompatActivity {

    // kena import import androidx.appcompat.widget.Toolbar; sebab guna androidX
    private Toolbar toolbar;

    //Firebase
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    //Recyler..
    private RecyclerView recyclerView;

    //Update input field...
    private EditText updatedurianName;
    private EditText updateDSpc;
    private EditText updateDChr;
    private ImageView updateDImg;
    private Button btnDeleteUp;
    private Button btnUpdateUp;

    //Variable
    private String durianID;
    private String name;
    private String species;
    private String dImgUri;
    private String characteristic;
    private String post_key;

    //uri to point to image and upload to firebase
    private Uri mImageUri, CropImageUri;
    private ImageView adddurianImage;
    private static int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_durian);

        //Setup Custom toolbar
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Durian Information");

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
        mStorageRef = FirebaseStorage.getInstance().getReference("Durian");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Durian");
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

                    Picasso.with(this).load(mImageUri).into(adddurianImage);
                }
                break;
            case 2:
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

                    mImageUri = data.getData();//get image data

                    Picasso.with(this).load(mImageUri).into(updateDImg);
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
    //Adapter
    @Override
    protected void onStart() {
        super.onStart();

        //nak guna firebaserecyleradapter kena letak compile 'com.firebaseui:firebase-ui-database:0.4.0' kat  build.gradle (module:app)
        FirebaseRecyclerAdapter<Durian, FarmerDurianActivity.MyViewHolder> adapter = new FirebaseRecyclerAdapter<Durian, FarmerDurianActivity.MyViewHolder>(
                Durian.class,  //Name of model class
                R.layout.item_data, //Row layout to show data
                FarmerDurianActivity.MyViewHolder.class, //Name of viewholder class
                mDatabase // Database Reference
        ) {
            @Override
            protected void populateViewHolder(FarmerDurianActivity.MyViewHolder viewHolder, final Durian model, final int position) {

                //Your Method to load Data
                viewHolder.setdurianName(model.getdurianName());
                viewHolder.setdurianSpecies(model.getdurianSpecies());
                viewHolder.setDurianImage(model.getdurianImage());
                viewHolder.setdurianCharacteristic(model.getdurianCharacteristic());


                // Setting bila click kat card2 recycler view tu nk jadi apa
                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //post_key untuk dapatkan integer position (kita click tu kat mana) so xyah amik id sbb id x update pon
                        post_key = getRef(position).getKey(); //post_key ni = id

                        durianID = model.getdurianID();
                        name = model.getdurianName();
                        species = model.getdurianSpecies();
                        dImgUri = model.getdurianImage();
                        characteristic = model.getdurianCharacteristic();

                    }
                });
            }
        };

        //run recycler view
        recyclerView.setAdapter(adapter);
    }
    //On Start End

    //Recycler view punya class dan view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private Context context;
        View myview;

        public MyViewHolder (View itemView){
            super(itemView);
            myview = itemView;
        }

        //Semua R.id. bawah ni ambik dari item_data.xml
        public void setdurianName(String name){
            TextView mName = myview.findViewById(R.id.name);
            //nak tukar texview kepada value dari database
            mName.setText(name);
        }

        //Semua R.id. bawah ni ambik dari item_data.xml
        public void setDurianImage(String name){
            ImageView imageView = (ImageView) myview.findViewById(R.id.durian_view);
            //nak tukar texview kepada value dari database
            Picasso.with(context).load(name).into(imageView);
        }

        public void setdurianSpecies(String species){
            TextView mSpecies = myview.findViewById(R.id.species);
            mSpecies.setText(species);
        }

        public void setdurianCharacteristic(String characteristic){
            TextView mChar = myview.findViewById(R.id.characteristic);
            mChar.setText(characteristic);
        }
    }
    // end of recylerview class

}
