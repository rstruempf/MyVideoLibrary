package com.ronstruempf.myvideolibrary.controller;

import com.ronstruempf.myvideolibrary.dal.DBHandler;
import com.ronstruempf.myvideolibrary.dal.ILocationDAL;
import com.ronstruempf.myvideolibrary.model.Video;
import com.ronstruempf.myvideolibrary.model.VideoLocationManager;

/**
 * Controller for video library app
 *
 * Created by Ron on 4/17/2016.
 */
public class VideoLibraryController {
    private VideoLocationManager _locations;
    // TODO: Add VideoManager here and provide access to that functionality
    // TODO: Add IVideoDAL to constructor and use in allocating VideoManager

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

    public Video getVideo(int videoId) {
        // TODO: Look up video from database
//        return new Video.Builder(videoId, "H.E.A.T.", 1995)
//                                    .location(1)
//                                    .rating(8)
//                                    .description("Pacino & De Niro, super cop vs. super criminal")
//                                    .imdbUrl("http://www.imdb.com/title/tt0113277/?ref_=nv_sr_3")
//                                    .build();
        return new Video.Builder(videoId, "Fantastic Four", 2005)
                .location(2)
                .rating(8)
                .description("Jessica Alba plays Susan Storm in classic Fantastic Four movie that shows their origins")
                .imdbUrl("http://www.imdb.com/title/tt0120667/?ref_=nv_sr_4")
                .build();
    }
}
