package com.example.pickuplaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Calendar;

public class Home extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    EditText txtPickupLocation,txtDeliveryLocation;
    Button btnNext;
    TextView pickUpDate, pickUpTime, deliveryDate, deliveryTime;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener firebaseAuthListener;

    //Custom Navigation bar
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

//    DatabaseReference reff;
//    SetupLaundry setupLaundry;

//    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
                        Intent p = new Intent(Home.this, UserProfile.class);
                        startActivity(p);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_createorder:
                        menuItem.setChecked(true);
                        Intent h = new Intent(Home.this, Home.class);
                        startActivity(h);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_orderhistory:
                        menuItem.setChecked(true);
                        Intent o = new Intent(Home.this, OrderHistory.class);
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

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);


        pickUpDate=findViewById(R.id.pDate);
        pickUpTime=findViewById(R.id.pTime);
        deliveryDate=findViewById(R.id.dDate);
        deliveryTime=findViewById(R.id.dTime);


        txtPickupLocation = (EditText)findViewById(R.id.pickup_location);
        txtDeliveryLocation = (EditText)findViewById(R.id.delivery_location);
        btnNext = (Button)findViewById(R.id.btn_next) ;


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(txtPickupLocation.getText().toString().isEmpty() || txtDeliveryLocation.getText().toString().isEmpty() || radioGroup.getCheckedRadioButtonId()==-1 ||
                        pickUpDate.getText().toString().isEmpty() || pickUpTime.getText().toString().isEmpty() || deliveryDate.getText().toString().isEmpty() ||
                        deliveryTime.getText().toString().isEmpty() || pickUpDate.getText().toString().equals("Set Pickup Date") || pickUpTime.getText().toString().equals("Set Pickup Time") ||
                        deliveryDate.getText().toString().equals("Set Delivery Date") || deliveryTime.getText().toString().equals("Set Delivery Time")){
                    Toast.makeText(Home.this, "Please Complete The Information", Toast.LENGTH_SHORT).show();
                }
                else{
                    Intent i = new Intent(Home.this, SetupItem.class);
                    i.putExtra("pickLoc",txtPickupLocation.getText().toString().trim());
                    i.putExtra("delLoc", txtDeliveryLocation.getText().toString().trim());
                    i.putExtra("kol", radioButton.getText().toString().trim());
                    i.putExtra("pickDay", pickUpDate.getText().toString().trim());
                    i.putExtra("pickTime", pickUpTime.getText().toString().trim());
                    i.putExtra("delDay", deliveryDate.getText().toString().trim());
                    i.putExtra("delTime", deliveryTime.getText().toString().trim());

                    startActivity(i);
                }

            }
        });

        pickUpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePickupDate();
            }
        });

        pickUpTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handlePickupTime();
            }
        });

        deliveryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDeliveryDate();
            }
        });

        deliveryTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDeliveryTime();
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }


//  Handle PickupDate&Time
    private void handlePickupDate(){
        final Calendar calendar = Calendar.getInstance();

        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);

                CharSequence charSequence = DateFormat.format("EEE, dd MMM yyyy", calendar1);
                pickUpDate.setText(charSequence);
            }
        }, YEAR, MONTH, DATE);
        datePickerDialog.show();
    }
    private void handlePickupTime(){
        Calendar calendar=Calendar.getInstance();

        int HOUR=calendar.get(Calendar.HOUR_OF_DAY);
        int MINUTE=calendar.get(Calendar.MINUTE);

        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(Calendar.HOUR, hourOfDay);
                calendar2.set(Calendar.MINUTE, minute);

                CharSequence charSequence = DateFormat.format("hh:mm a", calendar2);
                pickUpTime.setText(charSequence);
            }
        }, HOUR, MINUTE, is24HourFormat);
        timePickerDialog.show();
    }

//    Handle DeliveryDate&Time
private void handleDeliveryDate(){
    final Calendar calendar = Calendar.getInstance();

    int YEAR = calendar.get(Calendar.YEAR);
    int MONTH = calendar.get(Calendar.MONTH);
    int DATE = calendar.get(Calendar.DATE);

    DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int date) {

            Calendar calendar1 = Calendar.getInstance();
            calendar1.set(Calendar.YEAR, year);
            calendar1.set(Calendar.MONTH, month);
            calendar1.set(Calendar.DATE, date);

            CharSequence charSequence = DateFormat.format("EEE, dd MMM yyyy", calendar1);
            deliveryDate.setText(charSequence);
        }
    }, YEAR, MONTH, DATE);
    datePickerDialog.show();
}
    private void handleDeliveryTime(){
        Calendar calendar=Calendar.getInstance();

        int HOUR=calendar.get(Calendar.HOUR_OF_DAY);
        int MINUTE=calendar.get(Calendar.MINUTE);

        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

                Calendar calendar2 = Calendar.getInstance();
                calendar2.set(Calendar.HOUR, hourOfDay);
                calendar2.set(Calendar.MINUTE, minute);

                CharSequence charSequence = DateFormat.format("hh:mm a", calendar2);
                deliveryTime.setText(charSequence);
            }
        }, HOUR, MINUTE, is24HourFormat);
        timePickerDialog.show();
    }



    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(radioId);
    }

//    LogOut Menu Function
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(Home.this, MainActivity.class));
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
                Intent i = new Intent(Home.this, UserProfile.class);
                startActivity(i);
                break;
            }
            case R.id.makeOrder: {
                Intent i = new Intent(Home.this, Home.class);
                startActivity(i);
                break;
            }
            case R.id.orderHistory: {
                Intent i = new Intent(Home.this, OrderHistory.class);
                startActivity(i);
                break;
            }
            case R.id.homeAsUp: {
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    }
