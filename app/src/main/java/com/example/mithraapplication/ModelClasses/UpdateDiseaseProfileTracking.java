package com.example.mithraapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class UpdateDiseaseProfileTracking extends JsonServerObject{
    private String modified_user = "UT-9-2022-07-11-08:19:58-no_one_modified";
    private String disease_profile = null;
    private String enroll = "no";
    @SerializedName("base_line")
    private String registrationCompleteTime = "NULL";

    public String getModified_user() {
        return modified_user;
    }

    public void setModified_user(String modified_user) {
        this.modified_user = modified_user;
    }

    public String getDisease_profile() {
        return disease_profile;
    }

    public void setDisease_profile(String disease_profile) {
        this.disease_profile = disease_profile;
    }

    public String getEnroll() {
        return enroll;
    }

    public void setEnroll(String enroll) {
        this.enroll = enroll;
    }

    public String getRegistrationCompleteTime() {
        return registrationCompleteTime;
    }

    public void setRegistrationCompleteTime(String registrationCompleteTime) {
        this.registrationCompleteTime = registrationCompleteTime;
    }
}
