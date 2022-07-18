package com.example.mithraapplication.ModelClasses;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ParticipantAnswers implements Serializable {
    @SerializedName("qn_id")
    private String question_id = "null";
    @SerializedName("qn_no")
    private String question_no = "null";
    @SerializedName("question")
    private String question = "null";
    @SerializedName("ans")
    private String selected_answer = "null";
    @SerializedName("w")
    private String selected_answer_weightage = "null";
    @SerializedName("qn_start")
    private String question_start_time = "null";
    @SerializedName("qn_stop")
    private String question_stop_time = "null";
    @SerializedName("seconds")
    private String seconds_taken = "null";

    public String getQuestion_id() {
        return question_id;
    }

    public void setQuestion_id(String question_id) {
        this.question_id = question_id;
    }

    public String getQuestion_no() {
        return question_no;
    }

    public void setQuestion_no(String question_no) {
        this.question_no = question_no;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getSelected_answer() {
        return selected_answer;
    }

    public void setSelected_answer(String selected_answer) {
        this.selected_answer = selected_answer;
    }

    public String getSelected_answer_weightage() {
        return selected_answer_weightage;
    }

    public void setSelected_answer_weightage(String selected_answer_weightage) {
        this.selected_answer_weightage = selected_answer_weightage;
    }

    public String getQuestion_start_time() {
        return question_start_time;
    }

    public void setQuestion_start_time(String question_start_time) {
        this.question_start_time = question_start_time;
    }

    public String getQuestion_stop_time() {
        return question_stop_time;
    }

    public void setQuestion_stop_time(String question_stop_time) {
        this.question_stop_time = question_stop_time;
    }

    public String getSeconds_taken() {
        return seconds_taken;
    }

    public void setSeconds_taken(String seconds_taken) {
        this.seconds_taken = seconds_taken;
    }
}
