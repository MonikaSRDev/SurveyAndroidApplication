package com.example.mithraapplication;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.LocaleList;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.Adapters.ResearcherQuestionnaireAdapter;
import com.example.mithraapplication.ModelClasses.SurveyQuestionAnswers;

import java.util.ArrayList;
import java.util.Locale;

public class ResearcherQuestionnaireScreen extends AppCompatActivity {

    private ResearcherQuestionnaireAdapter questionnaireSingleAdapter;
    private ArrayList<SurveyQuestionAnswers> surveyQuestionAnswersArrayList = new ArrayList<>() ;
    private RecyclerView recyclerViewSurvey;
    private TextView participantName;
    private Button englishButton, kannadaButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questionnaire_single_screen);
        RegisterViews();
        initializeData();
        setRecyclerView();
    }

    private void RegisterViews(){
        recyclerViewSurvey = findViewById(R.id.questionnaireRV);
    }

    private void initializeData(){
        SurveyQuestionAnswers surveyQuestionAnswers = new SurveyQuestionAnswers();
        surveyQuestionAnswers.setE_Question("This is the first question");
        surveyQuestionAnswers.setQuestionNumber("1.");
        surveyQuestionAnswers.setSection("1");
        surveyQuestionAnswers.setSectionHeading("Please complete the question before moving to the next question.");
        surveyQuestionAnswers.setInputType("text");

        SurveyQuestionAnswers surveyQuestionAnswers1 = new SurveyQuestionAnswers();
        surveyQuestionAnswers1.setE_Question("This is the second question");
        surveyQuestionAnswers1.setQuestionNumber("2.");
        surveyQuestionAnswers1.setSection("1");
        surveyQuestionAnswers1.setSectionHeading("Please complete the question before moving to the next question.");
        surveyQuestionAnswers1.setInputType("numeric");

        SurveyQuestionAnswers surveyQuestionAnswers2 = new SurveyQuestionAnswers();
        surveyQuestionAnswers2.setE_Question("This is the third question");
        surveyQuestionAnswers2.setQuestionNumber("3.");
        surveyQuestionAnswers2.setSection("1");
        surveyQuestionAnswers2.setSectionHeading("Please complete the question before moving to the next question.");
        surveyQuestionAnswers2.setInputType("date");

        SurveyQuestionAnswers surveyQuestionAnswers3 = new SurveyQuestionAnswers();
        surveyQuestionAnswers3.setE_Question("This is the fourth question");
        surveyQuestionAnswers3.setQuestionNumber("4.");
        surveyQuestionAnswers3.setSection("1");
        surveyQuestionAnswers3.setSectionHeading("Please complete the question before moving to the next question.");
        surveyQuestionAnswers3.setInputType("single");

        SurveyQuestionAnswers surveyQuestionAnswers4 = new SurveyQuestionAnswers();
        surveyQuestionAnswers4.setE_Question("This is the fifth question");
        surveyQuestionAnswers4.setQuestionNumber("5.");
        surveyQuestionAnswers4.setSection("1");
        surveyQuestionAnswers4.setSectionHeading("Please complete the question before moving to the next question.");
        surveyQuestionAnswers4.setInputType("multi_choice");

        surveyQuestionAnswersArrayList.add(surveyQuestionAnswers);
        surveyQuestionAnswersArrayList.add(surveyQuestionAnswers1);
        surveyQuestionAnswersArrayList.add(surveyQuestionAnswers2);
        surveyQuestionAnswersArrayList.add(surveyQuestionAnswers3);
        surveyQuestionAnswersArrayList.add(surveyQuestionAnswers4);
    }

    private void setRecyclerView(){
        questionnaireSingleAdapter = new ResearcherQuestionnaireAdapter(ResearcherQuestionnaireScreen.this, surveyQuestionAnswersArrayList);
        recyclerViewSurvey.setAdapter(questionnaireSingleAdapter);
        recyclerViewSurvey.setLayoutManager(new LinearLayoutManager(ResearcherQuestionnaireScreen.this, LinearLayoutManager.VERTICAL, false));
    }

    /**
     * Description : This method is used to change the language of the screen based on the button clicked
     */
    private void onClickOfLanguageButton(){
        englishButton.setOnClickListener(v -> {
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButton.setTextColor(getResources().getColor(R.color.black, ResearcherQuestionnaireScreen.this.getTheme()));
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black, ResearcherQuestionnaireScreen.this.getTheme()));
            changeLocalLanguage("en");
        });

        kannadaButton.setOnClickListener(v -> {
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black, ResearcherQuestionnaireScreen.this.getTheme()));
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black, ResearcherQuestionnaireScreen.this.getTheme()));
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
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_selected_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black, ResearcherQuestionnaireScreen.this.getTheme()));
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_button);
            englishButton.setTextColor(getResources().getColor(R.color.black, ResearcherQuestionnaireScreen.this.getTheme()));
        }else{
            englishButton.setBackgroundResource(R.drawable.left_english_toggle_selected_button);
            englishButton.setTextColor(getResources().getColor(R.color.black, ResearcherQuestionnaireScreen.this.getTheme()));
            kannadaButton.setBackgroundResource(R.drawable.right_kannada_toggle_button);
            kannadaButton.setTextColor(getResources().getColor(R.color.black, ResearcherQuestionnaireScreen.this.getTheme()));
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
    }
}
