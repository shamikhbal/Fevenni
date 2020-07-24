package com.example.pickuplaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class RunnerOrderHistory extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private FirebaseAuth firebaseAuth;

    //Custom Navigation bar
    private Toolbar toolbar;
//    private DrawerLayout drawerLayout;
//    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner_order_history);

        //Custom Toolbar & Navigationbar
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("My Order");
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();

        mRecyclerView = findViewById(R.id.rv_OrderRunner);
        new FirebaseDatabaseHelper().readOrders(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Order> orders, final List<String> keys) {
                findViewById(R.id.pb_OrderRunner).setVisibility(View.GONE);
                new RunnerHistory_RecyclerView_Config().setConfig(mRecyclerView, RunnerOrderHistory.this, orders, keys);

                new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull final RecyclerView.ViewHolder target, int i) {
                        new AlertDialog.Builder(target.itemView.getContext())
                                .setMessage("Are you sure want to delete this order?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        String orderId=keys.get(target.getAdapterPosition());
                                        new FirebaseDatabaseHelper().deleteData(orderId, new FirebaseDatabaseHelper.DataStatus() {
                                            @Override
                                            public void DataIsLoaded(List<Order> orders, List<String> keys) { }

                                            @Override
                                            public void DataIsInserted() { }

                                            @Override
                                            public void DataIsUpdated() { }

                                            @Override
                                            public void DataIsDeleted() {
                                                Toast.makeText(RunnerOrderHistory.this, "Order Successfully Deleted", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        new FirebaseDatabaseHelper().readOrders(new FirebaseDatabaseHelper.DataStatus() {
                                            @Override
                                            public void DataIsLoaded(List<Order> orders, final List<String> keys) {
                                                findViewById(R.id.pb_OrderRunner).setVisibility(View.GONE);
                                                new RunnerHistory_RecyclerView_Config().setConfig(mRecyclerView, RunnerOrderHistory.this, orders, keys);
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
                                                                                }
                                        );
                                    }
                                }).create().show();
                    }
                }).attachToRecyclerView(mRecyclerView);
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

//    LogOut Menu Function
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(RunnerOrderHistory.this, MainActivity.class));
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
                Intent i = new Intent(RunnerOrderHistory.this, RunnerProfile.class);
                startActivity(i);
                break;
            }
            case R.id.searchorder: {
                Intent i = new Intent(RunnerOrderHistory.this, RunnerOrderRV.class);
                startActivity(i);
                break;
            }
            case R.id.myorder: {
                Intent in = new Intent(RunnerOrderHistory.this, RunnerOrderHistory.class);
                startActivity(in);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
