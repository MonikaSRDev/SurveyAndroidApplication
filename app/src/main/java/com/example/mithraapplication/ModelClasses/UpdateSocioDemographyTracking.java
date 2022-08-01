package com.example.mithraapplication.ModelClasses;

public class UpdateSocioDemographyTracking extends JsonServerObject{
    private String modified_user = "UT-9-2022-07-11-08:19:58-no_one_modified";
    private String socio_demography = null;
    private String enroll = null;

    public String getModified_user() {
        return modified_user;
    }

    public void setModified_user(String modified_user) {
        this.modified_user = modified_user;
    }

    public String getSocio_demography() {
        return socio_demography;
    }

    public void setSocio_demography(String socio_demography) {
        this.socio_demography = socio_demography;
    }

    public String getEnroll() {
        return enroll;
    }

    public void setEnroll(String enroll) {
        this.enroll = enroll;
    }
}
