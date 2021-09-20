package com.example.usergen.service.auth;

import androidx.annotation.NonNull;

import io.reactivex.rxjava3.core.Single;

public class AuthRepository {

    @NonNull
    private final AuthApi api;

    @NonNull
    private final TokenStorage tokenStorage;

    public AuthRepository(
            @NonNull AuthApi api,
            @NonNull TokenStorage tokenStorage
    ) {
        this.api = api;
        this.tokenStorage = tokenStorage;
    }

    @NonNull
    public Single<Boolean> login(@NonNull String username, @NonNull String password) {

        return api.login(username, password)
                .map(value -> {
                    tokenStorage.setToken(value);
                    return true;
                })
                .switchIfEmpty(Single.fromSupplier(() -> {
                    tokenStorage.setToken(null);
                    return false;
                }));
    }

    @NonNull
    public Single<Boolean> register(@NonNull String username, @NonNull String password) {

        return api.register(username, password);
    }

}
