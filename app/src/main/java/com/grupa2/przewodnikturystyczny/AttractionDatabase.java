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
            "CREATE TABLE " + AttrConst.TABLE_NAME + " ("
                    + AttrConst._ID + " INTEGER PRIMARY KEY,"
                    + AttrConst.COLUMN_ATTRACTION_NAME + " TEXT,"
                    + AttrConst.COLUMN_ADRES + " TEXT,"
                    + AttrConst.COLUMN_SHORT_DESCRIPTION + " TEXT,"
                    + AttrConst.COLUMN_FULL_DESCRIPTION + " TEXT,"
                    + AttrConst.COLUMN_PHOTO_1 + " TEXT,"
                    + AttrConst.COLUMN_PHOTO_2 + " TEXT,"
                    + AttrConst.COLUMN_PHOTO_3 + " TEXT )";

    private static final String TAG = "AttractionDatabase";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDatabase;
    private final Context mContext;
    private String orderBy = null;

    public static class AttrConst implements BaseColumns {
        public static final String TABLE_NAME = "attractions";
        public static final String COLUMN_ATTRACTION_NAME = "name";
        public static final String COLUMN_ADRES = "adres";
        public static final String COLUMN_SHORT_DESCRIPTION = "shortDescription";
        public static final String COLUMN_FULL_DESCRIPTION = "fullDescription";
        public static final String COLUMN_PHOTO_1 = "photo1";
        public static final String COLUMN_PHOTO_2 = "photo2";
        public static final String COLUMN_PHOTO_3 = "photo3";
        public static final String KEY = "key";
    }

    public class DatabaseHelper extends SQLiteOpenHelper {

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
        mContext = context;
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
        initialValues.put(AttrConst.COLUMN_PHOTO_1, attraction.getPhoto1());
        initialValues.put(AttrConst.COLUMN_PHOTO_2, attraction.getPhoto2());
        initialValues.put(AttrConst.COLUMN_PHOTO_3, attraction.getPhoto3());
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
        Cursor mCursor = mDatabase.query(AttrConst.TABLE_NAME, null, null, null, null, null, orderBy, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        return mCursor;
    }

    public void addAttractions() {
        insertAttraction(new Attraction("Hydropolis", "ul. Na Grobli 19, Wrocław 50-421", "Hydropolis to centrum wiedzy na temat wody, w którym w fascynujący sposób pokazane jest jej znaczenie dla człowieka",
                "Hydropolis to centrum wiedzy na temat wody, w którym w fascynujący sposób pokazane jest jej znaczenie dla człowieka. Wystawa zajmuje cztery tysiące metrów, w dawnych, ponad stuletnich zbiornikach czystej wody"
                        + "" + "Oryginalna scenografia oraz multimedia (ponad 60 monitorów dotykowych) sprawiają, że w Hydropolis nie można się nudzić, bo angażowane są niemal wszystkie zmysły." + "– Woda była na początku wszelkiego życia, nie tylko na Ziemi." +
                        " W najdalszych poznanych dotąd zakątkach kosmosu uczeni trafiają na ślady wody. Jej obecność to jeden pretekstów do podejrzeń, że we wszechświecie nie jesteśmy sami, bo woda daje życie – twierdzą autorzy wystawy w Hydropolis", "hydropolis1", "hydropolis2", "hydropolis3" ));

        insertAttraction(new Attraction("Panorama Racławicka", "ul. Purkyniego 11 50-155 Wrocław", "Panorama Racławicka to jedno z niewielu miejsc na świecie, gdzie podziwiać można relikt dziewiętnastowiecznej kultury masowej. Wielkie malowidło (15x114m)",
                "Panorama Racławicka – muzeum sztuki we Wrocławiu, oddział Muzeum Narodowego we Wrocławiu, założone w 1893 we Lwowie, od 1980 we Wrocławiu; eksponuje cykloramiczny obraz Bitwa pod Racławicami namalowany w latach 1893–1894 przez zespół malarzy pod kierunkiem Jana Styki" +
                        " i Wojciecha Kossaka. Obraz olejny przedstawia bitwę pod Racławicami (1794), jeden z epizodów insurekcji kościuszkowskiej, zwycięstwo wojsk polskich pod dowództwem gen. Tadeusza Kościuszki nad wojskami rosyjskimi pod dowództwem gen. Aleksandra Tormasowa." +
                        "Obok Panoramy siedmiogrodzkiej, Golgoty oraz Męczeństwa chrześcijan w cyrku Nerona jest to jedna z czterech XIX–wiecznych panoram, namalowanych pod kierownictwem Jana Styki i Wojciecha Kossaka.", "panorama1", "panorama2", "panorama3" ));
    }

    public Cursor getAttractionByName(String name) {
        Cursor cursor = mDatabase.query(AttrConst.TABLE_NAME, null, AttrConst.COLUMN_ATTRACTION_NAME + " = ?", new String[] {name}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        return cursor;
    }


}
