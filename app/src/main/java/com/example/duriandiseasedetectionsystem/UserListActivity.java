package com.example.duriandiseasedetectionsystem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.duriandiseasedetectionsystem.model.Farmer;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserListActivity extends AppCompatActivity {

    // kena import import androidx.appcompat.widget.Toolbar; sebab guna androidX
    private Toolbar toolbar;

    //Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    //Recyler..
    private RecyclerView recyclerView;

    //Variable
    private String post_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        //Setup Custom toolbar
        toolbar = findViewById(R.id.toolbar_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("List of User");

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
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Farmer");
        mDatabase.keepSynced(true); //keep synced ni untuk VIEW data recycler.. kalau nk INSERT data xyah letak ni lagi

        //Recyler.........................
        recyclerView = findViewById(R.id.recyler);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);

        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
    }
    //END of onCreate

    //START OF VIEW
    //untuk automatic fetch data dari database
    @Override
    protected void onStart() {
        super.onStart();

        //nak guna firebaserecyleradapter kena letak compile 'com.firebaseui:firebase-ui-database:0.4.0' kat  build.gradle (module:app)
        FirebaseRecyclerAdapter<Farmer, UserListActivity.MyViewHolder> adapter = new FirebaseRecyclerAdapter<Farmer, MyViewHolder>(
                Farmer.class,  //Name of model class
                R.layout.item_data_list_farmer, //Row layout to show data
                UserListActivity.MyViewHolder.class, //Name of viewholder class
                mDatabase // Database Reference
        ) {
            @Override
            protected void populateViewHolder(UserListActivity.MyViewHolder viewHolder, final Farmer model, final int position) {

                //Your Method to load Data
                viewHolder.setFarmerID(model.getFarmerID());
                viewHolder.setFarmerName(model.getFarmerName());
                viewHolder.setFarmerAddress(model.getFarmerAddress());
                viewHolder.setFarmerNoTel(model.getFarmerNoTel());
                viewHolder.setFarmerEmail(model.getFarmerEmail());
                viewHolder.setFarmerPassword(model.getFarmerPassword());


                // Setting bila click kat card2 recycler view tu nk jadi apa
                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //post_key untuk dapatkan integer position (kita click tu kat mana) so xyah amik id sbb id x update pon
                        post_key = getRef(position).getKey(); //post_key ni = id

                        //cmId = model.getCmID();
                        //cmName = model.getCmName();
                        //cmIns = model.getCmInstruction();

                        //call update dan delete data kt recyler view
                       // updateDeleteData();
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
        public void setFarmerID(String lsID){
            TextView lID = myview.findViewById(R.id.Farmer_id);
            //nak tukar texview kepada value dari database
            lID.setText(lsID);
        }

        public void setFarmerName(String lsName){
            TextView lName = myview.findViewById(R.id.Farmer_name);
            lName.setText(lsName);
        }

        public void setFarmerAddress(String lsAddress){
            TextView lAddress = myview.findViewById(R.id.Farmer_address);
            //nak tukar texview kepada value dari database
            lAddress.setText(lsAddress);
        }

        public void setFarmerNoTel(String lsNotel){
            TextView lNotel = myview.findViewById(R.id.Farmer_notel);
            lNotel.setText(lsNotel);
        }

        public void setFarmerEmail(String lsEmail){
            TextView lEmail = myview.findViewById(R.id.Farmer_email);
            //nak tukar texview kepada value dari database
            lEmail.setText(lsEmail);
        }

        public void setFarmerPassword(String lsPassword){
            TextView lPassword = myview.findViewById(R.id.Farmer_pass);
            lPassword.setText(lsPassword);
        }


    }
    // end of recylerview class
}
