package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.hbb20.CountryCodePicker;
import com.google.firebase.auth.PhoneAuthOptions;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    EditText getphonenumber;
    android.widget.Button sendotp;
    CountryCodePicker countrycodepicker;

    FirebaseAuth firebaseAuth;
    ProgressBar progressbarofmain;

    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    String countrycode;
    String phonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        countrycodepicker=findViewById(R.id.countryCodeHolder);
        sendotp=findViewById(R.id.sendOtp);
        getphonenumber=findViewById(R.id.number);
        progressbarofmain=findViewById(R.id.progressBar);

        //INSTANCE OF FIREBASE AUTH will get instance of current user
        firebaseAuth=FirebaseAuth.getInstance();
        countrycode=countrycodepicker.getSelectedCountryCodeWithPlus(); //firebase requires plus to sent the otp

        //if any one is changing the default country code
        countrycodepicker.setOnCountryChangeListener(new CountryCodePicker.OnCountryChangeListener() {
            @Override
            public void onCountrySelected() {
                countrycode=countrycodepicker.getSelectedCountryCodeWithPlus();
            }
        });

        sendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String number;
                number=getphonenumber.getText().toString();
                if(number.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Your Number",Toast.LENGTH_SHORT).show();
                }
                else if(number.length()<10)
                {
                    Toast.makeText(getApplicationContext(),"Please Enter Correct Number",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //to make the progress bar visible
                    progressbarofmain.setVisibility(View.VISIBLE);
                    phonenumber=countrycode+number;

                    //verifying the entered phone number
                    PhoneAuthOptions options=PhoneAuthOptions.newBuilder(firebaseAuth)
                            .setPhoneNumber(phonenumber)
                            .setTimeout(60L, TimeUnit.SECONDS)  //to resend the otp
                            .setActivity(MainActivity.this)
                            .setCallbacks(callbacks)
                            .build();

                    PhoneAuthProvider.verifyPhoneNumber(options);

                }
            }
        });

        callbacks=new PhoneAuthProvider.OnVerificationStateChangedCallbacks() { //sending the otp after verifying
            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                //TO AUTOMATICALLY FETCH THE OTP
            }

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                Toast.makeText(getApplicationContext(),"OTP is Sent",Toast.LENGTH_SHORT).show();
                progressbarofmain.setVisibility(View.INVISIBLE);
//                String otp=s;
                Intent intent=new Intent(MainActivity.this,UserAuthentication.class);
                intent.putExtra("otp",s); //s contains the otp sent by the firebase
                startActivity(intent);
            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {

            }
        };

    }

    //If user already registered then it doesn't need to create it account again
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent intent=new Intent(MainActivity.this,HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);  //to clear this activity
            startActivity(intent);
        }
    }

}