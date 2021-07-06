package com.softgyan.test.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DBHelper";
    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Test.DB";

    public DBHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate: creating database");
        createDatabase(db);
    }

    private void createDatabase(SQLiteDatabase db) {
        final String tempTable = "CREATE TABLE " + Contract.Temp.TABLE_NAME + " ( " +
                Contract.Temp._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Contract.Temp.IMAGE_NAME + " TEXT NOT NULL, " +
                Contract.Temp.IMAGE_URI + " TEXT NOT NULL " +
                ");";
        db.execSQL(tempTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d(TAG, "onUpgrade: upgrading table");
        final String DROP_TEMP_TABLE_QUERY = "DROP TABLE IF EXISTS " + Contract.Temp.TABLE_NAME;
        db.execSQL(DROP_TEMP_TABLE_QUERY);
        onCreate(db);
    }
}
