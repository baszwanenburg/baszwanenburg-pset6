package com.example.bas.pset6;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This fragment displays the entire Top 2000 playlist in a listview by reading
 * and parsing a JSON object from a web page.
 */
public class MyDatabase extends Fragment {
    private ArrayList<MusicClass> items = new ArrayList<>();
    private String url;

    // Required empty public constructor
    public MyDatabase() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_my_database, container, false);

        url = "https://raw.githubusercontent.com/baszwanenburg/test2/master/top2000";
        getJSON(url);

        return rootView;
    }

    /**
     * Fetches JSON from the web and parses it.
     */
    public void getJSON(String url) {
        RequestQueue RQ = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Create a new JSONObject and push the data into it
                            JSONArray arr = new JSONArray(response);

                            // Loop through JSONArray to add the items to an Arraylist
                            for (int i = 0; i < 100; i++) {
                                JSONObject obj = arr.getJSONObject(i);

                                String album = "https://pl.scdn.co/images/pl/default/29b2514f2e6a66d1f4e79d88eefc8d553a12e10b";
                                String rank = obj.getString("rank");
                                String track = obj.getString("title");
                                String artist = obj.getString("artist");
                                String year = obj.getString("release");
                                items.add(new MusicClass(album, rank, track, artist, year));
                            }

                            Context context = getActivity();

                            // Parse JSON to the adapter
                            MyAdapter adapter;
                            ListView view = getView().findViewById(R.id.list_view);
                            adapter = new MyAdapter(context, items);
                            view.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RQ.add(stringRequest);
    }
}