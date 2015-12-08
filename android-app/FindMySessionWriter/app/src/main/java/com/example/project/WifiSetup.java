package com.example.wlanhelper;

public class WifiSetup extends Exception {
    private static final long serialVersionUID = 42L; // for serialization

    public WifiSetup() {
    }

    public WifiSetup(String details) {
        super(details);
    }

    public WifiSetup(String details, Throwable throwable) {
        super(details, throwable);
    }
}
