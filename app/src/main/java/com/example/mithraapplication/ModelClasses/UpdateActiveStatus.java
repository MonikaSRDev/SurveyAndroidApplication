package com.example.mithraapplication.ModelClasses;

public class UpdateActiveStatus extends JsonServerObject{
    private String user_pri_id ="null";
    private String active = "null";

    public String getUser_pri_id() {
        return user_pri_id;
    }

    public void setUser_pri_id(String user_pri_id) {
        this.user_pri_id = user_pri_id;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }
}
