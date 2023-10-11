package com.example.testi;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import org.junit.Rule;
import org.junit.Test;

public class HomeActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<HomeActivity> activityRule = new ActivityScenarioRule<>(HomeActivity.class);

    @Test
    public void testGoToProfile() {
        Espresso.onView(ViewMatchers.withId(R.id.profileIcon))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.usernameText))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testGoToSettings() {
        Espresso.onView(ViewMatchers.withId(R.id.settingsIcon))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.musicSettindsSlider))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testGoToAG() {
        Espresso.onView(ViewMatchers.withId(R.id.animalGameIcon))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.animalGameFirstOption1))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testGoToCG() {
        Espresso.onView(ViewMatchers.withId(R.id.colorGameIcon))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.colourGameFirstOption1))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}
