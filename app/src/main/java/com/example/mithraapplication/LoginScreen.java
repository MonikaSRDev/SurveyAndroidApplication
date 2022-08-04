package com.example.mithraapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.mithraapplication.ModelClasses.UserLogin;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class LoginScreen extends AppCompatActivity implements HandleServerResponse {

    private EditText userNameET, userPasswordET;
    private TextView welcomeTV;
    private Button signInButton;
    private MithraUtility mithraUtility = new MithraUtility();
    private ConstraintLayout constraintlayout;

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
        signInButton = findViewById(R.id.signInButton);

        constraintlayout = findViewById(R.id.constraintLayoutLoginScreen);
        constraintlayout.setOnTouchListener((v, event) -> {
            if(getCurrentFocus()!=null){
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            }
            return false;
        });
    }

    /**
     * Description : This method is called when the user clicks on the sign-in button
     */
    private void onClickSignInButton() {
        signInButton.setOnClickListener(v -> {
            String userName = !userNameET.getText().toString().equals("") ? userNameET.getText().toString().replace(" ", "") : "NULL";
            String password = !userPasswordET.getText().toString().equals("") ? userPasswordET.getText().toString().replace(" ", "") : "NULL";


            if(!userName.equals("NULL") && !password.equals("NULL")){
                callServerForUserLogin(userName, password);
//                    moveToParticipantLandingPage(userName);
//                    moveToCoordinatorDashboard();
                mithraUtility.putSharedPreferencesData(LoginScreen.this, getString(R.string.userName), getString(R.string.user_name_participant), userName);
            }else if(userName.equals("NULL") && !password.equals("NULL")){
                userNameET.setError("Please enter username.");
            }else if(!userName.equals("NULL") && password.equals("NULL")){
                userPasswordET.setError("Please enter the password.");
            }else{
                userNameET.setError("Please enter username.");
                userPasswordET.setError("Please enter the password.");
            }

        });
    }

    /**
     * Description : This method is used to move to the participant landing page if the user is a participant
     */
    private void moveToParticipantLandingPage(String userName){
        Intent loginIntent = new Intent(LoginScreen.this, ParticipantLandingScreen.class);
        loginIntent.putExtra("FromActivity", "LoginScreen");
        startActivity(loginIntent);
        finish();
    }

    /**
     * Description : This method is used to move to the coordinator dashboard if the user is a coordinator
     */
    private void moveToCoordinatorDashboard(){
        Intent loginIntent = new Intent(LoginScreen.this, DashboardScreen.class);
        startActivity(loginIntent);
        finish();
    }

    /**
     * Description : call the server to get if the login is successful or not
     */
    private void callServerForUserLogin(String userName, String password){
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.user_table.login.login";
        UserLogin userLogin = new UserLogin();
        userLogin.setUserName(userName);
        userLogin.setUserPassword(password);
        userLogin.setActive("Yes");
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postUserLogin(LoginScreen.this, userLogin, url);
    }

    /**
     * Handle the success server response
     */
    @Override
    public void responseReceivedSuccessfully(String message) {
        Log.i("Message", "Success");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<UserLogin>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        ArrayList<UserLogin> userLogins;

        try{
            userLogins = gson.fromJson(jsonObject.get("message"), type);
            mithraUtility.putSharedPreferencesData(this, getString(R.string.user_role), getString(R.string.user_role), userLogins.get(0).getUserRole());
            if(userLogins.get(0).getUserRole().equals("participant")){
                mithraUtility.putSharedPreferencesData(this, getString(R.string.userName), getString(R.string.user_name_participant), userLogins.get(0).getUserName());
                mithraUtility.putSharedPreferencesData(this, getString(R.string.primaryID), getString(R.string.participantPrimaryID), userLogins.get(0).getUser_pri_id());
                moveToParticipantLandingPage(userLogins.get(0).getUserName());
            }else if(userLogins.get(0).getUserRole().equals("coordinator")){
                mithraUtility.putSharedPreferencesData(this, getString(R.string.userName), getString(R.string.user_name_coordinator), userLogins.get(0).getUserName());
                mithraUtility.putSharedPreferencesData(this, getString(R.string.primaryID), getString(R.string.coordinatorPrimaryID), userLogins.get(0).getUser_pri_id());
                moveToCoordinatorDashboard();
            }
        }catch(Exception e){
            Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }

        Log.i("Message", "Success");

        mithraUtility.putSharedPreferencesData(this, getString(R.string.authorization_tokens), getString(R.string.participant), getString(R.string.mp_token));
        mithraUtility.putSharedPreferencesData(this, getString(R.string.authorization_tokens), getString(R.string.coordinator), getString(R.string.mc_token));
        mithraUtility.putSharedPreferencesData(this, getString(R.string.authorization_tokens), getString(R.string.researcher), "");
    }

    /**
     * Description : Handles the failure server response
     */
    @Override
    public void responseReceivedFailure(String message) {
        Log.i("Message", "Failure");

        if(message!=null){
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            String serverErrorResponse = jsonObject.get("exception").toString();
            Toast.makeText(this, serverErrorResponse, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }

//    /**
//     * @param newConfig
//     * Description : This method is used to update the views on change of language
//     */
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        userNameET.setText(R.string.userid);
//        userPasswordET.setText(R.string.password);
//        welcomeTV.setText(R.string.welcome);
//        signInButton.setText(R.string.sign_in);
//    }
}


