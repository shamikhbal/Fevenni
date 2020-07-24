package com.example.pickuplaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackRunner extends AppCompatActivity {

    private ToggleButton btnd,btnl,btnp;
    private FirebaseAuth firebaseAuth;

    //Custom Navigation bar
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    TextView tshirt, scarf,pants, skirt, jacket, washingm,dryer,price,custName,custPhone;
    DatabaseReference dbreff;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_runner);

        //Custom Navigation bar
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Tracking");
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();

        tshirt = (TextView)findViewById(R.id.tshirt);
        scarf = (TextView)findViewById(R.id.scarf);
        pants = (TextView)findViewById(R.id.pants);
        skirt = (TextView)findViewById(R.id.skirt);
        jacket = (TextView)findViewById(R.id.jacket);
        washingm = (TextView)findViewById(R.id.washingmachine);
        dryer = (TextView)findViewById(R.id.dryer);
        price = (TextView)findViewById(R.id.totalprice);
        custName = findViewById(R.id.name);
        custPhone = findViewById(R.id.phone);

        Intent i = getIntent();
        String orderId=i.getStringExtra("orderID");


        dbreff = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Order").child(orderId);
        dbreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String tshirt1 = dataSnapshot.child("tshirt").getValue().toString();
                String scarf1 = dataSnapshot.child("scarf").getValue().toString();
                String pants1 = dataSnapshot.child("pants").getValue().toString();
                String skirt1 = dataSnapshot.child("skirt").getValue().toString();
                String jacket1 = dataSnapshot.child("jacket").getValue().toString();
                String washingm1 = dataSnapshot.child("washingMachine").getValue().toString();
                String dryer1 = dataSnapshot.child("dryers").getValue().toString();
                String totalprice1 = dataSnapshot.child("totalPrice").getValue().toString();

                tshirt.setText(tshirt1);
                scarf.setText(scarf1);
                pants.setText(pants1);
                skirt.setText(skirt1);
                jacket.setText(jacket1);
                washingm.setText(washingm1);
                dryer.setText(dryer1);
                price.setText(totalprice1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnp = (ToggleButton)findViewById(R.id.btn_p);
        btnl = (ToggleButton)findViewById(R.id.btn_l);
        btnd = (ToggleButton)findViewById(R.id.btn_d);

        //        Get User Gender & State Condition for Refference
        dbreff = FirebaseDatabase.getInstance().getReference("Track").child(orderId);
        dbreff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String pick = dataSnapshot.child("Pick Up").getValue().toString();
                String laund = dataSnapshot.child("Laundry").getValue().toString();
                String deliv = dataSnapshot.child("Delivery").getValue().toString();
                String name = dataSnapshot.child("customer name").getValue().toString();
                String phone = dataSnapshot.child("customer phone").getValue().toString();
                custName.setText(name);
                custPhone.setText(phone);
                if(pick.equals("Done")){
                    btnp.setChecked(true);
                }
                else{
                    btnp.setChecked(false);
                }

                if(laund.equals("Done")){
                    btnl.setChecked(true);
                }
                else{
                    btnl.setChecked(false);
                }

                if(deliv.equals("Done")){
                    btnd.setChecked(true);
                }
                else{
//                    btnp.setChecked(false);
//                    btnl.setChecked(false);
                    btnd.setChecked(false);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


        btnp.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (btnp.isChecked()){
                    dbreff.child("Pick Up").setValue("Done");
                }
            }
        });

        btnl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (btnl.isChecked()){
                    dbreff.child("Laundry").setValue("Done");
                }
            }
        });

        btnd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (btnd.isChecked()){
                    dbreff.child("Delivery").setValue("Done");
                }
            }
        });
    }
    //    LogOut Menu Function
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(TrackRunner.this, MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.runner_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout: {
                Logout();
            }
            case R.id.profile: {
                Intent i = new Intent(TrackRunner.this, RunnerProfile.class);
                startActivity(i);
                break;
            }
            case R.id.searchorder: {
                Intent i = new Intent(TrackRunner.this, RunnerOrderRV.class);
                startActivity(i);
                break;
            }
            case R.id.myorder: {
                Intent in = new Intent(TrackRunner.this, RunnerOrderHistory.class);
                startActivity(in);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}



