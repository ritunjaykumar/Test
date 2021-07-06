package com.softgyan.test.database;

import android.net.Uri;
import android.provider.BaseColumns;

public final class Contract {

    public final static class Temp implements BaseColumns {
        public static final String TABLE_NAME = "testTable";
        public static final String IMAGE_NAME = "imageName";
        public static final String IMAGE_URI = "imageUri";


        public static String CONTENT_AUTHORITY = "com.softgyan.test";
        public static Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_USER = TABLE_NAME;
        public static final Uri CONTENT_TEMP_URI =Uri.withAppendedPath(BASE_CONTENT_URI, PATH_USER);
    }
}
