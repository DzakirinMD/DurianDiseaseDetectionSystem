package com.example.duriandiseasedetectionsystem;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duriandiseasedetectionsystem.model.Disease;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class DiseaseActivity extends AppCompatActivity {

    // kena import import androidx.appcompat.widget.Toolbar; sebab guna androidX
    private Toolbar toolbar;

    //Floating button
    private FloatingActionButton flt_btn;

    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    //Recyler..
    private RecyclerView recyclerView;

    //Update input field...
    private EditText updateDName;
    private EditText updateDSvr;
    private EditText updateDSym;
    private Button btnDeleteUp;
    private Button btnUpdateUp;

    //Global Variable
    private String diseaseId;
    private String diseaseName;
    private int diseaseSeverity;
    private String diseaseSymptoms;
    private String post_key;

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
                //lepas inflate letak xml view mana yg nk di letak (custominputfield yg sendiri design)
                View myview = inflater.inflate(R.layout.custominputfield_disease, null);
                //massukan view tu dalam alert dialog
                myDialog.setView(myview);
                //nak sambungkan 2 input field dalam custom dialog tu dan create dialog tu
                final AlertDialog dialog = myDialog.create();

                //panggil data dari xml alert dialog tu. ni untuk INSERT
                final EditText add_disease_name = myview.findViewById(R.id.add_disease_name);
                final SeekBar seekBarDiseaseSeverity = myview.findViewById(R.id.seekBarDiseaseSeverity);
                final EditText add_disease_symptoms = myview.findViewById(R.id.add_disease_symptoms);

                Button btnSave = myview.findViewById(R.id.btn_save);

                //CREATE duiran
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String diseaseName = add_disease_name.getText().toString().trim();
                        int diseaseSeverity = seekBarDiseaseSeverity.getProgress();
                        String diseaseSymptom = add_disease_symptoms.getText().toString().trim();

                        // Error Checking
                        if (TextUtils.isEmpty(diseaseName)){
                            add_disease_name.setError("Name is required..");
                            return;
                        }
                        if (TextUtils.isEmpty(diseaseSymptom)){
                            add_disease_symptoms.setError("Characteristic is required..");
                            return;
                        }

                        //create randomkey untuk id
                        String diseaseID = mDatabase.push().getKey();

                        //Call model constructor
                        Disease data = new Disease(diseaseID, diseaseName, diseaseSeverity, diseaseSymptom);

                        //masuk kan value dalam id tu
                        mDatabase.child(diseaseID).setValue(data);

                        Toast.makeText(getApplicationContext(),"Data Inserted", Toast.LENGTH_SHORT).show();

                        //tutup balik dialog bila dh masuk data
                        dialog.dismiss();

                    }
                }); // End of btnSave

                //dah create show pulak
                dialog.show();


            }
        });
        //INSERT DATA PART END

    } // END of Oncreate


    //START OF VIEW
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

    //Recycler view punya class dan view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myview;

        public MyViewHolder (View itemView){
            super(itemView);
            myview = itemView;
        }

        //Semua R.id. bawah ni ambik dari item_data.xml
        public void setdName(String name){
            TextView mName = myview.findViewById(R.id.name);
            //nak tukar texview kepada value dari database
            mName.setText(name);
        }

        public void setdSeverity(int Severity){
            TextView mSeverity = myview.findViewById(R.id.severity);
            mSeverity.setText(Integer.toString(Severity));
            //pakai integer.tostring sbb nk convert int to string
        }

        public void setdSymptoms(String Symptoms){
            TextView mSymptoms = myview.findViewById(R.id.symptoms);
            mSymptoms.setText(Symptoms);
        }
    }
    // end of recylerview class

    //untuk update data dan show dialog dia
    public void  updateDeleteData(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(DiseaseActivity.this);
        LayoutInflater inflater = LayoutInflater.from(DiseaseActivity.this);

        View myview = inflater.inflate(R.layout.updatedeleteinputfield_disease, null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();

        //collect data from field of updatedeleteinputfield
        updateDName = myview.findViewById(R.id.upd_disease_name);
        final SeekBar updateDSvr = myview.findViewById(R.id.updseekBarDiseaseSeverity);
        updateDSym = myview.findViewById(R.id.upd_disease_symptoms);

        //ni untuk ambik apa yg ada kt recycler(item_data.xml) tu untuk letak kat dialog update
        //DARI GLOBAL VARIABLE
        updateDName.setText(diseaseName);
        updateDName.setSelection(diseaseName.length());

        //cari untuk seekbar sama
        updateDSvr.getProgress();

        updateDSym.setText(diseaseSymptoms);
        updateDSym.setSelection(diseaseSymptoms.length());

        //instantiate button
        btnDeleteUp = myview.findViewById(R.id.btn_delete_upd);
        btnUpdateUp = myview.findViewById(R.id.btn_update_upd);

        btnUpdateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //variable ni dari global variable
                diseaseName = updateDName.getText().toString().trim();
                int dSvrProgress = updateDSvr.getProgress();
                diseaseSymptoms = updateDSym.getText().toString().trim();

                System.out.println(diseaseName);
                System.out.println(dSvrProgress);
                System.out.println(diseaseSymptoms);
                System.out.println(post_key);

                //Call model constructor
                Disease data = new Disease(post_key,diseaseName,dSvrProgress,diseaseSymptoms);

                //masuk kan value dalam id tu (position dy)
                mDatabase.child(post_key).setValue(data);

                Toast.makeText(getApplicationContext(),"Data Updated", Toast.LENGTH_SHORT).show();

                dialog.dismiss();
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
