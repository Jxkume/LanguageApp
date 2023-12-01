package com.example.testi;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import static org.junit.Assert.fail;

import android.util.Log;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ColourGameInstrumentedTest {

    private final DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");
    private int xp;

    @Rule
    public ActivityScenarioRule<HomeActivity> activityRule = new ActivityScenarioRule<>(HomeActivity.class);

    @Before
    public void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        // Luodaan testisessio testausta varten
        TestSessionManager.createTestSessionGameTests();
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
    public void checkXpGainAfterGame() throws InterruptedException, ExecutionException {

        CountDownLatch latch = new CountDownLatch(1);

        // Painetaan väripelin ikonista
        Espresso.onView(ViewMatchers.withId(R.id.colorGameIcon))
                .perform(ViewActions.click());

        // Varmistetaan, että väripelin ensimmäinen aktiviteetti avautui
        Espresso.onView(ViewMatchers.withId(R.id.colourGameFirstOptionBackground1))
                .check(matches(isDisplayed()));

        // Pelataan peliä 5 kierrosta
        for (int i = 0; i < 5; i++) {
            Espresso.onView(ViewMatchers.withId(R.id.colourGameFirstOptionBackground1))
                    .perform(ViewActions.click());
        }

        // Odotetaan 5s, jotta seuraava aktiviteetti ehtii latautua
        latch.await(5, TimeUnit.SECONDS);

        // Varmistetaan, että päästiin väripelin toiseen aktiviteettiin
        Espresso.onView(ViewMatchers.withId(R.id.colourGameSecondText1))
                .check(matches(isDisplayed()));

        // Pelataan peliä loput 5 kierrosta
        for (int i = 0; i < 5; i++) {
            Espresso.onView(ViewMatchers.withId(R.id.colourGameSecondText1))
                    .perform(ViewActions.click());
        }

        // Odotetaan 10s, jotta peli ehtii loppumaan ja annetaan aikaa xp:lle päivittyä tietokantaan
        latch.await(10, TimeUnit.SECONDS);

        // Varmistetaan, että päästiin pelistä pois kotinäkymään
        Espresso.onView(ViewMatchers.withId(R.id.colorGameIcon))
                .check(matches(isDisplayed()));

        // Tarkistetaan tietokannasta, että päivittyikö session xp
        Tasks.await(sessionsRef.child(TestSessionManager.getTestSessionKey())
                .child("XP")
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        xp = dataSnapshot.getValue(Integer.class);
                    }
                })
                .addOnFailureListener(e -> Log.d("Session", "Xp:n hakemisessa tapahtui virhe.")));

        // Tarkistetaan, että xp ei ole 0 eli sitä lisättiin pelin aikana
        if (xp <= 0) {
            fail("Käyttäjä ei ansainnut kokemuspisteitä väripelistä");
        }
    }

}
