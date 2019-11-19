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
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.duriandiseasedetectionsystem.model.ControlMeasures;
        import com.example.duriandiseasedetectionsystem.model.Durian;
        import com.firebase.ui.database.FirebaseRecyclerAdapter;
        import com.google.android.material.floatingactionbutton.FloatingActionButton;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;



public class ControlMeasuresActivity extends AppCompatActivity {

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
    private EditText updateCMName;
    private EditText updateCMIns;
    private Button btnDeleteUp;
    private Button btnUpdateUp;

    //Variable
    private String cmId;
    private String cmName;
    private String cmIns;
    private String post_key;

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
                //lepas inflate letak xml view mana yg nk di letak (custominputfield yg sendiri design)
                View myview = inflater.inflate(R.layout.custominputfield_control_measures, null);
                //massukan view tu dalam alert dialog
                myDialog.setView(myview);
                //nak sambungkan 2 input field dalam custom dialog tu dan create dialog tu
                final AlertDialog dialog = myDialog.create();

                //panggil data dari xml alert dialog tu. ni untuk INSERT
                final EditText addCMName = myview.findViewById(R.id.add_cm_name);
                final EditText addCMInstruction = myview.findViewById(R.id.add_cm_instruction);


                Button btnSave = myview.findViewById(R.id.btn_save);

                //CREATE control measure
                btnSave.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String cmName = addCMName.getText().toString().trim();
                        String cmIns = addCMInstruction.getText().toString().trim();


                        // Error Checking
                        if (TextUtils.isEmpty(cmName)){
                            addCMName.setError("Name is required..");
                            return;
                        }

                        if (TextUtils.isEmpty(cmIns)){
                            addCMInstruction.setError("Species is required..");
                            return;
                        }

                        //create randomkey untuk id
                        String cmID = mDatabase.push().getKey();

                        //Call model constructor
                        ControlMeasures data = new ControlMeasures(cmID, cmName, cmIns);

                        //masuk kan value dalam id tu
                        mDatabase.child(cmID).setValue(data);

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
                viewHolder.setCmInstruction(model.getCmInstruction());



                // Setting bila click kat card2 recycler view tu nk jadi apa
                viewHolder.myview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //post_key untuk dapatkan integer position (kita click tu kat mana) so xyah amik id sbb id x update pon
                        post_key = getRef(position).getKey(); //post_key ni = id

                        cmId = model.getCmID();
                        cmName = model.getCmName();
                        cmIns = model.getCmInstruction();

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
        public void setCmName(String cmName){
            TextView mName = myview.findViewById(R.id.cmName);
            //nak tukar texview kepada value dari database
            mName.setText(cmName);
        }

        public void setCmInstruction(String cmInstruction){
            TextView CMInstruction = myview.findViewById(R.id.cmIns);
            CMInstruction.setText(cmInstruction);
        }

    }
    // end of recylerview class

    //untuk update data dan show dialog dia
    public void  updateDeleteData(){

        AlertDialog.Builder mydialog = new AlertDialog.Builder(ControlMeasuresActivity.this);
        LayoutInflater inflater = LayoutInflater.from(ControlMeasuresActivity.this);

        View myview = inflater.inflate(R.layout.updatedeleteinputfield_control_measures, null);
        mydialog.setView(myview);

        final AlertDialog dialog = mydialog.create();

        //collect data from field of updatedeleteinputfield
        updateCMName = myview.findViewById(R.id.upd_cm_name);
        updateCMIns = myview.findViewById(R.id.upd_cm_instruction);

        System.out.println("New CM name" + updateCMName);

        //ni untuk ambik apa yg ada kt recycler(item_data.xml) tu untuk letak kat dialog update
        updateCMName.setText(cmName);
        updateCMName.setSelection(cmName.length());

        updateCMIns.setText(cmIns);
        updateCMIns.setSelection(cmIns.length());



        //instantiate button
        btnDeleteUp = myview.findViewById(R.id.btn_delete_upd);
        btnUpdateUp = myview.findViewById(R.id.btn_update_upd);

        btnUpdateUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //variable ni dari global variable
                cmName = updateCMName.getText().toString().trim();
                cmIns = updateCMIns.getText().toString().trim();

                System.out.println(cmName);
                System.out.println(cmIns);
                System.out.println(post_key);

                //Call model constructor
                ControlMeasures data = new ControlMeasures(post_key,cmName,cmIns);

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
