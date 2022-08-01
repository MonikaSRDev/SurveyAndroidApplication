package com.example.mithraapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ParticipantDetails extends JsonServerObject implements Serializable {
    private String user_pri_id = "null";
    private String user_name = "null";
    private String enroll = "null";
    private String registration = "null";
    private String socio_demography = "null";
    private String disease_profile = "null";
    private String full_name = "null";
    private String age = "null";
    private String mobile_number = "null";
    private String village_name;
    private String shg_associate;
    private String panchayat;
    private String active;

    public String getUser_pri_id() {
        return user_pri_id;
    }

    public void setUser_pri_id(String user_pri_id) {
        this.user_pri_id = user_pri_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getEnroll() {
        return enroll;
    }

    public void setEnroll(String enroll) {
        this.enroll = enroll;
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public String getShg_associate() {
        return shg_associate;
    }

    public void setShg_associate(String shg_associate) {
        this.shg_associate = shg_associate;
    }

    public String getPanchayat() {
        return panchayat;
    }

    public void setPanchayat(String panchayat) {
        this.panchayat = panchayat;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
