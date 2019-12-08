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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duriandiseasedetectionsystem.model.ControlMeasures;
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


public class ControlMeasuresActivity extends AppCompatActivity {

    // kena import import androidx.appcompat.widget.Toolbar; sebab guna androidX
    private Toolbar toolbar;

    //Floating button
    private FloatingActionButton flt_btn;

    //Spinner
    private Spinner spinnerAddCm;
    private Spinner spinnerUpdCm;

    //SeekBar
    private SeekBar seekAddDiseaseSeverity;
    private SeekBar seekUpdDiseaseSeverity;

    //Firebase
    private DatabaseReference mDatabase;
    private StorageReference mStorageRef;
    private FirebaseAuth mAuth;

    //Recyler..
    private RecyclerView recyclerView;

    //Update input field...
    private EditText updateCMName;
    private EditText updateCMIns;
    private ImageView updateCMImg;
    private Button btnDeleteUp;
    private Button btnUpdateUp;

    //Variable
    private String cmId;
    private String cmName;
    private String cmDisease;
    private int cmSeverity;
    private String cmImage;
    private String cmIns;
    private String post_key;

    //uri to point to image and upload to firebase
    private Uri mImageUri;
    private ImageView addcmImage;
    private static int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_durian);

        //Setup Custom toolbar
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Control Measure Information");

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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Control_Measures");
        mStorageRef = FirebaseStorage.getInstance().getReference("Control_Measures");
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
                AlertDialog.Builder myDialog = new AlertDialog.Builder(ControlMeasuresActivity.this);

                //apa function layout inflater https://stackoverflow.com/questions/3477422/what-does-layoutinflater-in-android-do
                // LayoutInflater is used to create a new View (or Layout) object from one of your xml layouts.
                LayoutInflater inflater = LayoutInflater.from(ControlMeasuresActivity.this);
                //lepas inflate letak xml view mana yg nk di letak (create_durian yg sendiri design)
                View myview = inflater.inflate(R.layout.create_control_measures, null);
                //massukan view tu dalam alert dialog
                myDialog.setView(myview);
                //nak sambungkan 2 input field dalam custom dialog tu dan create dialog tu
                final AlertDialog dialog = myDialog.create();

                //panggil data dari xml create_control_measures ni untuk INSERT
                final EditText addCMName = myview.findViewById(R.id.add_cm_name);
                addcmImage = myview.findViewById(R.id.add_cm_image);
                seekAddDiseaseSeverity = myview.findViewById(R.id.add_seekBar_cm_severity);
                final EditText addCMInstruction = myview.findViewById(R.id.add_cm_instruction);
                //call spinner
                spinnerAddCm = (Spinner) myview.findViewById(R.id.add_spinner_disease_cm);
                spinnerAddCm.setOnItemSelectedListener(new CustomOnItemSelectedListener());


                //choose image from phone
                addcmImage.setOnClickListener(new View.OnClickListener() {
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

                //CREATE control measure
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cmName = addCMName.getText().toString().trim();
                        String cmIns = addCMInstruction.getText().toString().trim();
                        String cmDisease = String.valueOf(spinnerAddCm.getSelectedItem());
                        int diseaseSeverity = seekAddDiseaseSeverity.getProgress();

                        // Error Checking
                        if (TextUtils.isEmpty(cmName)) {
                            addCMName.setError("Name is required..");
                            return;
                        }

                        if (TextUtils.isEmpty(cmIns)) {
                            addCMInstruction.setError("Instruction is required..");
                            return;
                        }

                        //create randomkey untuk id
                        String cmID = mDatabase.push().getKey();

                        //get user Image
                        if (mImageUri != null) {
                            final StorageReference ref = mStorageRef.child("images/" + cmID + "." + getFileExtension(mImageUri));
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
                                                    createControlMeasures(cmID, cmName, cmDisease, diseaseSeverity, downloadUrl.toString(), cmIns);
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
                            Toast.makeText(getApplicationContext(), "Please enter an image", Toast.LENGTH_LONG).show();
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

        switch (PICK_IMAGE_REQUEST) {
            case 1:
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

                    mImageUri = data.getData();//get image data

                    Picasso.with(getApplicationContext()).load(mImageUri).into(addcmImage);

                }
                break;
            case 2:
                if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {

                    mImageUri = data.getData();//get image data

                    Picasso.with(this).load(mImageUri).into(updateCMImg);
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

    //Recycler view punya class dan view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private Context context;
        View myview;

        public MyViewHolder(View itemView) {
            super(itemView);
            myview = itemView;
        }

        //Semua R.id. bawah ni ambik dari item_data.xml
        public void setCmName(String cmName) {
            TextView mName = myview.findViewById(R.id.cmName_view);
            //nak tukar texview kepada value dari database
            mName.setText(cmName);
        }

        public void setCmDisease(String leafdurian) {
            TextView lDurian = myview.findViewById(R.id.cmDisease_view);
            lDurian.setText(leafdurian);
        }

        public void setCmSeverity(int Severity) {
            TextView mSeverity = myview.findViewById(R.id.cmSeverity_view);
            mSeverity.setText(Integer.toString(Severity));
            //pakai integer.tostring sbb nk convert int to string
        }

        public void setCmInstruction(String cmInstruction) {
            TextView CMInstruction = myview.findViewById(R.id.cmIns_view);
            CMInstruction.setText(cmInstruction);
        }

        //Semua R.id. bawah ni ambik dari item_data.xml
        public void setCMImage(String name) {
            ImageView imageView = (ImageView) myview.findViewById(R.id.cmImg_view);
            //nak tukar texview kepada value dari database
            Picasso.with(context).load(name).into(imageView);
        }

    }

    //untuk automatic fetch data dari database
    @Override
    protected void onStart() {
        super.onStart();

        //nak guna firebaserecyleradapter kena letak compile 'com.firebaseui:firebase-ui-database:0.4.0' kat  build.gradle (module:app)
        FirebaseRecyclerAdapter<ControlMeasures, MyViewHolder> adapter = new FirebaseRecyclerAdapter<ControlMeasures, MyViewHolder>(
                ControlMeasures.class,  //Name of model class
                R.layout.item_data_control_measures, //Row layout to show data
                MyViewHolder.class, //Name of viewholder class
                mDatabase // Database Reference
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final ControlMeasures model, final int position) {

                //Your Method to load Data
                viewHolder.setCmName(model.getCmName());
                viewHolder.setCmDisease(model.getCmDisease());
                viewHolder.setCmSeverity(model.getCmSeverity());
                viewHolder.setCmInstruction(model.getCmInstruction());
                viewHolder.setCMImage(model.getCmImage());

                // Setting bila click kat card2 recycler view tu nk jadi apa
                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //post_key untuk dapatkan integer position (kita click tu kat mana) so xyah amik id sbb id x update pon
                        post_key = getRef(position).getKey(); //post_key ni = id

                        cmId = model.getCmID();
                        cmName = model.getCmName();
                        cmDisease = model.getCmDisease();
                        cmSeverity = model.getCmSeverity();
                        cmIns = model.getCmInstruction();
                        cmImage = model.getCmImage();

                        //call update dan delete data kt recyler view
                        updateDeleteData();
                    }
                });
            }
        };

        //run recycler view
        recyclerView.setAdapter(adapter);
    }

    private void createControlMeasures(String cmID, String cmName, String cmDisease, int diseaseSeverity, String cmImg, String cmInsr) {

        ControlMeasures data = new ControlMeasures(cmID, cmName, cmDisease, diseaseSeverity, cmImg, cmIns);
        mDatabase.child(cmID).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getApplicationContext(), "Successfully Added", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ControlMeasuresActivity.this, "Failed.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //untuk update data dan show dialog dia
    public void updateDeleteData() {

        AlertDialog.Builder mydialog = new AlertDialog.Builder(ControlMeasuresActivity.this);
        LayoutInflater inflater = LayoutInflater.from(ControlMeasuresActivity.this);

        View myview = inflater.inflate(R.layout.updatedelete_control_measures, null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();

        //collect data from field of updatedelete_durian
        updateCMName = myview.findViewById(R.id.upd_cm_name);
        spinnerUpdCm = myview.findViewById(R.id.upd_spinner_disease_cm);
        spinnerUpdCm.setOnItemSelectedListener(new CustomOnItemSelectedListener());
        seekUpdDiseaseSeverity = myview.findViewById(R.id.upd_seekBar_cm_severity);
        updateCMIns = myview.findViewById(R.id.upd_cm_instruction);
        updateCMImg = myview.findViewById(R.id.upd_cm_image);

        System.out.println("New CM name" + updateCMName);

        //ni untuk ambik apa yg ada kt recycler(item_data.xml) tu untuk letak kat dialog update
        updateCMName.setText(cmName);
        updateCMName.setSelection(cmName.length());

        Picasso.with(getApplicationContext()).load(cmImage).into(updateCMImg);
        seekUpdDiseaseSeverity.getProgress();

        updateCMIns.setText(cmIns);
        updateCMIns.setSelection(cmIns.length());

        //choose image from phone
        updateCMImg.setOnClickListener(new View.OnClickListener() {
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
                cmName = updateCMName.getText().toString().trim();
                cmDisease = String.valueOf(spinnerAddCm.getSelectedItem());
                cmSeverity = seekUpdDiseaseSeverity.getProgress();
                cmIns = updateCMIns.getText().toString().trim();

                System.out.println(cmName);
                System.out.println(cmIns);
                System.out.println(post_key);

                //get user Image
                if (mImageUri != null) {
                    final StorageReference ref = mStorageRef.child("images/" + cmId + "." + getFileExtension(mImageUri));
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
                                            ControlMeasures data = new ControlMeasures(post_key, cmName, cmDisease, cmSeverity, downloadUrl.toString(), cmIns);
                                            mDatabase.child(post_key).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Data Updated", Toast.LENGTH_SHORT).show();
                                                        dialog.dismiss();
                                                    } else {
                                                        Toast.makeText(ControlMeasuresActivity.this, " Failed.", Toast.LENGTH_SHORT).show();
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
    //guide untuk spinner
    //https://www.mkyong.com/android/android-spinner-drop-down-list-example/
}
