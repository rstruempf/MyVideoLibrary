package com.ronstruempf.myvideolibrary.controller;

import com.ronstruempf.myvideolibrary.dal.DBHandler;
import com.ronstruempf.myvideolibrary.dal.ILocationDAL;
import com.ronstruempf.myvideolibrary.dal.IVideoDAL;
import com.ronstruempf.myvideolibrary.model.Video;
import com.ronstruempf.myvideolibrary.model.VideoLocationManager;
import com.ronstruempf.myvideolibrary.model.VideoManager;

/**
 * Controller for video library app
 *
 * Created by Ron on 4/17/2016.
 */
public class VideoLibraryController {
    private VideoLocationManager _locations;
    private VideoManager _videos;

    public VideoLibraryController(ILocationDAL locMgr, IVideoDAL videoMgr) {
        _locations = new VideoLocationManager(locMgr);
        _videos = new VideoManager(videoMgr);
    }

    /**
     * Access video location manager held by the controller
     *
     * @return Location manager
     */
    public VideoLocationManager getLocationManager() {
        return _locations;
    }

    public VideoManager getVideoManager() { return _videos; }

}
