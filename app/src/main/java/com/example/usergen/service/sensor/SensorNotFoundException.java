package com.example.usergen.service.sensor;

import androidx.annotation.NonNull;

public class SensorNotFoundException extends Exception {
    public SensorNotFoundException(@NonNull String message) {
        super(message);
    }
}
