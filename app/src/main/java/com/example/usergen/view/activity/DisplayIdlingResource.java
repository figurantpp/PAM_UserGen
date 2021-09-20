package com.example.usergen.view.activity;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import com.example.usergen.view.activity.single_user.ShowUserActivity;

import java.util.concurrent.atomic.AtomicBoolean;

@SuppressLint("VisibleForTests")
public class DisplayIdlingResource implements IdlingResource, ShowUserActivity.DisplayListener {

    @NonNull
    private final AtomicBoolean idle = new AtomicBoolean(false);

    @Nullable
    private IdlingResource.ResourceCallback callback;

    @NonNull
    @Override
    public String getName() {
        return DisplayIdlingResource.class.getSimpleName();
    }

    @Override
    public boolean isIdleNow() {
        return idle.get();
    }

    @Override
    public void registerIdleTransitionCallback(@NonNull ResourceCallback callback) {
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
