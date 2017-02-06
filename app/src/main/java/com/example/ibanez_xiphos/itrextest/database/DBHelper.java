package com.example.ibanez_xiphos.itrextest.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "myDB";
    public static final String TABLE_FILMS = "films";
    public static final String FILMS_ID = "id";
    public static final String FILMS_IMAGE = "image";
    public static final String FILMS_NAME = "name";
    public static final String FILMS_NAME_ENG = "name_eng";
    public static final String FILMS_PREMIERE = "premiere";
    public static final String FILMS_DESCRIPTION = "description";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+ TABLE_FILMS +" ("
                + FILMS_ID + " integer primary key autoincrement,"
                + FILMS_IMAGE + " text,"
                + FILMS_NAME + " text,"
                + FILMS_NAME_ENG + " text,"
                + FILMS_PREMIERE + " text,"
                + FILMS_DESCRIPTION + " text" + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
