package com.example.usergen.service.http;

public class HttpException extends RuntimeException {

    private final int status;

    public HttpException(int status) {
        super("Unexpected http response status " + status);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
