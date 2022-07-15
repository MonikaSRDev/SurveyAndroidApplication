package com.example.mithraapplication.ModelClasses;

public class TrackingParticipantStatus extends  JsonServerObject{
    private String name = "NULL";
    private String user_pri_id = "NULL";
    private String survey = "no";
    private String status = "no";
    private String refer = "no";
    private String active = "NULL";
    private String created_user = "NULL";
    private String modified_user = "UT-9-2022-07-11-08:19:58-no_one_modified";
    private String registration = "NULL";
    private String socio_demography = null;
    private String disease_profile = null;
    private String enroll = "no";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUser_pri_id() {
        return user_pri_id;
    }

    public void setUser_pri_id(String user_pri_id) {
        this.user_pri_id = user_pri_id;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
    }

    public String getSurvey() {
        return survey;
    }

    public void setSurvey(String survey) {
        this.survey = survey;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRefer() {
        return refer;
    }

    public void setRefer(String refer) {
        this.refer = refer;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreated_user() {
        return created_user;
    }

    public void setCreated_user(String created_user) {
        this.created_user = created_user;
    }

    public String getModified_user() {
        return modified_user;
    }

    public void setModified_user(String modified_user) {
        this.modified_user = modified_user;
    }

    public String getSocio_demography() {
        return socio_demography;
    }

    public void setSocio_demography(String socio_demography) {
        this.socio_demography = socio_demography;
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
}
