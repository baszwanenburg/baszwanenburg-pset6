package com.example.bas.pset6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CurrentUserFragment extends Fragment {
    private FirebaseUser user;
    private ArrayList<MusicClass> items = new ArrayList<>();
    private String userid;

    // Required empty public constructor
    public CurrentUserFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_user, container, false);

        user = FirebaseAuth.getInstance().getCurrentUser();
        updateUI(rootView);
        //makeListView(items);

        return rootView;
    }

    /**
     * Updates the database UI on whether a user is logged in or not
     */
    public void updateUI(View rootView) {
        if (user == null) {
            TextView loginText = rootView.findViewById(R.id.loginText);
            Button loginButton = rootView.findViewById(R.id.loginButton);
            loginText.setVisibility(View.VISIBLE);
            loginButton.setVisibility(View.VISIBLE);
            loginButton.setOnClickListener(new click());

        } else {
            Button logoutButton = rootView.findViewById(R.id.logoutButton);
            logoutButton.setVisibility(View.VISIBLE);
            logoutButton.setOnClickListener(new click());

            userid = user.getUid();
            getUserData();
        }
    }

    /**
     * Either navigates the user to the login screen, or will sign out the user.
     */
    private class click implements View.OnClickListener {
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.loginButton:
                    Context context = getActivity();

                    Intent intentlogin = new Intent(context, LoginScreen.class);
                    startActivity(intentlogin);
                    break;

                case R.id.logoutButton:
                    Context context2 = getActivity();

                    FirebaseAuth.getInstance().signOut();
                    Intent intent = new Intent(context2, LoginScreen.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    /**
     * Gets user data from Firebase
     */
    public void getUserData() {
        FirebaseDatabase fbdb = FirebaseDatabase.getInstance();
        DatabaseReference dbref = fbdb.getReference("User/user/" + userid);

        // Get the information from FireBase with a listener
        dbref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange (DataSnapshot dataSnapshot){
                // Fetch the username and email
                String username = dataSnapshot.child("username").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();

                // Fetch the favorites and put them in a HashMap
                DataSnapshot favs = dataSnapshot.child("favorites");
                HashMap<String, MusicClass> favorites = new HashMap<>();

                for (DataSnapshot shot: favs.getChildren()){
                    MusicClass newTrack = shot.getValue(MusicClass.class);
                    items.add(newTrack);
                }

                // Use the user class to set the appropriate text views
                UserClass user = new UserClass(userid, username, favorites, email);
                setUserViews(user);
                makeListView(items);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("Database error", databaseError.toString());
            }
        });
    }

    /**
     * Sets an instance of the User Class to fill in the appropriate textviews with the user's info.
     */
    public void setUserViews(UserClass user){
        String username = user.username;
        TextView usernameLogged = getView().findViewById(R.id.nameCurrent);
        usernameLogged.setText(username);

        String email = user.email;
        TextView useremailLogged = getView().findViewById(R.id.emailCurrent);
        useremailLogged.setText(email);

        TextView useridLogged = getView().findViewById(R.id.idCurrent);
        useridLogged.setText(userid);
    }

    /**
     * Creates a listview from the arraylist
     */
    public void makeListView(ArrayList<MusicClass> item) {
        Context context = getActivity();
        MyAdapter adapter;
        ListView listView = getView().findViewById(R.id.favoritesCurrent);
        adapter = new MyAdapter(context, item);
        listView.setAdapter(adapter);
    }
}