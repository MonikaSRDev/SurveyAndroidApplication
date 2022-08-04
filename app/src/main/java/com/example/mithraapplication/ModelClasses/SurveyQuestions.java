package com.example.mithraapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class SurveyQuestions extends JsonServerObject{
    @SerializedName("survey_pri_id")
    String type = "NULL";

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
