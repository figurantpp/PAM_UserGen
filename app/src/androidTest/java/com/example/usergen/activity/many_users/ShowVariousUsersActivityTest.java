package com.example.usergen.activity.many_users;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.model.BooleanIdlingResource;
import com.example.usergen.model.user.generator.RandomUserGeneratorInput;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class ShowVariousUsersActivityTest {

    @NonNull
    private Intent getIntent() {

        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                ShowVariousUsersActivity.class);

        intent.putExtra(ShowVariousUsersActivity.INPUT_EXTRA_KEY,
                new RandomUserGeneratorInput("US", "male").asBundle()
        );

        return intent;
    }

    @Rule
    public ActivityScenarioRule<ShowVariousUsersActivity> activityRule
            = new ActivityScenarioRule<>(getIntent());

    private BooleanIdlingResource idlingResource;

    @Before
    public void before() {
        activityRule.getScenario().onActivity(activity -> {
            idlingResource = activity.idleState;

            IdlingRegistry.getInstance().register(idlingResource);
        });
    }

    @After
    public void after() {

        IdlingRegistry.getInstance().unregister(idlingResource);
    }

    @Test
    public void testDisplays() {
    }

    @Test
    public void testShowsUsers() {

        onView(withText("")).check(doesNotExist());
    }


}