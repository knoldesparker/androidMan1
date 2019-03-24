package com.example.coruseratingapp;

/*
    * This is the model for courseReview
    * It's used to send and receive data from Firestore
    * MVC and POJO stuff
 */
public class courseReviewModel {
    float starScoreQ1, starScoreQ2, starScoreQ3;
    String finalNote;
    String grade;

    public courseReviewModel() {
    }

    public courseReviewModel(float starScoreQ1, float starScoreQ2, float starScoreQ3, String finalNote, String grade) {
        this.starScoreQ1 = starScoreQ1;
        this.starScoreQ2 = starScoreQ2;
        this.starScoreQ3 = starScoreQ3;
        this.finalNote = finalNote;
        this.grade = grade;
    }

    public float getStarScoreQ1() {
        return starScoreQ1;
    }

    public void setStarScoreQ1(float starScoreQ1) {
        this.starScoreQ1 = starScoreQ1;
    }

    public float getStarScoreQ2() {
        return starScoreQ2;
    }

    public void setStarScoreQ2(float starScoreQ2) {
        this.starScoreQ2 = starScoreQ2;
    }

    public float getStarScoreQ3() {
        return starScoreQ3;
    }

    public void setStarScoreQ3(float starScoreQ3) {
        this.starScoreQ3 = starScoreQ3;
    }

    public String getFinalNote() {
        return finalNote;
    }

    public void setFinalNote(String finalNote) {
        this.finalNote = finalNote;
    }

    public String getGrade() {
        return grade;
    }

    public void setAvageScore(String grade) {
        this.grade = grade;
    }
}
