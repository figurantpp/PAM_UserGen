package com.example.usergen.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

public class OnlineImageResource {

    @Nullable
    private URL url;

    @Nullable
    private Bitmap content;

    public OnlineImageResource(@Nullable URL url) {
        this.url = url;
    }

    public OnlineImageResource(@NonNull Bitmap bitmap) {
        this.content = bitmap;
    }

    @Nullable
    public URL getUrl() {
        return url;
    }

    public void setUrl(@Nullable URL url) {
        this.url = url;
    }

    @Override
    public boolean equals(Object other) {

        if (this == other) {
            return true;
        }

        if (other == null || getClass() != other.getClass()) {
            return false;
        }

        OnlineImageResource that = (OnlineImageResource) other;

        return Objects.equals(url, that.url) || Objects.equals(content, that.content) ||
                (content != null && content.sameAs(that.content));
    }

    @Override
    public int hashCode() {
        return Objects.hash(url, content);
    }

    public void setContent(@Nullable Bitmap content) {
        this.content = content;
    }

    @Nullable
    public Bitmap getBitmap() throws IOException  {

        if (content == null && url != null) {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            content = BitmapFactory.decodeStream(stream);
        }

        return content;
    }
}
