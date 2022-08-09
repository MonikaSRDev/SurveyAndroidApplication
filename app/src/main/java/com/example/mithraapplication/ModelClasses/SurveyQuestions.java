package com.example.mithraapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class SurveyQuestions extends JsonServerObject{
//    @SerializedName("type")
    private String filter_data = "null";

    public String getFilter_data() {
        return filter_data;
    }

    public void setFilter_data(String filter_data) {
        this.filter_data = filter_data;
    }
}
