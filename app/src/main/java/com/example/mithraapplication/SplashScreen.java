package com.example.mithraapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        loadSplashScreen();
    }

    /**
     * Description : This method is used to display the SplashScreen to the user
     */
    private void loadSplashScreen() {
        // Using handler with postDelayed called runnable run method
        new Handler().postDelayed(() -> {
            Intent i = new Intent(SplashScreen.this, LoginScreen.class);
            startActivity(i);
            finish();
        }, 3*1000);
    }
}