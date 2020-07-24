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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ComfirmOrder extends AppCompatActivity {
TextView shirt, pant, scarf, skirt, jacket, others, washingMachine, dryer, runnerFee, totalPrice;
Button comfirmOrder;
DatabaseReference reff, runnerReff, trackReff, genderReff, userInfo;
Order order;
int tshirt,pants, scarfs, Skirts, jackets, other, washMach, dry;
double sum;
private FirebaseAuth firebaseAuth;
String custName;
String custPhone;

    //Custom Navigation bar
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_order);

        //Custom Navigation bar
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_nav);

        drawerLayout=findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.nav_profile:
                        menuItem.setChecked(true);
                        Intent p = new Intent(ComfirmOrder.this, UserProfile.class);
                        startActivity(p);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_createorder:
                        menuItem.setChecked(true);
                        Intent h = new Intent(ComfirmOrder.this, Home.class);
                        startActivity(h);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_orderhistory:
                        menuItem.setChecked(true);
                        Intent o = new Intent(ComfirmOrder.this, OrderHistory.class);
                        startActivity(o);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        menuItem.setChecked(true);
                        Logout();
                        drawerLayout.closeDrawers();
                        return true;
                }

                return false;
            }
        });

        firebaseAuth=FirebaseAuth.getInstance();

        shirt = findViewById(R.id.ts_tv8);
        pant = findViewById(R.id.ts_tv9);
        scarf = findViewById(R.id.ts_tv4);
        skirt = findViewById(R.id.ts_tv3);
        jacket = findViewById(R.id.ts_tv5);
        others = findViewById(R.id.ts_tv6);
        washingMachine = findViewById(R.id.ts_tv7);
        dryer = findViewById(R.id.ts_tv10);
        totalPrice = findViewById(R.id.textView20);
        comfirmOrder=findViewById(R.id.btn_confirmorder);
        runnerFee=findViewById(R.id.ts_tv11);

        Intent intent = getIntent();

        final String pickLoc=intent.getStringExtra("PickLoc");
        final String delLoc=intent.getStringExtra("DelLoc");
        final String kol=intent.getStringExtra("Kol");
        final String pickDay=intent.getStringExtra("PickDay");
        final String pickTime=intent.getStringExtra("PickTime");
        final String delDay=intent.getStringExtra("DelDay");
        final String delTime=intent.getStringExtra("DelTime");

        tshirt = intent.getIntExtra("t-shirt",0);
        pants = intent.getIntExtra("Pant",0);
        scarfs = intent.getIntExtra("Scarf",0);
        Skirts = intent.getIntExtra("Skirt",0);
        jackets = intent.getIntExtra("Jacket",0);
        other = intent.getIntExtra("Other",0);
        washMach = intent.getIntExtra("Wash",0);
        dry = intent.getIntExtra("Dry",0);
        

        if(kol.equals("Kolej Pendeta Zaba (KPZ)")){
            sum = (washMach*5)+(dry*5)+3;
        }
        else if(kol.equals("Kolej BurhanuddinHelmi (KBH)")){
            sum = (washMach*4)+(dry*4)+3;
        }
        else if(kol.equals("Kolej Ibu Zain (KIZ)")){
            sum = (washMach*2.50)+(dry*2.50)+3;
        }
        else {
            sum = (washMach*2.50)+(dry*2.50)+3;
        }

        shirt.setText(""+tshirt);
        pant.setText(""+pants);
        scarf.setText(""+scarfs);
        skirt.setText(""+Skirts);
        jacket.setText(""+jackets);
        others.setText(""+other);
        washingMachine.setText(""+washMach);
        dryer.setText(""+dry);
        runnerFee.setText("RM3.00");
        totalPrice.setText("RM"+sum+"0");


//        Get User Gender & State Condition for Refference
        genderReff = FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
        genderReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String gend = dataSnapshot.child("gender").getValue().toString();
                custName = dataSnapshot.child("name").getValue().toString();
                custPhone = dataSnapshot.child("phone").getValue().toString();

                if(gend.equals("Male")){
                    runnerReff = FirebaseDatabase.getInstance().getReference().child("OrderMale");
                }
                else{
                    runnerReff = FirebaseDatabase.getInstance().getReference().child("OrderFemale");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        reff = FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Order");
//        runnerReff = FirebaseDatabase.getInstance().getReference().child(gender);
        order = new Order();

        comfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                order.setPickupLocation(pickLoc);
                order.setDeliveryLocation(delLoc);
                order.setKolej(kol);
                order.setpDay(pickDay);
                order.setpTime(pickTime);
                order.setpDay(delDay);
                order.setdDay(delDay);
                order.setdTime(delTime);

                order.setTshirt(tshirt);
                order.setPants(pants);
                order.setScarf(scarfs);
                order.setSkirt(Skirts);
                order.setJacket(jackets);
                order.setOthers(other);
                order.setWashingMachine(washMach);
                order.setDryers(dry);

                order.setTotalPrice(sum);

                String orderId = reff.push().getKey();
                reff.child(orderId).setValue(order);
                runnerReff.child(orderId).setValue(order);

                Toast.makeText(ComfirmOrder.this, "Order Successfully Created", Toast.LENGTH_LONG).show();
//                Intent i = new Intent(ComfirmOrder.this, OrderHistory.class);
//                i.putExtra("orderID",orderId);
//                startActivity(i);


                trackReff = FirebaseDatabase.getInstance().getReference("Track").child(orderId);

                trackReff.child("Pick Up").setValue("Pending");
                trackReff.child("Laundry").setValue("Pending");
                trackReff.child("Delivery").setValue("Pending");
                trackReff.child("customer name").setValue(custName);
                trackReff.child("customer phone").setValue(custPhone);
                trackReff.child("orderStatus").setValue("Pending");
            }
        });


    }

    //    LogOut Menu Function
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(ComfirmOrder.this, MainActivity.class));
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
                Intent i = new Intent(ComfirmOrder.this, UserProfile.class);
                startActivity(i);
                break;
            }
            case R.id.makeOrder: {
                Intent i = new Intent(ComfirmOrder.this, Home.class);
                startActivity(i);
                break;
            }
            case R.id.orderHistory: {
                Intent i = new Intent(ComfirmOrder.this, OrderHistory.class);
                startActivity(i);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
