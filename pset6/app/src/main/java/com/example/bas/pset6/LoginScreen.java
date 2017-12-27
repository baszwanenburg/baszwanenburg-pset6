package com.example.bas.pset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final String TAG = "Firebase response:";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        mAuth = FirebaseAuth.getInstance();

        Button login = findViewById(R.id.loginButton);
        Button register = findViewById(R.id.goToRegister);
        login.setOnClickListener(new Click());
        register.setOnClickListener(new Click());

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    // user is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in " + currentUser.getUid());
                } else {
                    // user is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                }
            }
        };
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.homeBar:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
        }
        return(super.onOptionsItemSelected(item));
    }

    private class Click implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loginButton:
                    try {
                        Log.d(TAG, "0");

                        EditText getEmail = findViewById(R.id.GetUserEmail);
                        EditText getPassword = findViewById(R.id.GetUserPassword);

                        String email = getEmail.getText().toString();
                        String password = getPassword.getText().toString();

                        loginUser(email, password);
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Ingevoerde gegevens onjuist", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                    break;
                case R.id.goToRegister:
                    goToRegister();
                    break;
            }
        }
    }

    public void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginScreen.this, "Succesvol ingelogd", Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            Toast.makeText(LoginScreen.this, "Authenticatie mislukt", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        // Send the user to the fragment activity if already logged in
        if (currentUser != null) {
            Toast.makeText(LoginScreen.this, "Currently logged in", Toast.LENGTH_SHORT).show();
            updateUI(currentUser);
        }
    }

    public void goToRegister() {
        Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
        startActivity(intent);
    }

    public void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Toast.makeText(getApplicationContext(), "Succesvol ingelogd", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(LoginScreen.this, FragmentActivity.class);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(getApplicationContext(), "Inloggen mislukt", Toast.LENGTH_SHORT).show();
        }
    }
}
