package com.grupa2.przewodnikturystyczny;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class AttractionDatabase {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "AttrConst.db";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + AttrConst.TABLE_NAME;
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + AttrConst.TABLE_NAME + " (" +
                    AttrConst._ID + " INTEGER PRIMARY KEY," +
                    AttrConst.COLUMN_ATTRACTION_NAME + " TEXT," +
                    AttrConst.COLUMN_ADRES + " TEXT," +
                    AttrConst.COLUMN_SHORT_DESCRIPTION + " TEXT," +
                    AttrConst.COLUMN_FULL_DESCRIPTION + " TEXT)";

    private static final String TAG = "AttractionDatabase";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDatabase;
    private final Context mContext;
    private String orderBy = null;

    public static class AttrConst implements BaseColumns {
        public static final String TABLE_NAME = "attractions";
        public static final String COLUMN_ATTRACTION_NAME = "name";
        public static final String COLUMN_ADRES = "adress";
        public static final String COLUMN_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_FULL_DESCRIPTION = "fullDescription";
        /*public static final String COLUMN_PHOTO_1 = "photo1";
        public static final String COLUMN_PHOTO_2 = "photo2";
        public static final String COLUMN_PHOTO_3 = "photo3";*/
        public static final String COLUMN_PHOTOS = "photos";

    }

    public class DatabaseHelper extends SQLiteOpenHelper {
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + AttrConst.TABLE_NAME;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public AttractionDatabase (Context context) {
        this.mContext = context;
    }

    public void open() throws SQLException {
        mDbHelper = new AttractionDatabase.DatabaseHelper(mContext);
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close () {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long insertAttraction (Attraction attraction) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(AttrConst.COLUMN_ATTRACTION_NAME, attraction.getName());
        initialValues.put(AttrConst.COLUMN_ADRES, attraction.getAdress());
        initialValues.put(AttrConst.COLUMN_SHORT_DESCRIPTION, attraction.getShortDesctription());
        initialValues.put(AttrConst.COLUMN_FULL_DESCRIPTION, attraction.getFullDescription());
        /*initialValues.put(AttrConst.COLUMN_PHOTO_1, attraction.getPhoto1());
        initialValues.put(AttrConst.COLUMN_PHOTO_2, attraction.getPhoto2());
        initialValues.put(AttrConst.COLUMN_PHOTO_3, attraction.getPhoto3());*/
        initialValues.put(AttrConst.COLUMN_PHOTOS, attraction.getPhotos());
        return mDatabase.insert(AttrConst.TABLE_NAME, null, initialValues);
    }

    public boolean deleteAllAttractions() {
        int doneDelete = 0;
        doneDelete = mDatabase.delete(AttrConst.TABLE_NAME, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public void setOrderBy(String txt) {
        orderBy = txt;
    }

    public Cursor fetchAllAttractions() {
        Cursor mCursor = mDatabase.query(AttrConst.TABLE_NAME, new String[]{AttrConst._ID, AttrConst.COLUMN_ATTRACTION_NAME,
                AttrConst.COLUMN_SHORT_DESCRIPTION, AttrConst.COLUMN_PHOTO_1}, null, null, null, null, orderBy, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public Cursor fetchAttractionByNameAndDistance(String inputText) throws SQLException {
        Log.w(TAG, "Szukamy: " + inputText);

        Cursor mCursor = null;

        if (inputText == null || inputText.length () == 0) {
            mCursor = mDatabase.query(AttrConst.TABLE_NAME, new String[] {AttrConst.COLUMN_ATTRACTION_NAME,
                    AttrConst.COLUMN_SHORT_DESCRIPTION, AttrConst.COLUMN_ADRES}, null, null, null, null, orderBy, null);

        } else {
            mCursor = mDatabase.query(AttrConst.TABLE_NAME, new String[] {AttrConst.COLUMN_ATTRACTION_NAME,
                            AttrConst.COLUMN_SHORT_DESCRIPTION, AttrConst.COLUMN_ADRES, AttrConst.COLUMN_DIST},
                    AttrConst.COLUMN_ATTRACTION_NAME + " like '%" + inputText + "%'" + AttrConst.COLUMN_DIST //może należałoby zrobić kolumnę distance (tylko problem z jej aktualizowaniem)
                    null, null, null, orderBy, null);
        }

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;

    }


}
