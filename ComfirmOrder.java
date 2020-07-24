package com.example.pickuplaundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ComfirmOrder extends AppCompatActivity {
TextView shirt, pant, scarf, skirt, jacket, others, washingMachine, dryer, totalPrice;
Button comfirmOrder;
DatabaseReference reff;
Order order;
int tshirt,pants, scarfs, Skirts, jackets, other, washMach, dry;
double sum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comfirm_order);

        shirt = findViewById(R.id.ts_tv8);
        pant = findViewById(R.id.ts_tv9);
        scarf = findViewById(R.id.ts_tv3);
        skirt = findViewById(R.id.ts_tv4);
        jacket = findViewById(R.id.ts_tv5);
        others = findViewById(R.id.ts_tv6);
        washingMachine = findViewById(R.id.ts_tv7);
        dryer = findViewById(R.id.ts_tv10);
        totalPrice = findViewById(R.id.textView20);
        comfirmOrder=findViewById(R.id.btn_confirmorder);

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
            sum = (washMach*5)+(dry*5);
        }
        else if(kol.equals("Kolej BurhanuddinHelmi (KBH)")){
            sum = (washMach*4)+(dry*4);
        }
        else if(kol.equals("Kolej Ibu Zain (KIZ)")){
            sum = (washMach*2.50)+(dry*2.50);
        }
        else {
            sum = (washMach*2.50)+(dry*2.50);
        }

        shirt.setText(""+tshirt);
        pant.setText(""+pants);
        scarf.setText(""+scarfs);
        skirt.setText(""+Skirts);
        jacket.setText(""+jackets);
        others.setText(""+other);
        washingMachine.setText(""+washMach);
        dryer.setText(""+dry);
        totalPrice.setText("RM"+sum);

        reff = FirebaseDatabase.getInstance().getReference("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Order");
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
                order.setdTime(delTime);

                order.setTshirt(tshirt);
                order.setTshirt(pants);
                order.setTshirt(scarfs);
                order.setTshirt(Skirts);
                order.setTshirt(jackets);
                order.setTshirt(other);
                order.setTshirt(washMach);
                order.setTshirt(dry);

                order.setTotalPrice(sum);

                reff.push().setValue(order);
                Toast.makeText(ComfirmOrder.this, "Order Saved", Toast.LENGTH_LONG).show();
            }
        });
    }
}
