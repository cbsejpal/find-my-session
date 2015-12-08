package com.example.wlanhelper;

public class WriteError extends Exception {
    private static final long serialVersionUID = 42L; // for serialization

    public WriteError() {
    }

    public WriteError(String details) {
        super(details);
    }

    public WriteError(String details, Throwable throwable) {
        super(details, throwable);
    }
}
