package com.example.mithraapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class PHQSurveyPostAnswers extends JsonServerObject{
    private String man_id = "null";
    private String created_user = "null";
    @SerializedName("sur_pri_id")
    private String type = "null";
    private String answer = "null";
    private String survey_start_time = "null";
    private String survey_end_time = "null";
    private String minutes = "null";
    private String score = "null";
    @SerializedName("full_name")
    private String ParticipantName = "null";
    private String screening_id = "null";
    private String shg="null";

    public String getMan_id() {
        return man_id;
    }

    public void setMan_id(String man_id) {
        this.man_id = man_id;
    }

    public String getCreated_user() {
        return created_user;
    }

    public void setCreated_user(String created_user) {
        this.created_user = created_user;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getSurvey_start_time() {
        return survey_start_time;
    }

    public void setSurvey_start_time(String survey_start_time) {
        this.survey_start_time = survey_start_time;
    }

    public String getSurvey_end_time() {
        return survey_end_time;
    }

    public void setSurvey_end_time(String survey_end_time) {
        this.survey_end_time = survey_end_time;
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

    public String getParticipantName() {
        return ParticipantName;
    }

    public void setParticipantName(String participantName) {
        ParticipantName = participantName;
    }

    public String getScreening_id() {
        return screening_id;
    }

    public void setScreening_id(String screening_id) {
        this.screening_id = screening_id;
    }

    public String getShg() {
        return shg;
    }

    public void setShg(String shg) {
        this.shg = shg;
    }
}
