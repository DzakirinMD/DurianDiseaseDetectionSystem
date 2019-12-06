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
import android.widget.TextView;
import android.widget.Toast;

import com.example.duriandiseasedetectionsystem.R;
import com.example.duriandiseasedetectionsystem.model.Leaf;
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

public class FarmerLeafActivity extends AppCompatActivity {

    // kena import import androidx.appcompat.widget.Toolbar; sebab guna androidX
    private Toolbar toolbar;

    //Floating button
    private FloatingActionButton flt_btn;

    //Search Bar
    private EditText search_bar;

    //Firebase
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    //Recyler..
    private RecyclerView recyclerView;

    //Update input field...
    private ImageView updateLImage;
    private EditText updateLChr;
    private Button btnDeleteUp;
    private Button btnUpdateUp;

    //Variable
    private String leafID;
    private String leafImage;
    private String leafCharacteristics;
    private String post_key;

    //uri to point to image and upload to firebase
    private Uri mImageUri;
    private ImageView addleafImage;
    private static int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_leaf);

        //Setup Custom toolbar
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Leaf Information");

        mAuth = FirebaseAuth.getInstance();

        //ambik current user yg tengah login
        FirebaseUser mUser = mAuth.getCurrentUser();
        //amik user punya id
        String uID = mUser.getUid();
        String identifier = mUser.getEmail();

        //check id sapa tengah login sekarang
        System.out.println("/n/n Current user is : " + identifier);

        // connect dengan database dan create table name. kalau nama table dh ada dy akan masukkan terus data
        //mDatabase = FirebaseDatabase.getInstance().getReference().child("TaskNote").child(uID);
        //mDatabase.keepSynced(true); //keep synced ni untuk VIEW data recycler.. kalau nk INSERT data xyah letak ni lagi
        //yg bawah ni dy x simpan sapa yg create note tu
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Leaf");
        mStorageRef = FirebaseStorage.getInstance().getReference("Leaf");
        mDatabase.keepSynced(true); //keep synced ni untuk VIEW data recycler.. kalau nk INSERT data xyah letak ni lagi

        //Recyler.........................
        recyclerView = findViewById(R.id.recyler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        //floating_button ni button bulat kecik tu (INSERT data)
        flt_btn = findViewById(R.id.floating_button);

        //INSERT DATA PART
        flt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Custom alert dialog. boleh guna untuk update
//                https://www.udemy.com/course/android-office-task-management-app-using-firebasereal-app/learn/lecture/12405078#content
//                section 8
                AlertDialog.Builder myDialog = new AlertDialog.Builder(FarmerLeafActivity.this);

                //apa function layout inflater https://stackoverflow.com/questions/3477422/what-does-layoutinflater-in-android-do
                // LayoutInflater is used to create a new View (or Layout) object from one of your xml layouts.
                LayoutInflater inflater = LayoutInflater.from(FarmerLeafActivity.this);
                //lepas inflate letak xml view mana yg nk di letak (custominputfield yg sendiri design)
                View myview = inflater.inflate(R.layout.custominputfield_leaf, null);
                //massukan view tu dalam alert dialog
                myDialog.setView(myview);
                //nak sambungkan 2 input field dalam custom dialog tu dan create dialog tu
                final AlertDialog dialog = myDialog.create();

                //panggil data dari xml customeinputfield_leaf.xml
                addleafImage = myview.findViewById(R.id.add_leaf_img);
                final EditText addLChr = myview.findViewById(R.id.add_leaf_characteristic);

                //choose image from phone
                addleafImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PICK_IMAGE_REQUEST = 1;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent,"Select Picture"), PICK_IMAGE_REQUEST);
                        //This will go to onActivityResult()
                    }
                });

                Button btnSave = myview.findViewById(R.id.btn_save);

                //CREATE Leaf
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String leafChar = addLChr.getText().toString().trim();

                        //Get current user
                        FirebaseUser user = mAuth.getCurrentUser();

                        //get user ID
                        String userID = user.getUid();

                        // Error Checking
                        if (TextUtils.isEmpty(leafChar)){
                            addLChr.setError("Characteristic is required..");
                            return;
                        }

                        //create randomkey untuk id
                        String leafID = mDatabase.push().getKey();

                        //get user Image
                        if (mImageUri != null) {
                            final StorageReference ref = mStorageRef.child("images/" + leafID + "." + getFileExtension(mImageUri));
                            ref.putFile(mImageUri)
                                    // Inside OnSuccess this to create data
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Uri downloadUrl = uri;

                                                    //Put user id into FarmerID and push all info into db
                                                    Leaf data = new Leaf(leafID, downloadUrl.toString(), leafChar);
                                                    mDatabase.child(leafID).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
                                                            } else {
                                                                Toast.makeText(FarmerLeafActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
                                                                dialog.dismiss();
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
                                            Toast.makeText(getApplicationContext(),"Failed to load Image", Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(),"No file selected", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    }
                });

                //dah create show pulak
                dialog.show();


            }
        });
        //INSERT DATA PART END

    } // END of Oncreate


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(PICK_IMAGE_REQUEST) {
            case 1:
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

                    mImageUri = data.getData();//get image data

                    Picasso.with(getApplicationContext()).load(mImageUri).into(addleafImage);

                }
                break;
            case 2:
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

                    mImageUri = data.getData();//get image data

                    Picasso.with(this).load(mImageUri).into(updateLImage);
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
        public void setLeafImage(String name){
            ImageView imageView = (ImageView) myview.findViewById(R.id.leaf_img_view);
            //nak tukar texview kepada value dari database
            Picasso.with(context).load(name).into(imageView);
        }

        public void setleafCharacteristics(String characteristic){
            TextView mChar = myview.findViewById(R.id.leaf_characteristic_view);
            mChar.setText(characteristic);
        }
    }
    // end of recylerview class


    //untuk automatic fetch data dari database
    @Override
    protected void onStart() {
        super.onStart();

        //nak guna firebaserecyleradapter kena letak compile 'com.firebaseui:firebase-ui-database:0.4.0' kat  build.gradle (module:app)
        FirebaseRecyclerAdapter<Leaf, FarmerLeafActivity.MyViewHolder> adapter = new FirebaseRecyclerAdapter<Leaf, FarmerLeafActivity.MyViewHolder>(
                Leaf.class,  //Name of model class
                R.layout.item_data_leaf, //Row layout to show data
                FarmerLeafActivity.MyViewHolder.class, //Name of viewholder class
                mDatabase // Database Reference
        ) {
            @Override
            protected void populateViewHolder(FarmerLeafActivity.MyViewHolder viewHolder, final Leaf model, final int position) {

                //Your Method to load Data
                viewHolder.setLeafImage(model.getLeafImage());
                viewHolder.setleafCharacteristics(model.getleafCharacteristics());

            }
        };



        //run recycler view
        recyclerView.setAdapter(adapter);
    }//On Start End

}
