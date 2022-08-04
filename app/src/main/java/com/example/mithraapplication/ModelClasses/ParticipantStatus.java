package com.example.mithraapplication.ModelClasses;

public class ParticipantStatus extends JsonServerObject{
    private String statusName = "NULL";
    private String enroll_completed = "NULL";
    private String enroll_remaining = "NULL";
    private String enroll_total = "NULL";

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    public String getEnroll_completed() {
        return enroll_completed;
    }

    public void setEnroll_completed(String enroll_completed) {
        this.enroll_completed = enroll_completed;
    }

    public String getEnroll_remaining() {
        return enroll_remaining;
    }

    public void setEnroll_remaining(String enroll_remaining) {
        this.enroll_remaining = enroll_remaining;
    }

    public String getEnroll_total() {
        return enroll_total;
    }

    public void setEnroll_total(String enroll_total) {
        this.enroll_total = enroll_total;
    }
}
