package com.ronstruempf.myvideolibrary.model;

import com.ronstruempf.myvideolibrary.dal.IVideoDAL;

import java.util.ArrayList;

/**
 * Class for managing video information
 *
 * Created by Ron on 4/24/2016.
 */
public class VideoManager {
    public VideoManager(IVideoDAL dal) {
        _dal = dal;
        _cache = null;
    }

    /**
     * Get count of items in cache
     *
     * @return Count of cache list
     */
    public int cacheCount() {
        if (_cache == null) {
            return 0;
        }
        return _cache.size();
    }

    /**
     * Get item from cache by index
     *
     * @param idx Index to get
     * @return Video at index
     */
    public Video getCached(int idx) {
        if (idx < 0 || idx >= cacheCount()) {
            return null;
        }
        return _cache.get(idx);
    }

    /**
     * Cache a list of all videos
     */
    public void cacheAll() {
        _cache = getAll();
    }

    /**
     * Look up Video by id
     *
     * @param id Id to look up
     * @return Video for id given
     */
    public Video getVideo(int id) {
        int idx = findId(id);
        if (idx >= 0) {
            return _cache.get(idx);
        }
        return _dal.getVideo(id);
    }

    /**
     * Set the cached list of videos
     *
     * @param list List to set
     */
    public void setCache(ArrayList<Video> list) {
        _cache = list;
    }

    /**
     * Get cached video list
     *
     * @return Video list
     */
    public ArrayList<Video> getCache() {
        return _cache;
    }

    /**
     * Get a list of all videos
     *
     * @return Video list
     */
    public ArrayList<Video> getAll() {
        ArrayList<Video> result = _dal.getAll();
        return result;
    }

    private int findId(int id) {
        for (int idx=0; idx < cacheCount(); idx++) {
            if (_cache.get(idx).getId() == id) {
                return idx;
            }
        }
        return -1;
    }

    private IVideoDAL _dal;
    private ArrayList<Video> _cache = new ArrayList<>();

}
