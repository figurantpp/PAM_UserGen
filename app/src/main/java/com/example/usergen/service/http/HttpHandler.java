package com.example.usergen.service.http;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.usergen.service.auth.TokenStorage;

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
    private final UrlProvider url;

    @Nullable
    private final Map<String, String> headers;

    @Nullable
    private final TokenStorage tokenStorage;

    public HttpHandler(@NonNull UrlProvider url) {
        this(url, null);
    }

    public HttpHandler(@NonNull UrlProvider url, @Nullable TokenStorage tokenStorage) {

        this.tokenStorage = tokenStorage;
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

        if (tokenStorage != null) {
            putToken(connection);
        }

        if (requestBody != null) {
            writeRequestBody(requestBody, connection);
        }

        connection.connect();

        JSONObject responseBody;

        try {
            responseBody = getResponseBody(connection.getInputStream());
        }
        catch (FileNotFoundException ignored) {
            responseBody = null;
        }

        String error = null;

        if (connection.getErrorStream() != null) {
            error = readStream(connection.getErrorStream());
        }

        return new HttpResponse(responseBody, connection.getResponseCode(), error);
    }

    private void putToken(HttpURLConnection connection) {

        String token = tokenStorage.getToken();

        if (token != null) {
            connection.setRequestProperty("Authorization",  "Bearer " + token);
        }
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

        String string = readStream(input);

        if (string == null || string.isEmpty()) {
            return null;
        }
        else {
            return new JSONObject(string);
        }
    }

    private String readStream(InputStream input) throws IOException {

        if (input == null) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        byte[] buffer = new byte[4096];

        int read;

        while ((read = input.read(buffer)) != -1) {

            output.write(buffer, 0, read);
        }

        return output.toString();
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

    @NonNull
    public HttpResponse put(@NonNull String endpoint, @NonNull JSONObject body)
            throws IOException, JSONException {
        return request("PUT", endpoint, body);
    }

    @NonNull
    public HttpResponse delete(@NonNull String endpoint)
            throws IOException, JSONException {
        return request("DELETE", endpoint, null);
    }

}
