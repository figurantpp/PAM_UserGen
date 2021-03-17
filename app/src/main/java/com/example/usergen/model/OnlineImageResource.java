package com.example.usergen.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.net.URL;

public class OnlineImageResource {

    private URL url;

    public OnlineImageResource(@Nullable URL url) {
        this.url = url;
    }

    @Nullable
    public URL getUrl() {
        return url;
    }

    public void setUrl(@Nullable URL url) {
        this.url = url;
    }
}
