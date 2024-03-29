package com.example.mithraapplication.ModelClasses;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class RegisterParticipant extends JsonServerObject implements Serializable {
    private String name = "null";
    @SerializedName("user_name")
    private String participantUserName = "null";
    @SerializedName("full_name")
    private String participantName = "null";
    @SerializedName("gender")
    private String participantGender = "null";
    @SerializedName("age")
    private String participantAge = "null";
    @SerializedName("mobile_number")
    private String participantPhoneNumber = "null";
    @SerializedName("panchayat")
    private String participantPanchayat = "null";
    @SerializedName("village_name")
    private String participantVillageName = "null";
    @SerializedName("shg")
    private String participantSHGAssociation = "null";
    private String user_pri_id = "null";
    private String created_user = "null";
    private String phq_scr_id = "null";
    private String man_id = "null";
    private String modified_user = "UT-9-2022-07-11-08:19:58-no_one_modified";
    private String active = "yes";
    private String location = "null";

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhq_scr_id() {
        return phq_scr_id;
    }

    public void setPhq_scr_id(String phq_scr_id) {
        this.phq_scr_id = phq_scr_id;
    }

    public String getMan_id() {
        return man_id;
    }

    public void setMan_id(String man_id) {
        this.man_id = man_id;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public JsonServerObject FromJson(String jsonString, String methodName){

        Gson gson = new Gson();
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject jsonObject1 = jsonObject.getAsJsonObject(methodName);
        return gson.fromJson(jsonObject1.toString(), RegisterParticipant.class);
    }
}
