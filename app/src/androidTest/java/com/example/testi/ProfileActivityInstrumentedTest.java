package com.example.testi;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.espresso.assertion.ViewAssertions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.runner.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ProfileActivityInstrumentedTest {
    @Rule
    public ActivityScenarioRule<ProfileActivity> activityRule = new ActivityScenarioRule<>(ProfileActivity.class);

    @Before
    public void setUp() {
        TestSessionManager.createTestSession();
    }

    @After
    public void tearDown() {
        TestSessionManager.deleteTestSession();
    }

    @Test
    public void testProfilePictureChange() throws InterruptedException {

        Espresso.onView(ViewMatchers.withId(R.id.profilePictureBackground))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.profilePictureOption9))
                .check(ViewAssertions.matches(ViewMatchers.isDisplayed()));

        Espresso.onView(ViewMatchers.withId(R.id.profilePictureOption9))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.newProfilePicture1))
                .check(ViewAssertions.matches(EspressoTestMatchers.withDrawable(R.drawable.catprofilepicture)));

        Espresso.onView((ViewMatchers.withId(R.id.homeIcon)))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.newProfilePictureNavbar))
                .check(ViewAssertions.matches(EspressoTestMatchers.withDrawable(R.drawable.catprofilepicture)));

        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");
        final int[] picIdFromDb = new int[1];

        CountDownLatch latch = new CountDownLatch(1);

        latch.await(5, TimeUnit.SECONDS);

        sessionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot sessionSnapshot : snapshot.getChildren()) {
                        String sessionKey = sessionSnapshot.getKey();
                        int sessionID = sessionSnapshot.child("SessionID").getValue(Integer.class);
                        if(sessionKey != null) {
                            if (sessionID == 5) {
                                Log.d("sessionkey", sessionKey);
                                picIdFromDb[0] = sessionSnapshot.child("PhotoID").getValue(Integer.class);
                                assertEquals(9, picIdFromDb[0]);
                            } else {
                                System.out.println("Session ID not found for key " + sessionKey);
                            }
                        } else {
                            System.out.println("Session not found");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });

    }

    @Test
    public void levelUpTest() {
        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");

        sessionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            int lvlFromDb;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot sessionSnapshot : snapshot.getChildren()) {
                        String sessionKey = sessionSnapshot.getKey();
                        int sessionID = sessionSnapshot.child("SessionID").getValue(Integer.class);
                        if(sessionKey != null) {
                            if (sessionID == 5) {
                                Log.d("sessionkey", sessionKey);
                                lvlFromDb = sessionSnapshot.child("Level").getValue(Integer.class);
                                assertEquals(1, lvlFromDb);
                                sessionSnapshot.child("XP").getRef().setValue(150);
                                lvlFromDb = sessionSnapshot.child("Level").getValue(Integer.class);
                                assertEquals(2, lvlFromDb);
                            } else {
                                System.out.println("Session ID not found for key " + sessionKey);
                            }
                        } else {
                            System.out.println("Session not found");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }
}
