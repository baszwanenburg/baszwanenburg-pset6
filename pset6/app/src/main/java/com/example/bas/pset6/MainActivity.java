package com.example.bas.pset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Main screen, allows the user to go to the Login screen (if not logged in already) or browse the database.
 */
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button login = findViewById(R.id.loginButton);
        login.setOnClickListener(new Click());

        Button database = findViewById(R.id.databaseButton);
        database.setOnClickListener(new Click());
    }

    /**
     * Lets the user choose to login or look through the playlist immediately.
     */
    private class Click implements View.OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.databaseButton:
                    goToMyDatabase();
                    break;
                case R.id.loginButton:
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null) {
                        goToLoginScreen();
                    }
                    else {
                        Toast.makeText(getApplicationContext(), "You are currently already logged in.", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }

    public void goToMyDatabase() {
        Intent intent = new Intent(this, FragmentActivity.class);
        startActivity(intent);
    }

    public void goToLoginScreen(){
        Intent intent = new Intent(this, LoginScreen.class);
        startActivity(intent);
    }
}