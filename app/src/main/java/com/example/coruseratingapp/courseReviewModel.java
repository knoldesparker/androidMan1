package com.example.coruseratingapp;

public class courseReviewModel {
    //TODO: create a starscore for each star
    float starScore;
    String finalNote;
    public courseReviewModel() {
    }

    public courseReviewModel(float starScore, String finalNote) {
        this.starScore = starScore;
        this.finalNote = finalNote;
    }

    public float getStarScore() {
        return starScore;
    }

    public void setStarScore(float starScore) {
        this.starScore = starScore;
    }

    public String getFinalNote() {
        return finalNote;
    }

    public void setFinalNote(String finalNote) {
        this.finalNote = finalNote;
    }
}
