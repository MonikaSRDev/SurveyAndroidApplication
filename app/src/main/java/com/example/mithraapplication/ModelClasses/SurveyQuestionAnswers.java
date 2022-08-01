package com.example.mithraapplication.ModelClasses;

public class SurveyQuestionAnswers extends JsonServerObject{
    private String e_Question = "null";
    private String inputType = "null";
    private String section = "null";
    private String sectionHeading = "null";
    private String questionNumber = "null";

    public String getE_Question() {
        return e_Question;
    }

    public void setE_Question(String e_Question) {
        this.e_Question = e_Question;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getSectionHeading() {
        return sectionHeading;
    }

    public void setSectionHeading(String sectionHeading) {
        this.sectionHeading = sectionHeading;
    }

    public String getQuestionNumber() {
        return questionNumber;
    }

    public void setQuestionNumber(String questionNumber) {
        this.questionNumber = questionNumber;
    }
}
