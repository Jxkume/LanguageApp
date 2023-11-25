package com.example.testi;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


public class SessionsActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<SessionsActivity> activityRule = new ActivityScenarioRule<>(SessionsActivity.class);

    @Before
    public void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        // Luodaan testisessio testausta varten
        TestSessionManager.createTestSessionSessionsTests();
        latch.await(2, TimeUnit.SECONDS);
        // Nyt päivittyy kaikki tiedot tietokannasta :D
        activityRule.getScenario().recreate();
        latch.await(2, TimeUnit.SECONDS);
        // Nyt päivittyy sovelluksen kieli :D
        activityRule.getScenario().recreate();
    }

    @After
    public void tearDown() {
        // Poistetaan testisessio testien jälkeen
        TestSessionManager.deleteTestSession();
    }

    @Test
    public void testSessionChoosing() throws InterruptedException {

        // Odotetaan tietokantaa :D
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(5, TimeUnit.SECONDS);

        // Painetaan testi sessiosta
        Espresso.onView(ViewMatchers.withText("testUser"))
                .perform(ViewActions.click());

        // Odotetaan UI :D
        CountDownLatch latch2 = new CountDownLatch(1);
        latch2.await(5, TimeUnit.SECONDS);

        // Varmistetaan, että ollaan päästy kotinäkymälle
        Espresso.onView(ViewMatchers.withId(R.id.userLevel))
                .check(matches(isDisplayed()));

        // Tarkistetaan jos ollaan otettu oikeat tiedot tietokannasta ja että ne näkyvät
        Espresso.onView(ViewMatchers.withId(R.id.userLevel))
                .check(ViewAssertions.matches(ViewMatchers.withText("5")));
    }

}