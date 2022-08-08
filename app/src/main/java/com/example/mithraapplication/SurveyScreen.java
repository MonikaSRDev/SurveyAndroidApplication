package com.example.mithraapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mithraapplication.ModelClasses.FrappeResponse;
import com.example.mithraapplication.ModelClasses.OptionsRequest;
import com.example.mithraapplication.ModelClasses.ParticipantAnswers;
import com.example.mithraapplication.ModelClasses.PostSurveyQuestions;
import com.example.mithraapplication.ModelClasses.QuestionAnswers;
import com.example.mithraapplication.ModelClasses.QuestionOptions;
import com.example.mithraapplication.ModelClasses.SurveyPostRequest;
import com.example.mithraapplication.ModelClasses.SurveyQuestions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SurveyScreen extends AppCompatActivity implements HandleServerResponse, HandleFileDownloadResponse {

    private TextView participantName, congratulationTV;
    private TextView questionNumber, totalQuestions, ph9Question, option_oneTV, option_twoTV, option_threeTV, option_fourTV;
    private Button nextButton, englishButton, kannadaButton;
    private ImageButton optionImageButtonOne, optionImageButtonTwo, optionImageButtonThree, optionImageButtonFour, speakerButton;
    private View option_view_one, option_view_two, option_view_three, option_view_four;
    private LinearLayout option1LinearLayout, option2LinearLayout, option3LinearLayout, option4LinearLayout;
    private int questionIndex = 0, selectedOptionValue = 0, totalScore = 0;
    private ArrayList<QuestionAnswers> questionArray = new ArrayList<>();
    private ArrayList<QuestionOptions> optionsArray = new ArrayList<>();
    private ArrayList<QuestionOptions> filteredOptionsArray = new ArrayList<>();
    private String surveyStartDateTime, surveyEndDateTime, totalSurveyTime, questionStartTime, questionEndTime, totalQuestionTime;
    private Dialog dialog;
    private HashMap<String, String> surveyPHQ9 = new HashMap<>();
    private ArrayList<ParticipantAnswers> surveyAnswers = new ArrayList<>();
    private final MithraUtility mithraUtility = new MithraUtility();
    private String selectedLanguage = "1", strSelectedOption = "null";
    private String postAnswers = "";
    private  MediaPlayer mediaPlayerAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_survey_screen);
        RegisterViews();
//        removeDataStoredSharedPreferences();
        getDataStoredFromSharedPreferences();
