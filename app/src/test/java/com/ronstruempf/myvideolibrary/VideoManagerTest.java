package com.ronstruempf.myvideolibrary;

import com.ronstruempf.myvideolibrary.dal.IVideoDAL;
import com.ronstruempf.myvideolibrary.model.Video;
import com.ronstruempf.myvideolibrary.model.VideoManager;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test VideoManager
 *
 * Created by Ron on 4/24/2016.
 */
public class VideoManagerTest {

    private VideoManager _mgr = new VideoManager(new TestVideoDAL());

    @Test
    public void whenCreatedCountShouldBe5() {
        _mgr.cacheAll();
        assertEquals(5, _mgr.cacheCount());
    }

    @Test
    public void whenCreatedGet0ShouldBeId1() {
        _mgr.cacheAll();
        Video video = _mgr.getCached(0);
        assertNotNull("Video should not be null", video);
        assertEquals(1, video.getId());
        assertEquals("H.E.A.T.", video.getName());
    }

    @Test
    public void whenCreatedGet6ShouldBeNULL() {
        _mgr.cacheAll();
        Video video = _mgr.getCached(6);
        assertNull("Video should be null", video);
    }

    @Test
    public void whenCreatedGetMinus1ShouldBeNULL() {
        _mgr.cacheAll();
        Video video = _mgr.getCached(-1);
        assertNull("Video should be null", video);
    }


    private class TestVideoDAL implements IVideoDAL {
        private ArrayList<Video> _list = new ArrayList<>();

        public TestVideoDAL() {
            _list.add(new Video.Builder(1, "H.E.A.T.", 1995).location(1).build());
            _list.add(new Video.Builder(2, "18 Again!", 1988).location(1).build());
            _list.add(new Video.Builder(3, "47 Ronin", 2013).location(2).build());
            _list.add(new Video.Builder(4, "Divergent", 2014).location(3).build());
            _list.add(new Video.Builder(5, "Fantastic Four", 2005).location(2).build());
        }

        @Override
        public Video getVideo(int id) {
            return null;
        }

        @Override
        public ArrayList<Video> getAll() {
            return _list;
        }

        private int maxId() {
            int result = 0;
            for (Video video : _list) {
                if (video.getId() > result) {
                    result = video.getId();
                }
            }
            return result;
        }
    }

}
