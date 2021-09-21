package com.example.usergen.service.http;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.util.Objects;

public class HttpResponse {

    @Nullable
    private final JSONObject body;

    private final int status;

    @Nullable
    private final String error;


    public HttpResponse(@Nullable JSONObject body, int status) {
        this(body,status, null);
    }

    public HttpResponse(@Nullable JSONObject body, int status, @Nullable String error) {
        this.body = body;
        this.status = status;
        this.error = error;
    }

    @Nullable
    public JSONObject getBody() {
        return body;
    }

    public int getStatus() {
        return status;
    }

    @Nullable
    public String getError() {
        return error;
    }

    @NonNull
    public JSONObject requireBody() {
        return Objects.requireNonNull(body, "Unavailable body requested");
    }

    @NonNull
    public HttpResponse requireOk() {
        if (status / 100 != 2) {

            if (error == null) {
                throw new HttpException(status);
            }
            else {
                throw new HttpException(status, error);
            }
        }

        return this;
    }

}
