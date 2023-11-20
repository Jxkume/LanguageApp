package com.example.testi;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CountDownLatch;

import androidx.annotation.NonNull;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import android.util.Log;



@RunWith(AndroidJUnit4.class)
@LargeTest
public class NewSessionActivityInstrumentedTest {

    @Rule
    public ActivityScenarioRule<NewSessionActivity> newSessionActivityRule =
            new ActivityScenarioRule<>(NewSessionActivity.class);

    @Test
    public void testStartNewSessionAndSaveToFirebase() throws InterruptedException {

        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");

        // Simuloidaan että käyttäjä loisi uuden session ja painaisi Valmis nappulasta

        Espresso.onView(ViewMatchers.withId(R.id.usernameEditText))
                .perform(ViewActions.typeText("TestUser"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.ageEditText))
                .perform(ViewActions.typeText("3"), ViewActions.closeSoftKeyboard());

        Espresso.onView(ViewMatchers.withId(R.id.newProfilePictureBackground1))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.profilePictureOption1))
                .perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.newSessionReadyButton))
                .perform(ViewActions.click());

        CountDownLatch latch = new CountDownLatch(1);
        latch.await(30, TimeUnit.SECONDS);


        sessionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean sessionFound = false;

                for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                    int sessionID = sessionSnapshot.child("SessionID").getValue(Integer.class);
                    if (sessionID == -1) {
                        // Varmistetaan että just luotu data on tietokannassa
                        assertEquals("TestUser", sessionSnapshot.child("Username").getValue(String.class));
                        assertEquals(3, sessionSnapshot.child("Age").getValue(Integer.class).intValue());

                        sessionFound = true;

                        // Poistetaan testi sessionin tietokannasta
                        String sessionKey = sessionSnapshot.getKey();
                        if (sessionKey != null) {
                            DatabaseReference newSessionRef = sessionsRef.child(sessionKey);
                            newSessionRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d("Test", "Session deleted successfully");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    fail("Error deleting session");
                                }
                            });
                        }
                        break;
                    }
                }
                if (!sessionFound) {
                    fail("Session not found for username: TestUser");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Test", "Database operation canceled", databaseError.toException());
            }
        });
    }
}
