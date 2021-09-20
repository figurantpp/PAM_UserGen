package com.example.usergen.service.http;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

public class HttpHandler {

    @NonNull
    private final UrlGetter url;

    @Nullable
    private final Map<String, String> headers;

    public HttpHandler(@NonNull UrlGetter url) {
        this(url, null);
    }

    public HttpHandler(
            @NonNull UrlGetter url,
            @Nullable Map<String, String> headers
    ) {
        this.headers = new LinkedHashMap<>();
        this.headers.put("Content-Type", "application/json");

        if (headers != null) {
            headers.forEach(this.headers::put);
        }

        this.url = url;
    }

    @NonNull
    public HttpResponse request(
            @NonNull String method,
            @NonNull String endpoint,
            @Nullable JSONObject requestBody
    ) throws IOException, JSONException {
        URL requestUrl = url.from(endpoint);

        HttpURLConnection connection = (HttpURLConnection) requestUrl.openConnection();

        connection.setRequestMethod(method);

        if (headers != null) {
            putHeaders(connection);
        }

        connection.connect();

        if (requestBody != null) {
            writeRequestBody(requestBody, connection);
        }

        JSONObject responseBody = null;

        try {
            responseBody = getResponseBody(connection.getInputStream());
        }
        catch (FileNotFoundException ignored) {

        }

        return new HttpResponse(responseBody, connection.getResponseCode());
    }

    private void writeRequestBody(JSONObject requestBody, HttpURLConnection connection) throws IOException {
        OutputStream outputStream = connection.getOutputStream();

        outputStream.write(requestBody.toString().getBytes());
    }

    private void putHeaders(HttpURLConnection connection) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            connection.setRequestProperty(entry.getKey(), entry.getValue());
        }
    }

    private JSONObject getResponseBody(InputStream input) throws IOException, JSONException {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];

        int read;

        while ((read = input.read(buffer)) != -1) {

            output.write(buffer, 0, read);
        }

        String string = output.toString();

        if (string == null || string.isEmpty()) {
            return null;
        }
        else {
            return new JSONObject(string);
        }
    }

    @NonNull
    public HttpResponse get(@NonNull String endpoint) throws IOException, JSONException {

        return request("GET", endpoint, null);
    }

    @NonNull
    public HttpResponse post(@NonNull String endpoint, @NonNull JSONObject body)
            throws IOException, JSONException {
        return request("POST", endpoint, body);
    }

}
