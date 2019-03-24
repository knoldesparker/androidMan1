package com.example.coruseratingapp;

/*
    * This is the Login Activity.
    * This is the acrivity where the user logs in to the app with a email
    * currently there is no sign-up functionality, only sign in.
    * it uses the users made in Firesotre.
 */



//Imports for android widgets and more
import android.app.Dialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

//Imports for Firebase, Firestore, Firestore Authentication
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    //Firebase Auth
    private FirebaseAuth mAuth;
    //Firebase
    private FirebaseAuth.AuthStateListener mAuthListener;
    //Log TAG
    private static final String TAG = "LoginActivity";
    private static final int ERROR_DIALOG_REQUEST = 9001;


    // widgets
    private EditText mEmail, mPassword;
    private Button mSignIn;

    //When the view is created, this code is run.
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();                 //assign mAuth to the FirebaseAuth instance
        mEmail = findViewById(R.id.email);                  //assigns mEmial to the field email
        mPassword = findViewById(R.id.password);            //assign mPassword to the field password
        mSignIn = findViewById(R.id.button_sign_in);        //assign mSignIn to the button button_sign_in

        setupFirebaseAuth();                                //Runs the setupFirebaseAuth method
        if(servicesOK()){
            mSignIn.setOnClickListener(this);
        }
    }


/*
    * onClick method on login activity
    * validates inputs
    * Usages Firebase signIn methods for email and password
    * Utilising The Tasks API
    * To handle success and failure in the same listener, attach an OnCompleteListener
    * onFailier: To be notified when the task fails
 *
 */
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.button_sign_in){
            //check if the fields are filled out
            if(!isEmpty(mEmail.getText().toString())
                    && !isEmpty(mPassword.getText().toString())){
                Log.d(TAG, "onClick: attempting to authenticate.");

                FirebaseAuth.getInstance().signInWithEmailAndPassword(mEmail.getText().toString(),
                        mPassword.getText().toString())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(getCurrentFocus().getRootView(), R.string.signInAuthError, Snackbar.LENGTH_SHORT).show();
                    }
                });
            }else{
                Snackbar.make(getCurrentFocus().getRootView(), R.string.signInError, Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    /*
        *serviceOK checks if there is a connection to google is available
     */
    public boolean servicesOK(){
        Log.d(TAG, "servicesOK: Checking Google Services.");

        int isAvailable = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(LoginActivity.this);

        if(isAvailable == ConnectionResult.SUCCESS){
            //everything is ok and the user can make mapping requests
            Log.d(TAG, "servicesOK: Play Services is OK");
            return true;
        }
        else if(GoogleApiAvailability.getInstance().isUserResolvableError(isAvailable)){
            //an error occured, but it's resolvable
            Log.d(TAG, "servicesOK: an error occured, but it's resolvable.");
            Dialog dialog = GoogleApiAvailability.getInstance().getErrorDialog(LoginActivity.this, isAvailable, ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        else{
            Toast.makeText(this, getString(R.string.serviceError), Toast.LENGTH_SHORT).show();
        }

        return false;
    }


    /**
     * Return true if the @param is null
     * @param string
     * @return
     */
    private boolean isEmpty(String string){
        return string.equals("");
    }

/*
    * setupFirebaseAuth
    * This method is used for checking if the user is logged in or not.
    * FLAG_ACTIVITY_NEW_TASK
    *   - If set, this activity will become the start of a new task on this history stack.
    * FLAG_ACTIVITY_CLEAR_TASK
    *   - If set in an Intent passed, this flag will cause any existing task that would be associated
    *       with the activity to be cleared before the activity is started
    *
    *
 */
    private void setupFirebaseAuth(){
        Log.d(TAG, "setupFirebaseAuth: started.");

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {

                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    Toast.makeText(LoginActivity.this, "Signed in", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();


                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }
/*
    * onStart method is called when the button is pressed
    * adds a state listener for firesotre auth
 */
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: called");
        FirebaseAuth.getInstance().addAuthStateListener(mAuthListener);
    }

    /*
        * onStop method is called when the page stops to load
     */
    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop: called");
        if (mAuthListener != null) {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthListener);
        }
    }


}
