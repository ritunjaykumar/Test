package com.softgyan.test.database;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.softgyan.test.models.TempModel;

import java.util.ArrayList;
import java.util.List;

public class TempQuery {
    private static final String TAG = "TempQuery";

    public static synchronized int insertData(Context context, @NonNull String imageName,
                                              @NonNull String imageUri) {
        ContentValues values = new ContentValues();
        values.put(Contract.Temp.IMAGE_NAME, imageName);
        values.put(Contract.Temp.IMAGE_URI, imageUri);
        try {
            final Uri insert = context.getContentResolver().insert(Contract.Temp.CONTENT_TEMP_URI, values);
            if (insert != null) {
                return (int) ContentUris.parseId(insert);
            }
            return -1;
        } catch (Exception e) {
            Log.d(TAG, "insertData: error : " + e.getMessage());
            return -1;
        }

    }


    public static synchronized List<TempModel> getAllData(Context context) {
        List<TempModel> tempModelList = new ArrayList<>();
        final Cursor query = context.getContentResolver().query(Contract.Temp.CONTENT_TEMP_URI,
                null, null, null, null);

        if (query.getCount() == 0) {
            Log.d(TAG, "getAllData: empty query");
            return tempModelList;
        }
        while (query.moveToNext()) {
            int id = query.getInt(query.getColumnIndex(Contract.Temp._ID));
            String imageName = query.getString(query.getColumnIndex(Contract.Temp.IMAGE_NAME));
            String imageUri = query.getString(query.getColumnIndex(Contract.Temp.IMAGE_URI));

            TempModel tempModel = new TempModel(id, imageName, imageUri);
            tempModelList.add(tempModel);
        }
        query.close();
        return tempModelList;

    }


    public static synchronized void deleteAllData(Context context) {
        final int delete = context.getContentResolver().delete(Contract.Temp.CONTENT_TEMP_URI,
                null, null);
        if (delete != 0) {
            Log.d(TAG, "deleteAllData: all deleted");
        } else {
            Log.d(TAG, "deleteAllData: delete files is " + delete);
        }
    }


}
