package com.lmiceli.habittracker.model;

/**
 * Created by lmiceli on 19/04/2016.
 */
public class Habit {

    private long id;
    private String desc;
    private boolean done;
    private double lat;
    private double lng;

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }
    // FIXME this will go in a different entity later

    public long getId() {
        return id;
    }

    void setId(long id) {
        this.id = id;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    // TODO set as 0,1 in a nice way
    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
