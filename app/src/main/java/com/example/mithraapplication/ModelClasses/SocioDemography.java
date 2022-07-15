package com.example.mithraapplication.ModelClasses;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;

public class SocioDemography extends JsonServerObject{
    private String user_pri_id = "NULL";
    @SerializedName("years_of_education")
    private String yearsOfEducation = "NULL";
    @SerializedName("marital_status")
    private String maritalStatus = "NULL";
    @SerializedName("religion")
    private String religion = "NULL";
    @SerializedName("caste")
    private String caste = "NULL";
    @SerializedName("type_of_family")
    private String typeOfFamily = "NULL";
    @SerializedName("number_of_adult_family_members")
    private String numFamilyAdultMembers = "NULL";
    @SerializedName("number_of_child_family_members")
    private String numFamilyChildMembers = "NULL";
    @SerializedName("occupation")
    private String occupation = "NULL";
    @SerializedName("total_family_income_per_month")
    private String familyIncome = "NULL";
    @SerializedName("total_earning_members_in_the_family")
    private String numEarningFamMembers = "NULL";
    @SerializedName("nearest_phc")
    private String nearestPHC = "NULL";
    @SerializedName("duration_of_association_with_cboshg")
    private String associationDuration = "NULL";
    @SerializedName("cbo_meetings_attended_in_last_months")
    private String CBOMeetings = "NULL";
    private String active = "null";
    private String created_user = "NULL";
    private String modified_user = "UT-9-2022-07-11-08:19:58-no_one_modified";



    public String getYearsOfEducation() {
        return yearsOfEducation;
    }

    public void setYearsOfEducation(String yearsOfEducation) {
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

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNearestPHC() {
        return nearestPHC;
    }

    public String getNumFamilyAdultMembers() {
        return numFamilyAdultMembers;
    }

    public void setNumFamilyAdultMembers(String numFamilyAdultMembers) {
        this.numFamilyAdultMembers = numFamilyAdultMembers;
    }

    public String getNumFamilyChildMembers() {
        return numFamilyChildMembers;
    }

    public void setNumFamilyChildMembers(String numFamilyChildMembers) {
        this.numFamilyChildMembers = numFamilyChildMembers;
    }

    public String getFamilyIncome() {
        return familyIncome;
    }

    public void setFamilyIncome(String familyIncome) {
        this.familyIncome = familyIncome;
    }

    public String getNumEarningFamMembers() {
        return numEarningFamMembers;
    }

    public void setNumEarningFamMembers(String numEarningFamMembers) {
        this.numEarningFamMembers = numEarningFamMembers;
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

    public String getUser_pri_id() {
        return user_pri_id;
    }

    public void setUser_pri_id(String user_pri_id) {
        this.user_pri_id = user_pri_id;
    }

    public String getCBOMeetings() {
        return CBOMeetings;
    }

    public void setCBOMeetings(String CBOMeetings) {
        this.CBOMeetings = CBOMeetings;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getCreated_user() {
        return created_user;
    }

    public void setCreated_user(String created_user) {
        this.created_user = created_user;
    }

    public String getModified_user() {
        return modified_user;
    }

    public void setModified_user(String modified_user) {
        this.modified_user = modified_user;
    }

    public JsonServerObject FromJson(String jsonString, String methodName){

        Gson gson = new Gson();
        JsonElement jsonElement = JsonParser.parseString(jsonString);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        JsonObject jsonObject1 = jsonObject.getAsJsonObject(methodName);
        return gson.fromJson(jsonObject1.toString(), SocioDemography.class);
    }
}
