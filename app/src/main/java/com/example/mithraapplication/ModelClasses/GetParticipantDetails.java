package com.example.mithraapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class GetParticipantDetails extends JsonServerObject{
    @SerializedName("user_id")
    private String user_pri_id = "NULL";

    public String getUser_pri_id() {
        return user_pri_id;
    }

    public void setUser_pri_id(String user_pri_id) {
        this.user_pri_id = user_pri_id;
    }
}
