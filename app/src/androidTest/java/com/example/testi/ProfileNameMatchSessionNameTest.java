package com.example.testi;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ActivityScenario;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProfileNameMatchSessionNameTest {

    // Testataan, etä käyttäjän luomassa profiilissa näkyy hänen antamansa tiedot (nimi ja ikä)

    @Test
    public void testUserCreationAndProfileDisplay() {
        // Avataan NewSessionActivity
        ActivityScenario.launch(NewSessionActivity.class);

        // Kirjataan nimimerkki ja ikä
        onView(withId(R.id.usernameEditText)).perform(typeText("Testi"), closeSoftKeyboard());
        onView(withId(R.id.ageEditText)).perform(typeText("8"), closeSoftKeyboard());

        onView(withId(R.id.createSessionButton)).perform(click());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        onView(withId(R.id.usernameTextView)).check(matches(withText("TestUser")));
        onView(withId(R.id.ageTextView)).check(matches(withText("25")));
    }
}
