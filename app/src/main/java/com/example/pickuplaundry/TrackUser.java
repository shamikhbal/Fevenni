package com.example.pickuplaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TrackUser extends AppCompatActivity {

    DatabaseReference reff, trackReff, cgenderReff, cRunnerReff, cTrackReff, cReff;
    FirebaseUser user;
    FirebaseDatabase order;
    private Button btnComplete;
    private Button btnCancel, btnReceipt;
    TextView pickup, laundry, delivery, runnerName, runnerPhone;
    private FirebaseAuth firebaseAuth;

    //Custom Navigation bar
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_user);

        //Custom Navigation bar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_nav);

        firebaseAuth=FirebaseAuth.getInstance();

        pickup = (TextView)findViewById(R.id.tv_pickup);
        laundry = (TextView)findViewById(R.id.tv_laundry);
        delivery = (TextView)findViewById(R.id.tv_delivery);
        btnCancel = findViewById(R.id.btn_cancel);
        btnComplete = findViewById(R.id.btn_complete);
        btnReceipt = findViewById(R.id.btn_receipt);
        runnerName =  findViewById(R.id.runner_name);
        runnerPhone = findViewById(R.id.runner_phone);


        Intent i = getIntent();
        final String orderId=i.getStringExtra("orderID");
        final String reffGender=i.getStringExtra("gender");

        trackReff = FirebaseDatabase.getInstance().getReference("Track").child(orderId);
        trackReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String status = dataSnapshot.child("orderStatus").getValue().toString();
                if(dataSnapshot.child("runner name").getValue()==null){
                    runnerName.setText("Pending");
                    btnComplete.setVisibility(View.GONE);
                    if(status.equals("Cancelled")){
                        btnCancel.setVisibility(View.GONE);
                    }
                    else {
                        btnCancel.setVisibility(View.VISIBLE);
                    }
                }
                else{
                    runnerName.setText(dataSnapshot.child("runner name").getValue().toString());
                    runnerPhone.setText(dataSnapshot.child("runner phone").getValue().toString());
                    btnCancel.setVisibility(View.GONE);
                    if(status.equals("Completed")){
                        btnComplete.setVisibility(View.GONE);
                        btnReceipt.setVisibility(View.VISIBLE);
                    }
                    else if(status.equals("Cancelled")){
                        btnComplete.setVisibility(View.GONE);
                        btnReceipt.setVisibility(View.GONE);
                    }
                    else{
                        btnComplete.setVisibility(View.VISIBLE);
                        btnReceipt.setVisibility(View.GONE);
                    }
                }

                String pick = dataSnapshot.child("Pick Up").getValue().toString();
                String laund = dataSnapshot.child("Laundry").getValue().toString();
                String del = dataSnapshot.child("Delivery").getValue().toString();


                pickup.setText(pick);
                laundry.setText(laund);
                delivery.setText(del);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//                        Get User Gender & State Condition for Refference
        cgenderReff = FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
        cgenderReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String gend = dataSnapshot.child("gender").getValue().toString();

                if(gend.equals("Male")){
//                    reffGender="OrderMale";
                    cRunnerReff = FirebaseDatabase.getInstance().getReference().child("OrderMale").child(orderId);
//                            cRunnerReff.removeValue();
                }
                else {
//                    reffGender="OrderFemale";
                    cRunnerReff = FirebaseDatabase.getInstance().getReference().child("OrderFemale").child(orderId);
//                            cRunnerReff.removeValue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        cReff = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Order").child(orderId);
        cTrackReff = FirebaseDatabase.getInstance().getReference("Track").child(orderId);
//        cRunnerReff = FirebaseDatabase.getInstance().getReference().child("OrderFemale").child(orderId);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent i = new Intent(TrackUser.this, OrderHistory.class);
//                startActivity(i);
                TrackUser.super.finish();

                cRunnerReff.removeValue();
//                cReff.removeValue();
//                cTrackReff.removeValue();

                reff = FirebaseDatabase.getInstance().getReference("Track").child(orderId);
                reff.child("orderStatus").setValue("Cancelled");

            }
        });

        btnComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reff = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Order").child(orderId);

//                Get Current Date Then Send Kt Table Order User
                String currentDate = new SimpleDateFormat("MM-dd-yyyy HH:mm", Locale.getDefault()).format(new Date());
                reff.child("Order Completed Date").setValue(currentDate);

//                update order status kt table user
                reff = FirebaseDatabase.getInstance().getReference("Track").child(orderId);
                reff.child("orderStatus").setValue("Completed");

                Intent i = new Intent(TrackUser.this, Receipt.class);
                i.putExtra("orderID",orderId);
                startActivity(i);
            }
        });

        btnReceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TrackUser.this, Receipt.class);
                i.putExtra("orderID",orderId);
                startActivity(i);
            }
        });btnReceipt.setVisibility(View.GONE);

    }

    //    LogOut Menu Function
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(TrackUser.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logoutMenu: {
                Logout();
                break;
            }
            case R.id.profile: {
                Intent i = new Intent(TrackUser.this, UserProfile.class);
                startActivity(i);
                break;
            }
            case R.id.makeOrder: {
                Intent i = new Intent(TrackUser.this, Home.class);
                startActivity(i);
                break;
            }
            case R.id.orderHistory: {
                Intent i = new Intent(TrackUser.this, OrderHistory.class);
                startActivity(i);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }


}
