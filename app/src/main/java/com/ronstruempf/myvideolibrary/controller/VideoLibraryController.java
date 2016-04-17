package com.ronstruempf.myvideolibrary.controller;

import com.ronstruempf.myvideolibrary.dal.DBHandler;
import com.ronstruempf.myvideolibrary.dal.ILocationDAL;
import com.ronstruempf.myvideolibrary.model.VideoLocationManager;

/**
 * Controller for video library app
 *
 * Created by Ron on 4/17/2016.
 */
public class VideoLibraryController {
    private VideoLocationManager _locations;

    public VideoLibraryController(ILocationDAL locMgr) {
        _locations = new VideoLocationManager(locMgr);
    }

    /**
     * Access video location manager held by the controller
     *
     * @return Location manager
     */
    public VideoLocationManager getLocationManager() {
        return _locations;
    }
}
