package com.example.mithraapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class ParticipantStatusScreen extends AppCompatActivity implements HandleServerResponse {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_screen);
        RegisterViews();
    }

    private void RegisterViews(){

    }

    @Override
    public void responseReceivedSuccessfully(String message) {

    }

    @Override
    public void responseReceivedFailure(String message) {

    }
}
