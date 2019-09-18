package com.teleclub.rabbtel.model;

import java.util.Date;

public class CallLogData {
    private int id;
    private String accessNumber;
    private String dialNumber;
    private Date dateCalled;
    private boolean favorited;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAccessNumber() {
        return accessNumber;
    }

    public void setAccessNumber(String accessNumber) {
        this.accessNumber = accessNumber;
    }

    public String getDialNumber() {
        return dialNumber;
    }

    public void setDialNumber(String dialNumber) {
        this.dialNumber = dialNumber;
    }

    public Date getDateCalled() {
        return dateCalled;
    }

    public void setDateCalled(Date dateCalled) {
        this.dateCalled = dateCalled;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
