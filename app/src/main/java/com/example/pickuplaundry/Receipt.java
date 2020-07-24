package com.example.pickuplaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Receipt extends AppCompatActivity {

    TextView tshirt, scarf,pants,skirt,jacket,washingm,dryer,price,date,orderID, rider;
    private FirebaseDatabase mFirebaseDatabase;
    DatabaseReference dbreff, trackReff;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);

        Intent i = getIntent();
        final String orderId=i.getStringExtra("orderID");

        tshirt = (TextView)findViewById(R.id.tshirt);
        scarf = (TextView)findViewById(R.id.scarf);
        pants = (TextView)findViewById(R.id.pants);
        skirt = (TextView)findViewById(R.id.skirt);
        jacket = (TextView)findViewById(R.id.jacket);
        washingm = (TextView)findViewById(R.id.washingmachine);
        dryer = (TextView)findViewById(R.id.dryer);
        price = (TextView)findViewById(R.id.price);
        date = findViewById(R.id.date);
        orderID = findViewById(R.id.order_id);
        rider = findViewById(R.id.rider);
        orderID.setText(orderId);

        trackReff = FirebaseDatabase.getInstance().getReference("Track").child(orderId);
        trackReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                rider.setText(dataSnapshot.child("runner name").getValue().toString().trim());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                String dateComplete = dataSnapshot.child("Order Completed Date").getValue().toString();

                tshirt.setText(tshirt1);
                scarf.setText(scarf1);
                pants.setText(pants1);
                skirt.setText(skirt1);
                jacket.setText(jacket1);
                washingm.setText(washingm1);
                dryer.setText(dryer1);
                price.setText("RM"+totalprice1);
                date.setText(dateComplete);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

