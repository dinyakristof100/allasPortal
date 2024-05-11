package hu.mobilalk.allasportal;

import androidx.annotation.NonNull;
import  androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private  static final int SECRET_KEY = 45;
    EditText userNameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordAgainET;


    private static final String LOG_TAG = RegisterActivity.class.toString();
    private  static final  String PREF_KEY = MainActivity.class.getPackage().toString();

    private SharedPreferences preferences;
    private FirebaseAuth myAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        int secret_key = getIntent().getIntExtra("SECRET_KEY",0);

        if(secret_key != 45){
            finish();
        }

        userNameET = findViewById(R.id.userNameEditText);
        emailET = findViewById(R.id.userEmailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        passwordAgainET = findViewById(R.id.passwordAgainEditText);

        preferences = getSharedPreferences(PREF_KEY, MODE_PRIVATE);

        String userName = preferences.getString("userName","");
        String password = preferences.getString("password","");

        userNameET.setText(userName);
        passwordET.setText(password);

        myAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void register(View view) {
        String userName = userNameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordAgain = passwordAgainET.getText().toString();

        if(!password.equals(passwordAgain)){
            Log.e(LOG_TAG,"unmatching passwords!");
            return;
        }else{
            Log.i(LOG_TAG,"Registered: "+userName+", email: "+email);
        }

        myAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(LOG_TAG, "User created succesfully: "+email);
                    startJobs();
                }else{
                    Log.d(LOG_TAG, "Couldn't created user");
                    Toast.makeText(RegisterActivity.this, "Couldn't created user: " +task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void startJobs() {
        Intent intent = new Intent(this, JobsActivity.class);
        //intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
    }

    public void cancel(View view) {
        finish();
    }
}