package com.example.usergen.service.favorite;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.UsergenApplication;
import com.example.usergen.model.User;
import com.example.usergen.service.auth.AuthApi;
import com.example.usergen.service.auth.AuthRepository;
import com.example.usergen.service.auth.TokenStorage;
import com.example.usergen.service.http.HttpHandler;
import com.example.usergen.service.http.OnlineImageResource;
import com.example.usergen.service.http.UrlProvider;
import com.example.usergen.util.UserApiInfo;

import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static com.example.usergen.util.RandomApiDate.parseRandomApiDate;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class FavoritesRepositoryTest {

    User sampleUser;

    FavoritesRepository repository;

    @Before
    public void setUp() throws Exception {

        sampleUser = new User(
                null,
                null,
                "mr",
                "bob jhonson",
                "bob.j@bob.com",
                "male",
                parseRandomApiDate("1993-07-20T09:44:18.674Z"),
                (short) 28,
                "US",
                new OnlineImageResource(
                        new URL("https://randomuser.me/api/portraits/men/87.jpg")
                )
        );


        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        TokenStorage storage = new TokenStorage(context);

        HttpHandler http = new HttpHandler(new UrlProvider(UsergenApplication.API_URL), storage);

        repository = new FavoritesRepository(http);

        new AuthRepository(new AuthApi(http), storage).login(
                UserApiInfo.API_SAMPLE_USERNAME,
                UserApiInfo.API_SAMPLE_PASSWORD
        ).subscribe();
    }

    @Test
    public void registersFavorite() {

        String id = repository.registerFavorite(sampleUser).blockingGet();

        assertThat(id, is(notNullValue()));
    }

    @Test
    public void listsFavorites() {

        String id = repository.registerFavorite(sampleUser).blockingGet();

        assertThat(id, is(notNullValue()));

        List<User> favoriteUsers = repository.listFavorites().blockingGet();

        boolean match = favoriteUsers.stream().map(user -> {

            assertThat(user.getApiId(), is(notNullValue()));
            assertThat(user.getEmail(), is(notNullValue()));
            assertThat(user.getGender(), is(notNullValue()));
            assertThat(user.getName(), is(notNullValue()));
            assertThat(user.getNationality(), is(notNullValue()));
            assertThat(user.getTitle(), is(notNullValue()));
            assertThat(user.getAge(), is(notNullValue()));
            assertThat(user.getBirthDate(), is(notNullValue()));
            assertThat(user.getProfileImage(), is(notNullValue()));
            assertThat(user.getProfileImage().getUrl(), is(notNullValue()));

            return user.getApiId();
        }).anyMatch(userId -> userId.equals(id));

        assertThat(match, is(true));
    }

    @Test
    public void deletesFavorites() {

        String id = repository.registerFavorite(sampleUser).blockingGet();

        assertThat(id, is(notNullValue()));

        repository.deleteFavorite(id).blockingAwait();

        List<User> favoriteUsers = repository.listFavorites().blockingGet();

        boolean match = favoriteUsers.stream().map(User::getApiId).anyMatch(userId -> userId.equals(id));

        assertThat(match, is(false));

    }
}