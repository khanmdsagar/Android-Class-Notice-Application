package com.example.sagar.classnotice;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sagar.classnotice.ModelClass.NoticeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditActivity extends AppCompatActivity {

    EditText noticeText,security;
    Button noticeUpdate,noticeDelete;

    DatabaseReference sRef;
    DatabaseReference databaseReference;

    String sec;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        noticeText = findViewById(R.id.noticeText);
        security = findViewById(R.id.security);
        noticeUpdate = findViewById(R.id.noticeUpdate);
        noticeDelete = findViewById(R.id.noticeDelete);

        Intent i = getIntent();
        final String text = i.getStringExtra("Text");
        final String count = i.getStringExtra("Count");

        final long count2 = Long.parseLong(count);

        final String date = i.getStringExtra("Date");
        final String time = i.getStringExtra("Time");
        final String id = i.getStringExtra("Id");

        noticeText.setText(text);

        getSupportActionBar().setTitle("Edit Notice");

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


        noticeUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text,secure;

                text = noticeText.getText().toString();
                secure = security.getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference("Class Notice").child(id);

                if (TextUtils.isEmpty(secure)){

                    Snackbar.make(v,"Please Enter Security Code",Snackbar.LENGTH_SHORT).show();
                }

                else if (sec.equals(secure)){

                    NoticeModel noticeModel = new NoticeModel(id,date,time,text,count2);

                    databaseReference.setValue(noticeModel);

                    Toast.makeText(EditActivity.this, "Notice Updated Successfully", Toast.LENGTH_SHORT).show();

//                    Intent intent = new Intent(EditActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
                }

                else {

                    Snackbar.make(v,"Check Security Code",Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        noticeDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String secur;

                secur = security.getText().toString();

                databaseReference = FirebaseDatabase.getInstance().getReference("Class Notice").child(id);

                if (TextUtils.isEmpty(secur)){

                    Snackbar.make(v,"Please Enter Security Code",Snackbar.LENGTH_SHORT).show();
                }

                else if (sec.equals(secur)) {
                    databaseReference.removeValue();

                    Toast.makeText(EditActivity.this, "Notice Deleted Successfully", Toast.LENGTH_SHORT).show();

//                    Intent intent = new Intent(EditActivity.this,MainActivity.class);
//                    startActivity(intent);
//                    finish();
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
