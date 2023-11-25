package com.example.testi;

import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.fail;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.matcher.ViewMatchers;

import com.google.android.gms.tasks.Tasks;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SettingsActivityInstrumentedTest {

    private final DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");
    private String language = "";

    @Rule
    public ActivityScenarioRule<SettingsActivity> activityRule = new ActivityScenarioRule<>(SettingsActivity.class);

    @Before
    public void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        // Luodaan testisessio testausta varten
        TestSessionManager.createTestSession();
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
    public void testShowSettings() {
        Espresso.onView(ViewMatchers.withId(R.id.soundSettindsSlider))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.musicSettindsSlider))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.deleteUserButton))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));
    }

    // Sovelluksen kielen vaihtaminen espanjan kielestä arabian kieleksi
    @Test
    public void testLanguageChange() throws InterruptedException, ExecutionException {

        // Painetaan aktiviteetissa olevasta lipun kuvasta, joka kertoo sovelluksen nykyisen kielen
        Espresso.onView(ViewMatchers.withId(R.id.flagImageView))
                .perform(ViewActions.click());

        // Varmistetaan, että näytöllä näkyy pop-up hakemalla kuvanäkymä sen sisältä
        Espresso.onView(ViewMatchers.withId(R.id.currentFlagInPopUp))
                .check(matches(isDisplayed()));

        // Valitaan kielivaihtoehdoista toinen (arabian kieli)
        Espresso.onView(ViewMatchers.withId(R.id.changeLanguageOption2))
                .perform(ViewActions.click());

        // Tarkistetaan, että kieli vaihtui arabiaksi varmistamalla yhden tekstinäkymän arvo
        Espresso.onView(ViewMatchers.withId(R.id.languageTextView))
                .check(ViewAssertions.matches(ViewMatchers.withText("لغة:")));

        // Odotetaan 10s, että kieli ehtii päivittyä tietokannassa
        CountDownLatch latch = new CountDownLatch(1);
        latch.await(10, TimeUnit.SECONDS);

        // Haetaan tietokannasta kielen arvo
        Tasks.await(sessionsRef.child(TestSessionManager.getTestSessionKey())
                .child("Language")
                .get()
                .addOnSuccessListener(dataSnapshot -> {
                    if (dataSnapshot.exists()) {
                        language = dataSnapshot.getValue(String.class);
                    }
                })
                .addOnFailureListener(e -> Log.d("Session", "Kielen hakemisessa tapahtui virhe.")));

        // Varmistetaan, että löydetty kieli on arabian kieli
        if (!language.equals("ar")) {
            fail("Tietokannasta ei löytynyt arabian kieltä");
        }

    }

    @Test
    public void testSessionDelete() throws InterruptedException {

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
