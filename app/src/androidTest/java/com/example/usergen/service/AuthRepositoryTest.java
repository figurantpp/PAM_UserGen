package com.example.usergen.service;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.UsergenApplication;
import com.example.usergen.service.http.HttpHandler;
import com.example.usergen.service.http.UrlGetter;

import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

public class AuthRepositoryTest {

    private AuthRepository repository;

    private TokenStorage tokenStorage;

    @Before
    public void before() {

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        tokenStorage = new TokenStorage(context);

        repository = new AuthRepository(
                new AuthApi(
                        new HttpHandler(new UrlGetter(UsergenApplication.API_URL))
                ),
                tokenStorage
        );
    }

    @Test
    public void loginSuccessSetsToken() {

        tokenStorage.setToken(null);

        boolean result = repository.login("bob", "123").blockingGet();

        assumeThat(result, is(true));

        assertThat(tokenStorage.getToken(), is(notNullValue()));
    }

    @Test
    public void loginFailureResetsToken() {

        tokenStorage.setToken("beep");

        boolean result = repository.login(
                UUID.randomUUID().toString(),
                UUID.randomUUID().toString()
        ).blockingGet();

        assumeThat(result, is(false));

        assertThat(tokenStorage.getToken(), is(nullValue()));
    }


    @Test
    public void registers() {

        String uuid = UUID.randomUUID().toString();

        Boolean result = repository.register(uuid, uuid).blockingGet();

        assertThat(result, is(true));
    }

    @Test
    public void reportsRegisterConflict() {

        String uuid = UUID.randomUUID().toString();

        Boolean first = repository.register(uuid, uuid).blockingGet();

        assumeThat(first, is(true));

        Boolean second = repository.register(uuid, uuid).blockingGet();

        assertThat(second, is(false));
    }
}