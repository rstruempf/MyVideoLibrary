package com.ronstruempf.myvideolibrary.model;

import java.util.ArrayList;

import com.ronstruempf.myvideolibrary.dal.ILocationDAL;

/**
 * Manager for a list of location records
 *
 * Created by Ron on 4/14/2016.
 */
public class VideoLocationManager {
    public VideoLocationManager(ILocationDAL dal) {
        _dal = dal;
        _list = _dal.getLocations();
    }

    public int count() {
        return _list.size();
    }

    public Location get(int idx) {
        if (idx < 0 || idx >= count()) {
            return null;
        }
        return _list.get(idx);
    }

    /**
     * Look up location id by name
     *
     * @param name Location name to look up
     * @return Location id or -1 if not found
     */
    public int getId(String name) {
        int idx = findName(name);
        if (idx >= 0) {
            return _list.get(idx).getId();
        }
        return -1;
    }

    /**
     * Look up Location name from location id
     *
     * @param id Id to look up
     * @return Name of location or empty string
     */
    public String getName(int id) {
        int idx = findId(id);
        if (idx >= 0) {
            return _list.get(idx).getLocation();
        }
        return "";
    }

    /**
     * Add location to list
     *
     * @param name Location to add
     * @return Location id
     */
    public int add(String name) {
        int result = _dal.addLocation(name);
        if (result > 0 && findName(name) < 0) {
            _list.add(new Location(result, name));
        }
        return result;
    }

    /**
     * Delete a location
     *
     * @param id Location id to removeLocation
     */
    public void delete(int id) {
        _dal.removeLocation(id);
        int idx;
        if ((idx=findId(id)) >= 0) {
            _list.remove(idx);
        }
    }


    private int findId(int id) {
        for (int idx=0; idx < _list.size(); idx++) {
            if (_list.get(idx).getId() == id) {
                return idx;
            }
        }
        return -1;
    }

    private int findName(String name) {
        for (int idx=0; idx < _list.size(); idx++) {
            if (_list.get(idx).getLocation().equalsIgnoreCase(name)) {
                return idx;
            }
        }
        return -1;
    }

    private ILocationDAL _dal;
    private ArrayList<Location> _list = new ArrayList<>();
}
