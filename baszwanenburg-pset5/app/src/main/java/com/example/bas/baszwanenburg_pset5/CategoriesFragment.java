package com.example.bas.baszwanenburg_pset5;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import org.json.JSONTokener;

import java.util.ArrayList;


public class CategoriesFragment extends ListFragment {
    public ArrayList<String> categories = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create new queue
        RequestQueue RQ = Volley.newRequestQueue(getActivity().getApplicationContext());

        // Add to queue
        String categories = "https://resto.mprog.nl/categories";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, categories,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse JSON to arraylist
                            makelistview(getTextJSON(response));
                        } catch (JSONException e) {
                            // Catch exception when needed
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            // Throw error when needed
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        RQ.add(stringRequest);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);
        setRetainInstance(true);
        return view;
    }

    public void makelistview(ArrayList<String> info) {
        // Set the adapter
        this.setListAdapter(new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_list_item_1, info));
    }

    public ArrayList<String> getTextJSON(String string) throws JSONException {
        try {
            // The following code is from the android developers website
            JSONObject object = (JSONObject) new JSONTokener(string).nextValue();
            JSONArray cat = object.getJSONArray("categories");
            // Loop through JSONArray to add the items to an Arraylist
            for (int i = 0; i < cat.length(); i++) {
                categories.add(cat.get(i).toString());
            }
            // Throw an exception when needed
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categories;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        // Get value of the clicked item
        String s = String.valueOf(l.getItemAtPosition(position));

        // Make new instance of class
        MenuFragment menuFragment = new MenuFragment();

        // Create bundle
        Bundle args = new Bundle();
        args.putString("category", s);
        menuFragment.setArguments(args);

        // Go to next fragment
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, menuFragment).addToBackStack(null).commit();
    }
}
