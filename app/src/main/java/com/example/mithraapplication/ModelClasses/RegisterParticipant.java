package com.example.mithraapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class RegisterParticipant {
    @SerializedName("full_name")
    private String participantName;
    @SerializedName("gender")
    private String participantGender;
    @SerializedName("age")
    private String participantAge;
    @SerializedName("mobile_number")
    private String participantPhoneNumber;
    @SerializedName("village_name")
    private String participantVillageName;
    @SerializedName("shg_associate")
    private String participantSHGAssociation;
    @SerializedName("user_name")
    private String participantUserName;
    @SerializedName("password")
    private String participantPassword;
    @SerializedName("doctype")
    private String doctype;

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

    public String getParticipantPassword() {
        return participantPassword;
    }

    public void setParticipantPassword(String participantPassword) {
        this.participantPassword = participantPassword;
    }

    public String getDoctype() {
        return doctype;
    }

    public void setDoctype(String doctype) {
        this.doctype = doctype;
    }
}
