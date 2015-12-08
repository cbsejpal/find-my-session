package com.example.wlanhelper;

public class ReadError extends Exception {
    private static final long serialVersionUID = 42L; // for serialization

    public ReadError() {
    }

    public ReadError(String details) {
        super(details);
    }

    public ReadError(String details, Throwable throwable) {
        super(details, throwable);
    }
}
