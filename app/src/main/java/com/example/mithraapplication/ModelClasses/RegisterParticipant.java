package com.example.mithraapplication.ModelClasses;

public class RegisterParticipant {
    private String ParticipantName;
    private int ParticipantGender;
    private int ParticipantAge;
    private PhoneNumber ParticipantPhoneNumber;
    private String ParticipantVillageName;
    private String ParticipantSHGAssociation;
    private int AutoGenerateCred;

    public String getParticipantName() {
        return ParticipantName;
    }

    public void setParticipantName(String participantName) {
        ParticipantName = participantName;
    }

    public int getParticipantGender() {
        return ParticipantGender;
    }

    public void setParticipantGender(int participantGender) {
        ParticipantGender = participantGender;
    }

    public int getParticipantAge() {
        return ParticipantAge;
    }

    public void setParticipantAge(int participantAge) {
        ParticipantAge = participantAge;
    }

    public PhoneNumber getParticipantPhoneNumber() {
        return ParticipantPhoneNumber;
    }

    public void setParticipantPhoneNumber(PhoneNumber participantPhoneNumber) {
        ParticipantPhoneNumber = participantPhoneNumber;
    }

    public String getParticipantVillageName() {
        return ParticipantVillageName;
    }

    public void setParticipantVillageName(String participantVillageName) {
        ParticipantVillageName = participantVillageName;
    }

    public String getParticipantSHGAssociation() {
        return ParticipantSHGAssociation;
    }

    public void setParticipantSHGAssociation(String participantSHGAssociation) {
        ParticipantSHGAssociation = participantSHGAssociation;
    }

    public int getAutoGenerateCred() {
        return AutoGenerateCred;
    }

    public void setAutoGenerateCred(int autoGenerateCred) {
        AutoGenerateCred = autoGenerateCred;
    }
}
