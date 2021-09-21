package com.example.usergen.view.activity.single_user;

import android.content.Context;
import android.content.Intent;
import android.widget.ImageView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.usergen.R;
import com.example.usergen.model.LambdaMatcher;
import com.example.usergen.service.generator.RandomUserGeneratorInput;
import com.example.usergen.util.ApiInfo;
import com.example.usergen.view.activity.DisplayIdlingResource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.stream.Stream;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasDataString;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class ShowUserActivityTest {

    @NonNull
    static Intent getActivityIntent() {

        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        Intent starter = new Intent(appContext, ShowUserActivity.class);

        RandomUserGeneratorInput input = new RandomUserGeneratorInput(
                "US", "female"
        );

        starter.putExtra(ShowUserActivity.INPUT_EXTRA_KEY, input.asBundle());

        return starter;
    }

    @Rule
    public ActivityScenarioRule<ShowUserActivity> rule
            = new ActivityScenarioRule<>(getActivityIntent());

    @Nullable
    private DisplayIdlingResource resource;

    @Before
    public void before() {
        Intents.init();

        rule.getScenario().onActivity(activity -> {
                    resource = new DisplayIdlingResource();
                    IdlingRegistry.getInstance().register(resource);
                    activity.displayListener = resource;
                }
        );
    }

    @After
    public void after() {
        Intents.release();

        IdlingRegistry.getInstance().unregister(resource);
    }

    @Test
    public void testDisplays() {

        Stream.of(
                R.id.showUserFragment_firstTitleTextView,
                R.id.showUserFragment_genderTextView,
                R.id.showUserFragment_emailTextView,
                R.id.showUserFragment_birthDateTextView,
                R.id.personNationality,
                R.id.showUserFragment_titleTextView,
                R.id.showUserFragment_ageTextView,
                R.id.showUserFragment_birthDateTextView,
                R.id.showUserFragment_emailTextView
        ).forEach(this::checkNotEmpty);

        onView(withId(R.id.personPicture)).check(
                matches(
                        new LambdaMatcher<>(
                                view -> ((ImageView) view).getDrawable() != null, ""
                        )
                )
        );

        onView(anyOf(withText("beep"), withId(R.id.loading_fragment_constraint_layout)))
                .check(doesNotExist());


    }


    @Test
    public void testNationalityClick() {

        onView(withId(R.id.personNationality)).perform(scrollTo(), click());

        intended(
                allOf(
                        hasAction(Intent.ACTION_VIEW),
                        hasDataString(containsString("geo:"))
                )
        );

    }

    @Test
    public void testDisplayedNationality() {

        onView(withId(R.id.personNationality))
                .check(matches(withText(not(isIn(ApiInfo.NATIONALITY_ACRONYMS)))));

    }

    private void checkNotEmpty(@IdRes int id) {
        onView(withId(id)).check(matches(not(withText(""))));
    }


}