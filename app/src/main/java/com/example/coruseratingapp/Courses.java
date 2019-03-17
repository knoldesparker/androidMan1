package com.example.coruseratingapp;

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
