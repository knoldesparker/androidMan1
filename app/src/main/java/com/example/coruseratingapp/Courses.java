package com.example.coruseratingapp;

/*
    * This is the model class for the Courses
    * It's used to send and receive data from Firestore
    * MVC and POJO stuff
 */

public class Courses {
    public String courseName, courseDesc;

    public Courses() {
    }

    public Courses(String courseName, String courseDesc) {
        this.courseName = courseName;
        this.courseDesc = courseDesc;
    }

    public String getCourseName() {
        return courseName;
    }

    public String getCourseDesc() {
        return courseDesc;
    }
}
