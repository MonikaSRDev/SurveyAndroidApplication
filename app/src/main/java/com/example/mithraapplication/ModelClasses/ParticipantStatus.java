package com.example.mithraapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

public class ParticipantStatus extends JsonServerObject{
    private String statusName = "NULL";
    private String completed = "NULL";
    @SerializedName("remaining")
    private String pending = "NULL";
    private String total = "NULL";

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public String getPending() {
        return pending;
    }

    public void setPending(String pending) {
        this.pending = pending;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
