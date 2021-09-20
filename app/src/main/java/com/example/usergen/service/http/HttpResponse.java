package com.example.usergen.service.http;

import androidx.annotation.Nullable;

import org.json.JSONObject;

public class HttpResponse {

    @Nullable
    private final JSONObject body;
    private final int status;

    public HttpResponse(@Nullable JSONObject body, int status) {
        this.body = body;
        this.status = status;
    }

    @Nullable
    public JSONObject getBody() {
        return body;
    }

    public int getStatus() {
        return status;
    }
}