//        initializeData();
//        startDateTime = mithraUtility.getCurrentTime();
//        setCardData();
        getSelectedOption();
        onClickNextButton();
        onClickOfLanguageButton();
        getCurrentLocale();
        callServerToGetPHQ9Questions();
        callServerToGetPHQ9Options();
    }

    private void getDataStoredFromSharedPreferences(){
        String user_primary_id = mithraUtility.getSharedPreferencesData(SurveyScreen.this, getString(R.string.primaryID), getString(R.string.participantPrimaryID));
        Gson gson = new Gson();
        String surveyAnswersJson = mithraUtility.getSharedPreferencesData(SurveyScreen.this, getString(R.string.survey_answers), user_primary_id);
        Type listType  = new TypeToken<SurveyPostRequest>() {}.getType();
        SurveyPostRequest surveyAnswersPostReq = gson.fromJson(surveyAnswersJson, listType);
        if(surveyAnswersPostReq!=null){
            ArrayList<ParticipantAnswers> answersArrayList = surveyAnswersPostReq.getAnswer();
            surveyAnswers = answersArrayList;
            for(int i = 0; i < surveyAnswers.size(); i++){
                postAnswers += getAnswersList(i, surveyAnswers) + ",";
                if(i == questionArray.size()-1){
                    postAnswers = postAnswers.substring(0, postAnswers.length()-1);
                }
            }
            questionIndex = answersArrayList.size();
            totalScore = Integer.parseInt(surveyAnswersPostReq.getScore());
            surveyStartDateTime = surveyAnswersPostReq.getSurvey_start(); // should it be updated or same?
        }
    }

    private void removeDataStoredSharedPreferences(){
        String user_primary_id = mithraUtility.getSharedPreferencesData(SurveyScreen.this, getString(R.string.primaryID), getString(R.string.participantPrimaryID));
        mithraUtility.removeSharedPreferencesData(SurveyScreen.this, getString(R.string.survey_answers), user_primary_id);
    }

    private void initializeData() {
        String message = "{\"message\":\n" +
                "\t[\n" +
                "\t\t{\"qn_number\":\"9\",\n" +
                "\t\t\"question\":\"Thoughts that you would be better off dead, or of hurting yourself. \",\n" +
                "\t\t\"option_1\":\"Not at all\",\n" +
                "\t\t\"option_2\":\"Several Days\",\n" +
                "\t\t\"option_3\":\"More than half the days\",\n" +
                "\t\t\"option_4\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೯\",\n" +
                "\t\t\"questionKannada\":\"ನೀವು ಸತ್ತರೆ ಚೆನ್ನಾಗಿರುತ್ತದೆ ಎಂಬ ಅಥವಾ ಯಾವುದಾದರೂ ರೀತಿಯಲ್ಲಿ ನಿಮ್ಮನ್ನು ಹಾನಿಪಡಿಸಿಕೊಳ್ಳುವ ಯೋಚನೆಗಳು.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\t\t\n" +
                "\t\t{\"qn_number\":\"8\",\n" +
                "\t\t\"question\":\"Moving or speaking so slowly that other people could have noticed. Or the opposite being so fidgety or restless that you have been moving around a lot more than usual. \\n\",\n" +
                "\t\t\"option_1\":\"Not at all\",\n" +
                "\t\t\"option_2\":\"Several Days\",\n" +
                "\t\t\"option_3\":\"More than half the days\",\n" +
                "\t\t\"option_4\":\"nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೮\",\n" +
                "\t\t\"questionKannada\":\"ಇತರರ ಗಮನಕ್ಕೆ ಬರುವಷ್ಟು ನಿದಾನವಾಗಿ ನಡೆದಾಡುವುದು ಅಥವಾ ಮಾತನಾಡುವುದು? ಅಥವಾ ತದ್ವಿರುದ್ಧವಾಗಿ - ಸಾಮಾನ್ಯಕ್ಕಿಂತ ಹೆಚ್ಚು ಅತ್ತಿಂದಿತ್ತ ಓಡಾಡುವಷ್ಟು ಚಡಪಡಿಕೆ ಅಥವಾ ಅಶಾಂತಿ.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\t\t\n" +
                "\t\t{\"qn_number\":\"7\",\n" +
                "\t\t\"question\":\"Trouble concentrating on things, such as reading the newspaper or watching television.  \",\n" +
                "\t\t\"option_1\":\"Not at all\",\n" +
                "\t\t\"option_2\":\"Several Days \",\n" +
                "\t\t\"option_3\":\"More than half the days\",\n" +
                "\t\t\"option_4\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೭\",\n" +
                "\t\t\"questionKannada\":\"ಪತ್ರಿಕೆಯನ್ನ ಓದಲು ಅಥವಾ ಟೆಲಿವಿಷನ್ ನೋಡುವುದು ಇತ್ಯಾದಿ ವಿಷಯಗಳಲ್ಲಿ ಗಮನ ಕೇಂದ್ರೀಕರಿಸಲು ತೊಂದರೆ.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\n" +
                "\t\t{\"qn_number\":\"6\",\n" +
                "\t\t\"question\":\"Feeling bad about yourself or that you are a failure or have let yourself or your family down.\",\n" +
                "\t\t\"option_1\":\"Not at all\",\n" +
                "\t\t\"option_2\":\"Several Days\",\n" +
                "\t\t\"option_3\":\"More than half the days\",\n" +
                "\t\t\"option_4\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೬\",\n" +
                "\t\t\"questionKannada\":\"ನಿಮ್ಮ ಬಗ್ಗೆ ನಿಮಗೆ ಕೆಟ್ಟ ಭಾವನೆ - ಅಥವಾ ನೀವು ವಿಫಲರು ಅಥವಾ ನೀವು ನಿಮ್ಮ ಹಾಗು ಕುಟುಂಬದವರ ನಿರೀಕ್ಷೆಗಿಂತ ಕೆಳಮಟ್ಟದಲ್ಲಿದ್ದೀರಿ ಎಂಬ ಭಾವನೆ.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\n" +
                "\t\t{\"qn_number\":\"5\",\n" +
                "\t\t\"question\":\"Poor appetite or over eating\",\n" +
                "\t\t\"option_1\":\"Not at all\",\n" +
                "\t\t\"option_2\":\"Several Days\",\n" +
                "\t\t\"option_3\":\"More than half the days\",\n" +
                "\t\t\"option_4\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೫\",\n" +
                "\t\t\"questionKannada\":\"ಕಡಿಮೆ ಹಸಿವು ಅಥವಾ ಅತಿಯಾಗಿ ತಿನ್ನುವುದು.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\n" +
                "\t\t{\"qn_number\":\"4\",\n" +
                "\t\t\"question\":\"Feeling tired or having little energy.\",\n" +
                "\t\t\"option_1\":\"Not at all\",\n" +
                "\t\t\"option_2\":\"Several Days\",\n" +
                "\t\t\"option_3\":\"More than half the days\",\n" +
                "\t\t\"option_4\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೪\",\n" +
                "\t\t\"questionKannada\":\"ಆಯಾಸಗೊಳ್ಳುವುದು ಅಥವಾ ಚೈತನ್ಯ ಇಲ್ಲದಿರುವುದು.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\n" +
                "\t\t{\"qn_number\":\"3\",\n" +
                "\t\t\"question\":\"Trouble falling or staying asleep, or sleeping too much\",\n" +
                "\t\t\"option_1\":\"Not at all\",\n" +
                "\t\t\"option_2\":\"Several Days\",\n" +
                "\t\t\"option_3\":\"More than half the days\",\n" +
                "\t\t\"option_4\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೩\",\n" +
                "\t\t\"questionKannada\":\"ನಿದ್ರೆ ಬರುವದು ಅಥವಾ ನಿದ್ರೆಯಲ್ಲಿರುವುದಕ್ಕೆ ತೊಂದರೆ ಅಥವಾ ಅತಿಯಾಗಿ ನಿದ್ರೆ ಮಾಡುವುದು.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\n" +
                "\t\t{\"qn_number\":\"1\",\n" +
                "\t\t\"question\":\"Little interest or pleasure in doing things.\",\n" +
                "\t\t\"option_1\":\"Not at all\",\n" +
                "\t\t\"option_2\":\"Several Days\",\n" +
                "\t\t\"option_3\":\"More than half the days\",\n" +
                "\t\t\"option_4\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೧\",\n" +
                "\t\t\"questionKannada\":\"ಕೆಲಸಗಳನ್ನು ಮಾಡುವುದರಲ್ಲಿ ಕಡಿಮೆ ಆಸಕ್ತಿ ಅಥವಾ ಆನಂದ.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"},\n" +
                "\n" +
                "\t\t{\"qn_number\":\"2\",\n" +
                "\t\t\"question\":\"Feeling down, depressed, or hopeless.\",\n" +
                "\t\t\"option_1\":\"Not at all\",\n" +
                "\t\t\"option_2\":\"Several days\",\n" +
                "\t\t\"option_3\":\"More than half the days\",\n" +
                "\t\t\"option_4\":\"Nearly every day\",\n" +
                "\t\t\"qn_numberKannada\":\"೨\",\n" +
                "\t\t\"questionKannada\":\"ಬೇಸರ, ಖಿನ್ನತೆ ಅಥವಾ ಹತಾಶೆ.\",\n" +
                "\t\t\"option_1Kannada\":\"ಇಲ್ಲವೇ ಇಲ್ಲ\",\n" +
                "\t\t\"option_2Kannada\":\"ಹಲವು ದಿನಗಳು\",\n" +
                "\t\t\"option_3Kannada\":\"ಅರ್ಧಕ್ಕಿಂತ ಹೆಚ್ಚು ದಿನಗಳು\",\n" +
                "\t\t\"option_4Kannada\":\"ಬಹುತೇಕ ಪ್ರತಿದಿನ\"}]}";
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
                surveyStartDateTime = mithraUtility.getCurrentTime();
                setCardData();
            }
        }catch(Exception e){
            Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
        }
    }


    /**
     * Description : This method is used to register the views
     */
    private void RegisterViews() {
        participantName = findViewById(R.id.participantNameTV);
        String participantUserName = mithraUtility.getSharedPreferencesData(this, getString(R.string.userName), getString(R.string.user_name_participant));
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
        speakerButton = findViewById(R.id.imageButtonSpeaker);

        option_view_one = findViewById(R.id.option_one_view);
        option_view_two = findViewById(R.id.option_two_view);
        option_view_three = findViewById(R.id.option_three_view);
        option_view_four = findViewById(R.id.option_four_view);

        englishButton = findViewById(R.id.englishQuestionButton);
        kannadaButton = findViewById(R.id.kannadaQuestionButton);

        congratulationTV = findViewById(R.id.congratulateTV);

        option1LinearLayout = findViewById(R.id.option_one_linearLayout);
        option2LinearLayout = findViewById(R.id.option_two_linearLayout);
        option3LinearLayout = findViewById(R.id.option_three_linearLayout);
        option4LinearLayout = findViewById(R.id.option_four_linearLayout);

        enableDisableButton(false);
    }

    /**
     * Description : Get the option selected by the participant and assign weightage to it.
     */
    private void getSelectedOption(){
        option1LinearLayout.setOnClickListener(v -> onClickOfOptionOne());

        option2LinearLayout.setOnClickListener(v -> onClickOfOptionTwo());

        option3LinearLayout.setOnClickListener(v -> onClickOfOptionThree());

        option4LinearLayout.setOnClickListener(v -> onClickOfOptionFour());

        optionImageButtonOne.setOnClickListener(v -> onClickOfOptionOne());

        optionImageButtonTwo.setOnClickListener(v -> onClickOfOptionTwo());

        optionImageButtonThree.setOnClickListener(v -> onClickOfOptionThree());

        optionImageButtonFour.setOnClickListener(v -> onClickOfOptionFour());
    }

    private void onClickOfOptionOne() {
        selectedOptionValue = Integer.parseInt(filteredOptionsArray.get(0).getOption_w());
        strSelectedOption = filteredOptionsArray.get(0).getOption_e();;
        questionEndTime = mithraUtility.getCurrentTime();//here or on click of next button??

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

    private void onClickOfOptionTwo() {
        selectedOptionValue = Integer.parseInt(filteredOptionsArray.get(1).getOption_w());
        strSelectedOption = filteredOptionsArray.get(1).getOption_e();
        questionEndTime = mithraUtility.getCurrentTime();

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

    private void onClickOfOptionThree() {
        selectedOptionValue = Integer.parseInt(filteredOptionsArray.get(2).getOption_w());
        strSelectedOption = filteredOptionsArray.get(2).getOption_e();
        questionEndTime = mithraUtility.getCurrentTime();

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

    private void onClickOfOptionFour() {
        selectedOptionValue = Integer.parseInt(filteredOptionsArray.get(3).getOption_w());
        strSelectedOption = filteredOptionsArray.get(3).getOption_e();
        questionEndTime = mithraUtility.getCurrentTime();

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

    /**
     * Description : Move to the next question on clicking the next button
     */
    private void onClickNextButton() {
        nextButton.setOnClickListener(v -> {
            surveyPHQ9.put(questionArray.get(questionIndex).getQn_number(), String.valueOf(selectedOptionValue));
            totalQuestionTime = mithraUtility.getTimeDifferenceSeconds(questionStartTime, questionEndTime);
            totalScore += selectedOptionValue;
            ParticipantAnswers participantAnswers = new ParticipantAnswers();
            participantAnswers.setQuestion_id(questionArray.get(questionIndex).getName());
            participantAnswers.setQuestion_no(questionArray.get(questionIndex).getQn_number());
            participantAnswers.setQuestion(questionArray.get(questionIndex).getQuestion_e());
            participantAnswers.setSelected_answer(strSelectedOption);
            participantAnswers.setSelected_answer_weightage(String.valueOf(selectedOptionValue));
            participantAnswers.setQuestion_start_time(questionStartTime);
            participantAnswers.setQuestion_stop_time(questionEndTime);
            participantAnswers.setSeconds_taken(totalQuestionTime);
            surveyAnswers.add(participantAnswers);
            if(questionIndex < questionArray.size()){
                postAnswers = postAnswers + getAnswersList(questionIndex, surveyAnswers) + ",";
            }else{
                postAnswers = postAnswers.substring(0, postAnswers.length()-1);
            }

            String user_primary_id = mithraUtility.getSharedPreferencesData(SurveyScreen.this, getString(R.string.primaryID), getString(R.string.participantPrimaryID));
            Gson gson = new Gson();
            String surveyAnswersJson = gson.toJson(getPostSurveyAnswers());
            mithraUtility.putSharedPreferencesData(SurveyScreen.this, getString(R.string.survey_answers), user_primary_id, surveyAnswersJson);
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
        });
    }

    private String getAnswersList(int position, ArrayList<ParticipantAnswers> surveyAnswersArrayList){
        List<String> surveyAnswer = new ArrayList<>();
        surveyAnswer.add("'qn_id'" + ":'" + surveyAnswersArrayList.get(position).getQuestion_id() +"'");
        surveyAnswer.add("'qn_no'" + ":'" + surveyAnswersArrayList.get(position).getQuestion_no()+"'");
        surveyAnswer.add("'question'" + ":'" + surveyAnswersArrayList.get(position).getQuestion()+"'");
        surveyAnswer.add("'ans'" + ":'" + surveyAnswersArrayList.get(position).getSelected_answer()+"'");
        surveyAnswer.add("'w'" + ":'" + surveyAnswersArrayList.get(position).getSelected_answer_weightage()+"'");
        surveyAnswer.add("'qn_start'" + ":'" + surveyAnswersArrayList.get(position).getQuestion_start_time()+"'");
        surveyAnswer.add("'qn_stop'" + ":'" + surveyAnswersArrayList.get(position).getQuestion_stop_time()+"'");
        surveyAnswer.add("'seconds'" + ":'" + surveyAnswersArrayList.get(position).getSeconds_taken()+"'");
        String surveyAnswerStr = String.join(",", surveyAnswer );
        surveyAnswerStr = "{" + surveyAnswerStr + "}";

        Log.i("ARRAY LIST", "Diseases Data - list" + surveyAnswer);
        Log.i("ARRAY LIST", "Diseases Data - String" + surveyAnswerStr);

        return surveyAnswerStr;
    }

    private SurveyPostRequest getPostSurveyAnswers(){
        totalSurveyTime = mithraUtility.getTimeDifferenceMinutes(surveyStartDateTime, surveyEndDateTime);
        SurveyPostRequest surveyPostRequest = new SurveyPostRequest();
        surveyPostRequest.setUser_pri_id(mithraUtility.getSharedPreferencesData(this, getString(R.string.primaryID), getString(R.string.participantPrimaryID)));
        surveyPostRequest.setCreated_user(mithraUtility.getSharedPreferencesData(this, getString(R.string.primaryID), getString(R.string.participantPrimaryID)));
        surveyPostRequest.setType("SUR0001");
        surveyPostRequest.setScore(String.valueOf(totalScore));
        surveyPostRequest.setMinutes(totalSurveyTime);
        surveyPostRequest.setAnswer(surveyAnswers);
        surveyPostRequest.setSurvey_start(surveyStartDateTime);
        surveyPostRequest.setSurvey_stop(surveyEndDateTime);
        return surveyPostRequest;
    }

    private void onClickOfSpeakerButton(String filepath, String filename){
        String path = getFilesDir().getAbsolutePath() + "/" + filename;
        File file = new File(path);
        speakerButton.setOnClickListener(v -> {
            if(file.exists()){
                mediaPlayerAudio = MediaPlayer.create(SurveyScreen.this, Uri.parse(path));
                if(mediaPlayerAudio.isPlaying()){
                    mediaPlayerAudio.stop();
                }else{
                    mediaPlayerAudio.start();
                }
            }else{
                downloadFileFromServer(filepath, filename);
            }
        });
    }

    private void downloadFileFromServer(String filepath, String filename){
        String path = getFilesDir().getAbsolutePath() + "/" + filename;
        File file = new File(path);
        if(!file.exists()){
            ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
            requestObject.setHandleFileDownloadResponse(this);
            requestObject.downloadFileRequest(SurveyScreen.this, filepath + "/"+filename);
        }
    }

    /**
     * Description : Sets the questions in the card view
     */
    private void setCardData() {
        questionStartTime = mithraUtility.getCurrentTime();
        if(mediaPlayerAudio!=null){
            mediaPlayerAudio.stop();
        }
        if(questionIndex < questionArray.size()){
            filteredOptionsArray = optionsArray.stream()
                    .filter(questionOptions -> !questionOptions.getQuestion_id().equalsIgnoreCase(questionArray.get(questionIndex).getName())).collect(Collectors.toCollection(ArrayList::new));
            if(selectedLanguage.equals("1")){
                ph9Question.setText(questionArray.get(questionIndex).getQuestion_e());
                totalQuestions.setText("of " + questionArray.size() +"");
                questionNumber.setText(String.valueOf(questionIndex + 1));
                option_oneTV.setText(filteredOptionsArray.get(0).getOption_e());
                option_twoTV.setText(filteredOptionsArray.get(1).getOption_e());
                option_threeTV.setText(filteredOptionsArray.get(2).getOption_e());
                option_fourTV.setText(filteredOptionsArray.get(3).getOption_e());
                onClickOfSpeakerButton(questionArray.get(questionIndex).getAudio_fileURL(), questionArray.get(questionIndex).getAudio_filename_e());
                downloadFileFromServer(questionArray.get(questionIndex).getAudio_fileURL(), questionArray.get(questionIndex).getAudio_filename_e());
            }else{
                ph9Question.setText(questionArray.get(questionIndex).getQuestion_k());
                totalQuestions.setText("of " + questionArray.size() +"");
                questionNumber.setText(String.valueOf(questionIndex + 1));
                option_oneTV.setText(filteredOptionsArray.get(0).getOption_k());
                option_twoTV.setText(filteredOptionsArray.get(1).getOption_k());
                option_threeTV.setText(filteredOptionsArray.get(2).getOption_k());
                option_fourTV.setText(filteredOptionsArray.get(3).getOption_k());
                onClickOfSpeakerButton(questionArray.get(questionIndex).getAudio_fileURL(), questionArray.get(questionIndex).getAudio_filename_k());
                downloadFileFromServer(questionArray.get(questionIndex).getAudio_fileURL(), questionArray.get(questionIndex).getAudio_filename_k());
            }

        }
        else{
            surveyEndDateTime = mithraUtility.getCurrentTime();
            showCongratulationAlert();
            callServerForPostSurveyAnswers();
            waitAndMoveToAnotherActivity();
        }
    }

    /**
     * Description : This method is used to wait for few seconds when the alert is showing and the  move to the participant landing screen.
     */
    private void waitAndMoveToAnotherActivity() {
        // Using handler with postDelayed called runnable run method
        new Handler().postDelayed(() -> {
            if(dialog.isShowing()){
                dialog.dismiss();
                Intent loginIntent = new Intent(SurveyScreen.this, ParticipantLandingScreen.class);
                loginIntent.putExtra("FromActivity", "SurveyScreen");
                startActivity(loginIntent);
                finish();
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
        dialog.setCanceledOnTouchOutside(false);
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
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.survey_questions.api.questions";
        SurveyQuestions surveyQuestions = new SurveyQuestions();
        surveyQuestions.setType("SUR0001");
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getPHQ9Questions(SurveyScreen.this, surveyQuestions, url);
    }

    /**
     * Description : Call the server to get the PHQ9 questions for the participant
     */
    private void callServerToGetPHQ9Options(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.survey_question_options.api.options";
        OptionsRequest optionsRequest = new OptionsRequest();
        optionsRequest.setFilter_data("{'sur_pri_id':'SUR0001'}");
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.getPHQ9Options(SurveyScreen.this, optionsRequest, url);
    }


    /**
     * Description : Update the server with the data entered by the user
     */
    private void callServerForPostSurveyAnswers(){
        String url = "http://"+ getString(R.string.base_url)+ "/api/method/mithra.mithra.doctype.survey_answer.api.survey_ans";
        totalSurveyTime = mithraUtility.getTimeDifferenceMinutes(surveyStartDateTime, surveyEndDateTime);
        PostSurveyQuestions surveyPostRequest = new PostSurveyQuestions();
        surveyPostRequest.setUser_pri_id(mithraUtility.getSharedPreferencesData(this, getString(R.string.primaryID), getString(R.string.participantPrimaryID)));
        surveyPostRequest.setCreated_user(mithraUtility.getSharedPreferencesData(this, getString(R.string.primaryID), getString(R.string.participantPrimaryID)));
        surveyPostRequest.setType("SUR0001");
        surveyPostRequest.setScore(String.valueOf(totalScore));
        surveyPostRequest.setMinutes(totalSurveyTime);
        surveyPostRequest.setAnswer(postAnswers.substring(0, postAnswers.length()-1));
        surveyPostRequest.setSurvey_start(surveyStartDateTime);
        surveyPostRequest.setSurvey_stop(surveyEndDateTime);
        ServerRequestAndResponse requestObject = new ServerRequestAndResponse();
        requestObject.setHandleServerResponse(this);
        requestObject.postSurveyAnswers(SurveyScreen.this, surveyPostRequest, url);
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
        if(jsonObject.get("message")!=null) {
            ArrayList<QuestionAnswers> questionAnswersArrayList = new ArrayList<>();

            try {
                questionAnswersArrayList = gson.fromJson(jsonObject.get("message"), type);
                if (questionAnswersArrayList.size() > 1) {
                    questionAnswersArrayList.sort(Comparator.comparingInt(question -> Integer.parseInt(question.getQn_number())));
                    Log.i("SurveyScreen", "responseReceivedSuccessfully : " + questionAnswersArrayList);
                    questionArray = questionAnswersArrayList;
//                    surveyStartDateTime = mithraUtility.getCurrentTime();
//                    setCardData();
                }
            } catch (Exception e) {
                removeDataStoredSharedPreferences();
//                Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                Type typeOptions = new TypeToken<ArrayList<QuestionOptions>>(){}.getType();
                if(jsonObject.get("message")!=null) {
                    ArrayList<QuestionOptions> questionOptionsArrayList = new ArrayList<>();
                    try {
                        questionOptionsArrayList = gson.fromJson(jsonObject.get("message"), typeOptions);
                        if (questionOptionsArrayList.size() > 0) {
                            optionsArray = questionOptionsArrayList;
                            surveyStartDateTime = mithraUtility.getCurrentTime();
                            setCardData();
                        }
                    } catch (Exception ex) {
                        Toast.makeText(this, jsonObject.get("message").toString(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        }else{
            JsonObject jsonObjectRegistration = JsonParser.parseString(message).getAsJsonObject();
            Type typeFrappe = new TypeToken<FrappeResponse>(){}.getType();
            if(jsonObjectRegistration.get("data")!=null) {
                FrappeResponse frappeResponse;
                frappeResponse = gson.fromJson(jsonObjectRegistration.get("data"), typeFrappe);
                removeDataStoredSharedPreferences();
            }
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
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButton.setOnClickListener(v -> {
            selectedLanguage = "1";
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
            changeLocalLanguage("en");
        });

        kannadaButton.setOnClickListener(v -> {
            selectedLanguage = "2";
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
            changeLocalLanguage("kn");
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
            selectedLanguage = "2";
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black));
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
        }else{
            selectedLanguage = "1";
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButton.setTextColor(getResources().getColor(R.color.black));
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_button);
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
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        nextButton.setText(R.string.next_question);
        if(congratulationTV!=null){
            congratulationTV.setText(R.string.congratulation_text);
        }
        if(questionArray.size()!=0){
            if(selectedLanguage.equals("1")){
                ph9Question.setText(questionArray.get(questionIndex).getQuestion_e());
                totalQuestions.setText("of " + questionArray.size() +"");
                questionNumber.setText(String.valueOf(questionIndex + 1));
                option_oneTV.setText(filteredOptionsArray.get(0).getOption_e());
                option_twoTV.setText(filteredOptionsArray.get(1).getOption_e());
                option_threeTV.setText(filteredOptionsArray.get(2).getOption_e());
                option_fourTV.setText(filteredOptionsArray.get(3).getOption_e());
                onClickOfSpeakerButton(questionArray.get(questionIndex).getAudio_fileURL(), questionArray.get(questionIndex).getAudio_filename_e());
                downloadFileFromServer(questionArray.get(questionIndex).getAudio_fileURL(), questionArray.get(questionIndex).getAudio_filename_e());
            }else{
                ph9Question.setText(questionArray.get(questionIndex).getQuestion_k());
                totalQuestions.setText("of " + questionArray.size() +"");
                questionNumber.setText(String.valueOf(questionIndex + 1));
                option_oneTV.setText(filteredOptionsArray.get(0).getOption_k());
                option_twoTV.setText(filteredOptionsArray.get(1).getOption_k());
                option_threeTV.setText(filteredOptionsArray.get(2).getOption_k());
                option_fourTV.setText(filteredOptionsArray.get(3).getOption_k());
                onClickOfSpeakerButton(questionArray.get(questionIndex).getAudio_fileURL(), questionArray.get(questionIndex).getAudio_filename_k());
                downloadFileFromServer(questionArray.get(questionIndex).getAudio_fileURL(), questionArray.get(questionIndex).getAudio_filename_k());
            }
        }
    }

    @Override
    public void onBackPressed() {
    }

    @Override
    public void fileDownloadedSuccessfully(byte[] message) {
        Log.i("VIDEODownload", "stop time :" + mithraUtility.getCurrentTime());
        FileOutputStream outputStream;
        String name = null;
        if(selectedLanguage.equals("1")){
            name = questionArray.get(questionIndex).getAudio_filename_e();
        }else{
            name = questionArray.get(questionIndex).getAudio_filename_k();
        }
        try{
            outputStream = this.openFileOutput(name, Context.MODE_PRIVATE);
            outputStream.write(message);
            outputStream.close();
            Toast.makeText(SurveyScreen.this, "File Downloaded Successfully", Toast.LENGTH_LONG).show();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void fileDownloadFailure(String message) {

    }
}
