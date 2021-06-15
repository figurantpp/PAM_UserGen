package com.example.usergen.model.user.generator;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.usergen.model.exception.NoNetworkException;
import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.model.user.User;
import com.example.usergen.model.user.UserStorage;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RandomUserGeneratorResolver {

    @NonNull
    public static RandomModelGenerator<User> resolveUserGenerator(
            @NonNull Context context,
            @NonNull RandomUserGeneratorInput input
    ) {
        return resolveUserGenerator(context, input, Executors.newSingleThreadExecutor());
    }

    @NonNull
    public static RandomModelGenerator<User> resolveUserGenerator(
            @NonNull Context context,
            @NonNull RandomUserGeneratorInput input,
            @NonNull ExecutorService service
            ) {
        try {
            return new NetworkRandomUserGenerator(context, input, service);
        } catch (NoNetworkException e) {
            return new StorageRandomUserGenerator(new UserStorage(context), input, service);
        }
    }
}
