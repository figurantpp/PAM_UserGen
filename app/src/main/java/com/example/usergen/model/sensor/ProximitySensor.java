package com.example.usergen.model.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;

import com.example.usergen.model.exception.SensorNotFoundException;

import java.util.function.Consumer;

public class ProximitySensor {

    @NonNull
    private final Consumer<Boolean> updateListener;

    public ProximitySensor(
            @NonNull Context context,
            @NonNull Consumer<Boolean> updateListener
    ) throws SensorNotFoundException {

        this.updateListener = updateListener;

    }


    public void emit(boolean close) {
        updateListener.accept(close);
    }

}
