package com.example.mithraapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {

    private static final int REQUEST_MAC_ADDRESS = 0;
    String macAddress = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        loadSplashScreen();
//        getMacAddressOfPhone();
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
        }, 3 * 1000);
    }

//    private void getMacAddressOfPhone() {
//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
//        WifiInfo wInfo = wifiManager.getConnectionInfo();
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_MAC_ADDRESS);
//        }else{
//            macAddress = wInfo.getMacAddress();
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        switch(requestCode){
//            case REQUEST_MAC_ADDRESS:
//                Toast.makeText(this, macAddress +"", Toast.LENGTH_LONG).show();
//
//        }
//    }
}