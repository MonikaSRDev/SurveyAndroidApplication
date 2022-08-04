package com.example.mithraapplication.ModelClasses;

public class UpdateScreeningStatus extends JsonServerObject{
    String screening_id = "null";

    public String getScreening_id() {
        return screening_id;
    }

    public void setScreening_id(String screening_id) {
        this.screening_id = screening_id;
    }
}
