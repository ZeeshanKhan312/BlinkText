package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

public class SplashScreen extends AppCompatActivity {

    private static  int splash_timer=3000; //3000->3s

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);// to remove title bar

        new Handler().postDelayed(new Runnable() {  //will delay to run this method until splash_time is over
            @Override
            public void run() {
                 Intent intent= new Intent(SplashScreen.this,MainActivity.class);
                 startActivity(intent);
                 finish(); //if press back so this will not reappear
            }
        },splash_timer);


    }
}