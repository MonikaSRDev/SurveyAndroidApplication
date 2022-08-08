package com.example.mithraapplication.ModelClasses;

public class QuestionOptions extends JsonServerObject{
   private String id = "null";
   private String sur_pri_id = "null";
   private String question_id = "null";
   private String option_no = "null";
   private String option_e = "null";
   private String option_k = "null";
   private String option_w = "null";

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSur_pri_id() {
        return sur_pri_id;
    }

    public void setSur_pri_id(String sur_pri_id) {
        this.sur_pri_id = sur_pri_id;
    }

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getOption_no() {
        return option_no;
    }

    public void setOption_no(String option_no) {
        this.option_no = option_no;
    }

    public String getOption_e() {
        return option_e;
    }

    public void setOption_e(String option_e) {
        this.option_e = option_e;
    }

    public String getOption_k() {
        return option_k;
    }

    public void setOption_k(String option_k) {
        this.option_k = option_k;
    }

    public String getOption_w() {
        return option_w;
    }

    public void setOption_w(String option_w) {
        this.option_w = option_w;
    }
}
