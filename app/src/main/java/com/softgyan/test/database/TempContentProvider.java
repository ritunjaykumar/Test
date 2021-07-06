package com.softgyan.test.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TempContentProvider extends ContentProvider {
    private static final int TEMP = 1;
    private static final int TEMP_ID = 2;
    private static final String TAG = "TempContentProvider";

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(Contract.Temp.CONTENT_AUTHORITY, Contract.Temp.PATH_USER, TEMP);
        sUriMatcher.addURI(Contract.Temp.CONTENT_AUTHORITY, Contract.Temp.PATH_USER + "/#", TEMP_ID);
    }

    private DBHelper dbHelper;

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        final int match = sUriMatcher.match(uri);
        SQLiteDatabase database = dbHelper.getReadableDatabase();
        if(match == TEMP){
            String sqlQuery = String.format("SELECT * FROM %s;", Contract.Temp.TABLE_NAME);
            return database.rawQuery(sqlQuery, selectionArgs);
        }
        throw new UnsupportedOperationException("invalid uri "+uri);

    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        if (!checkValidation(values)) {
            throw new UnsupportedOperationException("invalid image name or image uri");
        }
        final int match = sUriMatcher.match(uri);
        if (match == TEMP) {
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            final long insert = db.insert(Contract.Temp.TABLE_NAME, null, values);
            if (insert == -1) {
                Log.d(TAG, "insert: failed to insert");
                return null;
            }
            return ContentUris.withAppendedId(uri, insert);
        }

        throw new UnsupportedOperationException("invalid uri " + uri);

    }

    private boolean checkValidation(ContentValues values) {
        if (values == null) return false;
        final String imageName = values.getAsString(Contract.Temp.IMAGE_NAME);
        final String imageUri = values.getAsString(Contract.Temp.IMAGE_URI);
        return imageName != null && imageUri != null;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        switch (match) {
            //delete all records
            case TEMP: {
                return database.delete(Contract.Temp.TABLE_NAME, selection, selectionArgs);
            }
            // delete single record
            case TEMP_ID: {
                int id = (int) ContentUris.parseId(uri);
                selection = Contract.Temp._ID + "=?";
                selectionArgs = new String[]{String.valueOf(id)};
                return database.delete(Contract.Temp.TABLE_NAME, selection, selectionArgs);
            }
            default: {
                throw new UnsupportedOperationException("invalid uri " + uri);
            }
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
