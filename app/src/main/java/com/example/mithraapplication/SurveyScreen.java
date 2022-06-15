package com.example.mithraapplication;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mithraapplication.ModelClasses.QuestionAnswers;
import com.example.mithraapplication.ModelClasses.SurveyPostRequest;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class SurveyScreen extends AppCompatActivity implements HandleServerResponse {

    private TextView participantName;
    private TextView questionNumber, totalQuestions, ph9Question, option_oneTV, option_twoTV, option_threeTV, option_fourTV;
    private Button nextButton;
    private ImageButton optionImageButtonOne, optionImageButtonTwo, optionImageButtonThree, optionImageButtonFour;
    private int questionIndex = 0;
    private int selectedOptionValue = 0;
    private ArrayList<QuestionAnswers> questionArray = new ArrayList<>();
    private String startDateTime, endDateTime;
    private Dialog dialog;
    private HashMap<String, Integer> surveyPHQ9 = new HashMap<>();
    private MithraUtility mithraUtility = new MithraUtility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_screen);
        RegisterViews();
//        initializeData();
//        startDateTime = getCurrentTime();
//        setCardData();
        getSelectedOption();
        onClickNextButton();
        callServerToGetPHQ9Questions();
    }

    private void initializeData() {
        QuestionAnswers questionAnswers = new QuestionAnswers();
        questionAnswers.setQuestion("ABC");
        questionAnswers.setQn_number("1");
        questionAnswers.setOption_1("option 1");
        questionAnswers.setOption_2("option 2");
        questionAnswers.setOption_3("option 3");
        questionAnswers.setOption_4("option 4");

        questionArray.add(questionAnswers);

        QuestionAnswers questionAnswers1 = new QuestionAnswers();
        questionAnswers1.setQuestion("DEF");
        questionAnswers1.setQn_number("2");
        questionAnswers1.setOption_1("option 1");
        questionAnswers1.setOption_2("option 2");
        questionAnswers1.setOption_3("option 3");
        questionAnswers1.setOption_4("option 4");

        questionArray.add(questionAnswers1);

        QuestionAnswers questionAnswers2 = new QuestionAnswers();
        questionAnswers2.setQuestion("GHI");
        questionAnswers2.setQn_number("3");
        questionAnswers2.setOption_1("option 1");
        questionAnswers2.setOption_2("option 2");
        questionAnswers2.setOption_3("option 3");
        questionAnswers2.setOption_4("option 4");

        questionArray.add(questionAnswers2);
    }


    /**
     * Description : This method is used to register the views
     */
    private void RegisterViews() {
        participantName = findViewById(R.id.participantNameTV);
        questionNumber = findViewById(R.id.questionNumbers);
        totalQuestions = findViewById(R.id.totalQuestions);
        ph9Question = findViewById(R.id.questionsTV);
        nextButton = findViewById(R.id.nextQuestionButton);

        option_oneTV = findViewById(R.id.option_oneTV);
        option_twoTV = findViewById(R.id.option_twoTV);
        option_threeTV = findViewById(R.id.option_threeTV);
        option_fourTV = findViewById(R.id.option_fourTV);

        optionImageButtonOne = findViewById(R.id.optionImageBtn1);
        optionImageButtonTwo = findViewById(R.id.optionImageBtn2);
        optionImageButtonThree = findViewById(R.id.optionImageBtn3);
        optionImageButtonFour = findViewById(R.id.optionImageBtn4);

        enableDisableButton(false);
    }

    /**
     * Description : Get the option selected by the participant and assign weightage to it.
     */
    private void getSelectedOption(){
        optionImageButtonOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOptionValue = 1;
                optionImageButtonOne.setBackgroundResource(R.drawable.selected_option);
                optionImageButtonTwo.setBackgroundResource(R.color.white);
                optionImageButtonThree.setBackgroundResource(R.color.white);
                optionImageButtonFour.setBackgroundResource(R.color.white);
                enableDisableButton(true);
            }
        });

        optionImageButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOptionValue = 2;
                optionImageButtonOne.setBackgroundResource(R.color.white);
                optionImageButtonTwo.setBackgroundResource(R.drawable.selected_option);
                optionImageButtonThree.setBackgroundResource(R.color.white);
                optionImageButtonFour.setBackgroundResource(R.color.white);
                enableDisableButton(true);
            }
        });

        optionImageButtonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOptionValue = 3;
                optionImageButtonOne.setBackgroundResource(R.color.white);
                optionImageButtonTwo.setBackgroundResource(R.color.white);
                optionImageButtonThree.setBackgroundResource(R.drawable.selected_option);
                optionImageButtonFour.setBackgroundResource(R.color.white);
                enableDisableButton(true);
            }
        });

        optionImageButtonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOptionValue = 4;
                optionImageButtonOne.setBackgroundResource(R.color.white);
                optionImageButtonTwo.setBackgroundResource(R.color.white);
                optionImageButtonThree.setBackgroundResource(R.color.white);
                optionImageButtonFour.setBackgroundResource(R.drawable.selected_option);
                enableDisableButton(true);
            }
        });
    }

    /**
     * Description : Move to the next question on clicking the next button
     */
    private void onClickNextButton() {
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SurveyScreen.this,
                        "Selected Value : " + selectedOptionValue,
                        Toast.LENGTH_LONG).show();
                surveyPHQ9.put(questionArray.get(questionIndex).getQn_number(), selectedOptionValue);                questionIndex++;
                setCardData();
                enableDisableButton(false);
                optionImageButtonOne.setBackgroundResource(R.color.white);
                optionImageButtonTwo.setBackgroundResource(R.color.white);
                optionImageButtonThree.setBackgroundResource(R.color.white);
                optionImageButtonFour.setBackgroundResource(R.color.white);
            }
        });
    }

    /**
     * Description : Sets the questions in the card view
     */
    private void setCardData() {
        if(questionIndex < questionArray.size()){
            ph9Question.setText(questionArray.get(questionIndex).getQuestion());
            totalQuestions.setText("of " + questionArray.size() +"");
            questionNumber.setText(questionArray.get(questionIndex).getQn_number());
            option_oneTV.setText(questionArray.get(questionIndex).getOption_1());
            option_twoTV.setText(questionArray.get(questionIndex).getOption_2());
            option_threeTV.setText(questionArray.get(questionIndex).getOption_3());
            option_fourTV.setText(questionArray.get(questionIndex).getOption_4());
        }
        else{
            endDateTime = getCurrentTime();
            showCongratulationAlert();
            callServerForPostSurveyAnswers();
            waitAndMoveToAnotherActivity();
        }
    }

    /**
     * Description : Update the server with the data entered by the user
     */
    private void callServerForPostSurveyAnswers(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.phq9_session.api.phqsessionpost";
        SurveyPostRequest surveyPostRequest = new SurveyPostRequest();
        surveyPostRequest.setUser_name(mithraUtility.getSharedPreferencesData(this, getString(R.string.user_name), getString(R.string.user_name)));
        surveyPostRequest.setAnswer(surveyPHQ9);
        surveyPostRequest.setSession_start(startDateTime);
        surveyPostRequest.setSession_stop(endDateTime);
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postSurveyAnswers(SurveyScreen.this, surveyPostRequest, url);
    }

    /**
     * Description : This method is used to wait for few seconds when the alert is showing and the  move to the participant landing screen.
     */
    private void waitAndMoveToAnotherActivity() {
        new Handler().postDelayed(new Runnable() {
            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                if(dialog.isShowing()){
                    dialog.create();
                    Intent loginIntent = new Intent(SurveyScreen.this, ParticipantLandingScreen.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        }, 3*1000);
    }

    /**
     * Description : Enable and disable the button
     */
    private void enableDisableButton(boolean isEnabled){
        if(isEnabled){
            nextButton.setEnabled(true);
            nextButton.setBackgroundResource(R.drawable.button_background);
            nextButton.setTextColor(Color.parseColor("#ffffff"));
        }else{
            nextButton.setEnabled(false);
            nextButton.setBackgroundResource(R.drawable.rounded_corners);
            nextButton.setTextColor(getResources().getColor(R.color.text_color));
        }
    }

    /**
     * Description : Get the current time of the user device for calculation of duration taken to complete the survey
     */
    private String getCurrentTime(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        Log.d("SurveyScreen","getCurrentTime : " + strDate);
        return strDate;
    }

    /**
     * Description : Used to show alert to the participant after completing the survey
     */
    private void showCongratulationAlert(){
        dialog  = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.activity_congratulation_popup);
        dialog.setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
        wmlp.gravity = Gravity.CENTER;
        wmlp.dimAmount = 0.5f;
        dialog.getWindow().setAttributes(wmlp);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
        dialog.show();
    }

    /**
     * Description : Call the server to get the PHQ9 questions for the participant
     */
    private void callServerToGetPHQ9Questions(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.phq9_question_english.api.phqs";
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getPHQ9Questions(SurveyScreen.this, url);
    }

    /**
     * Handle the success server response
     */
    @Override
    public void responseReceivedSuccessfully(String message) {
        Log.i("SurveyScreen", "responseReceivedSuccessfully");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<QuestionAnswers>>(){}.getType();
        JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
        ArrayList<QuestionAnswers> questionAnswersArrayList = new ArrayList<>();

        try{
            questionAnswersArrayList = gson.fromJson(jsonObject.get("message"), type);
            Collections.sort(questionAnswersArrayList, Comparator.comparingInt(question -> Integer.parseInt(question.getQn_number())));
            Log.i("SurveyScreen", "responseReceivedSuccessfully : " +questionAnswersArrayList);
            questionArray = questionAnswersArrayList;
            startDateTime = getCurrentTime();
            setCardData();
        }catch(Exception e){
            Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Description : Handles the failure server response
     */
    @Override
    public void responseReceivedFailure(String message) {
        Log.i("SurveyScreen", "responseReceivedFailure : " +message);

        if(message!=null){
            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
            String serverErrorResponse = jsonObject.get("exception").toString();
            Toast.makeText(this, serverErrorResponse, Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
        }
    }
}
