package com.example.usergen.service.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;

import io.reactivex.rxjava3.core.Single;

public class OnlineImageResource {

    @Nullable
    private URL url;

    @Nullable
    private Bitmap content;


    public OnlineImageResource(@NonNull URL url) {

        Objects.requireNonNull(url);

        this.url = url;
    }

    public OnlineImageResource(@NonNull Bitmap bitmap) {

        Objects.requireNonNull(bitmap);

        this.content = bitmap;
    }

    @Nullable
    public URL getUrl() {
        return url;
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

    @NonNull
    public Bitmap getBitmapSync() throws IOException  {

        if (url == null && content == null) {
            throw new IllegalStateException("Invalid OnlineImageResource");
        }

        if (content == null) {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            InputStream stream = connection.getInputStream();

            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;

            content = BitmapFactory.decodeStream(stream, null, options);
        }

        return content;
    }

    @NonNull
    public Single<Bitmap> getBitmapAsync() {

        return Single.fromSupplier(this::getBitmapSync);
    }

    public boolean isLoaded() {
        return content != null;
    }

    @NonNull
    public Bitmap requireBitmap() {

        if (content == null) {
            throw new IllegalStateException("Unloaded bitmap was required");
        }

        return content;
    }
}
