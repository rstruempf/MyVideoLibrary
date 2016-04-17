package edu.westga.myvideolibrary.model;

/**
 * Video location records
 *
 * Created by Ron on 4/13/2016.
 */
public class Location {

    public static final int NOT_IN_LIBRARY = 0;

    public Location(int id, String location) {
        this._id = id;
        this._location = location;
    }

    public int getId() {
        return this._id;
    }

    public String getLocation() {
        return this._location;
    }

    private int _id;
    private String _location;
}
