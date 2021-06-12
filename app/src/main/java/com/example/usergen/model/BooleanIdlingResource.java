package com.example.usergen.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class BooleanIdlingResource implements IdlingResource {

    @Nullable
    private final AtomicBoolean idle = new AtomicBoolean(false);

    @Nullable
    private ResourceCallback callback;

    @Override
    @NonNull
    public String getName() {
        return BooleanIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        return idle.get();
    }

    @Override
    public void registerIdleTransitionCallback(@Nullable ResourceCallback callback) {
        this.callback = callback;
    }

    public void setIdle() {
        idle.set(true);

        if (callback != null) {
            callback.onTransitionToIdle();
        }
    }

    public void setNotIdle() {
        idle.set(false);
    }
}
