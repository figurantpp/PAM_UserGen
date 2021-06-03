package com.example.usergen.model.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.usergen.model.interfaces.RandomModelGenerator;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StorageRandomUserGenerator implements RandomModelGenerator<User> {

    @NonNull
    private final List<User> users;

    @NonNull
    private final Random random;

    @NonNull
    private final ExecutorService executor;

    public StorageRandomUserGenerator(
            @NonNull UserStorage storage,
            @Nullable RandomUserGeneratorInput input,
            @NonNull ExecutorService executor
    ) {

        List<User> users = storage.listStoredUsers();

        this.users = input == null ? users : filterInput(input, users);

        random = new Random();

        this.executor = executor;
    }

    public StorageRandomUserGenerator(
            @NonNull UserStorage storage,
            @Nullable RandomUserGeneratorInput input
    ) {
        this(storage, input, Executors.newSingleThreadExecutor());
    }

    public StorageRandomUserGenerator(@NonNull UserStorage storage) {
        this(storage, null);
    }

    @NonNull
    @Override
    public Future<User> nextRandomModel() {

        return executor.submit(() -> {

            if (this.users.size() == 0) {
                throw emptyException();
            } else {
                return users.get(Math.abs(random.nextInt() % this.users.size()));
            }

        });
    }


    @Override
    @NonNull
    public Future<List<User>> nextModels(int limit) {
        return executor.submit(() -> {

            if (this.users.size() == 0) {
                throw emptyException();
            } else {

                return random.ints(limit, 0, users.size())
                        .mapToObj(users::get)
                        .collect(Collectors.toList());
            }
        });
    }

    private List<User> filterInput(@NonNull RandomUserGeneratorInput input, @NonNull List<User> users) {

        Predicate<User> sameGender = user -> user.getGender().equals(input.getGender());

        Predicate<User> sameNationality = user -> input.getNationality() == null ||
                user.getNationality().equals(input.getNationality());

        return users.stream().filter(user -> sameGender.test(user) && sameNationality.test(user)

        ).collect(Collectors.toList());
    }
    private NoSuchElementException emptyException() {
        return new NoSuchElementException("Empty database storage, no random users to generate");
    }

}
