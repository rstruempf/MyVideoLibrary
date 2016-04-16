package edu.westga.myvideolibrary.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import edu.westga.myvideolibrary.model.Location;

/**
 * Database handler
 *
 * Created by Ron on 4/13/2016.
 */
public class DBHandler extends SQLiteOpenHelper implements ILocationDAL {

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

    @Override
    public ArrayList<Location> getLocations() {
        ArrayList<Location> results = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_LOCATION;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            db.close();
            return results;
        }
        do {
            Location location = new Location(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
            results.add(location);
        } while(cursor.moveToNext());
        db.close();
        return results;
    }

    @Override
    public int addLocation(String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result;

        // if location already exists, look up id
        if ((result=getLocationId(location,db)) > 0) {
            db.close();
            return result;
        }
        // otherwise insert new row
        ContentValues values = new ContentValues();
        values.put(COLUMN_LOCATION_LOCATION, location);
        result=(int)db.insert(TABLE_LOCATION, null, values);
        db.close();
        return result;
    }

    @Override
    public boolean removeLocation(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int count = db.delete(TABLE_LOCATION, COLUMN_LOCATION_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
        return count > 0;
    }

    /**
     * Look up a location by location name, return location id
     *
     * @param location Location to look up
     * @param db Database object to use
     * @return Location id
     */
    private int getLocationId(String location, SQLiteDatabase db) {
        String query = "SELECT " + COLUMN_LOCATION_ID + " FROM " + TABLE_LOCATION + " WHERE " + COLUMN_LOCATION_LOCATION + " =  \"" + location + "\"";
        int result = -1;

        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            result = Integer.parseInt(cursor.getString(0));
        }
        return result;
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
        // update actions on new version install
    }
}
