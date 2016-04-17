package edu.westga.myvideolibrary.controller;

import edu.westga.myvideolibrary.dal.DBHandler;
import edu.westga.myvideolibrary.dal.ILocationDAL;
import edu.westga.myvideolibrary.model.VideoLocationManager;

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
