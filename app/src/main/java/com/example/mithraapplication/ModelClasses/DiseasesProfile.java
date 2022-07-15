package com.example.mithraapplication.ModelClasses;

import java.io.Serializable;

public class DiseasesProfile extends JsonServerObject {
    private String diseaseName = "null";
    private String diseaseNameKannada = "null";
    private String specifyDisease = "null";
    private String diagnosedAge = "null";
    private String Diagnosed = "null";
    private String receivedTreatment = "null";
    private String limitActivities = "null";

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getDiseaseNameKannada() {
        return diseaseNameKannada;
    }

    public void setDiseaseNameKannada(String diseaseNameKannada) {
        this.diseaseNameKannada = diseaseNameKannada;
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

    public String getSpecifyDisease() {
        return specifyDisease;
    }

    public void setSpecifyDisease(String specifyDisease) {
        this.specifyDisease = specifyDisease;
    }
}
