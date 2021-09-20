package com.example.usergen.service.sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.usergen.util.Tags;

import java.util.function.Consumer;

public class ProximitySensor implements AutoCloseable {

    @NonNull
    private final Consumer<Boolean> updateListener;

    @NonNull
    private final SensorManager manager;

    @NonNull
    private final Sensor proximitySensor;

    @NonNull
    private final SensorEventListener sensorListener;

    public ProximitySensor(
            @NonNull Context context,
            @NonNull Consumer<Boolean> updateListener
    ) throws SensorNotFoundException {

        this.updateListener = updateListener;

        this.manager = getSensorManager(context);

        this.proximitySensor = getSensor();

        this.sensorListener = getEventListener();

        manager.registerListener(
                this.sensorListener,
                proximitySensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    @NonNull
    private SensorEventListener getEventListener() {
        return new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                Log.d(Tags.DEBUG, "onSensorChanged: range: " + event.values[0] + "max" +
                        proximitySensor.getMaximumRange());

                emit(event.values[0] < proximitySensor.getMaximumRange());
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}
        };
    }

    @NonNull
    private SensorManager getSensorManager(@NonNull Context context) throws SensorNotFoundException {
        SensorManager manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        if (manager == null) {
            throw new SensorNotFoundException("No sensor manager available");
        }

        return manager;
    }

    @NonNull
    private Sensor getSensor() throws SensorNotFoundException {
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        if (sensor == null) {
            throw new SensorNotFoundException("No proximity sensor available");
        }

        return sensor;
    }

    public void emit(boolean close) {
        updateListener.accept(close);
    }

    @Override
    public void close() {

        this.manager.unregisterListener(this.sensorListener, proximitySensor);

    }
}
