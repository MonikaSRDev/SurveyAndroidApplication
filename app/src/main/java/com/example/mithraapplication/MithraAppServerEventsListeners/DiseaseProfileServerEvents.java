package com.example.mithraapplication.MithraAppServerEventsListeners;

public interface DiseaseProfileServerEvents {

    void postDiseasesProfile(String message);

    void updateDiseaseTrackingDetails(String message);

    void individualDiseaseProfileDetails(String message);

    void updateDiseaseProfileDetails(String message);
}
