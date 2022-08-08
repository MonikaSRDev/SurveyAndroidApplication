package com.example.mithraapplication.ModelClasses;

public class OptionsRequest extends JsonServerObject{
    private String filter_data = "null";

    public String getFilter_data() {
        return filter_data;
    }

    public void setFilter_data(String filter_data) {
        this.filter_data = filter_data;
    }
}
