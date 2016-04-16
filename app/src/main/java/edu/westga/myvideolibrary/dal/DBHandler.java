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
        LocationDAL.initialize(db);
        initializeVideoLibrary(db);
        initializeWishList(db);
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

    /**
     * ILocationDAL
     */
    @Override
    public ArrayList<Location> getLocations() {
        return LocationDAL.getAll(this);
    }

    @Override
    public int addLocation(String location) {
        return LocationDAL.add(location, this);
    }

    @Override
    public boolean removeLocation(int id) {
        return LocationDAL.remove(id, this);
    }

    /**
     * Locations database access class
     */
    private static class LocationDAL {
        /**
         * Create and load initial data into Location table
         *
         * @param db Database instance
         */
        private static void initialize(SQLiteDatabase db) {
            String createLocationTable = "CREATE TABLE " + TABLE_LOCATION +
                    "(" + COLUMN_LOCATION_ID + " INTEGER PRIMARY KEY," +
                    COLUMN_LOCATION_LOCATION + " TEXT UNIQUE" +
                    ")";
            db.execSQL(createLocationTable);
            add("DVD", db);
            add("Digital", db);
            add("Amazon", db);
            add("Vudu", db);
        }

        /**
         * Utility function to get a location id from an open database
         *
         * @param location Location to find
         * @param db Database to look in
         * @return Location id, or -1 if not found
         */
        public static int getId(String location, SQLiteDatabase db) {
            String query = "SELECT " + COLUMN_LOCATION_ID + " FROM " + TABLE_LOCATION + " WHERE " + COLUMN_LOCATION_LOCATION + " =  \"" + location + "\"";
            int result = -1;

            Cursor cursor = db.rawQuery(query, null);
            if (cursor.moveToFirst()) {
                result = Integer.parseInt(cursor.getString(0));
            }
            return result;
        }

        /**
         * Utility function to add a location to an open database
         *
         * @param location Location to add
         * @param db Database to add to
         * @return Identity column of item added, or existing item if applicable
         */
        public static int add(String location, SQLiteDatabase db) {
            int result;

            // if location already exists, look up id
            if ((result=getId(location,db)) > 0) {
                return result;
            }
            // otherwise insert new row
            ContentValues values = new ContentValues();
            values.put(COLUMN_LOCATION_LOCATION, location);
            result=(int)db.insert(TABLE_LOCATION, null, values);
            return result;
        }


        /**
         * Add a location
         *
         * @param location Location to add
         * @param mgr DB Manager to use
         * @return Id of item added, or existing item if applicable
         */
        public static int add(String location, DBHandler mgr) {
            SQLiteDatabase db = mgr.getWritableDatabase();
            int result=add(location, db);

            db.close();
            return result;
        }

        /**
         * Delete a location
         *
         * @param id Id of location to delete
         * @param mgr DB Manager to use
         * @return True if item was deleted
         */
        public static boolean remove(int id, DBHandler mgr) {
            SQLiteDatabase db = mgr.getWritableDatabase();
            int count = db.delete(TABLE_LOCATION, COLUMN_LOCATION_ID + " = ?", new String[] { String.valueOf(id) });
            db.close();
            return count > 0;
        }

        /**
         * Get a list of all locations
         *
         * @param mgr DB Manager to use
         * @return List of locations
         */
        public static ArrayList<Location> getAll(DBHandler mgr) {
            ArrayList<Location> results = new ArrayList<>();
            String query = "SELECT * FROM " + TABLE_LOCATION;

            SQLiteDatabase db = mgr.getWritableDatabase();
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

    }

}
