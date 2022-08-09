package com.example.mithraapplication.MithraAppServerEventsListeners;

public interface PHQQuestionnaireServerEvents {

    void getPHQ9Questions(String message);

    void getPHQ9Options(String message);

    void postPHQAnswers(String message);
}
