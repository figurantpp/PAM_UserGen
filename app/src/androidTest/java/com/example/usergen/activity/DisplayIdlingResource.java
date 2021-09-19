package com.example.usergen.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import com.example.usergen.activity.single_user.ShowUserActivity;

import java.util.concurrent.atomic.AtomicBoolean;

public class DisplayIdlingResource implements IdlingResource, ShowUserActivity.DisplayListener {

    @NonNull
    private final AtomicBoolean idle = new AtomicBoolean(false);

    @Nullable
    private IdlingResource.ResourceCallback callback;

    @Override
    public String getName() {
        return DisplayIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        return idle.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    @Override
    public void onDisplay() {
        idle.set(true);

        if (callback != null) {
            callback.onTransitionToIdle();
        }
    }
}
