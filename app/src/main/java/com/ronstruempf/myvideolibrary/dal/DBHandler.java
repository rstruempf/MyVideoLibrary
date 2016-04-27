package com.ronstruempf.myvideolibrary.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;

import com.ronstruempf.myvideolibrary.model.Location;
import com.ronstruempf.myvideolibrary.model.Video;

/**
 * Database handler
 *
 * Created by Ron on 4/13/2016.
 */
public class DBHandler extends SQLiteOpenHelper implements ILocationDAL, IVideoDAL {
    private static String LOG_TAG = DBHandler.class.getSimpleName() + "_LOGTAG";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "myvideolibrary.db";

    public static final String TABLE_LOCATION = "location";
    public static final String COLUMN_LOCATION_ID = "_id";  // apparently SQLite expects pk column to be called _id
    public static final String COLUMN_LOCATION_LOCATION = "location";

    public static final String TABLE_VIDEO = "video";
    public static final String COLUMN_VIDEO_ID = "_id";     // apparently SQLite expects pk column to be called _id
    public static final String COLUMN_VIDEO_TITLE = "title";
    public static final String COLUMN_VIDEO_YEAR = "year";
    public static final String COLUMN_VIDEO_LOCATION = "location";
    public static final String COLUMN_VIDEO_RATING = "rating";
    public static final String COLUMN_VIDEO_DESCRIPTION = "description";
    public static final String COLUMN_VIDEO_IMDB_URL = "imdb";

    /**
     * Class constructor
     *
     * @param context Context to operate under
     */
    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        LocationDAL.initialize(db);
        VideoDAL.initialize(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
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
     * IVideoDAL
     */
    public Video getVideo(int id) {return VideoDAL.get(id, this);}

    public ArrayList<Video> getAllVideos() {return VideoDAL.getAll(this);}


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
            add("Not In Library", db, "0");
            add("DVD", db);
            add("Digital", db);
            add("Amazon", db);
            add("Vudu", db);
        }

        /**
         * Utility function to get a location id from an open database
         *
         * @param location Location to find
         * @param db       Database to look in
         * @return Location id, or -1 if not found
         */
        public static int getId(String location, SQLiteDatabase db) {
            int result = -1;

            // Doc: It appears that query() and rawQuery() are approximately equal and a matter of
            //  preference.
            Cursor cursor;
            try {
                cursor = db.query(
                        TABLE_LOCATION,
                        new String[]{COLUMN_LOCATION_ID},
                        COLUMN_LOCATION_LOCATION + " = ?",
                        new String[]{location},
                        null,   // group by
                        null,   // having
                        null); // order by
            }
            catch (Exception ex) {
                Log.e(DBHandler.LOG_TAG, "Query error: " + ex.getMessage());
                return result;
            }
            if (cursor.moveToFirst()) {
                result = Integer.parseInt(cursor.getString(0));
            }
            return result;
        }

        /**
         * Utility function to add a location to an open database
         *
         * @param location Location to add
         * @param db       Database to add to
         * @param id       Row id if set directly
         * @return Identity column of item added, or existing item if applicable
         */
        private static int add(String location, SQLiteDatabase db, String id) {
            int result;

            // if location already exists, look up id
            if ((result = getId(location, db)) > 0) {
                return result;
            }
            // otherwise insert new row
            ContentValues values = new ContentValues();
            if (id != null && id.length() > 0) {
                values.put(COLUMN_LOCATION_ID, id);
            }
            values.put(COLUMN_LOCATION_LOCATION, location);
            // TODO: Trap exception
            result = (int) db.insert(TABLE_LOCATION, null, values);
            return result;
        }

        /**
         * Utility function to add a location to an open database
         *
         * @param location Location to add
         * @param db       Database to add to
         * @return Identity column of item added, or existing item if applicable
         */
        private static int add(String location, SQLiteDatabase db) {
            return add(location, db, null);
        }


        /**
         * Add a location
         *
         * @param location Location to add
         * @param mgr      DB Manager to use
         * @return Id of item added, or existing item if applicable
         */
        public static int add(String location, DBHandler mgr) {
            SQLiteDatabase db = mgr.getWritableDatabase();
            int result = add(location, db);

            db.close();
            return result;
        }

