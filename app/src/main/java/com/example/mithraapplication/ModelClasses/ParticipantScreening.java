package com.example.mithraapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class ParticipantScreening extends  JsonServerObject{
    private String years = "Yes";
    private String resident = "Yes";
    private String cbo = "Yes";
    private String mental_illness = "No";
    private String substance = "No";
    private String suicide = "No";
    private String participate = "No";
    private String agree = "Yes";
    private int score = 0;
    private String name;

    public String getResident() {
        return resident;
    }

    public void setResident(String resident) {
        this.resident = resident;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public String getCbo() {
        return cbo;
    }

    public void setCbo(String cbo) {
        this.cbo = cbo;
    }

    public String getMental_illness() {
        return mental_illness;
    }

    public void setMental_illness(String mental_illness) {
        this.mental_illness = mental_illness;
    }

    public String getSubstance() {
        return substance;
    }

    public void setSubstance(String substance) {
        this.substance = substance;
    }

    public String getSuicide() {
        return suicide;
    }

    public void setSuicide(String suicide) {
        this.suicide = suicide;
    }

    public String getParticipate() {
        return participate;
    }

    public void setParticipate(String participate) {
        this.participate = participate;
    }

    public String getAgree() {
        return agree;
    }

    public void setAgree(String agree) {
        this.agree = agree;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
