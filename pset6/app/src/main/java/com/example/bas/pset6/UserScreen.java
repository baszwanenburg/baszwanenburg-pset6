package com.example.bas.pset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Converts the user information to the UI
 */
public class UserScreen extends AppCompatActivity {
    public ArrayList<MusicClass> usersFavorites = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_screen);

        Intent intent = getIntent();
        Bundle intentextra = getIntent().getExtras();
        String username = intentextra.getString("username");
        String id = intentextra.getString("id");

        setViews(username, id);

        /**
         * Fetches the user's favorites with the HashMap and makes a listview out of it.
         * If there are no favorites, the null object will display a message.
         */
        if (intent.getSerializableExtra("favorites") != null){
            HashMap<String, MusicClass> favorites = (HashMap<String, MusicClass>) intent.getSerializableExtra("favorites");

            for (MusicClass value: favorites.values()){
                usersFavorites.add(value);
            }
            makeListView(usersFavorites);
        }
    }

    /**
     * Sets the textviews with the user's username and ID.
     */
    public void setViews(String username, String id) {
        TextView usernameholder = findViewById(R.id.nameView);
        TextView useridholder = findViewById(R.id.idView);
        usernameholder.setText(username);
        useridholder.setText(id);
    }

    /**
     * Creates a listview from the arraylist.
     */
    public void makeListView(ArrayList<MusicClass> item) {
        MyAdapter adapter;
        ListView view = findViewById(R.id.favoritesView);
        adapter = new MyAdapter(this, item);
        view.setAdapter(adapter);
    }
}