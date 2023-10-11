package com.example.testi;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import org.junit.Rule;
import org.junit.Test;

public class SplashActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<SplashActivity> activityRule = new ActivityScenarioRule<>(SplashActivity.class);

    @Test
    public void testSplashScreenTransition() {

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Tarkistetaan löytyykö nappi, jos löytyy niin päästiin seuraavaan aktiviteettiin
        Espresso.onView(ViewMatchers.withId(R.id.session1_btn))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    @Test
    public void testSplashScreenTransition2() {

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Tämän testin ei pitäisi mennä läpi
        Espresso.onView(ViewMatchers.withId(R.id.profilePictureBackground))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }
}