package com.example.bas.baszwanenburg_pset5;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class RestoDatabase extends SQLiteOpenHelper {
    private static RestoDatabase instance;
    private static int version = 3;
    private static String databasename = "resto";


    private RestoDatabase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public static RestoDatabase getInstance(Context context) {
        if (instance == null) {
            instance = new RestoDatabase(context.getApplicationContext(), databasename, null, version);
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table resto (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, price DOUBLE, amount INTEGER);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "resto");
        onCreate(db);
    }

    public Cursor selectAll() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor lol = db.rawQuery("SELECT * FROM resto", null);
        return lol;
    }

    public void insert(String name, double price) {
        SQLiteDatabase db = getWritableDatabase();
        // Try to get information about the meal that is already in the database
        Cursor cursor = db.rawQuery("SELECT * FROM resto WHERE name = '"+ name + "'",null);
        ContentValues values =  new ContentValues();

        // Check if item is in the database
        if (cursor != null && cursor.moveToFirst()){
            Integer tempamount = cursor.getInt(cursor.getColumnIndex("amount"));
            Integer newamount = 1 + tempamount;
            values.put("amount", newamount);
            db.update("resto", values,"name=?", new String[]{name});
        }

        // If not, add it to the database
        else {
            addItem(name, price);
        }
    }

    public void addItem(String name, double price) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put("name", name);
        values.put("price", price);
        values.put("amount", 1);
        db.insert("resto",null, values);
    }


    public void clear() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("resto", null, null);
    }
}
