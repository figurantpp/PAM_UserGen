package com.example.usergen.service.generator;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.usergen.model.User;
import com.example.usergen.service.storage.UserStorage;
import com.example.usergen.util.Tags;

public class RandomUserGeneratorResolver {

    @NonNull
    public static RandomModelGenerator<User> resolveUserGenerator(
            @NonNull Context context,
            @NonNull RandomUserGeneratorInput input) {
        try {
            return new NetworkRandomUserGenerator(context, input);
        } catch (NoNetworkException e) {
            Log.e(Tags.ERROR, "Failed to connect to network");
            return new StorageRandomUserGenerator(new UserStorage(context), input);
        }
    }
}
