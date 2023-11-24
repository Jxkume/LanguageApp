package com.example.testi;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.fail;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    @Test
    public void testSessionDelete() throws InterruptedException {

        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");

        // Tarkistetaan että päästiin settings aktivitiin
        Espresso.onView(ViewMatchers.withId(R.id.deleteUserButton))
                .check(matches(isDisplayed()));

        // Painetaan "Poista käyttäjä" nappia että session poistuu
        Espresso.onView(ViewMatchers.withId(R.id.deleteUserButton))
                .perform(ViewActions.click());


        CountDownLatch latch = new CountDownLatch(1);
        latch.await(1, TimeUnit.SECONDS);

        // Tarkistetaan, että session on poistettu tietokannasta
        sessionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                    int sessionID = sessionSnapshot.child("SessionID").getValue(Integer.class);
                    if (sessionID == -1) {
                        String username = sessionSnapshot.child("Username").getValue(String.class);
                        if ("testUser".equals(username)) {
                            // Jos löydettiin testUser, niin testi epäonnistuu
                            fail("Session found for username: testUser");
                        }
                        break;
                    }
                    // Jos testUser ei löydy, niin testi menee läpi
                    Log.d("Test", "Session not found for username: testUser");

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
