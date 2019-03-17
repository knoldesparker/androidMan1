package com.example.coruseratingapp;

public class courseQuestionModel {
    String question1, question2, question3;
    String documentID;

    public courseQuestionModel() {
    }

    public courseQuestionModel(String question1, String question2, String question3, String documentID) {
        this.question1 = question1;
        this.question2 = question2;
        this.question3 = question3;
    }

    public String getQuestion1() {
        return question1;
    }

    public void setQuestion1(String question1) {
        this.question1 = question1;
    }

    public String getQuestion2() {
        return question2;
    }

    public void setQuestion2(String question2) {
        this.question2 = question2;
    }

    public String getQuestion3() {
        return question3;
    }

    public void setQuestion3(String question3) {
        this.question3 = question3;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }
}