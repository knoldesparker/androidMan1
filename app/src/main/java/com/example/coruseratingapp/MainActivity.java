package com.example.coruseratingapp;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity  {



    private static String TAG ="MainActivity";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference courseListRef = db.collection("courses");
    private CourseAdapter adapter;


   // RecyclerView recyclerView;
    //RecyclerView.Adapter mAdapter;
    //RecyclerView.LayoutManager layoutManager;
    //private FirebaseRecyclerAdapter<Courses, CourseListHolder> firebaseRecyclerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpRecyclerView();


        //RecyclerView recyclerView = findViewById(R.id.recycler_view);
        //recyclerView.setLayoutManager(new LinearLayoutManager(this));

       // DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
       // Query query = rootRef.child("Users");


    }

    private void setUpRecyclerView() {


        Query query = courseListRef.orderBy("courseName", Query.Direction.DESCENDING);
        final FirestoreRecyclerOptions<Courses> options = new FirestoreRecyclerOptions.Builder<Courses>()
                .setQuery(query, Courses.class)
                .build();

        adapter = new CourseAdapter(options);

        final RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new CourseAdapter.onItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                Courses course = documentSnapshot.toObject(Courses.class);

                String id = documentSnapshot.getId();
                String path = documentSnapshot.getReference().getPath();
                String blob = adapter.getItem(position).courseName;


                Toast.makeText(MainActivity.this,"Position" + position + " ID: " + id, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(MainActivity.this, CourseRatingActivity.class);

                String listString = String.join(id);
                // String message = listString;
                intent.putExtra("courseID", id);
                intent.putExtra("coursePath", path);
                intent.putExtra("courseName", blob);
                startActivity(intent);



                //startActivity(new Intent(MainActivity.this,CourseRatingActivity.class));
                //courseListRef.document(documentSnapshot.getId())
                   //     .collection("courseReview").add(course);


            }
        });
    }


/* Old Ways
    private void getCourseList(){
        Query query2 = FirebaseDatabase.getInstance()
                .getReference()
                .child("courses");

        FirebaseRecyclerOptions<Courses> options =
                new FirebaseRecyclerOptions.Builder<Courses>()
                        .setQuery(query2, Courses.class)
                        .build();
        }
*/




    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: Called ");
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: Called");
        adapter.stopListening();

        /*if (firebaseRecyclerAdapter!= null) {
            firebaseRecyclerAdapter.stopListening();
        }
        */
    }

}