        /**
         * Delete a location
         *
         * @param id  Id of location to delete
         * @param mgr DB Manager to use
         * @return True if item was deleted
         */
        public static boolean remove(int id, DBHandler mgr) {
            SQLiteDatabase db = mgr.getWritableDatabase();
            // TODO: Trap exception
            int count = db.delete(
                    TABLE_LOCATION,
                    COLUMN_LOCATION_ID + " = ?",
                    new String[]{String.valueOf(id)}
            );
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

            SQLiteDatabase db = mgr.getReadableDatabase();
            Cursor cursor;

            try {
                cursor = db.query(
                        TABLE_LOCATION,
                        new String[]{COLUMN_LOCATION_ID, COLUMN_LOCATION_LOCATION},
                        null,   // where statement
                        null,   // where parameters
                        null,   // group by
                        null,   // having
                        null); // order by
            }
            catch (Exception ex) {
                Log.e(DBHandler.LOG_TAG, "Query error: " + ex.getMessage());
                db.close();
                return results;
            }

            if (!cursor.moveToFirst()) {
                db.close();
                return results;
            }
            do {
                Location location = new Location(Integer.parseInt(cursor.getString(0)), cursor.getString(1));
                results.add(location);
            } while (cursor.moveToNext());
            db.close();
            return results;
        }
    }

    /**
     * Video database access class
     */
    private static class VideoDAL {
        /**
         * Create videos table
         *
         * @param db Database instance
         */
        private static void initialize(SQLiteDatabase db) {
            String createVideoTable = "CREATE TABLE " + TABLE_VIDEO +
                    "(" + COLUMN_VIDEO_ID + " INTEGER PRIMARY KEY," +
                          COLUMN_VIDEO_TITLE + " TEXT UNIQUE," +
                          COLUMN_VIDEO_YEAR + " INTEGER," +
                          COLUMN_VIDEO_LOCATION + " INTEGER," +
                          COLUMN_VIDEO_RATING + " INTEGER," +
                          COLUMN_VIDEO_DESCRIPTION + " TEXT," +
                          COLUMN_VIDEO_IMDB_URL + " TEXT" +
                    ")";
            db.execSQL(createVideoTable);
       }

        /**
         * Get a video by id
         *
         * @param mgr DB Manager to use
         * @return Video (null if not found)
         */
        @Nullable
        public static Video get(int id, DBHandler mgr) {
            SQLiteDatabase db = mgr.getReadableDatabase();
            ArrayList<Video> results = getter(db, COLUMN_VIDEO_ID + " = ?", new String[]{String.valueOf(id)});
            db.close();
            if (results.size() == 0) {
                return null;
            }
            return results.get(0);
        }

        /**
         * Get a list of all videos
         *
         * @param mgr DB Manager to use
         * @return List of videos
         */
        public static ArrayList<Video> getAll(DBHandler mgr) {
            SQLiteDatabase db = mgr.getReadableDatabase();
            ArrayList<Video> results = getter(db, null, null);
            db.close();
            return results;
        }

        private static ArrayList<Video> getter(SQLiteDatabase db, String where, String[] params) {
            ArrayList<Video> results = new ArrayList<>();
            Cursor cursor;

            try {
                cursor = db.query(
                        TABLE_VIDEO,
                        new String[]{COLUMN_VIDEO_ID,
                                COLUMN_VIDEO_TITLE,
                                COLUMN_VIDEO_YEAR,
                                COLUMN_VIDEO_LOCATION,
                                COLUMN_VIDEO_RATING,
                                COLUMN_VIDEO_DESCRIPTION,
                                COLUMN_VIDEO_IMDB_URL
                        },
                        where,  // where statement
                        params, // where parameters
                        null,   // group by
                        null,   // having
                        null); // order by
            }
            catch (Exception ex) {
                db.close();
                Log.e(DBHandler.LOG_TAG, "Query error on videos table: " + ex.getMessage());
                return results;
            }

            if (!cursor.moveToFirst()) {
                return results;
            }
            do {
                Video video = new Video.Builder(
                        Integer.parseInt(cursor.getString(0)),
                        cursor.getString(1),
                        Integer.parseInt(cursor.getString(2)))
                        .location(Integer.parseInt(cursor.getString(3)))
                        .rating(Integer.parseInt(cursor.getString(4)))
                        .description(cursor.getString(5))
                        .imdbUrl(cursor.getString(6))
                        .build();
                results.add(video);
            } while(cursor.moveToNext());
            return results;
        }

    }

}
