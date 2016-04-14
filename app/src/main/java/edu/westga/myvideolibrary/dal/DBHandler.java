package edu.westga.myvideolibrary.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Database handler
 *
 * Created by Ron on 4/13/2016.
 */
public class DBHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myvideolibrary.db";

    public static final String TABLE_LOCATION = "location";
    public static final String COLUMN_LOCATION_ID = "id";
    public static final String COLUMN_LOCATION_LOCATION = "location";

    /**
     * Class constructor
     *
     * @param context Application context
     * @param name Unused
     * @param factory Cursor factory
     * @param version Unused
     */
    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        initializeLocations(db);
        initializeVideoLibrary(db);
        initializeWishList(db);
    }

    /**
     * Create and load initial data into Location table
     *
     * @param db Database instance
     */
    private void initializeLocations(SQLiteDatabase db) {
        String createLocationTable = "CREATE TABLE " + TABLE_LOCATION +
                "(" + COLUMN_LOCATION_ID + " INTEGER PRIMARY KEY," +
                COLUMN_LOCATION_LOCATION + " TEXT UNIQUE" +
                ")";
        db.execSQL(createLocationTable);
        addLocation("DVD");
        addLocation("Digital");
        addLocation("Amazon");
        addLocation("Vudu");
    }

    /**
     * Add a location, return identity value
     *
     * @param location Location to add
     * @return Identity column
     */
    public int addLocation(String location) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOCATION_LOCATION, location);

        SQLiteDatabase db = this.getWritableDatabase();

        // TODO: Add location
        // Query id for location
        // if found, return id
        // if row count 1, get and return ident
        // else:
        db.insert(TABLE_LOCATION, null, values);
        // get ident for this insert
        db.close();
        return 0;
    }

    /**
     * Create video library table
     *
     * @param db Database instance
     */
    private void initializeVideoLibrary(SQLiteDatabase db) {
        // TODO: Create video library table
    }

    /**
     * Create wish list table
     *
     * @param db Database instance
     */
    private void initializeWishList(SQLiteDatabase db) {
        // TODO: Create wishlist table
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {

    }
}
