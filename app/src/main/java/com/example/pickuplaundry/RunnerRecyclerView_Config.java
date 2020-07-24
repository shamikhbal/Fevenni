package com.example.pickuplaundry;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RunnerRecyclerView_Config {

    private Context mContext;
    private RunnerRecyclerView_Config.OrderAdapter mOrdersAdapter;
    private DatabaseReference reff1, reff2, genderReff, runnerInfo, trackRunnerInfo;

    public void setConfig (RecyclerView recyclerView, Context context, List<Order> orders, List<String> keys){

        mContext = context;
        mOrdersAdapter = new RunnerRecyclerView_Config.OrderAdapter(orders,keys);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(mOrdersAdapter);
    }

    class OrderItemView extends RecyclerView.ViewHolder{
        private TextView pDay;
        private TextView pTime;
        private TextView pLocation;
        private TextView dDay;
        private TextView dTime;
        private TextView dLocation;
        private TextView cLaundry;
        private Button accept;

        private String keys;
        String gender;
        String runnerName, runnerPhone;
        int totalOrder, latestTotalOrder;

        public OrderItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext). inflate (R.layout.runner_order_list_item,parent,false));

            pDay= (TextView) itemView.findViewById(R.id.tv_pDay);
            pTime= (TextView) itemView.findViewById(R.id.tv_pTime);
            pLocation= (TextView) itemView.findViewById(R.id.tv_pLocation);
            dDay= (TextView) itemView.findViewById(R.id.tv_dDay);
            dTime= (TextView) itemView.findViewById(R.id.tv_dTime);
            dLocation= (TextView) itemView.findViewById(R.id.tv_dLocation);
            cLaundry= (TextView) itemView.findViewById(R.id.tv_cLaundry);
            accept= (Button) itemView.findViewById(R.id.btn_accept);

            //        Get User Gender & State Condition for Refference
            genderReff = FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
            genderReff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String gend = dataSnapshot.child("gender").getValue().toString();
                    if(gend.equals("Male")){
                        gender="OrderMale";
                        if(dataSnapshot.child("total order").getValue()==null){
                            genderReff.child("total order").setValue("0");
                        }
                        else{
                            totalOrder=Integer.parseInt(dataSnapshot.child("total order").getValue().toString());
                        }
                    }
                    else{
                        gender="OrderFemale";
                        if(dataSnapshot.child("total order").getValue()==null){
                            genderReff.child("total order").setValue("0");
                        }
                        else{
                            totalOrder=Integer.parseInt(dataSnapshot.child("total order").getValue().toString());

                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });


            accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

//                    firebase reffs
                    reff1 = FirebaseDatabase.getInstance().getReference(gender).child(keys);
                    reff2 = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Order").child(keys);

//                    copy user object
                    ValueEventListener valueEventListener = new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            reff2.setValue(dataSnapshot.getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isComplete()) {
                                        Log.d(TAG, "Success!");
                                    } else {
                                        Log.d(TAG, "Copy failed!");
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {}
                    };
                    reff1.addListenerForSingleValueEvent(valueEventListener);
                    reff1.removeValue();

                    latestTotalOrder=totalOrder+1;
                    genderReff.child("total order").setValue(latestTotalOrder);

                    trackRunnerInfo = FirebaseDatabase.getInstance().getReference("Track").child(keys);
                    runnerInfo = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
                    runnerInfo.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            trackRunnerInfo.child("runner name").setValue(dataSnapshot.child("name").getValue().toString());
                            trackRunnerInfo.child("runner phone").setValue(dataSnapshot.child("phone").getValue().toString());
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }
            });

        }

        public void bind(Order order, String key){
            pDay.setText(order.getpDay());
            pTime.setText(order.getpTime());
            pLocation.setText(order.getPickupLocation());
            dDay.setText(order.getdDay());
            dTime.setText(order.getdTime());
            dLocation.setText(order.getDeliveryLocation());
            cLaundry.setText(order.getKolej());
            this.keys=key;
        }
    }
    class OrderAdapter extends RecyclerView.Adapter<RunnerRecyclerView_Config.OrderItemView>{
        private List<Order> mOrderList;
        private List<String> mKey;

        public OrderAdapter(List<Order> mOrderList, List<String> mKey) {
            this.mOrderList = mOrderList;
            this.mKey = mKey;
        }

        @NonNull
        @Override
        public RunnerRecyclerView_Config.OrderItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RunnerRecyclerView_Config.OrderItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RunnerRecyclerView_Config.OrderItemView holder, final int position) {
            holder.bind(mOrderList.get(position), mKey.get(position));
        }


        @Override
        public int getItemCount() {
            return mOrderList.size();
        }
    }
}

