package com.example.coruseratingapp;

import android.content.Intent;
import android.support.annotation.NonNull;
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
        getIncomingIntent();
        setValuesOnScreen();
        setupFirebaseAuth();
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

    private void saveRating(){

        String a1String = editTextfinalNote.getText().toString();
        float ratingBarQ1Rating = ratingBarQ1.getRating();
        float ratingBarQ2Rating = ratingBarQ2.getRating();
        float ratingBarQ3Rating = ratingBarQ3.getRating();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();



        if (a1String.trim().isEmpty()){
            Toast.makeText(this, "Please write a message", Toast.LENGTH_SHORT).show();
            return;
        }

        DocumentReference courseRef = FirebaseFirestore.getInstance()
                .collection(pathForCourse+ "/courseReview").document(userId);
            courseRef.set(new courseReviewModel(ratingBarQ1Rating,ratingBarQ2Rating,ratingBarQ3Rating,a1String));
        Toast.makeText(this, "Review added", Toast.LENGTH_SHORT).show();

        finish();
    }


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
