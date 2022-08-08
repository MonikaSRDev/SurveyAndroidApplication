package com.example.mithraapplication.ModelClasses;

public class QuestionAnswers {
    private String name = "null";
    private String qn_number = "null";
    private String question_e = "null";
    private String question_k = "null";
    private String type = "null";
    private String active = "null";
    private String audio_filename_e = "que1eng.ogg";
    private String audio_filename_k = "que1kan.ogg";
    private String audio_fileURL = "http://192.168.2.120:5000/downloadfile/Audios";
    private String option_type = "null";
    private String qn_branch = "null";
    private String section = "null";
    private String section_name = "null";

    public String getQn_number() {
        return qn_number;
    }

    public void setQn_number(String qn_number) {
        this.qn_number = qn_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestion_e() {
        return question_e;
    }

    public void setQuestion_e(String question_e) {
        this.question_e = question_e;
    }

    public String getQuestion_k() {
        return question_k;
    }

    public void setQuestion_k(String question_k) {
        this.question_k = question_k;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getAudio_filename_e() {
        return audio_filename_e;
    }

    public void setAudio_filename_e(String audio_filename_e) {
        this.audio_filename_e = audio_filename_e;
    }

    public String getAudio_filename_k() {
        return audio_filename_k;
    }

    public void setAudio_filename_k(String audio_filename_k) {
        this.audio_filename_k = audio_filename_k;
    }

    public String getAudio_fileURL() {
        return audio_fileURL;
    }

    public void setAudio_fileURL(String audio_fileURL) {
        this.audio_fileURL = audio_fileURL;
    }

    public String getOption_type() {
        return option_type;
    }

    public void setOption_type(String option_type) {
        this.option_type = option_type;
    }

    public String getQn_branch() {
        return qn_branch;
    }

    public void setQn_branch(String qn_branch) {
        this.qn_branch = qn_branch;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSection_name() {
        return section_name;
    }

    public void setSection_name(String section_name) {
        this.section_name = section_name;
    }
}
