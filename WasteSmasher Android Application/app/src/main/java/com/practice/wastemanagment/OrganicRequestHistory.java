package com.practice.wastemanagment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.practice.wastemanagment.Adapter.organicRequestAdapter;

import java.util.ArrayList;

public class OrganicRequestHistory extends AppCompatActivity implements
        AdapterView.OnItemSelectedListener  {

    String[] types = { "Organic Waste", "Inorganic Waste"};

    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;
    organicRequestAdapter adapter;
    ArrayList<OrganicWaste> list;
    String type;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organic_request_history);

        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,types);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spin.setAdapter(aa);
       }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        type=types[position];
        //Toast.makeText(OrganicRequestHistory.this,type,Toast.LENGTH_LONG).show();
        if(type==types[0]){
            recyclerView=findViewById(R.id.organic_history_list);
            databaseReference= FirebaseDatabase.getInstance().getReference("OrganicRequest");
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
            uid=firebaseUser.getUid();

            list=new ArrayList<>();
            adapter=new organicRequestAdapter(this, list);
            recyclerView.setAdapter(adapter);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        OrganicWaste history=snapshot1.getValue(OrganicWaste.class);
                        if(uid.toString().equals(history.userId.toString())) {
                            list.add(history);
                            //Toast.makeText(OrganicRequestHistory.this,history.userId,Toast.LENGTH_LONG).show();
                        }

                    }
                    adapter.notifyDataSetChanged();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            recyclerView=findViewById(R.id.organic_history_list);
            databaseReference= FirebaseDatabase.getInstance().getReference("InorganicRequest");
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
            uid=firebaseUser.getUid();

            list=new ArrayList<>();
            adapter=new organicRequestAdapter(this, list);
            recyclerView.setAdapter(adapter);

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for(DataSnapshot snapshot1:snapshot.getChildren()){
                        OrganicWaste history=snapshot1.getValue(OrganicWaste.class);
                        if(uid.toString().equals(history.userId.toString())) {
                            list.add(history);
                            //Toast.makeText(OrganicRequestHistory.this,history.userId,Toast.LENGTH_LONG).show();
                        }

                    }
                    adapter.notifyDataSetChanged();
                }


                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}