package com.example.usergen.service.generator;

import androidx.annotation.NonNull;

public class NoNetworkException extends Exception {

    public NoNetworkException(@NonNull String error) {
        super(error);
    }

}
