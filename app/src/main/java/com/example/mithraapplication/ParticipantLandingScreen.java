package com.example.mithraapplication;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import java.util.Locale;

public class ParticipantLandingScreen extends AppCompatActivity {

    private Button englishButton, kannadaButton;
    private CardView surveyButton, videoButton, activityButton;
    private TextView participantNameTV, surveyTV, videoTV, activityTV, logoutTV;
    private MithraUtility mithraUtility = new MithraUtility();
    private ImageView logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_landing_screen);
        RegisterViews();
        onClickOfSurveyButton();
        onClickOfVideoButton();
        onClickOfActivityButton();
        onClickOfLanguageButton();
        checkFromActivity();
        onClickOfLogoutButton();
        getCurrentLocale();
    }

    private void onClickOfLogoutButton(){
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipantLandingScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });

        logoutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipantLandingScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Description : This method is used to register the layout views to the activity
     */
    private void RegisterViews() {
        englishButton = findViewById(R.id.englishLPButton);
        kannadaButton = findViewById(R.id.kannadaLPButton);
        surveyButton = findViewById(R.id.surveyCardView);
        videoButton = findViewById(R.id.videoCardView);
        activityButton = findViewById(R.id.activityCardView);
        participantNameTV = findViewById(R.id.participantNameLPTV);
        surveyTV = findViewById(R.id.surveyTV);
        videoTV = findViewById(R.id.videoTV);
        activityTV = findViewById(R.id.activityTV);
        logoutTV = findViewById(R.id.logoutTV);
        logoutButton = findViewById(R.id.logoutIcon);
        String participantUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.user_name), getString(R.string.user_name_participant));
        participantNameTV.setText(participantUserName);
    }

    private void checkFromActivity(){
        String fromActivity = getIntent().getStringExtra("FromActivity");
        if(fromActivity.equals("NULL") || fromActivity.equals("LoginScreen")){
            ViewGroup.LayoutParams params = surveyButton.getLayoutParams();
            params.height = 430;
            params.width = 430;
            surveyButton.requestLayout();
            surveyButton.setCardElevation(15);

            ViewGroup.LayoutParams params1 = videoButton.getLayoutParams();
            params1.height = 340;
            params1.width = 340;
            videoButton.requestLayout();
            videoButton.setEnabled(false);

        }else if(fromActivity.equals("SurveyScreen")){
            ViewGroup.LayoutParams params = videoButton.getLayoutParams();
            params.height = 430;
            params.width = 430;
            videoButton.requestLayout();
            videoButton.setCardElevation(15);
            videoButton.setEnabled(true);

            ViewGroup.LayoutParams params1 = surveyButton.getLayoutParams();
            params1.height = 340;
            params1.width = 340;
            surveyButton.requestLayout();
        }else{
            ViewGroup.LayoutParams params = activityButton.getLayoutParams();
            params.height = 430;
            params.width = 430;
            activityButton.requestLayout();
            activityButton.setCardElevation(15);

            ViewGroup.LayoutParams params1 = surveyButton.getLayoutParams();
            params1.height = 340;
            params1.width = 340;
            surveyButton.requestLayout();

            ViewGroup.LayoutParams params2 = videoButton.getLayoutParams();
            params2.height = 340;
            params2.width = 340;
            videoButton.requestLayout();
        }
    }

    /**
     * Description : This method is used to move to the survey screen on clicking the survey logo
     */
    private void onClickOfSurveyButton() {
        surveyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipantLandingScreen.this, SurveyScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Description : This method is used to move to the video screen on clicking the video logo
     */
    private void onClickOfVideoButton() {
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ParticipantLandingScreen.this, VideoScreen.class);
                startActivity(intent);
                finish();
            }
        });
    }

    /**
     * Description : This method is used to move to the activity screen on clicking the activity logo
     */
    private void onClickOfActivityButton() {
        activityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ParticipantLandingScreen.this, ActivitiesScreen.class);
//                startActivity(intent);
//                finish();
            }
        });
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                englishButton.setBackgroundResource(R.drawable.left_selected_toggle_button);
                englishButton.setTextColor(getResources().getColor(R.color.black));
                kannadaButton.setBackgroundResource(R.drawable.right_unselected_toggle_button);
                kannadaButton.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("en");
            }
        });

        kannadaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kannadaButton.setBackgroundResource(R.drawable.right_selected_toggle_button);
                kannadaButton.setTextColor(getResources().getColor(R.color.black));
                englishButton.setBackgroundResource(R.drawable.left_unselected_toggle_button);
                englishButton.setTextColor(getResources().getColor(R.color.black));
                changeLocalLanguage("kn");
            }
        });
    }

    /**
     * @param selectedLanguage
     * Description : This method is used to change the content of the screen to user selected language
     */
    public void changeLocalLanguage(String selectedLanguage){
        Locale myLocale = new Locale(selectedLanguage);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.setLocale(myLocale);
        res.updateConfiguration(conf, dm);
        onConfigurationChanged(conf);
    }

    public void getCurrentLocale(){
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        LocaleList lang = conf.getLocales();
        if(lang.get(0).getLanguage().equals("kn")){
            kannadaButton.setBackgroundResource(R.drawable.right_selected_toggle_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
            englishButton.setBackgroundResource(R.drawable.left_unselected_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
        }else{
            englishButton.setBackgroundResource(R.drawable.left_selected_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
            kannadaButton.setBackgroundResource(R.drawable.right_unselected_toggle_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
        }
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
        logoutTV.setText(R.string.logout);
        surveyTV.setText(R.string.survey);
        videoTV.setText(R.string.video);
        activityTV.setText(R.string.activity);
    }
}
