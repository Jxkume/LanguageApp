package com.example.testi;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class ProfileNameMatchSessionNameTest {

    //private final DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");

    @Rule
    public ActivityScenarioRule<ProfileActivity> activityRule = new ActivityScenarioRule<>(ProfileActivity.class);

    @Before
    public void setUp() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);
        TestSessionManager.createTestSession();
        latch.await(2, TimeUnit.SECONDS);
        activityRule.getScenario().recreate();
        latch.await(2, TimeUnit.SECONDS);
        activityRule.getScenario().recreate();
    }

    @After
    public void tearDown() {
        TestSessionManager.deleteTestSession();
    }

    // Testataan, että TestSessionManagerin kautta luodussa profiilissa näkyy
    // annetut tiedot (nimi, level ja exp)

    @Test
    public void testUserCreationAndProfileDisplay() throws InterruptedException {
        Espresso.onView(ViewMatchers.withId(R.id.usernameText))
                        .check(ViewAssertions.matches(ViewMatchers.withText("- testUser -")));
        Espresso.onView(ViewMatchers.withId(R.id.levelText))
                .check(ViewAssertions.matches(ViewMatchers.withText("Taso 1")));
        Espresso.onView(ViewMatchers.withId(R.id.currentXpText))
                .check(ViewAssertions.matches(ViewMatchers.withText("50 / 100 xp")));
    }
}
