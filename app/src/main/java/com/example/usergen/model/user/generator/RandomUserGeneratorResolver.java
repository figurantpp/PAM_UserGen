package com.example.usergen.model.user.generator;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.usergen.model.exception.NoNetworkException;
import com.example.usergen.model.interfaces.RandomModelGenerator;
import com.example.usergen.model.user.User;
import com.example.usergen.model.user.UserStorage;

public class RandomUserGeneratorResolver {

    @NonNull
    public static RandomModelGenerator<User> resolveUserGenerator(
            @NonNull Context context,
            @NonNull RandomUserGeneratorInput input) {
        try {
            return new NetworkRandomUserGenerator(context, input);
        } catch (NoNetworkException e) {
            return new StorageRandomUserGenerator(new UserStorage(context), input);
        }
    }
}
