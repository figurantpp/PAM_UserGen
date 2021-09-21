package com.example.usergen.view.activity.main;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.usergen.R;
import com.example.usergen.view.activity.single_user.ShowUserActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.times;
import static androidx.test.espresso.intent.matcher.ComponentNameMatchers.hasShortClassName;
import static androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@RunWith(AndroidJUnit4.class)

public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule
            = new ActivityScenarioRule<>(MainActivity.class);

    @Before
    public void before() {
        Intents.init();
    }

    @After
    public void after() {
        Intents.release();
    }


    @Test
    public void startsShowResultsActivity() {

        onView(withText("Female")).perform(click());

        onView(withId(R.id.mainActivity_searchButton)).perform(scrollTo(), click());

        intended(
                allOf(
                        hasComponent(hasShortClassName(".view.activity.single_user." + ShowUserActivity.class.getSimpleName())),
                        toPackage("com.example.usergen")
                )
        );

    }

    @Test
    public void reportsEmptyGender() {

        onView(withText(R.string.error_empty)).check(doesNotExist());

        onView(withId(R.id.mainActivity_searchButton)).perform(scrollTo(), click());

        onView(withText(R.string.error_empty)).check(matches(isDisplayed()));

        intended(anyIntent(), times(0));

    }
}
