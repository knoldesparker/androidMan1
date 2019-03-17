package com.example.coruseratingapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CourseRatingActivity extends AppCompatActivity {
    private TextView textViewQ1;
    private TextView Title;
    private EditText editTextfinalNote;
    private RatingBar ratingBarQ1, ratingBarQ2, getRatingBarQ3;
    DocumentSnapshot documentSnapshot;
    private String test;
    private String idForCourse;
    private String pathForCourse;
    private String nameForCourse;

    private static final String TAG = "CourseRatingActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_rating);
        Title = findViewById(R.id.courseName);
        textViewQ1 = findViewById(R.id.tvQ1);
        ratingBarQ1 = findViewById(R.id.ratingQ1);
        ratingBarQ2 = findViewById(R.id.ratingQ2);
        getRatingBarQ3 = findViewById(R.id.ratingQ3);
        editTextfinalNote = findViewById(R.id.etfinalNote);
        getIncomingIntent();
        setValuesOnScreen();
    }


    private void getIncomingIntent(){

        Log.d(TAG, "getIncomingIntent: checking for incoming intents.");
        String courseID;
        String coursePath;

        if(getIntent().hasExtra("courseID") && getIntent().hasExtra("coursePath")) {
            Log.d(TAG, "getIncomingIntent: found intent extras.");

            idForCourse = getIntent().getStringExtra("courseID");
            pathForCourse = getIntent().getStringExtra("coursePath");
            nameForCourse = getIntent().getStringExtra("courseName");

            Log.d(TAG, "getIncomingIntent: courseID: " + idForCourse);
            Log.d(TAG, "getIncomingIntent: coursePath: " + pathForCourse);
            Log.d(TAG, "getIncomingIntent: courseName: " + nameForCourse);
        }
    }

    private void setValuesOnScreen(){
        Log.d(TAG, "setValuesOnScreen: setting values on screen");
        CollectionReference courseRef = FirebaseFirestore.getInstance()
                .collection("courseQuestions");



        Title.setText(nameForCourse);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.new_course_rating, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_rating:
                saveRating();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    private void saveRating(){

        String a1String = editTextfinalNote.getText().toString();
        float rating = ratingBarQ1.getRating();



        if (a1String.trim().isEmpty()){
            Toast.makeText(this, "Please write a message", Toast.LENGTH_SHORT).show();
            return;
        }

        CollectionReference courseRef = FirebaseFirestore.getInstance()
                .collection(pathForCourse+ "/courseReview");
            courseRef.add(new courseReviewModel(rating,a1String));
        Toast.makeText(this, "Review added", Toast.LENGTH_SHORT).show();

        finish();
    }

}
