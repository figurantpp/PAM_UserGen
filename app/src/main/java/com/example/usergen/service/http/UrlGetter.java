package com.example.usergen.service.http;

import androidx.annotation.NonNull;

import java.net.MalformedURLException;
import java.net.URL;

public class UrlGetter {

    @NonNull
    private final String base;

    public UrlGetter(@NonNull String base) {
        this.base = base;
    }

    @NonNull
    public URL from(@NonNull String endpoint) {

        try {
            return new URL(base + endpoint);
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }
}
