package com.example.pickuplaundry;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class EditProfile extends AppCompatActivity {
    private  static final int RESULT_LOAD_IMAGE = 1;
    EditText editTextName, editTextPhone;
    DatabaseReference reff, imgReff;
    StorageReference mStorageRef;
    Button btn_update;
    User user;
    Spinner college;
    ImageView imgProfile;
    public Uri imguri;
    private StorageTask uploadTask;
    String role;
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        editTextName = findViewById(R.id.txt_name);
        editTextPhone = findViewById(R.id.txt_phone);
        college = findViewById(R.id.spinner);
        btn_update=findViewById(R.id.btn_update);
        imgProfile= (ImageView) findViewById(R.id.imgProfile);
        pb = findViewById(R.id.progressBar);
        pb.setVisibility(View.GONE);

        addItemOnSpinner();

        mStorageRef = FirebaseStorage.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        imgReff = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
        imgReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                role=dataSnapshot.child("role").getValue().toString();
                if(dataSnapshot.child("imgUrl").getValue()==null){
                    imgProfile.setImageResource(R.drawable.profile);
                }
                else{
                    String a = dataSnapshot.child("name").getValue().toString();
                    String e = dataSnapshot.child("phone").getValue().toString();

                    editTextName.setText(a);
                    editTextPhone.setText(e);

                    String img = dataSnapshot.child("imgUrl").getValue().toString();
                    Glide.with(EditProfile.this).load(img).apply(RequestOptions.circleCropTransform()).into(imgProfile);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(EditProfile.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileChooser();
            }
        });

        reff = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
        user = new User();

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(editTextName.getText()==null || editTextPhone.getText()==null || college.getSelectedItem()==null){
                    Toast.makeText(EditProfile.this, "Please Fill In The Information", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(uploadTask!=null && uploadTask.isInProgress()){
                        Toast.makeText(EditProfile.this,"Upload in progress",Toast.LENGTH_LONG).show();

                    }else{
//                        reff.child("name").setValue(editTextName.getText().toString().trim());
//                        reff.child("phone").setValue(editTextPhone.getText().toString().trim());
//                        reff.child("college").setValue(college.getSelectedItem().toString().trim());

                        pb.setVisibility(View.VISIBLE);
                        FileUploder();

                    }
                }
            }
        });

    }

    private String getExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cr.getType(uri));
    }

    private void FileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    private void FileUploder(){
        if(imguri != null){
            final StorageReference Ref = mStorageRef.child(System.currentTimeMillis()+"."+getExtension(imguri));
            uploadTask = Ref.putFile(imguri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Get a URL to the uploaded content
                            //Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            Ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Uri downloadUrl=uri;
                                    String imgUrl=downloadUrl.toString().trim();
                                    imgReff = FirebaseDatabase.getInstance().getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid()).child("Profile");
                                    imgReff.child("imgUrl").setValue(imgUrl);

                                    reff.child("name").setValue(editTextName.getText().toString().trim());
                                    reff.child("phone").setValue(editTextPhone.getText().toString().trim());
                                    reff.child("college").setValue(college.getSelectedItem().toString().trim());
                                }
                            });
                            pb.setVisibility(View.GONE);
                            Toast.makeText(EditProfile.this,"Profile Updated",Toast.LENGTH_LONG).show();

                            if(role.equals("User")){
                                Intent i = new Intent(EditProfile.this, UserProfile.class);
                                startActivity(i);
                            }
                            else{
                                Intent i = new Intent(EditProfile.this, RunnerProfile.class);
                                startActivity(i);
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception exception) {
                            // Handle unsuccessful uploads
                            // ...
                            Toast.makeText(EditProfile.this, "Profile Update Failed", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
        else {
            reff.child("name").setValue(editTextName.getText().toString().trim());
            reff.child("phone").setValue(editTextPhone.getText().toString().trim());
            reff.child("college").setValue(college.getSelectedItem().toString().trim());
            if(role.equals("User")){
                Intent i = new Intent(EditProfile.this, UserProfile.class);
                startActivity(i);
            }
            else{
                Intent i = new Intent(EditProfile.this, RunnerProfile.class);
                startActivity(i);
            }
            pb.setVisibility(View.GONE);
            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data!=null && data.getData()!=null){
            imguri=data.getData();
            imgProfile.setImageURI(imguri);

        }else {
            Glide.with(this)
                    .load(mStorageRef)
                    .into(imgProfile);
        }
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

}
