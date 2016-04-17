package com.ronstruempf.myvideolibrary.model;

/**
 * Video library entry object
 *
 * Created by Ron on 4/17/2016.
 */
public final class Video {

    public int getId() {
        return _id;
    }

    public String getName() {
        return _name;
    }

    public int getYear() {
        return _year;
    }

    public int getLocation() {
        return _location;
    }

    public boolean isInLibrary() {
        return _location != Location.NOT_IN_LIBRARY;
    }

    public int getRating() {
        return _rating;
    }

    public String getDescription() {
        if (_desc == null) {
            return "";
        }
        return _desc;
    }

    public String getIMDbUrl() {
        if (_imdb == null) {
            return "";
        }
        return _imdb;
    }

    /*
     * Setters - immutable, so each returns a new object
     */
    public Video updateId(int id) {
        return new Builder(this).id(id).build();
    }

    /*
     * Internal components of Video class
     */
    public static class Builder {

        public Builder(int id, String name, int year) {
            _id = id;
            _name = name;
            _year = year;
        }

        public Builder(Video video) {
            this(video.getId(), video.getName(), video.getYear());
            _location = video.getLocation();
            _rating = video.getRating();
            _desc = video.getDescription();
            _imdb = video.getIMDbUrl();
        }

        public Builder id(int id) {
            _id = id;
            return this;
        }

        public Builder name(String name) {
            _name = name;
            return this;
        }

        public Builder year(int year) {
            _year = year;
            return this;
        }

        public Builder location(int location) {
            _location = location;
            return this;
        }

        public Builder rating(int rating) {
            _rating = rating;
            return this;
        }

        public Builder description(String desc) {
            _desc = desc;
            return this;
        }

        public Builder imdbUrl(String url) {
            _imdb = url;
            return this;
        }

        public Video build() {
            return new Video(this);
        }

        private int _id = 0;
        private String _name;
        private int _year;

        private int _location = Location.NOT_IN_LIBRARY;
        private int _rating = 0;
        private String _desc;
        private String _imdb;
    }

    private Video(Builder builder) {
        _id = builder._id;
        _name = builder._name;
        _year = builder._year;
        _location = builder._location;
        _rating = builder._rating;
        _desc = builder._desc;
        _imdb = builder._imdb;
    }

    /*
     * Member variables
     */
    private int _id;
    private String _name;   // No "The" at the beginning, always "..., The"
    private int _year;      // year movie was released
    private int _location;  // see source database, 0 = not in library
    private int _rating;    // 0-10, show as 0-5 stars with halves
    private String _desc;   // movie description
    private String _imdb;   // IMDb url
}
