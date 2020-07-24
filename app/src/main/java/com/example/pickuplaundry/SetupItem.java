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

public class SetupItem extends AppCompatActivity {
    Button btnSubmit;
    TextView tshirt, pant, skirt, scarf, jacket, other, washMachine, dryer;
    private FirebaseAuth firebaseAuth;

    //Custom Navigation bar
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_item);

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
                        Intent p = new Intent(SetupItem.this, UserProfile.class);
                        startActivity(p);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_createorder:
                        menuItem.setChecked(true);
                        Intent h = new Intent(SetupItem.this, Home.class);
                        startActivity(h);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_orderhistory:
                        menuItem.setChecked(true);
                        Intent o = new Intent(SetupItem.this, OrderHistory.class);
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

        btnSubmit = findViewById(R.id.btn_next);
        tshirt = findViewById(R.id.tshirtQ);
        pant = findViewById(R.id.pantQ);
        skirt = findViewById(R.id.skirtQ);
        scarf = findViewById(R.id.scarfQ);
        jacket = findViewById(R.id.jacketQ);
        other = findViewById(R.id.otherQ);
        washMachine = findViewById(R.id.washingMachineQ);
        dryer = findViewById(R.id.dryerQ);

        Intent i = getIntent();
        final String pickLoc=i.getStringExtra("pickLoc");
        final String delLoc=i.getStringExtra("delLoc");
        final String kol=i.getStringExtra("kol");
        final String pickDay=i.getStringExtra("pickDay");
        final String pickTime=i.getStringExtra("pickTime");
        final String delDay=i.getStringExtra("delDay");
        final String delTime=i.getStringExtra("delTime");


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int tshirtq = 0;
                int pantq = 0;
                int skirtq = 0;
                int scarfq = 0;
                int jacketq = 0;
                int otherq = 0;
                int washq = 0;
                int dryq = 0;

                 tshirtq = Integer.parseInt(tshirt.getText().toString());
                 pantq = Integer.parseInt(pant.getText().toString());
                 skirtq = Integer.parseInt(skirt.getText().toString());
                 scarfq = Integer.parseInt(scarf.getText().toString());
                 jacketq = Integer.parseInt(jacket.getText().toString());
                 otherq = Integer.parseInt(other.getText().toString());
                 washq = Integer.parseInt(washMachine.getText().toString());
                 dryq = Integer.parseInt(dryer.getText().toString());

                 int totalItem = tshirtq + pantq + skirtq + scarfq + jacketq + otherq;

                 if(washq<=0 || dryq<=0){
                     Toast.makeText(SetupItem.this, "Washing Machine & Dryer Cannot Be Less Than 1", Toast.LENGTH_SHORT).show();
                 }
                 else if(washq>5 || dryq>5){
                     Toast.makeText(SetupItem.this, "Washing Machine & Dryer Cannot Be More Than 5", Toast.LENGTH_SHORT).show();
                 }
                 else if(totalItem >30 && (washq==1 || dryq==1)){
                     Toast.makeText(SetupItem.this, "Too Many Item For 1 Washing Machine & Dryer", Toast.LENGTH_SHORT).show();
                 }
                 else if(totalItem >60 && (washq==2 || dryq==2)){
                     Toast.makeText(SetupItem.this, "Too Many Item For 2 Washing Machine & Dryer", Toast.LENGTH_SHORT).show();
                 }
                 else if(totalItem >90 && (washq==3 || dryq==3)){
                     Toast.makeText(SetupItem.this, "Too Many Item For 3 Washing Machine & Dryer", Toast.LENGTH_SHORT).show();
                 }
                 else if(totalItem >120 && (washq==4 || dryq==4)){
                     Toast.makeText(SetupItem.this, "Too Many Item For 4 Washing Machine & Dryer", Toast.LENGTH_SHORT).show();
                 }
                 else if(totalItem >150 && (washq==5 || dryq==5)){
                     Toast.makeText(SetupItem.this, "Too Many Item For 5 Washing Machine & Dryer", Toast.LENGTH_SHORT).show();
                 }
                 else{
                     Intent intent = new Intent(SetupItem.this,ComfirmOrder.class);

//                Time and Location Setting
                     intent.putExtra("PickLoc", pickLoc);
                     intent.putExtra("DelLoc", delLoc);
                     intent.putExtra("Kol", kol);
                     intent.putExtra("PickDay", pickDay);
                     intent.putExtra("PickTime", pickTime);
                     intent.putExtra("DelDay", delDay);
                     intent.putExtra("DelTime", delTime);

//                Setup Item
                     intent.putExtra("t-shirt", tshirtq);
                     intent.putExtra("Pant", pantq);
                     intent.putExtra("Skirt", skirtq);
                     intent.putExtra("Scarf", scarfq);
                     intent.putExtra("Jacket", jacketq);
                     intent.putExtra("Other", otherq);
                     intent.putExtra("Wash", washq);
                     intent.putExtra("Dry", dryq);

                     startActivity(intent);
                 }

            }
        });

    }

    //    LogOut Menu Function
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(SetupItem.this, MainActivity.class));
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
                Intent i = new Intent(SetupItem.this, UserProfile.class);
                startActivity(i);
                break;
            }
            case R.id.makeOrder: {
                Intent i = new Intent(SetupItem.this, Home.class);
                startActivity(i);
                break;
            }
            case R.id.orderHistory: {
                Intent i = new Intent(SetupItem.this, OrderHistory.class);
                startActivity(i);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

