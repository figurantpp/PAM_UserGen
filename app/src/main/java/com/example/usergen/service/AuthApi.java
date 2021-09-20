package com.example.usergen.service;

import androidx.annotation.NonNull;

import com.example.usergen.service.http.HttpException;
import com.example.usergen.service.http.HttpHandler;
import com.example.usergen.service.http.HttpResponse;

import org.json.JSONObject;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class AuthApi {

    // todo: consider mockito todo do unit tests or
    //  keep just doing integration test

    // todo: consider getting rid of AuthApiTest and test just AuthRepositoryTest
    //  as it is a larger integration test and it tests effectively the same thing

    private final HttpHandler http;

    public AuthApi(@NonNull HttpHandler http) {
        this.http = http;
    }

    @NonNull
    public Maybe<String> login(@NonNull String username, @NonNull String password) {

        return Maybe.fromSupplier(() -> {

            JSONObject body = new JSONObject();
            body.put("username", username);
            body.put("password", password);

            HttpResponse response = http.post("/user/login", body);

            if (isOk(response.getStatus())) {

                return response.getBody().getString("token");

            } else if (response.getStatus() == 403) {
                return null;
            } else {
                throw new HttpException(response.getStatus());
            }
        });

    }


    @NonNull
    public Single<Boolean> register(@NonNull String username, @NonNull String password) {

        return Single.fromSupplier(() -> {

            JSONObject body = new JSONObject();
            body.put("username", username);
            body.put("password", password);

            HttpResponse response = http.post("/user/register", body);

            if (isOk(response.getStatus())) {

                return true;
            } else if (response.getStatus() == 409) {
                return false;
            } else {
                throw new HttpException(response.getStatus());
            }
        });

    }

    private boolean isOk(int status) {
        return status / 100 == 2;
    }

}
