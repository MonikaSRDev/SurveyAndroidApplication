package com.example.mithraapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ParticipantLandingScreen extends AppCompatActivity {

    private Button englishButton, kannadaButton;
    private ImageView surveyImage, videoImage, activityImage;
    private TextView participantNameTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_participant_landing_screen);
        RegisterViews();
        onClickOfSurveyButton();
        onClickOfVideoButton();
        onClickOfActivityButton();
    }

    /**
     * Description : This method is used to register the layout views to the activity
     */
    private void RegisterViews() {
        englishButton = findViewById(R.id.englishLPButton);
        kannadaButton = findViewById(R.id.kannadaLPButton);
        surveyImage = findViewById(R.id.surveyButton);
        videoImage = findViewById(R.id.videoButton);
        activityImage = findViewById(R.id.activityButton);
        participantNameTV = findViewById(R.id.participantNameLPTV);
    }

    /**
     * Description : This method is used to move to the survey screen on clicking the survey logo
     */
    private void onClickOfSurveyButton() {
        surveyImage.setOnClickListener(new View.OnClickListener() {
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
        videoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ParticipantLandingScreen.this, QuestionActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
    }

    /**
     * Description : This method is used to move to the activity screen on clicking the activity logo
     */
    private void onClickOfActivityButton() {
        activityImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(ParticipantLandingScreen.this, QuestionActivity.class);
//                startActivity(intent);
//                finish();
            }
        });
    }
}
