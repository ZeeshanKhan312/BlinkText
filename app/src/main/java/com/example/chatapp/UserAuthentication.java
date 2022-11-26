 package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatapp.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

 public class UserAuthentication extends AppCompatActivity {
    TextView changeNumber;
     EditText otp;
     android.widget.Button verifyB;

     FirebaseAuth firebaseAuth;
     ProgressBar progressbarofAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_authentication);
        changeNumber=findViewById(R.id.changeNumber);
        otp=findViewById(R.id.number);
        verifyB=findViewById(R.id.verify);
        progressbarofAuth=findViewById(R.id.progressBarOfUser);

        firebaseAuth=FirebaseAuth.getInstance();

        //to change the number
        changeNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(UserAuthentication.this,MainActivity.class);
                startActivity(intent);
            }
        });

        //To Verify the OTP
        verifyB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredotp=otp.getText().toString();
                if(enteredotp.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter your OTP First ",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    progressbarofAuth.setVisibility(View.VISIBLE);
                    String otprecieved=getIntent().getStringExtra("otp");
                    //firebase OTP is authenticated in a key value format so can't compare directly like strings
                    PhoneAuthCredential credential= PhoneAuthProvider.getCredential(otprecieved,enteredotp);
                    signInWithPhoneAuthCredential(credential);
                }
            }
        });
    }


     private void signInWithPhoneAuthCredential(PhoneAuthCredential credential)
     {
         firebaseAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {
                 if(task.isSuccessful())
                 {
                     progressbarofAuth.setVisibility(View.INVISIBLE);
                     Toast.makeText(getApplicationContext(),"Login Success",Toast.LENGTH_SHORT).show();
                     Intent intent=new Intent(UserAuthentication.this,CreateProfile.class);
                     startActivity(intent);
                     finish();
                 }
                 else
                 {
                     if(task.getException() instanceof FirebaseAuthInvalidCredentialsException)
                     {
                         progressbarofAuth.setVisibility(View.INVISIBLE);
                         Toast.makeText(getApplicationContext(),"Login Failed",Toast.LENGTH_SHORT).show();
                     }
                 }

             }
         });
     }


 }