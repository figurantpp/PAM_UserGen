package com.example.usergen;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.usergen.service.auth.AuthApi;
import com.example.usergen.service.auth.AuthRepository;
import com.example.usergen.service.auth.TokenStorage;
import com.example.usergen.service.http.HttpHandler;
import com.example.usergen.service.http.UrlProvider;
import com.example.usergen.service.settings.SettingsRepository;

public class UsergenApplication extends Application {

    public static final String API_URL = "http://192.168.0.10:5000";

    private TokenStorage tokenStorage;

    @NonNull
    private TokenStorage getTokenStorage() {
        if (tokenStorage == null) {
            tokenStorage = new TokenStorage(this);
        }

        return tokenStorage;
    }

    @NonNull
    private HttpHandler getHttpHandler() {
        return new HttpHandler(
                new UrlProvider(API_URL),
                getTokenStorage()
        );
    }

    @NonNull
    public AuthRepository getAuthRepository() {
        return new AuthRepository(
                new AuthApi(getHttpHandler()),
                getTokenStorage()
        );
    }


    @NonNull
    public SettingsRepository getSettingsRepository() {
        return new SettingsRepository(
                getHttpHandler()
        );
    }

    @NonNull
    public static UsergenApplication from(@NonNull Activity activity) {
        return (UsergenApplication) (activity.getApplication());
    }

    @NonNull
    public static UsergenApplication from(@NonNull Fragment fragment) {
        return from(fragment.requireActivity());
    }

}
