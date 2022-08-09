package com.example.mithraapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PHQParticipantDetails extends JsonServerObject implements Serializable {
    @SerializedName("name")
    private String PHQScreeningID = "null";
    @SerializedName("man_id")
    private String ManualID = "null";
    @SerializedName("full_name")
    private String PHQParticipantName = "null";
    @SerializedName("score")
    private int PHQScreeningScore = 0;
    @SerializedName("screening_id")
    private String Screening_ID = "null";
    @SerializedName("shg")
    private String SHGName = "null";
    @SerializedName("screen_score")
    private String ScreeningConsentScore = "null";
    private String register = "no";

    public String getPHQScreeningID() {
        return PHQScreeningID;
    }

    public void setPHQScreeningID(String PHQScreeningID) {
        this.PHQScreeningID = PHQScreeningID;
    }

    public String getPHQParticipantName() {
        return PHQParticipantName;
    }

    public void setPHQParticipantName(String PHQParticipantName) {
        this.PHQParticipantName = PHQParticipantName;
    }

    public int getPHQScreeningScore() {
        return PHQScreeningScore;
    }

    public void setPHQScreeningScore(int PHQScreeningScore) {
        this.PHQScreeningScore = PHQScreeningScore;
    }

    public String getManualID() {
        return ManualID;
    }

    public void setManualID(String manualID) {
        ManualID = manualID;
    }

    public String getScreening_ID() {
        return Screening_ID;
    }

    public void setScreening_ID(String screening_ID) {
        Screening_ID = screening_ID;
    }

    public String getSHGName() {
        return SHGName;
    }

    public void setSHGName(String SHGName) {
        this.SHGName = SHGName;
    }

    public String getScreeningConsentScore() {
        return ScreeningConsentScore;
    }

    public void setScreeningConsentScore(String screeningConsentScore) {
        ScreeningConsentScore = screeningConsentScore;
    }

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }
}
