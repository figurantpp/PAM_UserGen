package com.example.usergen.activity;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.usergen.activity.single_user.ShowUserActivity;
import com.example.usergen.model.sensor.ProximitySensor;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class ActivitySensorTest {

    @Rule
    public ActivityScenarioRule<ShowUserActivity> activityRule
            = new ActivityScenarioRule<>(ShowUserActivityTest.getActivityIntent());

    @Test
    public void testSensor() {

        activityRule.getScenario().onActivity(activity ->  {

            ProximitySensor lightSensorListener = activity.getProximitySensor();

            assertThat(lightSensorListener, not(is(nullValue())));

            assertNotNull(lightSensorListener);

            lightSensorListener.emit(true);

            assertThat(activity.isFinishing(), is(true));

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

    }

}
