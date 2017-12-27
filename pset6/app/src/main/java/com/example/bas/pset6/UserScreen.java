package com.example.bas.pset6;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Converts the user information to the UI
 */
public class UserScreen extends AppCompatActivity {
    private ArrayList<MusicClass> items = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        Bundle intentextra = getIntent().getExtras();
        String username = intentextra.getString("username");
        String id = intentextra.getString("id");

        setViews(username, id);
        getUserData(id);
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

    /**
     * Gets user data from Firebase
     */
    public void getUserData(String id) {
        FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
        DatabaseReference dbref = fbdb.getReference("User/user/" + id);

        // Get the information from FireBase with a listener
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                // Fetch the favorites and put them in a HashMap
                DataSnapshot favs = dataSnapshot.child("favorites");

                for (DataSnapshot shot: favs.getChildren()){
                    MusicClass newTrack = shot.getValue(MusicClass.class);
                    items.add(newTrack);
                }
                makeListView(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database error", databaseError.toString());
            }
        });
    }

    /**
     * Sets the textviews with the user's username and ID.
     */
    public void setViews(String username, String id) {
        TextView userName = findViewById(R.id.nameView);
        TextView userId = findViewById(R.id.idView);
        userName.setText(username);
        userId.setText(id);
    }

    /**
     * Creates a listview from the arraylist.
     */
    public void makeListView(ArrayList<MusicClass> item) {
        MyAdapter adapter;
        ListView listView = findViewById(R.id.favoritesView);
        adapter = new MyAdapter(this, item);
        listView.setAdapter(adapter);
    }
}