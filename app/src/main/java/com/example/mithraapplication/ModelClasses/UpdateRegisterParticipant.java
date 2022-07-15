package com.example.mithraapplication.ModelClasses;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

public class UpdateRegisterParticipant extends JsonServerObject{

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
    private String user_pri_id = "NULL";
    private String created_user = "NULL";
    private String modified_user = "UT-9-2022-07-11-08:19:58-no_one_modified";

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

    public String getParticipantPanchayat() {
        return participantPanchayat;
    }

    public void setParticipantPanchayat(String participantPanchayat) {
        this.participantPanchayat = participantPanchayat;
    }

    public String getUser_pri_id() {
        return user_pri_id;
    }

    public void setUser_pri_id(String user_pri_id) {
        this.user_pri_id = user_pri_id;
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

    public JsonServerObject FromJson(String jsonString, String methodName){

        Gson gson = new Gson();
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject jsonObject1 = jsonObject.getAsJsonObject(methodName);
        return gson.fromJson(jsonObject1.toString(), UpdateRegisterParticipant.class);
    }
}
