package com.example.usergen.service.auth;

import androidx.annotation.NonNull;

import com.example.usergen.service.http.HttpException;
import com.example.usergen.service.http.HttpHandler;
import com.example.usergen.service.http.HttpResponse;

import org.json.JSONObject;

import io.reactivex.rxjava3.annotations.CheckReturnValue;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

public class AuthApi {

    private final HttpHandler http;

    public AuthApi(@NonNull HttpHandler http) {
        this.http = http;
    }

    @NonNull
    @CheckReturnValue
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
    @CheckReturnValue
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

    @NonNull
    @CheckReturnValue
    public Single<Boolean> check() {

        return Single.fromSupplier(() -> {

            HttpResponse response = http.get("/user/check");

            if (response.isOk()) {
                return true;
            }
            else if (response.getStatus() == 401) {
                return false;
            } else {
                response.requireOk();
                return false;
            }
        });
    }

    private boolean isOk(int status) {
        return status / 100 == 2;
    }

}
