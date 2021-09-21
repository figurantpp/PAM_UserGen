package com.example.usergen.service.settings;

import androidx.annotation.NonNull;

import com.example.usergen.model.Settings;
import com.example.usergen.service.http.HttpHandler;

import org.json.JSONObject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;

import static com.example.usergen.util.Util.toPascal;

public class SettingsRepository {

    private final HttpHandler http;

    public SettingsRepository(@NonNull HttpHandler handler) {
        this.http = handler;
    }

    @NonNull
    public Completable saveSettings(@NonNull Settings settings) {

        return Completable.fromAction(() -> {

            JSONObject body = new JSONObject();
            body.put("sexQuery", toPascal(settings.getSexQuery()));

            if (settings.getNationalityQuery() == null) {
                body.put("nationalityQuery", JSONObject.NULL);
            }
            else {
                body.put("nationalityQuery", settings.getNationalityQuery());
            }

            http.put("/settings", body).requireOk();
        });
    }

    @NonNull
    public Maybe<Settings> getSettings() {

        return Maybe.fromSupplier(() -> {

            JSONObject body = http.get("/settings").requireOk().getBody();

            if (body == null) {
                return null;
            }
            else {

                String nationalityQuery = body.isNull("nationalityQuery")
                        ? null
                        : body.getString("nationalityQuery").toUpperCase();

                return new Settings(
                        body.getString("sexQuery").toLowerCase(),
                        nationalityQuery
                );
            }
        });
    }
}
