package com.example.testi;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import static org.junit.Assert.fail;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.matcher.ViewMatchers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class SessionsActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<SessionsActivity> activityRule = new ActivityScenarioRule<>(SessionsActivity.class);

    @Before
    public void setUp() {
        // Luodaan testisessio testausta varten
        TestSessionManager.createTestSessionSessionsTest();
    }

    @Test
    public void testTransitionToHomeActivityFromSplash() throws InterruptedException {

        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");

        // Odotetaan, että Sessions näkymä päivittyy (Firebase on liian hidas niin joudutaan odottaa sitä)
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(1, TimeUnit.SECONDS);
        activityRule.getScenario().recreate();

        // Valitaan testisessio nimen perusteella
        Espresso.onView(ViewMatchers.withText("testUser"))
                .perform(ViewActions.click());

        // Odotetaan että Espresso kerkee "nähdä" kaikki UI:n elementit
        latch.await(1, TimeUnit.SECONDS);

        // Tarkistetaan että päästiin Home aktivitiin
        Espresso.onView(ViewMatchers.withId(R.id.settingsIcon))
                .check(matches(isDisplayed()));

        // Painetaan settingsIconia
        Espresso.onView(ViewMatchers.withId(R.id.settingsIcon))
                .perform(ViewActions.click());

        // Tarkistetaan että päästiin settings aktivitiin
        Espresso.onView(ViewMatchers.withId(R.id.deleteUserButton))
                .check(matches(isDisplayed()));

        // Painetaan "Poista käyttäjä" nappia että session poistuu
        Espresso.onView(ViewMatchers.withId(R.id.deleteUserButton))
                .perform(ViewActions.click());


        // Tarkistetaan, että session on poistettu tietokannasta
        sessionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                    int sessionID = sessionSnapshot.child("SessionID").getValue(Integer.class);
                    if (sessionID == 1) {
                        String username = sessionSnapshot.child("Username").getValue(String.class);
                        if ("testUser".equals(username)) {
                            // Jos löydettiin testUser, niin testi epäonnistuu
                            fail("Session found for username: testUser");
                        }
                        break;
                    }
                    // Jos testUser ei löydy, niin testi menee läpi
                    Log.d("Test", "Session not found for username: testUser CORRECTOOOO");
                    break;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Test", "Database operation canceled", databaseError.toException());
            }
        });
    }

}