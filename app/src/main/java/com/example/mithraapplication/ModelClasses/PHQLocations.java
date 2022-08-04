package com.example.mithraapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PHQLocations  extends JsonServerObject implements Serializable {
    @SerializedName("coordinator")
    private String coordinatorName = "null";
    private String eligible = "null";
    @SerializedName("shg")
    private String SHGName = "null";
    @SerializedName("village")
    private String VillageName = "null";
    @SerializedName("panchayat")
    private String PanchayatName = "null";
    private String name = "null";
    private String active = "null";

    public PHQLocations(String SHGName, String villageName, String panchayatName) {
        this.SHGName = SHGName;
        VillageName = villageName;
        PanchayatName = panchayatName;
    }

    public PHQLocations() {
    }

    public String getCoordinatorName() {
        return coordinatorName;
    }

    public void setCoordinatorName(String coordinatorName) {
        this.coordinatorName = coordinatorName;
    }

    public String getEligible() {
        return eligible;
    }

    public void setEligible(String eligible) {
        this.eligible = eligible;
    }

    public String getSHGName() {
        return SHGName;
    }

    public void setSHGName(String SHGName) {
        this.SHGName = SHGName;
    }

    public String getVillageName() {
        return VillageName;
    }

    public void setVillageName(String villageName) {
        VillageName = villageName;
    }

    public String getPanchayatName() {
        return PanchayatName;
    }

    public void setPanchayatName(String panchayatName) {
        PanchayatName = panchayatName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
