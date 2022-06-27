package com.example.mithraapplication.ModelClasses;

import java.util.List;

public class DiseasesProfilePostRequest extends  JsonServerObject{
    private String user_name = "NULL";
    private String diabetes_mellitus;
    private String hypertension;
    private String heart_disease;
    private String thyroid;
    private String chronic_liver_disease;
    private String chronic_renal_disease;
    private String malignancy;
    private String disabilities;
    private String gastric_problem;
    private String mental_illness;
    private String epilepsy;
    private String asthma;
    private String skin_disease;
    private String other_diseases;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getDiabetes_mellitus() {
        return diabetes_mellitus;
    }

    public void setDiabetes_mellitus(String diabetes_mellitus) {
        this.diabetes_mellitus = diabetes_mellitus;
    }

    public String getHypertension() {
        return hypertension;
    }

    public void setHypertension(String hypertension) {
        this.hypertension = hypertension;
    }

    public String getHeart_disease() {
        return heart_disease;
    }

    public void setHeart_disease(String heart_disease) {
        this.heart_disease = heart_disease;
    }

    public String getThyroid() {
        return thyroid;
    }

    public void setThyroid(String thyroid) {
        this.thyroid = thyroid;
    }

    public String getChronic_liver_disease() {
        return chronic_liver_disease;
    }

    public void setChronic_liver_disease(String chronic_liver_disease) {
        this.chronic_liver_disease = chronic_liver_disease;
    }

    public String getChronic_renal_disease() {
        return chronic_renal_disease;
    }

    public void setChronic_renal_disease(String chronic_renal_disease) {
        this.chronic_renal_disease = chronic_renal_disease;
    }

    public String getMalignancy() {
        return malignancy;
    }

    public void setMalignancy(String malignancy) {
        this.malignancy = malignancy;
    }

    public String getDisabilities() {
        return disabilities;
    }

    public void setDisabilities(String disabilities) {
        this.disabilities = disabilities;
    }

    public String getGastric_problem() {
        return gastric_problem;
    }

    public void setGastric_problem(String gastric_problem) {
        this.gastric_problem = gastric_problem;
    }

    public String getMental_illness() {
        return mental_illness;
    }

    public void setMental_illness(String mental_illness) {
        this.mental_illness = mental_illness;
    }

    public String getEpilepsy() {
        return epilepsy;
    }

    public void setEpilepsy(String epilepsy) {
        this.epilepsy = epilepsy;
    }

    public String getAsthma() {
        return asthma;
    }

    public void setAsthma(String asthma) {
        this.asthma = asthma;
    }

    public String getSkin_disease() {
        return skin_disease;
    }

    public void setSkin_disease(String skin_disease) {
        this.skin_disease = skin_disease;
    }

    public String getOther_diseases() {
        return other_diseases;
    }

    public void setOther_diseases(String other_diseases) {
        this.other_diseases = other_diseases;
    }
}
