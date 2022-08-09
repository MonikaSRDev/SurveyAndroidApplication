package com.example.mithraapplication.MithraAppServerEventsListeners;

public interface RegistrationServerEvents {

    void getAllPHQParticipants(String message);

    void loginForParticipant(String message);

    void registerParticipant(String message);

    void createTrackingStatus(String message);

    void individualParticipantDetails(String message);

    void updateParticipantDetails(String message);

    void updateParticipantPassword(String message);

    void updateRegisterStatus(String message);
}
