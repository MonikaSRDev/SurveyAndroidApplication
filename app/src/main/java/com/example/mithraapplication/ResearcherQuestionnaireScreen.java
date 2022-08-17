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
import com.example.mithraapplication.ModelClasses.QuestionAnswers;

import java.util.ArrayList;
import java.util.Locale;

public class ResearcherQuestionnaireScreen extends AppCompatActivity {

    private ResearcherQuestionnaireAdapter questionnaireSingleAdapter;
    private ArrayList<QuestionAnswers> surveyQuestionAnswersArrayList = new ArrayList<>() ;
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
        QuestionAnswers surveyQuestionAnswers = new QuestionAnswers();
        surveyQuestionAnswers.setQuestion_e("This is the first question");
        surveyQuestionAnswers.setQn_number("1.");
        surveyQuestionAnswers.setSection("1");
        surveyQuestionAnswers.setSection_name("Please complete the question before moving to the next question.");
        surveyQuestionAnswers.setOption_type("text");

        QuestionAnswers surveyQuestionAnswers1 = new QuestionAnswers();
        surveyQuestionAnswers1.setQuestion_e("This is the second question");
        surveyQuestionAnswers1.setQn_number("2.");
        surveyQuestionAnswers1.setSection("1");
        surveyQuestionAnswers1.setSection_name("Please complete the question before moving to the next question.");
        surveyQuestionAnswers1.setOption_type("numeric");

        QuestionAnswers surveyQuestionAnswers2 = new QuestionAnswers();
        surveyQuestionAnswers2.setQuestion_e("This is the third question");
        surveyQuestionAnswers2.setQn_number("3.");
        surveyQuestionAnswers2.setSection("1");
        surveyQuestionAnswers2.setSection_name("Please complete the question before moving to the next question.");
        surveyQuestionAnswers2.setOption_type("date");

        QuestionAnswers surveyQuestionAnswers3 = new QuestionAnswers();
        surveyQuestionAnswers3.setQuestion_e("This is the fourth question");
        surveyQuestionAnswers3.setQn_number("4.");
        surveyQuestionAnswers3.setSection("1");
        surveyQuestionAnswers3.setSection_name("Please complete the question before moving to the next question.");
        surveyQuestionAnswers3.setOption_type("single");

        QuestionAnswers surveyQuestionAnswers4 = new QuestionAnswers();
        surveyQuestionAnswers4.setQuestion_e("This is the fifth question");
        surveyQuestionAnswers4.setQn_number("5.");
        surveyQuestionAnswers4.setSection("1");
        surveyQuestionAnswers4.setSection_name("Please complete the question before moving to the next question.");
        surveyQuestionAnswers4.setOption_type("multi_choice");

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
