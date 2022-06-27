package com.example.mithraapplication.ModelClasses;

import java.util.HashMap;

public class SurveyPostRequest extends  JsonServerObject{
    private String user_name = "NULL";
    private String answer = "NULL" ;
    private String session_start = "NULL";
    private String session_stop = "NULL";

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getSession_start() {
        return session_start;
    }

    public void setSession_start(String session_start) {
        this.session_start = session_start;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSession_stop() {
        return session_stop;
    }

    public void setSession_stop(String session_stop) {
        this.session_stop = session_stop;
    }
}
