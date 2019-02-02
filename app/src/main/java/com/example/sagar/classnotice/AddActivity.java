package com.example.sagar.classnotice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sagar.classnotice.ModelClass.NoticeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Calendar;

public class AddActivity extends AppCompatActivity {

    DatabaseReference databaseReference;
    DatabaseReference sRef;

    String sec;
    private long count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        final TextView noticeDate = findViewById(R.id.noticeDate);
        final TextView noticeTime = findViewById(R.id.noticeTime);

        final EditText security = findViewById(R.id.security);
        final EditText noticeText = findViewById(R.id.noticeText);

        Button noticeUpdate = findViewById(R.id.noticeUpdate);

        //setting date
        Calendar calendar = Calendar.getInstance();
        final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        //setting time
        Calendar calendar2 = Calendar.getInstance();
        final String currentTime = DateFormat.getTimeInstance().format(calendar2.getTime());

        noticeDate.setText(currentDate);
        noticeTime.setText(currentTime);

        //checking network
        final CheckingNetwork checkingNetwork = new CheckingNetwork(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("Class Notice");

        getSupportActionBar().setTitle("Add Notice");

        //getting security code
        sRef = FirebaseDatabase.getInstance().getReference("Security").child("postSecurity");

        sRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                sec = value;
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });

        //setting post number
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){

                    long order = dataSnapshot.getChildrenCount();

                    count = - order;
                }else {

                    count = 1;
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        noticeUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String date,time,secur,text;

                date = noticeDate.getText().toString();
                time = noticeTime.getText().toString();
                secur = security.getText().toString();
                text = noticeText.getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference("Class Notice");

                if (TextUtils.isEmpty(secur)){

                    Snackbar.make(v,"Security Code is Required",Snackbar.LENGTH_SHORT).show();
                }

                else if (TextUtils.isEmpty(text)){

                    Snackbar.make(v,"Notice Text is Required",Snackbar.LENGTH_SHORT).show();
                }

                else if (!checkingNetwork.isConnected()){

                    Snackbar.make(v,"Internet Connection is Required",Snackbar.LENGTH_SHORT).show();
                }

                else if (sec.equals(secur)){

                    String id = databaseReference.push().getKey();

                    NoticeModel noticeModel = new NoticeModel(id,date,time,text,count);

                    databaseReference.child(id).setValue(noticeModel);

                    Toast.makeText(AddActivity.this, "Notice added successfully", Toast.LENGTH_SHORT).show();

                }

                else {

                    Snackbar.make(v,"Check Security Code",Snackbar.LENGTH_SHORT).show();
                }

            }

        });

        //back button
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

}
