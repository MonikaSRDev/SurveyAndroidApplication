package com.example.mithraapplication.MithraAppServerEventsListeners;

public interface SocioDemographyServerEvents {

    void postSocioDemography(String message);

    void updateSocioTrackingDetails(String message);

    void individualSocioDemography(String message);

    void updateSocioDemographyDetails(String message);
}
