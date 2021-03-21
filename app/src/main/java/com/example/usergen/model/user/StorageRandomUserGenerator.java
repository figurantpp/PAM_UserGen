package com.example.usergen.model.user;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.usergen.BuildConfig;
import com.example.usergen.model.interfaces.RandomModelGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StorageRandomUserGenerator implements RandomModelGenerator<User> {

    @NonNull
    private final List<User> users;

    @NonNull
    private final List<Boolean> selectedIndexes;

    @NonNull
    private final Random random;

    @NonNull
    private final ExecutorService executor;

    public StorageRandomUserGenerator(@NonNull Context context) {

        this.users = new UserStorage(context).listStoredUsers();

        selectedIndexes = new ArrayList<>(users.size());

        random = new Random();

        this.executor = Executors.newSingleThreadExecutor();
    }

    @NonNull
    @Override
    public Future<User> nextRandomModel() {

        return executor.submit(() -> {

            if (BuildConfig.DEBUG && users.size() != selectedIndexes.size()) {
                throw new AssertionError("Assertion failed");
            }

            int count = 0;
            int index;

            while (selectedIndexes.get(index = random.nextInt()) && count != users.size()) {

                selectedIndexes.set(index, true);

                return users.get(index);
            }

            return users.get(random.nextInt());

        });
    }
}
