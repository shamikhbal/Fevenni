package com.example.pickuplaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import static android.view.View.VISIBLE;

public class RunnerOrderRV extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reff, genderReff;
    TextView noOrder;

    //Custom Navigation bar
    private Toolbar toolbar;
//    private DrawerLayout drawerLayout;
//    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner_order_rv);

        //Custom Navigation bar
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Search Order");
        setSupportActionBar(toolbar);

//        drawerLayout=findViewById(R.id.drawer_layout);
//        navigationView=findViewById(R.id.navigationView);

        firebaseAuth=FirebaseAuth.getInstance();

        noOrder = findViewById(R.id.no_order);
        noOrder.setVisibility(VISIBLE);

                //        Get User Gender & State Condition for Refference
        genderReff = FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
        genderReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String gend = dataSnapshot.child("gender").getValue().toString();

                if(gend.equals("Male")){
                    mRecyclerView = findViewById(R.id.rv_OrderRunner);
                    new Runner_FirebaseDataHelper().readOrders(new Runner_FirebaseDataHelper.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Order> orders, List<String> keys) {
                            findViewById(R.id.pb_OrderRunner).setVisibility(View.GONE);
                            noOrder.setVisibility(View.GONE);
                            new RunnerRecyclerView_Config().setConfig(mRecyclerView, RunnerOrderRV.this, orders, keys);
                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
                else{
                    mRecyclerView = findViewById(R.id.rv_OrderRunner);
                    new Runner_FirebaseDataHelper_Female().readOrders(new Runner_FirebaseDataHelper_Female.DataStatus() {
                        @Override
                        public void DataIsLoaded(List<Order> orders, List<String> keys) {
                            findViewById(R.id.pb_OrderRunner).setVisibility(View.GONE);
                            new RunnerRecyclerView_Config().setConfig(mRecyclerView, RunnerOrderRV.this, orders, keys);
                        }

                        @Override
                        public void DataIsInserted() {

                        }

                        @Override
                        public void DataIsUpdated() {

                        }

                        @Override
                        public void DataIsDeleted() {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    //    LogOut Menu Function
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(RunnerOrderRV.this, MainActivity.class));
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
                Intent i = new Intent(RunnerOrderRV.this, RunnerProfile.class);
                startActivity(i);
                break;
            }
            case R.id.searchorder: {
                Intent i = new Intent(RunnerOrderRV.this, RunnerOrderRV.class);
                startActivity(i);
                break;
            }
            case R.id.myorder: {
                Intent in = new Intent(RunnerOrderRV.this, RunnerOrderHistory.class);
                startActivity(in);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

