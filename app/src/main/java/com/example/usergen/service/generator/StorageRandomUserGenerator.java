package com.example.usergen.service.generator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.usergen.model.User;
import com.example.usergen.service.storage.UserStorage;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.core.Single;

public class StorageRandomUserGenerator implements RandomModelGenerator<User> {

    @NonNull
    private final List<User> users;

    @NonNull
    private final Random random;

    public StorageRandomUserGenerator(
            @NonNull UserStorage storage,
            @Nullable RandomUserGeneratorInput input
    ) {

        List<User> users = storage.listStoredUsers();

        this.users = input == null ? users : filterInput(input, users);

        random = new Random();
    }

    public StorageRandomUserGenerator(@NonNull UserStorage storage) {
        this(storage, null);
    }

    @NonNull
    @Override
    public Single<User> nextRandomModel() {

        return Single.fromSupplier(() -> {

            if (this.users.size() == 0) {
                throw emptyException();
            } else {
                return users.get(Math.abs(random.nextInt() % this.users.size()));
            }

        });
    }


    @Override
    @NonNull
    public Single<List<User>> nextModels(int limit) {
        return Single.fromSupplier(() -> {

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
