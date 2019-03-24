package com.example.coruseratingapp;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


public class MainActivity extends AppCompatActivity  {

    //Firebase Auth
    private FirebaseAuth mAuth;
    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;

    //Tag for debugging
    private static String TAG ="MainActivity";
    //Get instance of firestore
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    //creates a referance to the collection in Firestore
    private CollectionReference courseListRef = db.collection("courses");

    // Instansiates the CourseAdapter as adapter
    private CourseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpRecyclerView();
        setupFirebaseAuth();
    }

    //sends a query to the Firestore based of the courseListRef, and order it by courseName
    private void setUpRecyclerView() {
        Query query = courseListRef.orderBy("courseName", Query.Direction.ASCENDING);
        final FirestoreRecyclerOptions<Courses> options = new FirestoreRecyclerOptions.Builder<Courses>()
                .setQuery(query, Courses.class)
                .build();

        adapter = new CourseAdapter(options);

        //binding to the recycler view
        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        //Sets click listener on the recyclerView.
        //gets the information on each element on the list
        //fills out the information to the intent and sends it to the CourseRatingActivity.
        adapter.setOnItemClickListener(new CourseAdapter.onItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {


                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                String blob = adapter.getItem(position).courseName;


                Toast.makeText(MainActivity.this,getString(R.string.toastSelectPosition) + position + getString(R.string.toastSelectID) + id, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, CourseRatingActivity.class);

                intent.putExtra("courseID", id);
                intent.putExtra("coursePath", path);
                intent.putExtra("courseName", blob);
                startActivity(intent);
            }
        });
    }

    //Creates the menu up top to logout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);

        return true;
    }

    //setup a case for clicking the sign out field and calls the sign out method
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.optionSignOut:
                signOut();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //signOut method
    //Uses the firebase Auth signOut function
    private void signOut(){
        Log.d(TAG, "signOut: signing out");
        FirebaseAuth.getInstance().signOut();
    }

    //Checks the state of the user that is sign in.
    //ether the user is active or is signing out
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        };
    }

    //Runs when activity loads, and actions happens
    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Called ");
        adapter.startListening();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);

    }

    //runs when activity stops.
    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Called");
        adapter.stopListening();
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }

}
