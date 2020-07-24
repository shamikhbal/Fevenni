package com.example.pickuplaundry;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class SetupItem extends AppCompatActivity {
    Button btnSubmit;
    TextView tshirt, pant, skirt, scarf, jacket, other, washMachine, dryer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_item);

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
        });

    }
}

