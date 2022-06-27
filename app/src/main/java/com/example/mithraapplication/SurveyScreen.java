package com.example.mithraapplication;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
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
    private View option_view_one, option_view_two, option_view_three, option_view_four;
    private int questionIndex = 0;
    private int selectedOptionValue = 0;
    private ArrayList<QuestionAnswers> questionArray = new ArrayList<>();
    private String startDateTime, endDateTime;
    private Dialog dialog;
    private HashMap<String, String> surveyPHQ9 = new HashMap<>();
    private String surveyAnswers = "{";
//    private ArrayList<String surveyPHQ9 = "NULL";
    private MithraUtility mithraUtility = new MithraUtility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_screen);
        RegisterViews();
//        initializeData();
//        startDateTime = mithraUtility.getCurrentTime();
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
        String participantUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.user_name), getString(R.string.user_name_participant));
        if(!participantUserName.equals("NULL")){
           participantName.setText(participantUserName);
        }
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

        option_view_one = findViewById(R.id.option_one_view);
        option_view_two = findViewById(R.id.option_two_view);
        option_view_three = findViewById(R.id.option_three_view);
        option_view_four = findViewById(R.id.option_four_view);

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
                option_view_one.setVisibility(View.VISIBLE);
                option_view_two.setVisibility(View.INVISIBLE);
                option_view_three.setVisibility(View.INVISIBLE);
                option_view_four.setVisibility(View.INVISIBLE);

                option_oneTV.setTextColor(getResources().getColor(R.color.options_color));
                option_twoTV.setTextColor(getResources().getColor(R.color.text_color));
                option_threeTV.setTextColor(getResources().getColor(R.color.text_color));
                option_fourTV.setTextColor(getResources().getColor(R.color.text_color));

                enableDisableButton(true);
            }
        });

        optionImageButtonTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOptionValue = 2;

                option_view_one.setVisibility(View.INVISIBLE);
                option_view_two.setVisibility(View.VISIBLE);
                option_view_three.setVisibility(View.INVISIBLE);
                option_view_four.setVisibility(View.INVISIBLE);

                option_oneTV.setTextColor(getResources().getColor(R.color.text_color));
                option_twoTV.setTextColor(getResources().getColor(R.color.options_color));
                option_threeTV.setTextColor(getResources().getColor(R.color.text_color));
                option_fourTV.setTextColor(getResources().getColor(R.color.text_color));

                enableDisableButton(true);
            }
        });

        optionImageButtonThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOptionValue = 3;

                option_view_one.setVisibility(View.INVISIBLE);
                option_view_two.setVisibility(View.INVISIBLE);
                option_view_three.setVisibility(View.VISIBLE);
                option_view_four.setVisibility(View.INVISIBLE);

                option_oneTV.setTextColor(getResources().getColor(R.color.text_color));
                option_twoTV.setTextColor(getResources().getColor(R.color.text_color));
                option_threeTV.setTextColor(getResources().getColor(R.color.options_color));
                option_fourTV.setTextColor(getResources().getColor(R.color.text_color));

                enableDisableButton(true);
            }
        });

        optionImageButtonFour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedOptionValue = 4;

                option_view_one.setVisibility(View.INVISIBLE);
                option_view_two.setVisibility(View.INVISIBLE);
                option_view_three.setVisibility(View.INVISIBLE);
                option_view_four.setVisibility(View.VISIBLE);

                option_oneTV.setTextColor(getResources().getColor(R.color.text_color));
                option_twoTV.setTextColor(getResources().getColor(R.color.text_color));
                option_threeTV.setTextColor(getResources().getColor(R.color.text_color));
                option_fourTV.setTextColor(getResources().getColor(R.color.options_color));

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
                surveyPHQ9.put(questionArray.get(questionIndex).getQn_number(), String.valueOf(selectedOptionValue));
                surveyAnswers = surveyAnswers + "'" +questionArray.get(questionIndex).getQn_number().charAt(0) + "':'" + String.valueOf(selectedOptionValue).charAt(0) + "',";
                questionIndex++;
                setCardData();
                enableDisableButton(false);
                option_view_one.setVisibility(View.INVISIBLE);
                option_view_two.setVisibility(View.INVISIBLE);
                option_view_three.setVisibility(View.INVISIBLE);
                option_view_four.setVisibility(View.INVISIBLE);

                option_oneTV.setTextColor(getResources().getColor(R.color.text_color));
                option_twoTV.setTextColor(getResources().getColor(R.color.text_color));
                option_threeTV.setTextColor(getResources().getColor(R.color.text_color));
                option_fourTV.setTextColor(getResources().getColor(R.color.text_color));
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
            endDateTime = mithraUtility.getCurrentTime();
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
        surveyPostRequest.setUser_name(mithraUtility.getSharedPreferencesData(this, getString(R.string.user_name), getString(R.string.user_name_participant)));
//        String diseaseStr = String.join(",", (CharSequence) surveyPHQ9);
        Log.i("SURVEY QUESTIONS", "String List" + surveyPHQ9);
        Log.i("SURVEY QUESTIONS", "Character List" + surveyAnswers);
//        String answers = String.join(",", surveyAnswers);
        surveyAnswers = surveyAnswers.substring(0, surveyAnswers.length()-1) + "}";
        Log.i("SURVEY QUESTIONS", "After Character List" + surveyAnswers);
        surveyPostRequest.setAnswer(surveyAnswers);
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
                    dialog.dismiss();
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
            if(questionAnswersArrayList.size() > 1){
                questionAnswersArrayList.sort(Comparator.comparingInt(question -> Integer.parseInt(question.getQn_number())));
                Log.i("SurveyScreen", "responseReceivedSuccessfully : " +questionAnswersArrayList);
                questionArray = questionAnswersArrayList;
                startDateTime = mithraUtility.getCurrentTime();
                setCardData();
            }
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

//        if(message!=null){
//            JsonObject jsonObject = JsonParser.parseString(message).getAsJsonObject();
//            String serverErrorResponse = jsonObject.get("exception").toString();
//            Toast.makeText(this, serverErrorResponse, Toast.LENGTH_LONG).show();
//        }else{
//            Toast.makeText(this, "Something went wrong. Please try again later.", Toast.LENGTH_LONG).show();
//        }
    }

    /**
     * @param newConfig
     * Description : This method is used to update the views on change of language
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
