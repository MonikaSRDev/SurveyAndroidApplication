package com.example.mithraapplication.ModelClasses;

import java.io.Serializable;

public class DiseasesProfile extends JsonServerObject {
    private String DiseaseName = "NULL";
    private String diagnosedAge = "NULL";
    private String Diagnosed = "NULL";
    private String receivedTreatment = "NULL";
    private String limitActivities = "NULL";

    public String getDiseaseName() {
        return DiseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        DiseaseName = diseaseName;
    }

    public String getDiagnosedAge() {
        return diagnosedAge;
    }

    public void setDiagnosedAge(String diagnosedAge) {
        this.diagnosedAge = diagnosedAge;
    }

    public String getDiagnosed() {
        return Diagnosed;
    }

    public void setDiagnosed(String diagnosed) {
        Diagnosed = diagnosed;
    }

    public String getReceivedTreatment() {
        return receivedTreatment;
    }

    public void setReceivedTreatment(String receivedTreatment) {
        this.receivedTreatment = receivedTreatment;
    }

    public String getLimitActivities() {
        return limitActivities;
    }

    public void setLimitActivities(String limitActivities) {
        this.limitActivities = limitActivities;
    }

}
