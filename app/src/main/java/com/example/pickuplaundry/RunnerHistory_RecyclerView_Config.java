package com.example.pickuplaundry;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class RunnerHistory_RecyclerView_Config {

    private Context mContext;
    private RunnerHistory_RecyclerView_Config.OrderAdapter mOrdersAdapter;
    private DatabaseReference statusReff;

    public void setConfig (RecyclerView recyclerView, Context context, List<Order> orders, List<String> keys){

        mContext = context;
        mOrdersAdapter = new RunnerHistory_RecyclerView_Config.OrderAdapter(orders,keys);
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
        private ImageView orderStatus;

        private String keys;

        public OrderItemView(ViewGroup parent){
            super(LayoutInflater.from(mContext). inflate (R.layout.order_list_item,parent,false));

            pDay= (TextView) itemView.findViewById(R.id.tv_pDay);
            pTime= (TextView) itemView.findViewById(R.id.tv_pTime);
            pLocation= (TextView) itemView.findViewById(R.id.tv_pLocation);
            dDay= (TextView) itemView.findViewById(R.id.tv_dDay);
            dTime= (TextView) itemView.findViewById(R.id.tv_dTime);
            dLocation= (TextView) itemView.findViewById(R.id.tv_dLocation);
            cLaundry= (TextView) itemView.findViewById(R.id.tv_cLaundry);
            orderStatus= (ImageView) itemView.findViewById(R.id.status);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(mContext, TrackRunner.class);
                    i.putExtra("orderID",keys);

                    mContext.startActivity(i);
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

            //                    Get User Gender & State Condition for Refference
            statusReff = FirebaseDatabase.getInstance().getReference("Track").child(key);
            statusReff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String status = dataSnapshot.child("orderStatus").getValue().toString();
                    if(status.equals("Cancelled")){
                        orderStatus.setImageResource(R.drawable.ic_clear_black_24dp);
                    }
                    else if(status.equals("Completed")){
                        orderStatus.setImageResource(R.drawable.ic_complete);
                    }
                    else {
                        orderStatus.setImageResource(R.drawable.ic_pending);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            this.keys=key;
        }
    }
    class OrderAdapter extends RecyclerView.Adapter<RunnerHistory_RecyclerView_Config.OrderItemView>{
        private List<Order> mOrderList;
        private List<String> mKey;

        public OrderAdapter(List<Order> mOrderList, List<String> mKey) {
            this.mOrderList = mOrderList;
            this.mKey = mKey;
        }

        @NonNull
        @Override
        public RunnerHistory_RecyclerView_Config.OrderItemView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new RunnerHistory_RecyclerView_Config.OrderItemView(parent);
        }

        @Override
        public void onBindViewHolder(@NonNull RunnerHistory_RecyclerView_Config.OrderItemView holder, final int position) {
            holder.bind(mOrderList.get(position), mKey.get(position));
            //Intent intent = new Intent(mContext,OrderDetails.class);
            //intent.putExtra("pDay",pDay.get(position));
            //intent.putExtra("pTime",pTime.get(position));
//            ConstraintLayout parent = (ConstraintLayout) holder.title.getParent();
//            parent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Toast.makeText(mContext,  "Clicked recycler view item at position " + position, Toast.LENGTH_SHORT).show();
//                }
//            });
        }


        @Override
        public int getItemCount() {
            return mOrderList.size();
        }
    }
}


