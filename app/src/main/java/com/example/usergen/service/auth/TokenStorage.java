package com.example.usergen.service.auth;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class TokenStorage {

    private final SharedPreferences preferences;

    private String tokenCache;

    private boolean hasCache = false;

    public TokenStorage(@NonNull Context context) {

        preferences = context.getSharedPreferences(
                "theese_token_preferences",
                Context.MODE_PRIVATE
        );
    }

    public void setToken(@Nullable String token) {
        tokenCache = token;
        hasCache = true;
        saveToken(token);
    }

    @Nullable
    public String getToken() {

        if (!hasCache) {
            tokenCache = fetchToken();
            hasCache = true;
        }

        return tokenCache;
    }

    private void saveToken(String token) {
        preferences.edit().putString("token", token).apply();
    }

    private String fetchToken() {
        return preferences.getString("token", null);
    }

}
