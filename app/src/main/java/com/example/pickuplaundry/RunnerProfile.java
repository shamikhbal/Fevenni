package com.example.pickuplaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

public class RunnerProfile extends AppCompatActivity {

    private  static final int RESULT_LOAD_IMAGE = 1;
    TextView name,phone,college,role,gender,email, totalOrder, totalProfit;
    Button btnShow,btnUpload, btnChoosePic, btnEdit;
    ImageView imgProfile;
    DatabaseReference reff, imgReff;
    StorageReference mStorageRef;
    public Uri imguri;
    private StorageTask uploadTask;
    //Custom Navigation bar
    private FirebaseAuth firebaseAuth;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    int profit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runner_profile);

        //Custom Navigation bar
        toolbar=findViewById(R.id.toolbar);
        toolbar.setTitle("Profile");
        setSupportActionBar(toolbar);

        firebaseAuth=FirebaseAuth.getInstance();

        name=(TextView)findViewById(R.id.tv_name);
        role=(TextView)findViewById(R.id.tv_role);
        gender=(TextView)findViewById(R.id.tv_gender);
        phone=(TextView)findViewById(R.id.tv_phone);
        college=(TextView)findViewById(R.id.tv_college);
        email=(TextView)findViewById(R.id.tv_email);
        imgProfile= (ImageView) findViewById(R.id.imgProfile);
        btnEdit=(Button) findViewById(R.id.btn_edit);
        totalOrder=findViewById(R.id.total_order);
        totalProfit=findViewById(R.id.total_profit);


        mStorageRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        imgReff = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
        imgReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.child("imgUrl").getValue()==null){
                    imgProfile.setImageResource(R.drawable.profile);
                    if(dataSnapshot.child("total order").getValue()==null){
                        totalOrder.setText("0");
                    }
                    else{
                        totalOrder.setText(dataSnapshot.child("total order").getValue().toString());
                        profit=Integer.parseInt(dataSnapshot.child("total order").getValue().toString())*3;
                        totalProfit.setText("RM "+profit+".00");
                    }
                }
                else{
                    if(dataSnapshot.child("total order").getValue()==null){
                        totalOrder.setText("0");
                    }
                    else{
                        totalOrder.setText(dataSnapshot.child("total order").getValue().toString());
                        profit=Integer.parseInt(dataSnapshot.child("total order").getValue().toString())*3;
                        totalProfit.setText("RM "+profit+".00");
                    }
                    String img = dataSnapshot.child("imgUrl").getValue().toString();
                    Glide.with(RunnerProfile.this).load(img).apply(RequestOptions.circleCropTransform()).into(imgProfile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(RunnerProfile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        reff = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());

        reff.child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String a = dataSnapshot.child("name").getValue().toString();
                String b = dataSnapshot.child("role").getValue().toString();
                String c = dataSnapshot.child("college").getValue().toString();
                String d = dataSnapshot.child("gender").getValue().toString();
                String e = dataSnapshot.child("phone").getValue().toString();
                String f = dataSnapshot.child("email").getValue().toString();

                name.setText(a);
                role.setText(b);
                college.setText(c);
                gender.setText(d);
                phone.setText(e);
                email.setText(f);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RunnerProfile.this, EditProfile.class);
                startActivity(i);
            }
        });
    }

    //    LogOut Menu Function
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(RunnerProfile.this, MainActivity.class));
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
                Intent i = new Intent(RunnerProfile.this, RunnerProfile.class);
                startActivity(i);
                break;
            }
            case R.id.searchorder: {
                Intent i = new Intent(RunnerProfile.this, RunnerOrderRV.class);
                startActivity(i);
                break;
            }
            case R.id.myorder: {
                Intent in = new Intent(RunnerProfile.this, RunnerOrderHistory.class);
                startActivity(in);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
