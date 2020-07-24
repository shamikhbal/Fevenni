package com.example.pickuplaundry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class Signup extends AppCompatActivity implements View.OnClickListener{

    private EditText editTextName, editTextEmail, editTextPassword, editTextPhone;
    private ProgressBar progressBar;
    private RadioGroup groupGender, groupROle;
    private RadioButton rbGender, rbRole;
    private Spinner college;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextName = findViewById(R.id.txt_name);
        editTextEmail = findViewById(R.id.txt_email);
        editTextPassword = findViewById(R.id.txt_pass);
        editTextPhone = findViewById(R.id.txt_phone);
        progressBar = findViewById(R.id.progressbar);
        groupGender = findViewById(R.id.radio_gender);
        groupROle = findViewById(R.id.radio_role);
        college = findViewById(R.id.spinner);
        progressBar.setVisibility(View.GONE);

        addItemOnSpinner();

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.btn_next).setOnClickListener(this);
    }

    private void addItemOnSpinner() {
        college = findViewById(R.id.spinner);
        List<String> list = new ArrayList<String>();
        list.add("Kolej Pendeta Zaaba");
        list.add("Kolej Burhanuddin Helmi");
        list.add("Kolej Ibrahim Yaakob");
        list.add("Kolej Keris Mas");
        list.add("Kolej Rahim Kajai");
        list.add("Kolej Ibu Zain");
        list.add("Kolej Aminudin Baki");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        college.setAdapter(dataAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            //handle the already login user
        }
    }

    private void registerUser() {
        final String name = editTextName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        final String phone = editTextPhone.getText().toString().trim();

        final String gender = rbGender.getText().toString();
        final String role = rbRole.getText().toString();
        final String kolej = college.getSelectedItem().toString();

        if (name.isEmpty()) {
            editTextName.setError("Please Enter Your Name");
            editTextName.requestFocus();
            return;
        }

        if (email.isEmpty()) {
            editTextEmail.setError("Please Enter Your Email");
            editTextEmail.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Email Invalid");
            editTextEmail.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            editTextPassword.setError("Please Enter Your Password");
            editTextPassword.requestFocus();
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password Is Too Short");
            editTextPassword.requestFocus();
            return;
        }

        if (phone.isEmpty()) {
            editTextPhone.setError("Please Enter Your Phone Number");
            editTextPhone.requestFocus();
            return;
        }

        if (phone.length() != 10) {
            editTextPhone.setError("Phone Number Invalid");
            editTextPhone.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            User user = new User(
                                    name,
                                    email,
                                    phone,
                                    gender,
                                    role,
                                    kolej
                            );

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile")
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        Toast.makeText(Signup.this, "Success", Toast.LENGTH_LONG).show();
                                    } else {
                                        //display a failure message
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(Signup.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {

        int selectedIdGen = groupGender.getCheckedRadioButtonId();
        rbGender = findViewById(selectedIdGen);
        int selectedIdRole = groupROle.getCheckedRadioButtonId();
        rbRole = findViewById(selectedIdRole);
        
        switch (v.getId()) {
            case R.id.btn_next:
                registerUser();
                break;
        }
    }
}

