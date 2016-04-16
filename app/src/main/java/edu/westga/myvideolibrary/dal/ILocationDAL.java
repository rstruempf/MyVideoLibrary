package edu.westga.myvideolibrary.dal;

import java.util.ArrayList;

import edu.westga.myvideolibrary.model.Location;

/**
 * Interface for location DB layer
 *
 * Created by Ron on 4/14/2016.
 */
public interface ILocationDAL {

    /**
     * Get a list of all locations
     *
     * @return List of locations
     */
    ArrayList<Location> getLocations();

    /**
     * Add a location to the database
     *
     * @param location Location to add
     * @return Location id
     */
    int addLocation(String location);

    /**
     * Delete a location by id
     *
     * @param id Location id
     */
    boolean removeLocation(int id);
}
