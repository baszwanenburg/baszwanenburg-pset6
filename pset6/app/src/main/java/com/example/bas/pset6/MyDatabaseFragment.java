package com.example.bas.pset6;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * This fragment displays the entire Top 2000 playlist in a
 * listview by reading and parsing a JSON object from a web page.
 */
public class MyDatabaseFragment extends Fragment {

    private ArrayList<MusicClass> items = new ArrayList<>();
    private String url;

    // Required empty public constructor
    public MyDatabaseFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater
                .inflate(R.layout.fragment_my_database, container, false);

        // JSON containing the 1999 Top 2000 playlist
        url = "https://raw.githubusercontent.com/antvis/g2-react/master/examples/top2000.json";
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

                            // Loop through JSONArray to add the items to an ArrayList
                            for (int i = 0; i < 100; i++) {
                                JSONObject obj = arr.getJSONObject(i);

                                String album = "https://pl.scdn.co/images/pl/default/29b2514f2e6a66d1f4e79d88eefc8d553a12e10b";
                                String rank = obj.getString("rank");
                                String track = obj.getString("title");
                                String artist = obj.getString("artist");
                                String year = obj.getString("release");
                                items.add(new MusicClass(album, rank, track, artist, year));
                            }

                            // Parse JSON to the adapter
                            Context context = getActivity();
                            MyAdapter adapter;
                            ListView listView = getView().findViewById(R.id.list_view);

                            adapter = new MyAdapter(context, items);
                            listView.setAdapter(adapter);

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