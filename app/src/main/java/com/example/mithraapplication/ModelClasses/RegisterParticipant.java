package com.example.mithraapplication.ModelClasses;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

public class RegisterParticipant extends JsonServerObject {
    @SerializedName("user_name")
    private String participantUserName = "NULL";
    @SerializedName("full_name")
    private String participantName = "NULL";
    @SerializedName("gender")
    private String participantGender = "NULL";
    @SerializedName("age")
    private String participantAge = "NULL";
    @SerializedName("mobile_number")
    private String participantPhoneNumber = "NULL";
    @SerializedName("panchayat")
    private String participantPanchayat = "NULL";
    @SerializedName("village_name")
    private String participantVillageName = "NULL";
    @SerializedName("shg_associate")
    private String participantSHGAssociation = "NULL";
    private String screeningid = "NULL";
//    @SerializedName("password")
//    private String participantPassword = "NULL";

    public String getParticipantName() {
        return participantName;
    }

    public void setParticipantName(String participantName) {
        this.participantName = participantName;
    }

    public String getParticipantGender() {
        return participantGender;
    }

    public void setParticipantGender(String participantGender) {
        this.participantGender = participantGender;
    }

    public String getParticipantAge() {
        return participantAge;
    }

    public void setParticipantAge(String participantAge) {
        this.participantAge = participantAge;
    }

    public String getParticipantPhoneNumber() {
        return participantPhoneNumber;
    }

    public void setParticipantPhoneNumber(String participantPhoneNumber) {
        this.participantPhoneNumber = participantPhoneNumber;
    }

    public String getParticipantVillageName() {
        return participantVillageName;
    }

    public void setParticipantVillageName(String participantVillageName) {
        this.participantVillageName = participantVillageName;
    }

    public String getParticipantSHGAssociation() {
        return participantSHGAssociation;
    }

    public void setParticipantSHGAssociation(String participantSHGAssociation) {
        this.participantSHGAssociation = participantSHGAssociation;
    }

    public String getParticipantUserName() {
        return participantUserName;
    }

    public void setParticipantUserName(String participantUserName) {
        this.participantUserName = participantUserName;
    }

//    public String getParticipantPassword() {
//        return participantPassword;
//    }
//
//    public void setParticipantPassword(String participantPassword) {
//        this.participantPassword = participantPassword;
//    }

    public String getParticipantPanchayat() {
        return participantPanchayat;
    }

    public void setParticipantPanchayat(String participantPanchayat) {
        this.participantPanchayat = participantPanchayat;
    }

    public String getScreeningid() {
        return screeningid;
    }

    public void setScreeningid(String screeningid) {
        this.screeningid = screeningid;
    }

    public JsonServerObject FromJson(String jsonString, String methodName){

        Gson gson = new Gson();
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject jsonObject1 = jsonObject.getAsJsonObject(methodName);
        return gson.fromJson(jsonObject1.toString(), RegisterParticipant.class);
    }
}
