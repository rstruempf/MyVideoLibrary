package com.ronstruempf.myvideolibrary;

import org.junit.Test;

import java.util.ArrayList;

import com.ronstruempf.myvideolibrary.dal.ILocationDAL;
import com.ronstruempf.myvideolibrary.model.Location;
import com.ronstruempf.myvideolibrary.model.VideoLocationManager;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class LocationManagerTest {

    private VideoLocationManager _mgr = new VideoLocationManager(new TestLocationDAL());

    @Test
    public void whenCreatedCountShouldBe4() {
        assertEquals(4, _mgr.count());
    }

    @Test
    public void whenCreatedGet0ShouldBeId1LocationDVD() {
        Location loc = _mgr.get(0);
        assertNotNull("Location should not be null", loc);
        assertEquals(1, loc.getId());
        assertEquals("DVD", loc.getLocation());
    }

    @Test
    public void whenCreatedGet5ShouldBeNULL() {
        Location loc = _mgr.get(5);
        assertNull("Location should be null", loc);
    }

    @Test
    public void whenCreatedGetMinus1ShouldBeNULL() {
        Location loc = _mgr.get(-1);
        assertNull("Location should be null", loc);
    }

    @Test
    public void whenCreatedGetIdDigitalShouldReturn2() {
        assertEquals(2, _mgr.getId("Digital"));
    }

    @Test
    public void whenCreatedGetIdJunkShouldReturnMinus1() {
        assertEquals(-1, _mgr.getId("Junk"));
    }

    @Test
    public void whenCreatedGetName3ShouldReturnAmazon() {
        assertEquals("Amazon", _mgr.getName(3));
    }

    @Test
    public void whenCreatedGetName5ShouldReturnEmptyString() {
        assertEquals("", _mgr.getName(5));
    }

    @Test
    public void whenAddNewShouldReturnId5AndHaveCount5() {
        assertEquals(5, _mgr.add("Test"));
        assertEquals(5, _mgr.count());
    }

    @Test
    public void whenAddNewShouldFindName() {
        _mgr.add("Test");
        assertEquals(5, _mgr.getId("Test"));
    }

    @Test
    public void whenAddExistingShouldReturnId1AndHaveCount4() {
        assertEquals(1, _mgr.add("DVD"));
        assertEquals(4, _mgr.count());
    }

    @Test
    public void whenDeleteExistingShouldHaveCount3() {
        _mgr.delete(2);
        assertEquals(3, _mgr.count());
    }

    @Test
    public void whenDeleteNonExistingShouldHaveCount4() {
        _mgr.delete(6);
        assertEquals(4, _mgr.count());
    }


    private class TestLocationDAL implements ILocationDAL {
        private ArrayList<Location> _list = new ArrayList<>();

        public TestLocationDAL() {
            _list.add(new Location(1,"DVD"));
            _list.add(new Location(2,"Digital"));
            _list.add(new Location(3,"Amazon"));
            _list.add(new Location(4,"Vudu"));
        }

        @Override
        public ArrayList<Location> getLocations() {
            return _list;
        }

        @Override
        public int addLocation(String location) {
            if (location.length() == 0) {
                return -1;
            }
            int result = findLocation(location);
            if (result < 1) {
                result = maxLocationId() + 1;
                _list.add(new Location(result,location));
            }
            return result;
        }

        @Override
        public boolean removeLocation(int id) {
            return true;
        }

        private int findLocation(String name) {
            for (Location loc : _list) {
                if (loc.getLocation().equalsIgnoreCase(name)) {
                    return loc.getId();
                }
            }
            return -1;
        }

        private int maxLocationId() {
            int result = 0;
            for (Location loc : _list) {
                if (loc.getId() > result) {
                    result = loc.getId();
                }
            }
            return result;
        }
    }

}
