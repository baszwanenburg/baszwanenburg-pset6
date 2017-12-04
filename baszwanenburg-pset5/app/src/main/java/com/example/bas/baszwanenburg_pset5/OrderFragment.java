package com.example.bas.baszwanenburg_pset5;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;


public class OrderFragment extends DialogFragment implements View.OnClickListener {
    RestoDatabase db;
    RestoAdapter adapter;
    Button clear;
    Button place;
    Integer totalprice = 0;
    Cursor cursor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);

        // Put the information from the database in the listview
        ListView list = (ListView) view.findViewById(R.id.list);
        db = RestoDatabase.getInstance(getContext());
        cursor = db.selectAll();
        adapter = new RestoAdapter(getContext(), cursor);
        list.setAdapter(adapter);

        // Create the listeners
        clear = (Button) view.findViewById(R.id.clear);
        place = (Button) view.findViewById(R.id.order);
        clear.setOnClickListener(this);
        place.setOnClickListener(this);

        // Set the total price of the order, when the order is not empty
        totalprice();
        TextView placeholder = view.findViewById(R.id.totaal);
        if (totalprice!=0) {
            placeholder.setText("Total price: $" + totalprice.toString());
        }

        setRetainInstance(true);
        return view;
    }

    public void totalprice(){
        // Loop through the database to calculate the total price of an order
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Integer prijs = cursor.getInt(cursor.getColumnIndex("price"));
            Integer aantal = cursor.getInt(cursor.getColumnIndex("amount"));
            Integer total = aantal * prijs;
            totalprice = totalprice + total;
            cursor.moveToNext();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.clear:
                clear();
                break;
            case R.id.order:
                order();
                break;
        }
    }

    public void order() {
        // When there are items in the order, place the order
        if (totalprice!=0) {
            getData();
        }
        // Let the user know that you can't place an empty order
        else{
            Context context = getContext();
            Toast.makeText(context, "You can't place an empty order!", Toast.LENGTH_LONG).show();
        }
    }

    public void clear() {
        // Clear the database and close the dialog window
        db.clear();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commit();
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }


    public void getData() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://resto.mprog.nl/order";
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObj = new JSONObject(response);
                            String temp = (jsonObj.getString("preparation_time"));
                            Context context = getContext();
                            // Let the user know how long the order will take through a toast
                            Toast.makeText(context, "Order placed, it will take " + temp + " minutes.", Toast.LENGTH_LONG).show();
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
        queue.add(stringRequest);
    }
}
