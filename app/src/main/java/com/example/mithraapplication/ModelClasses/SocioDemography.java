package com.example.mithraapplication.ModelClasses;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

public class SocioDemography extends JsonServerObject{
    @SerializedName("user_name")
    private String userName;
    @SerializedName("years_of_education")
    private int yearsOfEducation;
    @SerializedName("marital_status")
    private String maritalStatus;
    @SerializedName("religion")
    private String religion;
    @SerializedName("caste")
    private String caste;
    @SerializedName("type_of_family")
    private String typeOfFamily;
    @SerializedName("number_of_family_members")
    private int numFamilyMembers;
    @SerializedName("occupation")
    private String occupation;
    @SerializedName("total_family_income_per_month")
    private int familyIncome;
    @SerializedName("total_earning_members_in_the_family")
    private int numEarningFamMembers;
    @SerializedName("nearest_phc")
    private String nearestPHC;
    @SerializedName("duration_of_association_with_cboshg")
    private String associationDuration;
    @SerializedName("cbo_meetings_attended_in_last_months")
    private String CBOMeetings;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getYearsOfEducation() {
        return yearsOfEducation;
    }

    public void setYearsOfEducation(int yearsOfEducation) {
        this.yearsOfEducation = yearsOfEducation;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getCaste() {
        return caste;
    }

    public void setCaste(String caste) {
        this.caste = caste;
    }

    public String getTypeOfFamily() {
        return typeOfFamily;
    }

    public void setTypeOfFamily(String typeOfFamily) {
        this.typeOfFamily = typeOfFamily;
    }

    public int getNumFamilyMembers() {
        return numFamilyMembers;
    }

    public void setNumFamilyMembers(int numFamilyMembers) {
        this.numFamilyMembers = numFamilyMembers;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public int getFamilyIncome() {
        return familyIncome;
    }

    public void setFamilyIncome(int familyIncome) {
        this.familyIncome = familyIncome;
    }

    public int getNumEarningFamMembers() {
        return numEarningFamMembers;
    }

    public void setNumEarningFamMembers(int numEarningFamMembers) {
        this.numEarningFamMembers = numEarningFamMembers;
    }

    public String getNearestPHC() {
        return nearestPHC;
    }

    public void setNearestPHC(String nearestPHC) {
        this.nearestPHC = nearestPHC;
    }

    public String getAssociationDuration() {
        return associationDuration;
    }

    public void setAssociationDuration(String associationDuration) {
        this.associationDuration = associationDuration;
    }

    public String getCBOMeetings() {
        return CBOMeetings;
    }

    public void setCBOMeetings(String CBOMeetings) {
        this.CBOMeetings = CBOMeetings;
    }

    public JsonServerObject FromJson(String jsonString, String methodName){

        Gson gson = new Gson();
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject jsonObject1 = jsonObject.getAsJsonObject(methodName);
        return gson.fromJson(jsonObject1.toString(), SocioDemography.class);
    }
}
