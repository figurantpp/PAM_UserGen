package com.example.usergen.activity;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.R;
import com.example.usergen.model.LambdaMatcher;
import com.example.usergen.model.user.RandomUserGeneratorInput;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ShowResultsActivityTest {

    @NonNull
    private Intent getIntent() {

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Intent starter = new Intent(appContext, ShowResultsActivity.class);

        RandomUserGeneratorInput input = new RandomUserGeneratorInput(
                "US", "female"
        );

        starter.putExtra(ShowResultsActivity.INPUT_BUNDLE_KEY, input.asBundle());

        return starter;
    }

    @Rule
    public ActivityScenarioRule<ShowResultsActivity> rule = new ActivityScenarioRule<>(getIntent());

    private final DisplayIdlingResource resource = new DisplayIdlingResource();

    @Before
    public void before() {
        Intents.init();

        rule.getScenario().onActivity(activity ->{
                    IdlingRegistry.getInstance().register(resource);
                    activity.listener = resource;
                }
        );
    }

    @After
    public void after(){
        Intents.release();

        IdlingRegistry.getInstance().unregister(resource);
    }

    @Test
    public void testWorking() {

        Stream.of(
                R.id.personFirstandSecondName,
                R.id.personGender,
                R.id.personEmail,
                R.id.personDayOfBirth,
                R.id.personNacionality,
                R.id.personTitle,
                R.id.personAge,
                R.id.personDayOfBirth,
                R.id.personEmail
        ).forEach(this::checkNotEmpty);

        onView(withId(R.id.personPicture)).check(
                matches(
                        new LambdaMatcher<>(
                                view -> ((ImageView) view).getDrawable() != null, ""
                        )
                )
        );


    }


    @Test
    public void testNationalityClick() {

        onView(withId(R.id.personNacionality)).perform(click());

        intended(IntentMatchers.anyIntent());

    }

    private void checkNotEmpty(@IdRes int id) {
        onView(withId(id)).check(matches(not(withText(""))));
    }


}