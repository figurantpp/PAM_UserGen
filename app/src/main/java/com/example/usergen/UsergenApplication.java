package com.example.usergen;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.usergen.service.AuthApi;
import com.example.usergen.service.AuthRepository;
import com.example.usergen.service.TokenStorage;
import com.example.usergen.service.http.HttpHandler;
import com.example.usergen.service.http.UrlGetter;

public class UsergenApplication extends Application {

    public static final String API_URL = "http://192.168.0.10:5000";

    private HttpHandler httpHandler;

    @NonNull
    private HttpHandler getHttpHandler() {
        if (httpHandler == null) {
            httpHandler = new HttpHandler(new UrlGetter(API_URL));
        }

        return httpHandler;
    }

    private AuthRepository authRepository;

    @NonNull
    public AuthRepository getAuthRepository() {

        if (authRepository == null) {
            authRepository = new AuthRepository(
                    new AuthApi(getHttpHandler()),
                    new TokenStorage(this)
            );
        }

        return authRepository;
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
