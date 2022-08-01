package com.example.mithraapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mithraapplication.Adapters.QuestionnaireSingleAdapter;
import com.example.mithraapplication.ModelClasses.SurveyQuestionAnswers;

import java.util.ArrayList;

public class QuestionnaireSingleScreen extends AppCompatActivity {

    private QuestionnaireSingleAdapter questionnaireSingleAdapter;
    private ArrayList<SurveyQuestionAnswers> surveyQuestionAnswersArrayList = new ArrayList<>() ;
    private RecyclerView recyclerViewSurvey;

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
        questionnaireSingleAdapter = new QuestionnaireSingleAdapter(QuestionnaireSingleScreen.this, surveyQuestionAnswersArrayList);
        recyclerViewSurvey.setAdapter(questionnaireSingleAdapter);
        recyclerViewSurvey.setLayoutManager(new LinearLayoutManager(QuestionnaireSingleScreen.this, LinearLayoutManager.VERTICAL, false));
    }
}
