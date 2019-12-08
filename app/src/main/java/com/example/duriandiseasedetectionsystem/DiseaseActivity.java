package com.example.duriandiseasedetectionsystem;

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


public class DiseaseActivity extends AppCompatActivity {

    // kena import import androidx.appcompat.widget.Toolbar; sebab guna androidX
    private Toolbar toolbar;

    //Floating button
    private FloatingActionButton flt_btn;

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
        setContentView(R.layout.activity_disease);

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
        System.out.println("/n/n Current user is : " + identifier);

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

        //floating_button ni button bulat kecik tu (INSERT data)
        flt_btn = findViewById(R.id.floating_button);

        //INSERT DATA PART
        flt_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Custom alert dialog. boleh guna untuk update
//                https://www.udemy.com/course/android-office-task-management-app-using-firebasereal-app/learn/lecture/12405078#content
//                section 8
                AlertDialog.Builder myDialog = new AlertDialog.Builder(DiseaseActivity.this);

                //apa function layout inflater https://stackoverflow.com/questions/3477422/what-does-layoutinflater-in-android-do
                // LayoutInflater is used to create a new View (or Layout) object from one of your xml layouts.
                LayoutInflater inflater = LayoutInflater.from(DiseaseActivity.this);
                //lepas inflate letak xml view mana yg nk di letak (create_durian yg sendiri design)
                View myview = inflater.inflate(R.layout.create_disease, null);
                //massukan view tu dalam alert dialog
                myDialog.setView(myview);
                //nak sambungkan 2 input field dalam custom dialog tu dan create dialog tu
                final AlertDialog dialog = myDialog.create();

                //panggil data dari xml alert dialog tu. ni untuk INSERT
                final EditText add_disease_name = myview.findViewById(R.id.add_disease_name);
                seekDiseaseSeverity = myview.findViewById(R.id.seekBar_disease_severity);
                adddiseaseImage = myview.findViewById(R.id.add_disease_img);
                final EditText add_disease_symptoms = myview.findViewById(R.id.add_disease_symptoms);

