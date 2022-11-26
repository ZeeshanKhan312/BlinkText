package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.chatapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateProfile extends AppCompatActivity {
    ImageView image;
    EditText name;
    android.widget.Button continueBtn;
    ProgressBar progressBarOfProfile;

    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    FirebaseStorage firebaseStorage;
    Uri selectedImageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        image=findViewById(R.id.getUserImageinImageView);
        name=findViewById(R.id.getUserName);
        continueBtn=findViewById(R.id.saveProfile);
        progressBarOfProfile=findViewById(R.id.progressbarofCreateProfile);

        firebaseAuth= FirebaseAuth.getInstance(); //will return which user is currently logged in
        firebaseDatabase=FirebaseDatabase.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT); //selecting image from photos
                intent.setType("image/*");
                startActivityForResult(intent,45); //to set the image Uri
            }
        });

        continueBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName= name.getText().toString();
                if(userName.isEmpty()){
                    name.setError("Please Enter Your Name");
                    return;
                }
                progressBarOfProfile.setVisibility(View.VISIBLE);
                if(selectedImageUri!=null){
                    //to store the image in firebase database
                    StorageReference imgRefrence= firebaseStorage.getReference().child("Profiles").child(firebaseAuth.getUid());
                    imgRefrence.putFile(selectedImageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                            if(task.isSuccessful()){
                                imgRefrence.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {  //uri is the user profile link
                                        String imageUrl= uri.toString();
                                        String Uid=firebaseAuth.getUid();
                                        String phoneNumber=firebaseAuth.getCurrentUser().getPhoneNumber();
                                        User user = new User(Uid,userName,phoneNumber,imageUrl);

                                        firebaseDatabase.getReference()
                                                .child("users")
                                                .child(Uid)//so that every user will make a new one instead of replacing any old user
                                                .setValue(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        progressBarOfProfile.setVisibility(View.INVISIBLE);
                                                        Intent intent = new Intent(CreateProfile.this, HomeActivity.class);
                                                        startActivity(intent);
                                                        finish();
                                                    }
                                                });
                                    }
                                });
                            }
                        }
                    });
                }

                else{
                    String Uid=firebaseAuth.getUid();
                    String phoneNumber=firebaseAuth.getCurrentUser().getPhoneNumber();
                    User user = new User(Uid,userName,phoneNumber,"No Image");

                    firebaseDatabase.getReference()
                            .child("users")
                            .child(Uid)//so that every user will make a new one instead of replacing any old user
                            .setValue(user)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    progressBarOfProfile.setVisibility(View.INVISIBLE);
                                    Intent intent = new Intent(CreateProfile.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(data.getData()!=null){
            image.setImageURI(data.getData());
            selectedImageUri=data.getData();
        }
    }
}