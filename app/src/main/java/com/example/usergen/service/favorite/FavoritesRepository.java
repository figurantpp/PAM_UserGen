package com.example.usergen.service.favorite;

import androidx.annotation.NonNull;

import com.example.usergen.model.User;
import com.example.usergen.service.http.HttpHandler;
import com.example.usergen.service.http.OnlineImageResource;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

import static com.example.usergen.util.UserApiDate.formatUserApiDate;
import static com.example.usergen.util.UserApiDate.parseUserApiDate;
import static com.example.usergen.util.Util.toPascal;

public class FavoritesRepository {

    @NonNull
    private final HttpHandler http;

    public FavoritesRepository(@NonNull HttpHandler http) {
        this.http = http;
    }

    @NonNull
    public Single<String> registerFavorite(@NonNull User user) {

        return Single.fromSupplier(() -> {

            URL url = user.getProfileImage().getUrl();

            Objects.requireNonNull(url, "Input user must have an image url");

            JSONObject body = new JSONObject();
            body.put("name", user.getName());
            body.put("title", toPascal(user.getTitle()));
            body.put("email", user.getEmail());
            body.put("sex", toPascal(user.getGender()));
            body.put("nationality", toPascal(user.getNationality()));
            body.put("birthDate", formatUserApiDate(user.getBirthDate()));
            body.put("imageUrl", url.toString());

            JSONObject response = http.post("/favorites", body).requireOk().requireBody();

            return (String) response.get("id");
        });


    }

    @NonNull
    public Single<List<User>> listFavorites() {

        return Single.fromSupplier(() -> {

            JSONObject response = http.get("/favorites").requireOk().requireBody();

            JSONArray items = response.getJSONArray("items");

            List<User> result = new ArrayList<>(items.length());

            for (int i = 0; i < items.length(); ++i) {
                JSONObject item = items.getJSONObject(i);

                result.add(new User(
                        null,
                        item.getString("id"),
                        item.getString("title"),
                        item.getString("name"),
                        item.getString("email"),
                        item.getString("sex"),
                        parseUserApiDate(item.getString("birthDate")),
                        (short) item.getInt("age"),
                        item.getString("nationality"),
                        new OnlineImageResource(new URL(item.getString("imageUrl"))),
                        false
                ));
            }

            return result;
        });
    }

    @NonNull
    public Completable deleteFavorite(@NonNull String id) {

        return Completable.fromAction(() -> http.delete("/favorites/" + id).requireOk());
    }
}