                //choose image from phone
                adddiseaseImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PICK_IMAGE_REQUEST = 1;
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                        //This will go to onActivityResult()
                    }
                });

                Button btnSave = myview.findViewById(R.id.btn_save);

                //CREATE duiran
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String diseaseName = add_disease_name.getText().toString().trim();
                        int diseaseSeverity = seekDiseaseSeverity.getProgress();
                        String diseaseSymptom = add_disease_symptoms.getText().toString().trim();

                        // Error Checking
                        if (TextUtils.isEmpty(diseaseName)) {
                            add_disease_name.setError("Name is required..");
                            return;
                        }
                        if (TextUtils.isEmpty(diseaseSymptom)) {
                            add_disease_symptoms.setError("Symptoms is required..");
                            return;
                        }

                        //create randomkey untuk id
                        String diseaseID = mDatabase.push().getKey();

                        //get user Image
                        if (mImageUri != null) {
                            final StorageReference ref = mStorageRef.child("images/" + diseaseID + "." + getFileExtension(mImageUri));
                            ref.putFile(mImageUri)
                                    // Inside OnSuccess this to create data
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                @Override
                                                public void onSuccess(Uri uri) {
                                                    Uri downloadUrl = uri;

                                                    //call method to create data
                                                    createDisease(diseaseID, diseaseName, diseaseSeverity, diseaseSymptom, downloadUrl.toString());
                                                    dialog.dismiss();

                                                }
                                            });
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(getApplicationContext(), "Failed to load Image", Toast.LENGTH_LONG).show();
                                            dialog.dismiss();
                                        }
                                    });
                        } else {
                            Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }

                    }
                }); // End of btnSave

                //dah create show pulak
                dialog.show();


            }
        });
        //INSERT DATA PART END

    } // END of Oncreate


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (PICK_IMAGE_REQUEST) {
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
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private Context context;
        View myview;

        public MyViewHolder(View itemView) {
            super(itemView);
            myview = itemView;
        }

        //Semua R.id. bawah ni ambik dari item_data.xml
        public void setdName(String name) {
            TextView mName = myview.findViewById(R.id.disease_name_view);
            //nak tukar texview kepada value dari database
            mName.setText(name);
        }

        public void setdSeverity(int Severity) {
            TextView mSeverity = myview.findViewById(R.id.disease_severity_view);
            mSeverity.setText(Integer.toString(Severity));
            //pakai integer.tostring sbb nk convert int to string
        }

        //Semua R.id. bawah ni ambik dari item_data.xml
        public void setdImage(String name) {
            ImageView imageView = (ImageView) myview.findViewById(R.id.disease_img_view);
            //nak tukar texview kepada value dari database
            Picasso.with(context).load(name).into(imageView);
        }

        public void setdSymptoms(String Symptoms) {
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
        FirebaseRecyclerAdapter<Disease, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Disease, MyViewHolder>(
                Disease.class,  //Name of model class
                R.layout.item_data_disease, //Row layout to show data
                MyViewHolder.class, //Name of viewholder class
                mDatabase // Database Reference
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Disease model, final int position) {

                //Your Method to load Data
                viewHolder.setdName(model.getDiseaseName());
                viewHolder.setdSeverity(model.getDiseaseSeverity());
                viewHolder.setdSymptoms(model.getDiseaseSymptoms());
                viewHolder.setdImage(model.getDiseaseImage());


                // Setting bila click kat card2 recycler view tu nk jadi apa
                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //post_key untuk dapatkan integer position (kita click tu kat mana) so xyah amik id sbb id x update pon
                        post_key = getRef(position).getKey(); //post_key ni = id

                        diseaseId = model.getDiseaseID();
                        diseaseName = model.getDiseaseName();
                        diseaseSeverity = model.getDiseaseSeverity();
                        diseaseSymptoms = model.getDiseaseSymptoms();
                        diseaseImage = model.getDiseaseImage();

                        //call update dan delete data kt recyler view
                        updateDeleteData();
                    }
                });
            }
        };

        //run recycler view
        recyclerView.setAdapter(adapter);
    }
    //On Start End

    private void createDisease(String diseaseID, String diseaseName, int diseaseSeverity, String diseaseSymptom, String diseaseImage) {

        Disease data = new Disease(diseaseID, diseaseName, diseaseSeverity, diseaseSymptom, diseaseImage);
        mDatabase.child(diseaseID).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DiseaseActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //untuk update data dan show dialog dia
    public void updateDeleteData() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(DiseaseActivity.this);
        LayoutInflater inflater = LayoutInflater.from(DiseaseActivity.this);

        View myview = inflater.inflate(R.layout.updatedelete_disease, null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();

        //collect data from field of updatedelete_durian
        updateDiseaseName = myview.findViewById(R.id.upd_disease_name);
        seekBarupdateDiseaseSeverity = myview.findViewById(R.id.seekBar_upd_disease_severity);
        updateDiseaseSym = myview.findViewById(R.id.upd_disease_symptoms);
        updateDiseaseImage = myview.findViewById(R.id.upd_disease_img);

        //ni untuk ambik apa yg ada kt recycler(item_data.xml) tu untuk letak kat dialog update
        //DARI GLOBAL VARIABLE
        updateDiseaseName.setText(diseaseName);
        updateDiseaseName.setSelection(diseaseName.length());

        //cari untuk seekbar sama
        seekBarupdateDiseaseSeverity.getProgress();

        Picasso.with(getApplicationContext()).load(diseaseImage).into(updateDiseaseImage);

        updateDiseaseSym.setText(diseaseSymptoms);
        updateDiseaseSym.setSelection(diseaseSymptoms.length());

        //choose image from phone
        updateDiseaseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PICK_IMAGE_REQUEST = 2;
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                //This will go to onActivityResult()
            }
        });

        //instantiate button
        btnDeleteUp = myview.findViewById(R.id.btn_delete_upd);
        btnUpdateUp = myview.findViewById(R.id.btn_update_upd);

        btnUpdateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //variable ni dari global variable
                diseaseName = updateDiseaseName.getText().toString().trim();
                diseaseSeverity = seekBarupdateDiseaseSeverity.getProgress();
                diseaseSymptoms = updateDiseaseSym.getText().toString().trim();

                System.out.println(diseaseName);
                System.out.println(diseaseSymptoms);
                System.out.println(post_key);

                //get user Image
                if (mImageUri != null) {
                    final StorageReference ref = mStorageRef.child("images/" + diseaseId + "." + getFileExtension(mImageUri));
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
                                            Disease data = new Disease(post_key, diseaseName, diseaseSeverity, diseaseSymptoms, downloadUrl.toString());
                                            mDatabase.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Data Updated", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                    } else {
                                                        Toast.makeText(DiseaseActivity.this, " Failed.", Toast.LENGTH_SHORT).show();
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
                                    Toast.makeText(getApplicationContext(), "No file selected", Toast.LENGTH_LONG).show();
                                    dialog.dismiss();
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter an image", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            }
        });

        btnDeleteUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDatabase.child(post_key).removeValue();

                dialog.dismiss();
            }
        });

        dialog.show();
    }

}
