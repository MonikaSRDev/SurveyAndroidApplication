package com.example.mithraapplication.ModelClasses;

public class Locations extends JsonServerObject{
    private String name;
    private String panchayat;
    private String village;
    private String shg;
    private String active = "null";
    private String created_user = "null";
    private String modified_user = "null";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPanchayat() {
        return panchayat;
    }

    public void setPanchayat(String panchayat) {
        this.panchayat = panchayat;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getShg() {
        return shg;
    }

    public void setShg(String shg) {
        this.shg = shg;
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
}
