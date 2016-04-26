package com.ronstruempf.myvideolibrary.dal;

import com.ronstruempf.myvideolibrary.model.Video;

import java.util.ArrayList;

/**
 * Video DB interface
 *
 * Created by Ron on 4/24/2016.
 */
public interface IVideoDAL {

    /**
     * Look up a video for a given video id
     *
     * @param id Video id
     * @return Video or null if not found
     */
    Video getVideo(int id);

    /**
     * Get all videos in database
     *
     * @return List of videos
     */
    ArrayList<Video> getAll();

}
