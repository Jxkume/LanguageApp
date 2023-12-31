package com.example.testi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TestSessionManager {

    private static String testSessionKey;

    public static void createTestSession() {
        Session testSession = new Session(-1, 7, "testUser", 50, 1, 5, "es");
        FirebaseManager.getInstance().pushSessionToDatabase(testSession);
        testSessionKey = testSession.getSessionUniqueKey();
    }

    public static void createTestSessionSessionsTests() {
        Session testSession = new Session(5, 7, "testUser", 50, 5, 5, "es");
        FirebaseManager.getInstance().pushSessionToDatabase(testSession);
        testSessionKey = testSession.getSessionUniqueKey();
    }

    public static void createTestSessionGameTests() {
        Session testSession = new Session(-1, 7, "testUser", 0, 1, 5, "fi");
        FirebaseManager.getInstance().pushSessionToDatabase(testSession);
        testSessionKey = testSession.getSessionUniqueKey();
    }

    public static void deleteTestSession() {
        if (testSessionKey != null) {
            // Haetaan Session-node tietokannasta
            DatabaseReference sessionRef = FirebaseDatabase.getInstance().getReference().child("Sessions").child(testSessionKey);
            // Poistetaan oikea sessio avaimen avulla
            sessionRef.removeValue();
        }
    }

    public static String getTestSessionKey() {
        return testSessionKey;
    }

}
