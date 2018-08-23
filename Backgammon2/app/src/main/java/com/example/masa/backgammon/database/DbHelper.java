package com.example.masa.backgammon.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {


    private static final String SQL_CREATE_ENTRIES = "CREATE TABLE "
            + TableEntry.TABLE_NAME + " (" +TableEntry._ID
            + " INTEGER PRIMARY KEY,"
            + TableEntry.PLAYER_ONE + " TEXT, "
            + TableEntry.PLAYER_TWO + " TEXT, "
            + TableEntry.PLAYER_ONE_POINTS + " INTEGER, "
            + TableEntry.PLAYER_TWO_POINTS + " INTEGER, "
            + TableEntry.TIME + " TEXT" + " );";

    private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS "
            + TableEntry.TABLE_NAME;

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Scores.db";

    public DbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_CREATE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
