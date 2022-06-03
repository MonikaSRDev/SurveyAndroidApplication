package com.example.mithraapplication;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mithraapplication.ModelClasses.UserLogin;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    private EditText userNameET, userPasswordET;
    private TextView welcomeTV, getStartedTV;
    private Button signInButton;

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
        userNameET = findViewById(R.id.userNameET);
        userPasswordET = findViewById(R.id.userPasswordET);

        welcomeTV = findViewById(R.id.welcomeTV);
        getStartedTV = findViewById(R.id.lets_get_startedTV);

        signInButton = findViewById(R.id.signInButton);
    }

    /**
     * Description : This method is called when the user clicks on the sign-in button
     */
    private void onClickSignInButton() {
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = userNameET.getText().toString();
                String password = userPasswordET.getText().toString();

                callServerForUserLogin(userName, password);

                Intent loginIntent = new Intent(LoginActivity.this, AddNewParticipantActivity.class);
                startActivity(loginIntent);
                finish();
            }
        });
    }

    /**
     * Description : call the server to get if the login is successful or not
     */
    private void callServerForUserLogin(String userName, String password){
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName(userName);
        userLogin.setUserPassword(password);
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.userLogin(LoginActivity.this, userLogin);
    }

    /**
     * @param selectedLanguage
     * Description : This method is used to change the content of the screen to user selected language
     */
    private void changeLocalLanguage(String selectedLanguage){
        Locale myLocale = new Locale(selectedLanguage);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }

    /**
     * @param newConfig
     * Description : This method is used to update the views on change of language
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        userNameET.setText(R.string.userid);
        userPasswordET.setText(R.string.password);
        getStartedTV.setText(R.string.let_s_get_started);
        welcomeTV.setText(R.string.welcome);
        signInButton.setText(R.string.sign_in);
    }

}
