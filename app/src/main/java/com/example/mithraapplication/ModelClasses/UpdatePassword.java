package com.example.mithraapplication.ModelClasses;

public class UpdatePassword extends JsonServerObject{
    private String name = "null";
    private String password = "null";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
