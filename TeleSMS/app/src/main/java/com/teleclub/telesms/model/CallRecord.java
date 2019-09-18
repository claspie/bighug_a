package com.teleclub.telesms.model;

public class CallRecord {
    private int id;
    private String phoneNumber;
    private String accessNumber;
    private String calledTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setAccessNumber(String accessNumber) {
        this.accessNumber = accessNumber;
    }

    public String getAccessNumber() {
        return accessNumber;
    }

    public String getCalledTime() {
        return calledTime;
    }

    public void setCalledTime(String calledTime) {
        this.calledTime = calledTime;
    }
}
