package com.example.pickuplaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    EditText emailId, password;
    Button btnSignup, btnSignin;
    TextView forgot;
    ImageView logo;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuhthStateListener;
    DatabaseReference reff;
    String userRole;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.txt_email);
        password = findViewById(R.id.txt_pass);
        btnSignin = findViewById(R.id.btn_signin2);
        btnSignup = findViewById(R.id.btn_next);
        logo = findViewById(R.id.imageView);
        logo.setImageResource(R.drawable.logo);
        forgot = findViewById(R.id.forgot);
        pb = findViewById(R.id.progressBar2);
        pb.setVisibility(View.GONE);

        mAuhthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mFirebaseUser = mFirebaseAuth.getCurrentUser();
                if(mFirebaseUser != null){

//                    Get UserRole Dari Table Profile, Then Check==Runner/User, Then Baru Intent
                    reff = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
                    reff.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String userRole = dataSnapshot.child("role").getValue().toString();
                            if(userRole.equals("Runner")){
                                Toast.makeText(MainActivity.this, "You Are Logged In", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this, RunnerOrderRV.class);
                                startActivity(i);
                            }
                            else{
                                Toast.makeText(MainActivity.this, "You Are Logged In", Toast.LENGTH_SHORT).show();
                                Intent i = new Intent(MainActivity.this, Home.class);
                                startActivity(i);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

//                    Toast.makeText(MainActivity.this, "You Are Logged In", Toast.LENGTH_SHORT).show();
//                    Intent i = new Intent(MainActivity.this, Home.class);
//                    startActivity(i);
                }
                else{
                    Toast.makeText(MainActivity.this, "Please Login", Toast.LENGTH_SHORT).show();
                }
            }
        };

        btnSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailId.getText().toString();
                String pass = password.getText().toString();
                pb.setVisibility(View.VISIBLE);

                if(email.isEmpty()){
                    emailId.setError("Please Enter Your Email");
                    emailId.requestFocus();
                }
                else if(pass.isEmpty()){
                    password.setError("Please Enter Your Password");
                    password.requestFocus();
                }
                else if(email.isEmpty() && pass.isEmpty()){
                    Toast.makeText(MainActivity.this, "Fields Are Empty", Toast.LENGTH_SHORT).show();
                }
                else if(!(email.isEmpty() && pass.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                pb.setVisibility(View.GONE);
                                Toast.makeText(MainActivity.this, "Login Error, Please Login Again", Toast.LENGTH_SHORT).show();
                            }
                            else{

//                                Get UserRole Dari Table Profile, Then Check==Runner/User, Then Baru Intent
                                reff = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
                                reff.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        String userRole = dataSnapshot.child("role").getValue().toString();
                                        pb.setVisibility(View.GONE);
                                        if(userRole.equals("Runner")){
                                            Toast.makeText(MainActivity.this, "You Are Logged In", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(MainActivity.this, RunnerOrderRV.class);
                                            startActivity(i);
                                        }
                                        else{
                                            Toast.makeText(MainActivity.this, "You Are Logged In", Toast.LENGTH_SHORT).show();
                                            Intent i = new Intent(MainActivity.this, Home.class);
                                            startActivity(i);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

//                                Intent i = new Intent(MainActivity.this, Home.class);
//                                startActivity(i);
                            }
                        }
                    });
                }
                else {
                    Toast.makeText(MainActivity.this, "Error Occured!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent i = new Intent(MainActivity.this, Signup.class);
                startActivity(i);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ForgotPassword.class);
                startActivity(i);
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

    @Override
    protected void onStart() {
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuhthStateListener);
    }
}
