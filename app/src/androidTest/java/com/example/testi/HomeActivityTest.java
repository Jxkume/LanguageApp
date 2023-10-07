package com.example.testi;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;
import androidx.test.espresso.Espresso;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Instrumentation;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class HomeActivityTest {

    @Rule
    public IntentsTestRule<HomeActivity> mActivityRule = new IntentsTestRule<>(HomeActivity.class);

    @Test
    public void testProfileIconClick() {
        // Click on the profile icon
        Espresso.onView(withId(R.id.profileIcon)).perform(click());

        // Check if the expected activity is launched (ProfileActivity)
        intending(hasComponent(ProfileActivity.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));
    }

    @Test
    public void testSettingsIconClick() {
        // Click on the settings icon
        Espresso.onView(withId(R.id.settingsIcon)).perform(click());

        // Check if the expected activity is launched (SettingsActivity)
        intending(hasComponent(SettingsActivity.class.getName())).respondWith(new Instrumentation.ActivityResult(0, null));
    }

    // Add similar tests for other icons (animalGameIcon, colourGameIcon)
}
