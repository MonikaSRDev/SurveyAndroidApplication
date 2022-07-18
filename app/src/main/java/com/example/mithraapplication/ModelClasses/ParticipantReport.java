package com.example.mithraapplication.ModelClasses;

public class ParticipantReport extends JsonServerObject{
    private String surveyName = "null";
    private String surveyStart ="null";
    private String surveyStop = "null";
    private String surveyScore = "null";
    private String modulesAssigned = "null";
    private String activityCompleted = "null";

    public String getSurveyName() {
        return surveyName;
    }

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }

    public String getSurveyStart() {
        return surveyStart;
    }

    public void setSurveyStart(String surveyStart) {
        this.surveyStart = surveyStart;
    }

    public String getSurveyStop() {
        return surveyStop;
    }

    public void setSurveyStop(String surveyStop) {
        this.surveyStop = surveyStop;
    }

    public String getSurveyScore() {
        return surveyScore;
    }

    public void setSurveyScore(String surveyScore) {
        this.surveyScore = surveyScore;
    }

    public String getModulesAssigned() {
        return modulesAssigned;
    }

    public void setModulesAssigned(String modulesAssigned) {
        this.modulesAssigned = modulesAssigned;
    }

    public String getActivityCompleted() {
        return activityCompleted;
    }

    public void setActivityCompleted(String activityCompleted) {
        this.activityCompleted = activityCompleted;
    }
}
