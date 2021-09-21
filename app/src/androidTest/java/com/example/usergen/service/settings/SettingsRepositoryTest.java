package com.example.usergen.service.settings;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.UsergenApplication;
import com.example.usergen.model.Settings;
import com.example.usergen.service.auth.AuthApi;
import com.example.usergen.service.auth.AuthRepository;
import com.example.usergen.service.auth.TokenStorage;
import com.example.usergen.service.http.HttpHandler;
import com.example.usergen.service.http.UrlProvider;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static com.example.usergen.util.ApiInfo.API_SAMPLE_PASSWORD;
import static com.example.usergen.util.ApiInfo.API_SAMPLE_USERNAME;
import static com.example.usergen.util.ApiInfo.NATIONALITY_ACRONYMS;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

public class SettingsRepositoryTest {

    private SettingsRepository repository;

    @Before
    public void before() {

        Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

        TokenStorage tokenStorage = new TokenStorage(context);

        HttpHandler http = new HttpHandler(
                new UrlProvider(UsergenApplication.API_URL),
                tokenStorage
        );

        repository = new SettingsRepository(http);

        new AuthRepository(new AuthApi(http), tokenStorage).login(
                API_SAMPLE_USERNAME,
                API_SAMPLE_PASSWORD
        ).subscribe();

    }

    @Test
    public void savesAndGetSettings() {

        Settings previous = repository.getSettings().blockingGet();

        String previousNationality = previous == null ? null : previous.getNationalityQuery();

        Settings updated = new Settings(
                "female",
                randomNationalityAvoiding(previousNationality)
        );

        repository.saveSettings(updated).blockingAwait();

        Settings current = repository.getSettings().blockingGet();

        assertThat(current, is(not(nullValue())));

        assertThat(current, is(equalTo(updated)));
    }

    @Test
    public void savesNullNationality() {

        Settings updated = new Settings(
                "male",
                null
        );

        repository.saveSettings(updated).blockingAwait();

        Settings current = repository.getSettings().blockingGet();

        assertThat(current, is(not(nullValue())));

        assertThat(current, is(equalTo(updated)));
    }

    private String randomNationalityAvoiding(@Nullable String avoid) {
        Random random = new Random();

        String value;

        do {
            int index = random.nextInt(NATIONALITY_ACRONYMS.length);

            value = NATIONALITY_ACRONYMS[index];
        }
        while (avoid.equals(value));

        return value;
    }
}