package com.example.sagar.classnotice;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.sagar.classnotice.HolderClass.DataAdapter;
import com.example.sagar.classnotice.ModelClass.NoticeModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView noticeRecyclerView;
    List<NoticeModel> notice;
    DataAdapter adapter;

    FloatingActionButton addNotice;

    DatabaseReference databaseReference;

    long count = 0;

    private static final String ID = "notify";
    private static final String NAME = "Class Notice";
    private static final String DESCRIPTION = "New notice is available.";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            NotificationChannel channel = new NotificationChannel(ID,NAME,NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(DESCRIPTION);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        addNotice = findViewById(R.id.addNotice);

        noticeRecyclerView = findViewById(R.id.noticeRecyclerView);
        noticeRecyclerView.setHasFixedSize(true);
        noticeRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getSupportActionBar().setTitle("Home");

        //service
        Intent serviceIntent = new Intent(MainActivity.this, MyService.class);
        ContextCompat.startForegroundService(MainActivity.this, serviceIntent);

        //checking network
        final CheckingNetwork checkingNetwork = new CheckingNetwork(this);
        if (!checkingNetwork.isConnected()){

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Network Error")
                    .setMessage("Internet Connection is Required to See New Notice")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }

        notice = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Class Notice");

        Query myNoticeQuery = databaseReference.orderByChild("count").limitToFirst(6);
        myNoticeQuery.keepSynced(true);


        addNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,AddActivity.class));
            }
        });


        //reading data
        myNoticeQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                notice.clear();

                for (DataSnapshot noticeSnap : dataSnapshot.getChildren()){

                    NoticeModel noticeModel = noticeSnap.getValue(NoticeModel.class);

                    notice.add(noticeModel);
                }

                adapter = new DataAdapter(getApplicationContext(),notice);
                noticeRecyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //getting data count for notification
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                long order = dataSnapshot.getChildrenCount();

                if (count == 0){
                    count = order;
                }

                else if (count>order){
                    count = order;
                }

                else if (order == 0){
                    count = order;
                }

                else if (count<order){
                    sendNotification();
                    count = order;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        final AlertDialog.Builder builderBack = new AlertDialog.Builder(this);
        builderBack.setMessage("Do you want to exit?");
        builderBack.setCancelable(true);
        builderBack.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builderBack.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialogBack = builderBack.create();
        alertDialogBack.show();
    }

    //menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    // menu item clicked
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id==R.id.item1){

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Developer")
                    .setMessage("Khan Md Sagar\nkhanmdsagar96@gmail.com")
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();

            return true;
        }
        return true;
    }

    private void sendNotification(){

        Intent intent = new Intent(MainActivity.this,MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this,
                100,intent,PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationCompat.Builder nBuilder =
                new NotificationCompat.Builder(this,ID)
                .setContentTitle("Class Notice")
                .setContentText("New notice is available")
                        .setSmallIcon(R.drawable.notification)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat nManager =  NotificationManagerCompat.from(this);
        nManager.notify(1, nBuilder.build());

    }
}
