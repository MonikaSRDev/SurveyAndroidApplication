package com.example.mithraapplication.ModelClasses;

public class DiseasesProfile {
    private String DiseaseName;
    private String diagnosedAge;
    private String Diagnosed;
    private String receivedTreatment;
    private String limitActivities;

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
