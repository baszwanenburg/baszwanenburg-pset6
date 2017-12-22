package com.example.bas.pset6;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;

/**
 * Creates a custom listview adapter for an array with the usernames in it
 */
public class UserAdapter extends ArrayAdapter<String> {
    private Context context;
    private ArrayList<String> user;

    public UserAdapter(Context context, ArrayList<String> listofusers) {
        super(context, 0, listofusers);
        this.context = context;
        this.user = listofusers;
    }

    /**
     * Inflate the layout and set the views with information from the array
     */
    @Override
    public View getView(int position, View view, ViewGroup group) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.listview_layout_users, null);
        TextView username = view.findViewById(R.id.usernameView);
        username.setText(user.get(position));

        return view;
    }
}