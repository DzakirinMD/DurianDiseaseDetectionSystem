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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.duriandiseasedetectionsystem.model.Durian;
import com.example.duriandiseasedetectionsystem.model.Leaf;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class LeafActivity extends AppCompatActivity {

    // kena import import androidx.appcompat.widget.Toolbar; sebab guna androidX
    private Toolbar toolbar;

    //Spinner
    private Spinner spinner, spinnerUpdate;

    //Floating button
    private FloatingActionButton flt_btn;

    //Firebase
    private DatabaseReference mDatabase,durianDatabase;
    private FirebaseAuth mAuth;

    //Recyler..
    private RecyclerView recyclerView;

    //Update input field...
    private EditText updateLName;
    private EditText updateLDurian;
    private EditText updateLChr;
    private Button btnDeleteUp;
    private Button btnUpdateUp;

    //Variable
    private String lId;
    private String name;
    private String leafdurian;
    private String characteristic;
    private String post_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_durian);

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
        durianDatabase = FirebaseDatabase.getInstance().getReference().child("Durian");
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Leaf");
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
                AlertDialog.Builder myDialog = new AlertDialog.Builder(LeafActivity.this);

                //apa function layout inflater https://stackoverflow.com/questions/3477422/what-does-layoutinflater-in-android-do
                // LayoutInflater is used to create a new View (or Layout) object from one of your xml layouts.
                LayoutInflater inflater = LayoutInflater.from(LeafActivity.this);
                //lepas inflate letak xml view mana yg nk di letak (custominputfield yg sendiri design)
                View myview = inflater.inflate(R.layout.custominputfield_leaf, null);
                //massukan view tu dalam alert dialog
                myDialog.setView(myview);
                //nak sambungkan 2 input field dalam custom dialog tu dan create dialog tu
                final AlertDialog dialog = myDialog.create();

                //panggil data dari xml alert dialog tu. ni untuk INSERT
                final EditText addLName = myview.findViewById(R.id.add_leaf_name);
                final Spinner spinner = myview.findViewById(R.id.spinnerLeaf);
                final EditText addLChr = myview.findViewById(R.id.add_leaf_characteristic);

                Button btnSave = myview.findViewById(R.id.btn_save);

                //START Untuk populate value SPINNER
                Query query = durianDatabase.orderByChild("dName");
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final List<String> titleList = new ArrayList<String>();
                        for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                            String titlename = dataSnapshot1.child("dName").getValue(String.class);
                            titleList.add(titlename);
                        }
                        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LeafActivity.this, android.R.layout.simple_spinner_item, titleList);
                        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(arrayAdapter);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(LeafActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                    }
                });
                //END populate value SPINNER

                //CREATE leaf
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String lName = addLName.getText().toString().trim();
                        String lDurian = spinner.getSelectedItem().toString();
                        String lChar = addLChr.getText().toString().trim();

                        // Error Checking
                        if (TextUtils.isEmpty(lName)){
                            addLName.setError("Name is required..");
                            return;
                        }
                        if (TextUtils.isEmpty(lChar)){
                            addLChr.setError("Characteristic is required..");
                            return;
                        }

                        //create randomkey untuk id
                        String dID = mDatabase.push().getKey();

                        //Call model constructor
                        Leaf data = new Leaf(dID, lName, lDurian, lChar);

                        //masuk kan value dalam id tu dan push to database
                        mDatabase.child(dID).setValue(data);

                        Toast.makeText(getApplicationContext(),"Data Inserted", Toast.LENGTH_SHORT).show();

                        //tutup balik dialog bila dh masuk data
                        dialog.dismiss();

                    }
                });

                //dah create show pulak
                dialog.show();


            }
        });
        //INSERT DATA PART END

    } // END of Oncreate


    //START OF VIEW

    //Recycler view punya class dan view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder{

        View myview;

        public MyViewHolder (View itemView){
            super(itemView);
            myview = itemView;
        }

        //Semua R.id. bawah ni ambik dari item_data.xml
        public void setLeafName(String name){
            TextView lName = myview.findViewById(R.id.name);
            //nak tukar texview kepada value dari database
            lName.setText(name);
        }

        public void setLeafDurian(String leafdurian){
            TextView lDurian = myview.findViewById(R.id.leafdurian);
            lDurian.setText(leafdurian);
        }

        public void setLeafCharacteristic(String characteristic){
            TextView mChar = myview.findViewById(R.id.characteristic);
            mChar.setText(characteristic);
        }
    }
    // end of recylerview class


    //untuk automatic fetch data dari database
    @Override
    protected void onStart() {
        super.onStart();

        //nak guna firebaserecyleradapter kena letak compile 'com.firebaseui:firebase-ui-database:0.4.0' kat  build.gradle (module:app)
        FirebaseRecyclerAdapter<Leaf, MyViewHolder> adapter = new FirebaseRecyclerAdapter<Leaf, MyViewHolder>(
                Leaf.class,  //Name of model class
                R.layout.item_data_leaf, //Row layout to show data
                MyViewHolder.class, //Name of viewholder class
                mDatabase // Database Reference
        ) {
            @Override
            protected void populateViewHolder(MyViewHolder viewHolder, final Leaf model, final int position) {

                //Your Method to load Data
                viewHolder.setLeafName(model.getLeafName());
                viewHolder.setLeafDurian(model.getLeafDurian());
                viewHolder.setLeafCharacteristic(model.getLeafCharacteristic());


                // Setting bila click kat card2 recycler view tu nk jadi apa
                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //post_key untuk dapatkan integer position (kita click tu kat mana) so xyah amik id sbb id x update pon
                        post_key = getRef(position).getKey(); //post_key ni = id

                        //Global Variable
                        lId = model.getLeafID();
                        name = model.getLeafName();
                        leafdurian = model.getLeafDurian();
                        characteristic = model.getLeafCharacteristic();

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



    //untuk update data dan show dialog dia
    public void  updateDeleteData(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(LeafActivity.this);
        LayoutInflater inflater = LayoutInflater.from(LeafActivity.this);

        View myview = inflater.inflate(R.layout.updatedeleteinputfield_leaf, null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();

        //collect data from field of updatedeleteinputfield
        updateLName = myview.findViewById(R.id.upd_leaf_name);
        updateLChr = myview.findViewById(R.id.upd_leaf_characteristic);
        final Spinner spinnerUpdate = myview.findViewById(R.id.spinnerLeafUpdate);

        //START Untuk populate value SPINNER
        Query query = durianDatabase.orderByChild("dName");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<String> titleList = new ArrayList<String>();
                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    String titlename = dataSnapshot1.child("dName").getValue(String.class);
                    titleList.add(titlename);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(LeafActivity.this, android.R.layout.simple_spinner_item, titleList);
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerUpdate.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(LeafActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        //END populate value SPINNER

        //ni untuk ambik apa yg ada kt recycler(item_data.xml) tu untuk letak kat dialog update
        //yg dalam setText tu dari Global Variable
        updateLName.setText(name);
        updateLName.setSelection(name.length());

        updateLChr.setText(characteristic);
        updateLChr.setSelection(characteristic.length());


        //instantiate button
        btnDeleteUp = myview.findViewById(R.id.btn_delete_upd);
        btnUpdateUp = myview.findViewById(R.id.btn_update_upd);

        btnUpdateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //variable ni dari global variable
                name = updateLName.getText().toString().trim();
                leafdurian = spinnerUpdate.getSelectedItem().toString();
                characteristic = updateLChr.getText().toString().trim();

                System.out.println(name);
                System.out.println(leafdurian);
                System.out.println(characteristic);
                System.out.println(post_key);

                //Call model constructor
                Leaf data = new Leaf(post_key,name,leafdurian,characteristic);

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
