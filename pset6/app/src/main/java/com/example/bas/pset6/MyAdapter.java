package com.example.bas.pset6;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This adapter allows for the use of a customized listview,
 * in which the information of all tracks will be displayed.
 */
public class MyAdapter extends ArrayAdapter<MusicClass> {
    private ArrayList<MusicClass> items = new ArrayList<>();
    private FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    private FirebaseDatabase fbdb;
    private DatabaseReference dbref;

    public MyAdapter(Context context, ArrayList<MusicClass> tracks) {
        super(context, 0, tracks);
    }

    /**
     * Parses the information from the array into the custom row layout.
     */
    @Override
    public View getView(int position, View view, ViewGroup group) {
        MusicClass track = getItem(position);
        view = LayoutInflater.from(getContext()).inflate(R.layout.listview_layout, group, false);

        final ImageView img = view.findViewById(R.id.albumView);
        final TextView rankView = view.findViewById(R.id.rankView);
        final ImageButton favButton = view.findViewById(R.id.favView);

        String url = track.getImageURL();
        Picasso.with(getContext()).load(url).into(img);

        TextView RankView = view.findViewById(R.id.rankView);
        RankView.setText(track.getRank());

        TextView TrackView = view.findViewById(R.id.trackView);
        TrackView.setText(track.getTrack());

        TextView ArtistView = view.findViewById(R.id.artistView);
        ArtistView.setText(track.getArtist());

        TextView YearView = view.findViewById(R.id.yearView);
        YearView.setText(track.getYear());

        /**
         * This will add the appropriate instance of the Music Class to the favorites Hash Map
         * of the current user upon clicking the favorite button within the adapter listview.
         */
        favButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    fbdb = FirebaseDatabase.getInstance();
                    String userid = user.getUid();
                    dbref = fbdb.getReference("User/user/" + userid);

                    dbref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            // Get the HashHamp with the already added favorites
                            DataSnapshot value = dataSnapshot.child("favorites");
                            HashMap<String, MusicClass> favorites = (HashMap<String, MusicClass>) value.getValue();

                            // If there are no favorites yet, create a new empty HashMap to store them
                            if (favorites == null) {
                                favorites = new HashMap<>();
                            }


                            String rankM = rankView.getText().toString();

                            String albumM  = new String();
                            String trackM  = new String();
                            String artistM = new String();
                            String yearM   = new String();

                            for (MusicClass M:items) {
                                if (rankM.equals(M.getTrack())) {
                                    albumM  = M.getTrack();
                                    trackM  = M.getArtist();
                                    artistM = M.getImageURL();
                                    yearM   = M.getYear();
                                    break;
                                }
                            }

                            // Update the values of the favorites HashMap
                            MusicClass character = new MusicClass(albumM, rankM, trackM, artistM, yearM);
                            favorites.put(rankM, character);

                            // Update the user database by adding the newly favorited track
                            String userid = user.getUid();
                            fbdb = FirebaseDatabase.getInstance();
                            dbref = fbdb.getReference("User");

                            try {
                                dbref.child("user").child(userid).child("favorites").setValue(favorites);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            Log.d("onCancelled MusicClass", databaseError.toString());
                        }
                    });
                    favButton.setBackgroundResource(R.drawable.ic_check);

                    // Let the user know that the item was added
                    Toast.makeText(getContext(), "Toegevoegd aan favorieten", Toast.LENGTH_SHORT).show();
                } else {
                    // Warn the user that adding favorites when not logged in is not possible
                    Toast.makeText(getContext(), "Favorieten toevoegen is niet mogelijk zonder login", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

}