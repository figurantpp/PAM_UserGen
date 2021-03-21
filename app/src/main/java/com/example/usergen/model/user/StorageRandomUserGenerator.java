package com.example.usergen.model.user;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.usergen.BuildConfig;
import com.example.usergen.model.interfaces.RandomModelGenerator;

import java.nio.channels.NoConnectionPendingException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StorageRandomUserGenerator implements RandomModelGenerator<User> {

    @NonNull
    private final List<User> users;

    @NonNull
    private final Random random;

    @NonNull
    private final ExecutorService executor;

    public StorageRandomUserGenerator(@NonNull UserStorage storage) {

        this.users = storage.listStoredUsers();

        random = new Random();

        this.executor = Executors.newSingleThreadExecutor();
    }

    @NonNull
    @Override
    public Future<User> nextRandomModel() {

        return executor.submit(() -> {

            if (this.users.size() == 0) {
                throw new NoSuchElementException("Empty Database Storage; No Random User to Generate");
            }
            else {
                return users.get(random.nextInt() % this.users.size());
            }

        });
    }
}
