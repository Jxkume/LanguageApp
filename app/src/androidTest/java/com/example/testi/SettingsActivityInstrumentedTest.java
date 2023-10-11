package com.example.testi;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import org.junit.Rule;
import org.junit.Test;

public class SettingsActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<SettingsActivity> activityRule = new ActivityScenarioRule<>(SettingsActivity.class);

    @Test
    public void testShowSettings() {
        Espresso.onView(ViewMatchers.withId(R.id.soundSettindsSlider))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.musicSettindsSlider))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.deleteUserButton))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

}
