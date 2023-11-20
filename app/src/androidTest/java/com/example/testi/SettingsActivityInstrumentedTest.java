package com.example.testi;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SettingsActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<SettingsActivity> activityRule = new ActivityScenarioRule<>(SettingsActivity.class);

    @Before
    public void setUp() {
        // Luodaan testisessio testausta varten
        TestSessionManager.createTestSession();
    }

    @After
    public void tearDown() {
        // Poistetaan testisessio testien jälkeen
        TestSessionManager.deleteTestSession();
    }

    @Test
    public void testShowSettings() {
        Espresso.onView(ViewMatchers.withId(R.id.soundSettindsSlider))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.musicSettindsSlider))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.deleteUserButton))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    // Sovelluksen kielen vaihtaminen
    // Testissä sovelluksen kieli on alussa automaattisesti suomen kieli, sillä se on default
    @Test
    public void testLanguageChange() throws InterruptedException {

        CountDownLatch latch = new CountDownLatch(1);

        latch.await(5, TimeUnit.SECONDS);

        // Annetaan LanguageManager-luokalle tietoon testisession id
        LanguageManager.getInstance().setSessionID(-1);

        // Painetaan aktiviteetissa olevasta lipun kuvasta, joka kertoo sovelluksen nykyisen kielen
        Espresso.onView(ViewMatchers.withId(R.id.flagImageView))
                .perform(ViewActions.click());

        // Valitaan kielivaihtoehdoista ensimmäinen (espanjan kieli)
        Espresso.onView(ViewMatchers.withId(R.id.changeLanguageOption1))
                .perform(ViewActions.click());

        // Odotetaan 5s, jotta kieli ehtii päivittymään tietokantaan
        latch.await(5, TimeUnit.SECONDS);

    }

}
