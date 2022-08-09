package com.example.mithraapplication.MithraAppServerEventsListeners;

public interface SurveyServerEvents {

    void getSurveyQuestions(String message);

    void getSurveyOptions(String message);

    void postSurveyAnswers(String message);
}
