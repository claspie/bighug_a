package com.teleclub.rajatalk.model;

public class AccessNumber {
    private String address;
    private String number;

    public AccessNumber(String address, String number) {
        this.address = address;
        this.number = number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
