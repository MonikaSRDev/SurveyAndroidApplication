package com.example.mithraapplication.ModelClasses;

public class PHQLocations  extends JsonServerObject{
    private String SHGName = null;
    private String VillageName = null;
    private String PanchayatName = null;

    public PHQLocations(String SHGName, String villageName, String panchayatName) {
        this.SHGName = SHGName;
        VillageName = villageName;
        PanchayatName = panchayatName;
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
}
