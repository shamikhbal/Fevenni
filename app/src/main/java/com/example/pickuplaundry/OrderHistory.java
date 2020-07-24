package com.example.pickuplaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class OrderHistory extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

//    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference reff;
    String status;

    //Custom Navigation bar
    private Toolbar toolbar;
//    private DrawerLayout drawerLayout;
//    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);

        //Custom Navigation bar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseAuth=FirebaseAuth.getInstance();

        mRecyclerView = findViewById(R.id.rv_OrderRunner);
        new FirebaseDatabaseHelper().readOrders(new FirebaseDatabaseHelper.DataStatus() {
            @Override
            public void DataIsLoaded(List<Order> orders, final List<String> keys) {
                findViewById(R.id.pb_OrderRunner).setVisibility(View.GONE);
                new RecyclerView_Config().setConfig(mRecyclerView, OrderHistory.this, orders, keys);

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
                                                Toast.makeText(OrderHistory.this, "Order Successfully Deleted", Toast.LENGTH_SHORT).show();
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
                                                new RecyclerView_Config().setConfig(mRecyclerView, OrderHistory.this, orders, keys);
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
        }
        );

    }

    //    LogOut Menu Function
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(OrderHistory.this, MainActivity.class));
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
                Intent i = new Intent(OrderHistory.this, UserProfile.class);
                startActivity(i);
                break;
            }
            case R.id.makeOrder: {
                Intent i = new Intent(OrderHistory.this, Home.class);
                startActivity(i);
                break;
            }
            case R.id.orderHistory: {
                Intent i = new Intent(OrderHistory.this, OrderHistory.class);
                startActivity(i);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        FragmentManager fm  = getSupportFragmentManager();
        switch (menuItem.getItemId()){
            case R.id.nav_profile:

                Intent p = new Intent(OrderHistory.this, UserProfile.class);
                startActivity(p);
                break;
            case R.id.nav_createorder:

                Intent h = new Intent(OrderHistory.this, Home.class);
                startActivity(h);
                break;
            case R.id.nav_orderhistory:

                Intent o = new Intent(OrderHistory.this, OrderHistory.class);
                startActivity(o);
                break;
            case R.id.nav_logout:
                Logout();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        menuItem.setChecked(true);
        getSupportActionBar().setTitle("Title");
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }


}
