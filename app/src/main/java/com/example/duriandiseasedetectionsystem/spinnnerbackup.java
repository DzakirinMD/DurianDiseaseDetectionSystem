//package com.example.duriandiseasedetectionsystem;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//        import android.os.Bundle;
//        import android.widget.ArrayAdapter;
//        import android.widget.Spinner;
//        import android.widget.Toast;
//
//        import com.google.firebase.database.DataSnapshot;
//        import com.google.firebase.database.DatabaseError;
//        import com.google.firebase.database.DatabaseReference;
//        import com.google.firebase.database.FirebaseDatabase;
//        import com.google.firebase.database.Query;
//        import com.google.firebase.database.ValueEventListener;
//
//        import java.util.ArrayList;
//        import java.util.List;
//
//
//
//        // Xpakaipun java file ni
//
//public class spinnnerbackup extends AppCompatActivity {
//    //SPINNER STUFF https://inducesmile.com/android-programming/how-to-populate-spinner-with-android-firebase-database/
//    private Spinner spinner;
//    private DatabaseReference mDatabase;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_disease);
//
//        mDatabase = FirebaseDatabase.getInstance().getReference().child("Durian");
//        spinner = (Spinner)findViewById(R.id.spinnerLeaf);
//
//        Query query = mDatabase.orderByChild("dName");
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                final List<String> titleList = new ArrayList<String>();
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
//                    String titlename = dataSnapshot1.child("dName").getValue(String.class);
//                    titleList.add(titlename);
//                }
//                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(spinnnerbackup.this, android.R.layout.simple_spinner_item, titleList);
//                arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                spinner.setAdapter(arrayAdapter);
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(spinnnerbackup.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
//            }
//        });
//
//    }
//}
