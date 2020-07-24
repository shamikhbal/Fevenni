package com.example.pickuplaundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity {
    RadioGroup radioGroup;
    RadioButton radioButton;
    Spinner pickUpDay,deliveryDay,pickUpTime,deliveryTime;
    EditText txtPickupLocation,txtDeliveryLocation;
    Button btnNext;
//    DatabaseReference reff;
//    SetupLaundry setupLaundry;

//    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);


        pickUpDay=findViewById(R.id.spinner1);
        pickUpTime=findViewById(R.id.spinner2);
        deliveryDay=findViewById(R.id.spinner3);
        deliveryTime=findViewById(R.id.spinner4);

        addPickupDay();
        addPickupTime();
        addDeliveryDay();
        addDeliveryTime();

        txtPickupLocation = (EditText)findViewById(R.id.pickup_location);
        txtDeliveryLocation = (EditText)findViewById(R.id.delivery_location);
        btnNext = (Button)findViewById(R.id.btn_next) ;


//        reff = FirebaseDatabase.getInstance().getReference().child("SetupLaundry");
//        setupLaundry = new SetupLaundry();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                setupLaundry.setPickupLocation(txtPickupLocation.getText().toString().trim());
//                setupLaundry.setDeliveryLocation(txtDeliveryLocation.getText().toString().trim());
//                setupLaundry.setKolej(radioButton.getText().toString().trim());
//                setupLaundry.setpDay(pickUpDay.getSelectedItem().toString().trim());
//                setupLaundry.setpTime(pickUpTime.getSelectedItem().toString().trim());
//                setupLaundry.setdDay(deliveryDay.getSelectedItem().toString().trim());
//                setupLaundry.setdTime(deliveryTime.getSelectedItem().toString().trim());

//                reff.push().setValue(setupLaundry);
//                Toast.makeText(Home.this, "data inserted successfully", Toast.LENGTH_LONG).show();
                Intent i = new Intent(Home.this, SetupItem.class);
                i.putExtra("pickLoc",txtPickupLocation.getText().toString().trim());
                i.putExtra("delLoc", txtDeliveryLocation.getText().toString().trim());
                i.putExtra("kol", radioButton.getText().toString().trim());
                i.putExtra("pickDay", pickUpDay.getSelectedItem().toString().trim());
                i.putExtra("pickTime", pickUpTime.getSelectedItem().toString().trim());
                i.putExtra("delDay", deliveryDay.getSelectedItem().toString().trim());
                i.putExtra("delTime", deliveryTime.getSelectedItem().toString().trim());

                startActivity(i);
            }
        });



//        String pDay [] = {"Tuesday","Thursday","Saturday" };
//        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,pDay);
//        pickUpDay.setAdapter(adapter1);
//
//        String pTime [] = {"9.00 a.m","12.00 p.m","3.00 p.m"};
//        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,pTime);
//        pickUpTime.setAdapter(adapter2);
//
//        String dDay [] = {"Wednesday","Friday","Sunday"};
//        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,dDay);
//        deliveryDay.setAdapter(adapter3);
//
//        String dTime [] = {"9.00 a.m","12.00 p.m","3.00 p.m"};
//        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,dTime);
//        deliveryTime.setAdapter(adapter4);


//        Toast.makeText(Home.this,"Firebase connection Success",Toast.LENGTH_LONG).show();


    }

    private void addPickupDay() {
        List<String> list = new ArrayList<String>();
        list.add("Wednesday");
        list.add("Friday");
        list.add("Sunday");
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, list);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickUpDay.setAdapter(adapter1);
    }

    private void addPickupTime() {
        List<String> list = new ArrayList<String>();
        list.add("9.00 a.m");
        list.add("12.00 p.m");
        list.add("3.00 p.m");
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, list);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pickUpTime.setAdapter(adapter2);
    }

    private void addDeliveryDay(){
        List<String> list = new ArrayList<String>();
        list.add("Tuesday");
        list.add("Thursday");
        list.add("Saturday");
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, list);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliveryDay.setAdapter(adapter3);
    }

    private void addDeliveryTime(){
        List<String> list = new ArrayList<String>();
        list.add("9.00 a.m");
        list.add("12.00 p.m");
        list.add("3.00 p.m");
        ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, list);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        deliveryTime.setAdapter(adapter4);
    }

    public void checkButton(View v){
        int radioId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(radioId);
    }
}
