package com.example.usergen.view.activity.user_list;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.service.generator.RandomUserGeneratorInput;
import com.example.usergen.service.testing.BooleanIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class UserListActivityTest {

    @NonNull
    private Intent getIntent() {

        Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(),
                UserListActivity.class);

        intent.putExtra(UserListActivity.INPUT_EXTRA_KEY,
                new RandomUserGeneratorInput("US", "male").asBundle()
        );

        return intent;
    }

    @Rule
    public ActivityScenarioRule<UserListActivity> activityRule
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