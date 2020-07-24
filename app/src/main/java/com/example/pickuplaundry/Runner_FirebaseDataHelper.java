package com.example.pickuplaundry;

import android.graphics.Path;
import android.icu.text.RelativeDateTimeFormatter;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Runner_FirebaseDataHelper {
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reff, genderReff;
    private List<Order> orders = new ArrayList<>();
    String gender;
//    private OrderDetail orderID=new OrderDetail();

    public interface DataStatus{
        void DataIsLoaded(List<Order> orders, List<String> keys);
        void DataIsInserted();
        void DataIsUpdated();
        void DataIsDeleted();
    }

    public Runner_FirebaseDataHelper() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        reff = firebaseDatabase.getReference("OrderMale");
    }

    public void readOrders(final Runner_FirebaseDataHelper.DataStatus dataStatus){
        reff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                orders.clear();
                List<String> keys = new ArrayList<>();
                for(DataSnapshot keyNode: dataSnapshot.getChildren()){
                    keys.add(keyNode.getKey());
                    Order order = keyNode.getValue(Order.class);
                    orders.add(order);

                }
                dataStatus.DataIsLoaded(orders,keys);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
