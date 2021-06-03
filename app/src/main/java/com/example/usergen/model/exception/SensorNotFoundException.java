package com.example.usergen.model.exception;

import androidx.annotation.NonNull;

public class SensorNotFoundException extends Exception {
    public SensorNotFoundException(@NonNull String message) {
        super(message);
    }
}
