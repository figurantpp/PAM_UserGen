package com.example.usergen.service.http;

import androidx.annotation.NonNull;

public class HttpException extends RuntimeException {

    public HttpException(int status) {
        super("Unexpected http response status " + status);
    }

    public HttpException(int status, @NonNull String error) {
        super("HTTP error " + status + ":\n" + error);
    }
}
