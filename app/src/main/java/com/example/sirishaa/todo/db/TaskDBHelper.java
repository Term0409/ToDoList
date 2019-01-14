package com.example.sirishaa.todo.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TaskDBHelper extends SQLiteOpenHelper{

    public TaskDBHelper(Context context) {
        super(context, "com.example.sirishaa.todo.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE Tasks (_id INTEGER PRIMARY KEY AUTOINCREMENT, title TEXT NOT NULL);");

        

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Tasks" );
        onCreate(db);

    }
}
