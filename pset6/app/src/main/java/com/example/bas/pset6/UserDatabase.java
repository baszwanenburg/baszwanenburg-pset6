package com.example.bas.pset6;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Creates a listview of all the usernames (out of firebase) of the users
 */
public class UserDatabase extends Fragment {
    private HashMap<String, String> nameAndID;
    private HashMap<String,String> favorites;
    private ArrayList<UserClass> userDatabase = new ArrayList<UserClass>();
    private String id;
    private String email;

    // Required empty public constructor
    public UserDatabase() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_user_database, container, false);

        nameAndID = new HashMap<>();
        getData();

        return rootView;
    }

    /**
     * Gets the data from the database in firebase
     */
    public void getData() {
        FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
        DatabaseReference dbref = fbdb.getReference("User/user/");

        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Look through Firebase and find the requested information
                for (DataSnapshot child:dataSnapshot.getChildren()){
                    String userid = child.getKey();
                    String username = dataSnapshot.child(userid).child("username").getValue().toString();
                    String email = dataSnapshot.child(userid).child("email").getValue().toString();

                    // Get the user's favorites
                    DataSnapshot favoriteskeyvalue = dataSnapshot.child(userid).child("favorites");
                    HashMap<String, MusicClass> favorites = new HashMap<>();

                    for (DataSnapshot x: favoriteskeyvalue.getChildren()){
                        MusicClass example = x.getValue(MusicClass.class);
                        favorites.put(example.getRank().toString(), example);
                    }

                    // Create new instance of the user class and and it to the database
                    UserClass user = new UserClass(userid, username, favorites, email);
                    userDatabase.add(user);
                    nameAndID.put(username,userid);
                }
                makeListView(nameAndID);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database or connectivity error", databaseError.toString());
            }
        });
    }

    /**
     * Creates listview from arraylist by first getting the needed information from the hasmap
     */
    public void makeListView(HashMap hashMap) {
        ArrayList<String> users = new ArrayList<String>(hashMap.keySet());
        Context context = getActivity();

        // Link the listview and adapter
        ListView listView = getView().findViewById(R.id.listviewUserDatabase);
        UserAdapter adapter;
        adapter = new UserAdapter(context, users);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new clicklistener());
    }

    /**
     * Sends the current user to the appropriate profile page upon clicking an item.
     */
    private class clicklistener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int Int, long idl) {
            // Get the username
            TextView usernameView = view.findViewById(R.id.usernameView);
            String username = usernameView.getText().toString();

            // Find the user matching the clicked username and fetch the appropriate info
            for (int i = 0; i < userDatabase.size(); i++) {
                if (userDatabase.get(i).username.toString().equals(username)){
                    id = userDatabase.get(i).id.toString();
                    email = userDatabase.get(i).email.toString();
                    favorites = userDatabase.get(i).favorites;
                }
            }

            goToDetails(username);
        }
    }

    /**
     * Puts information in intent and navigates the user to the page with details about the user
     */
    public void goToDetails(String username) {
        Context context = getActivity();
        Intent intent = new Intent(context, UserScreen.class);
        intent.putExtra("username", username);
        intent.putExtra("id", id);
        intent.putExtra("email", email);
        intent.putExtra("favorites", favorites);
        startActivity(intent);
    }
}