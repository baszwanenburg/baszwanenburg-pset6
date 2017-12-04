package com.example.bas.baszwanenburg_pset5;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class RestoAdapter extends CursorAdapter {
    public RestoAdapter(Context context, Cursor cursor) {
        super(context, cursor, R.layout.rowlayout);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.rowlayout, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor){
        // Find view
        TextView textje = view.findViewById(R.id.textView2);
        TextView amountje = view.findViewById(R.id.textView);
        TextView price = view.findViewById(R.id.textView3);

        // Calculate the total price
        Integer aantal = cursor.getInt(cursor.getColumnIndex("amount"));
        Integer prijs = cursor.getInt(cursor.getColumnIndex("price"));
        Integer total = aantal * prijs;

        // Show item
        textje.setText(cursor.getString(cursor.getColumnIndex("name")));
        amountje.setText(cursor.getString(cursor.getColumnIndex("amount"))+" x");
        price.setText("$" + total.toString());
    }
}