package com.example.pickuplaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
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

public class UserProfile extends AppCompatActivity {
    private  static final int RESULT_LOAD_IMAGE = 1;
    TextView name,phone,college,role,gender,email;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

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
                        Intent p = new Intent(UserProfile.this, UserProfile.class);
                        startActivity(p);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_createorder:
                        menuItem.setChecked(true);
                        Intent h = new Intent(UserProfile.this, Home.class);
                        startActivity(h);
                        drawerLayout.closeDrawers();
                        return true;
                    case R.id.nav_orderhistory:
                        menuItem.setChecked(true);
                        Intent o = new Intent(UserProfile.this, OrderHistory.class);
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

        name=(TextView)findViewById(R.id.tv_name);
        role=(TextView)findViewById(R.id.tv_role);
        gender=(TextView)findViewById(R.id.tv_gender);
        phone=(TextView)findViewById(R.id.tv_phone);
        college=(TextView)findViewById(R.id.tv_college);
        email=(TextView)findViewById(R.id.tv_email);
        imgProfile= (ImageView) findViewById(R.id.imgProfile);
        btnEdit=(Button) findViewById(R.id.btn_edit);


        mStorageRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        imgReff = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
        imgReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.child("imgUrl").getValue()==null){
                    imgProfile.setImageResource(R.drawable.profile);
                }
                else{
                    String img = dataSnapshot.child("imgUrl").getValue().toString();
                    Glide.with(UserProfile.this).load(img).apply(RequestOptions.circleCropTransform()).into(imgProfile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UserProfile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
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
                Intent i = new Intent(UserProfile.this, EditProfile.class);
                startActivity(i);
            }
        });
    }

    //    LogOut Menu Function
    private void Logout(){
        firebaseAuth.signOut();
        finish();
        startActivity(new Intent(UserProfile.this, MainActivity.class));
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
                break;
            }
            case R.id.makeOrder: {
                Intent i = new Intent(UserProfile.this, Home.class);
                startActivity(i);
                break;
            }
            case R.id.orderHistory: {
                Intent i = new Intent(UserProfile.this, OrderHistory.class);
                startActivity(i);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
