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

    private static final int DATABASE_VERSION = 3;
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
        // if a newer version is installed, delete the previous tables and recreate
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOCATION);
        LocationDAL.initialize(db);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIDEO);
        VideoDAL.initialize(db);
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
            add("Amazon (Jess)", db);
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
            try {
                result = (int) db.insert(TABLE_LOCATION, null, values);
            }
            catch (Exception ex) {
                Log.e(DBHandler.LOG_TAG, "LocationDAL.add() Query error: " + ex.getMessage());
                return 0;
            }
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
            add(db, "8mm I",  1999, "DVD", 0, "", "");
            add(db, "8mm II",  2005, "DVD", 0, "", "");
            add(db, "9",  2009, "DVD", 0, "", "");
            add(db, "12:01",  1993, "DVD", 0, "World is in a one day time loop, and one guy knows that it is happening", "");
            add(db, "13th Warrior, The",  1999, "DVD", 0, "Antonio Banderas is an Arabic man who joins a group of 12 Viking warriors to help fight a terrible enemy", "");
            add(db, "18 Again!",  1988, "DVD", 0, "George Burns, 81, switches places with his 18 year old grandson after a car accident", "");
            add(db, "47 Ronin",  2013, "Digital", 0, "Keanu Reeves is an outsider who becomes an outcast Samuri, a Ronin, along with others, to defend the name of their leader", "");
            add(db, "50 First Dates",  2004, "DVD", 0, "Drew Barymore has a condition where she forgets everything every night.  Adam Sandler falls in love with her", "");
            add(db, "300",  2007, "Digital", 0, "300 naked Spartans battle a huge army led by someone so powerful, his followers believe he is a god", "");
            add(db, "Abysss, The",  1989, "DVD", 0, "A submersible oil rig habitat is used to look into a lost submarine and finds underwater aliens", "");
            add(db, "Addicted to Love",  1997, "DVD", 0, "", "");
            add(db, "Air America",  1990, "DVD", 0, "Mel Gibson is a member of a civilian air support team in Laos during the Vietnam war", "");
            add(db, "Air Force One",  1997, "DVD", 0, "Harrison Ford is the U.S. President when Air Force One is taken over by terrorists", "");
            add(db, "Airplane!",  1980, "DVD", 0, ":)", "");
            add(db, "Aladdin",  1992, "DVD", 0, "Robin Williams is a genie", "");
            add(db, "After Earth",  2013, "Digital", 0, "Will Smith's son, Jaden, crosses a hostile Earth to save his father", "");
            add(db, "Aliens in the Attic",  2009, "DVD", 0, "", "");
            add(db, "All About Steve",  2009, "DVD", 0, "", "");
            add(db, "All Dogs Go to Heaven",  1989, "DVD", 0, "", "");
            add(db, "Almost an Angel",  1990, "DVD", 0, "", "");
            add(db, "America's Sweethearts",  2001, "DVD", 0, "", "");
            add(db, "American Beauty",  1999, "DVD", 0, "", "");
            add(db, "American Pie",  1999, "DVD", 0, "", "");
            add(db, "American Tail, An 1-3",  1986, "DVD", 0, "", "");
            add(db, "American Wedding, An",  2003, "DVD", 0, "", "");
            add(db, "Anger Management",  2003, "DVD", 0, "", "");
            add(db, "Anne Frank",  2001, "DVD", 0, "", "");
            add(db, "Apocalypse Now",  1979, "DVD", 0, "", "");
            add(db, "Assault on Precinct 13",  2005, "DVD", 0, "", "");
            add(db, "Austin Powers In Goldmember",  2002, "Digital", 0, "", "");
            add(db, "Avatar",  2009, "Digital", 0, "Blue People", "");
        }

        private static int locationId(String loc) {
            switch (loc) {
                case "DVD":
                    return 1;
                case "Digital":
                    return 2;
                case "Amazon":
                    return 3;
                case "Vudu":
                    return 4;
                default:
                    return 0;
            }
        }

        private static int add(SQLiteDatabase db, String title, int year, String loc, int rating, String description, String imdb) {
            int result;
            int location = locationId(loc);

            // if location already exists, look up id
            // TODO: See if title already exists
//            if ((result = getId(title, db)) > 0) {
//                return result;
//            }
            // otherwise insert new row
            ContentValues values = new ContentValues();
            values.put(COLUMN_VIDEO_TITLE, title );
            values.put(COLUMN_VIDEO_YEAR, year );
            values.put(COLUMN_VIDEO_LOCATION, location );
            values.put(COLUMN_VIDEO_RATING, rating );
            values.put(COLUMN_VIDEO_DESCRIPTION, description );
            values.put(COLUMN_VIDEO_IMDB_URL, imdb );
            try {
                result = (int) db.insert(TABLE_VIDEO, null, values);
            }
            catch (Exception ex) {
                Log.e(DBHandler.LOG_TAG, "VideoDAL.add() Query error: " + ex.getMessage());
                return 0;
            }
            return result;
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
