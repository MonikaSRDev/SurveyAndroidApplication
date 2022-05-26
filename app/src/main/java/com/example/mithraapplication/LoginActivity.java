package com.example.mithraapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText userName, userPassword;
    private ImageButton signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        RegisterViews();
        onClickSignInButton();
    }

    /**
     * Description : This method is used to register the views
     */
    private void RegisterViews() {
        userName = findViewById(R.id.userNameET);
        userPassword = findViewById(R.id.userPasswordET);
        signInButton = findViewById(R.id.signInButton);
    }

    /**
     * Description : This method is called when the user clicks on the sign-in button
     */
    private void onClickSignInButton() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(LoginActivity.this, AddNewParticipantActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

}
