package com.example.mithraapplication.ModelClasses;

public class UpdateRegisterStatus extends JsonServerObject{
    private String register = "no";

    public String getRegister() {
        return register;
    }

    public void setRegister(String register) {
        this.register = register;
    }
}
