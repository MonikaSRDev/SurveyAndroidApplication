package com.example.mithraapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class PostSurveyQuestions extends JsonServerObject{
    private String user_pri_id = "null";
    private String created_user = "null";
    @SerializedName("sur_pri_id")
    private String survey_primary_id = "SUR0001";
    @SerializedName("sur_ins_id")
    private String survey_instance_id = "SIID1";
    private String answer = "null";
    private String survey_start = "null";
    private String survey_stop = "null";
    private String minutes = "null";
    private String score = "null";

    public String getUser_pri_id() {
        return user_pri_id;
    }

    public void setUser_pri_id(String user_pri_id) {
        this.user_pri_id = user_pri_id;
    }

    public String getCreated_user() {
        return created_user;
    }

    public void setCreated_user(String created_user) {
        this.created_user = created_user;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSurvey_start() {
        return survey_start;
    }

    public void setSurvey_start(String survey_start) {
        this.survey_start = survey_start;
    }

    public String getSurvey_stop() {
        return survey_stop;
    }

    public void setSurvey_stop(String survey_stop) {
        this.survey_stop = survey_stop;
    }

    public String getMinutes() {
        return minutes;
    }

    public void setMinutes(String minutes) {
        this.minutes = minutes;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getSurvey_instance_id() {
        return survey_instance_id;
    }

    public void setSurvey_instance_id(String survey_instance_id) {
        this.survey_instance_id = survey_instance_id;
    }

    public String getSurvey_primary_id() {
        return survey_primary_id;
    }

    public void setSurvey_primary_id(String survey_primary_id) {
        this.survey_primary_id = survey_primary_id;
    }
}
