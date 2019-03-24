package com.example.coruseratingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RatingBar.OnRatingBarChangeListener;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class CourseRatingActivity extends AppCompatActivity {
    private TextView textViewQ1;
    private TextView textViewQ2;
    private TextView textViewQ3;
    private TextView Title;
    private EditText editTextfinalNote;
    private RatingBar ratingBarQ1, ratingBarQ2, ratingBarQ3;
    DocumentSnapshot documentSnapshot;
    private String test;
    private String idForCourse;
    private String pathForCourse;
    private String nameForCourse;
    private String grade;
    private Button emButton;
    boolean rb1Flag = false;
    boolean rb2Flag = false;

    DecimalFormat df = new DecimalFormat("#.##");

    private static final String TAG = "CourseRatingActivity";

    //Firebase Auth
    private FirebaseAuth mAuth;
    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_rating);
        Title = findViewById(R.id.courseName);
        textViewQ1 = findViewById(R.id.tvQ1);
        textViewQ2 = findViewById(R.id.tvQ2);
        textViewQ3 = findViewById(R.id.tvQ3);
        ratingBarQ1 = findViewById(R.id.ratingQ1);
        ratingBarQ2 = findViewById(R.id.ratingQ2);
        ratingBarQ3 = findViewById(R.id.ratingQ3);
        editTextfinalNote = findViewById(R.id.etfinalNote);
        emButton = findViewById(R.id.email_button);
        getIncomingIntent();
        setValuesOnScreen();
        setupFirebaseAuth();
        emButton.setOnClickListener(onClickListenerMail);
        addListenerOnRatingBar();
    }

    private void addListenerOnRatingBar() {
        ratingBarQ1.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d(TAG, "onRatingChanged: swapping value on r1 " + rating);

                if (rating > 0.0){
                    Log.d(TAG, "onRatingChanged: Flag for rb1 is true ");
                    rb1Flag = true;
                } else {
                    Log.d(TAG, "onRatingChanged: Flag for rb1 is false");
                    rb1Flag = false;
                }
            }
        });
        ratingBarQ2.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Log.d(TAG, "onRatingChanged: swapping value on r2 " + rating);

                if (rating > 0.0){
                    Log.d(TAG, "onRatingChanged: Flag for rb2 is true ");
                    rb2Flag = true;
                } else {
                    Log.d(TAG, "onRatingChanged: Flag for rb2 is false");
                    rb2Flag = false;
                }

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference courseQuestionsRef = db.collection("courseQuestions");
        courseQuestionsRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot queryDocumentSnapshots, FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                String quest1 = "";
                String quest2 = "";
                String quest3 = "";
                String documentId = "";

                for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    courseQuestionModel courseQ = documentSnapshot.toObject(courseQuestionModel.class);
                    courseQ.setDocumentID(documentSnapshot.getId());

                    documentId = courseQ.getDocumentID();
                    quest1= courseQ.getQuestion1();
                    quest2= courseQ.getQuestion2();
                    quest3= courseQ.getQuestion3();
                    Log.d(TAG, "onEvent: Getting data" + documentId + quest1 + " " + quest2 + " " + quest3);
                }
                Log.d(TAG, "onEvent: " + documentId);
                textViewQ1.setText(quest1);
                textViewQ2.setText(quest2);
                textViewQ3.setText(quest3);

                FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
                Log.d(TAG, "onEvent: ");

            }
        });
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
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference courseQuestionsRef = db.collection("courseQuestions");

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

    private void courseRating(){

        float ratingBarQ1Rating = ratingBarQ1.getRating();
        float ratingBarQ2Rating = ratingBarQ2.getRating();
        float ratingBarQ3Rating = ratingBarQ3.getRating();
        float avageScore;
        int avageIntScore;

        avageScore = (ratingBarQ1Rating+ratingBarQ2Rating+ratingBarQ3Rating)/3;
        avageIntScore = Math.round(avageScore);
        Log.d(TAG, "courseRating: GRADE: "+ avageIntScore);

        if (avageIntScore >= 4.5){
            grade = "A+";
            Log.d(TAG, "courseRating: avageIntScore >= 4.5 " + grade);

        }
        else if (avageIntScore < 4.4 && avageIntScore > 4){
            grade = "A";
            Log.d(TAG, "courseRating: avageIntScore <= 4.4 && avageIntScore > 4 " + grade);

        }
        else if (avageIntScore <= 4 && avageIntScore > 3.5){
            grade = "B";
            Log.d(TAG, "courseRating: avageIntScore <= 4 && avageIntScore > 3.5 " + grade);
        }
        else if (avageIntScore <= 3.5 && avageIntScore > 2){
            grade = "C";
        } 
        else if (avageIntScore <= 2 && avageIntScore > 1.5) {
            grade = "D";
            Log.d(TAG, "courseRating: avageIntScore <= 2 && avageIntScore > 1.5");
        }
        else if (avageIntScore <= 1.5 && avageIntScore > 1){
            grade = "E";
            Log.d(TAG, "courseRating: avageIntScore <= 1.5 && avageIntScore > 1");
        }
        else if (avageIntScore <= 1 && avageIntScore >= 0.0){
            grade = "F";
            Log.d(TAG, "courseRating: avageIntScore <= 1 && avageIntScore >= 0.0");
        }
    }



    private void saveRating() {

        //if (rb1Flag & rb2Flag) {


            courseRating();
            String a1String = editTextfinalNote.getText().toString();
            float ratingBarQ1Rating = ratingBarQ1.getRating();
            float ratingBarQ2Rating = ratingBarQ2.getRating();
            float ratingBarQ3Rating = ratingBarQ3.getRating();

            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

            if (a1String.trim().isEmpty()) {
                Toast.makeText(this, "Please write a message", Toast.LENGTH_SHORT).show();
                return;
            }


            Log.d(TAG, "saveRating: avageScore: " + grade);
            DocumentReference courseRef = FirebaseFirestore.getInstance()
                    .collection(pathForCourse + "/courseReview").document(userId);
            courseRef.set(new courseReviewModel(ratingBarQ1Rating, ratingBarQ2Rating, ratingBarQ3Rating, a1String, grade));
            Toast.makeText(this, "Review added", Toast.LENGTH_SHORT).show();

            finish();
        }
       // else {
         //   Toast.makeText(this, "Please give a rating", Toast.LENGTH_SHORT).show();

        //}
    //}





    private View.OnClickListener onClickListenerMail = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            courseRating();
            String a1String = editTextfinalNote.getText().toString();
            float ratingBarQ1Rating = ratingBarQ1.getRating();
            float ratingBarQ2Rating = ratingBarQ2.getRating();
            float ratingBarQ3Rating = ratingBarQ3.getRating();


            if (a1String.trim().isEmpty()) {
                Log.d(TAG, "onClick: empty sting");
                Toast.makeText(getApplicationContext(), "please insert a message", Toast.LENGTH_SHORT).show();


            } else {
                //Gets the id for the logged in user
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                //Sets the text for the mail
                String textMessage = "Hello there my fellow app \n" +
                textViewQ1.getText() + " : " + Float.toString(ratingBarQ1Rating) + "\n" +
                textViewQ2.getText() + " : " + Float.toString(ratingBarQ2Rating) + "\n" +
                textViewQ3.getText() + " : " + Float.toString(ratingBarQ3Rating) + "\n" +
                "Final Note :" + a1String;

                //Sets the subject field for the mail
                String mailSubject = "Course Rating for " + nameForCourse + " by student " + userId;
                //sets the reciviant mail for the mail. Has to be a array of emails.
                String mailAdress[] = {"courserating@kea.dk"};

                //Creates an intent
                Intent sendIntent = new Intent();
                //Setes the Action to a SEND. ACTION_SEND represent an action where you send data to another app
                sendIntent.setAction(Intent.ACTION_SEND);
                //Sets the fields of the mail with reciver,subject and text
                sendIntent.putExtra(Intent.EXTRA_EMAIL, mailAdress);
                sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, mailSubject);
                //Sets the type of the intent to text
                sendIntent.setType("text/plain");


                // Always use string resources for UI text.
                // This says something like "Share this photo with"
                String title = getResources().getString(R.string.fui_continue);
                // Create intent to show the chooser dialog
                Intent chooser = Intent.createChooser(sendIntent, title);

                // Verify the original intent will resolve to at least one activity
                if (sendIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(chooser);
                }
            }
        }
    };







    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                }
            }
        };
    }

}
