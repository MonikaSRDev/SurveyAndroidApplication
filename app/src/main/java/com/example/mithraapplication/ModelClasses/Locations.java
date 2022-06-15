package com.example.mithraapplication.ModelClasses;

public class Locations extends JsonServerObject{
    private String name;
    private String panchayat;
    private String village;
    private String shg;

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
}
