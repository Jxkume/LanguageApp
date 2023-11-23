package com.example.testi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseManager {

    private static FirebaseManager instance;
    private DatabaseReference rootRef;

    private FirebaseManager() {
        FirebaseDatabase FirebaseDatabase = com.google.firebase.database.FirebaseDatabase.getInstance();
        // https://kieliappi-default-rtdb.europe-west1.firebasedatabase.app/ on meidän root
        rootRef = FirebaseDatabase.getReference();
    }

    public static synchronized FirebaseManager getInstance() {
        if (instance == null) {
            instance = new FirebaseManager();
        }
        return instance;
    }

    // Session tallentaminen tietokantaan
    public void pushSessionToDatabase(Session session) {

        // Haetaan tietokannasta Sessions-node
        DatabaseReference sessionsRef = rootRef.child("Sessions");
        // Generoidaan uniikki avain
        String sessionKey = sessionsRef.push().getKey();

        if (sessionKey != null) {

            // Tallennetaan avain Session-luokkaan
            session.setSessionUniqueKey(sessionKey);

            DatabaseReference newSessionRef = sessionsRef.child(sessionKey);

            // Asetetaan uuteen sessioon kaikki sen tiedot, jotka saadaan Session-luokasta
            newSessionRef.child("SessionID").setValue(session.getSessionID());
            newSessionRef.child("Age").setValue(session.getAge());
            newSessionRef.child("Username").setValue(session.getUsername());
            newSessionRef.child("XP").setValue(session.getXp());
            newSessionRef.child("Level").setValue(session.getLevel());
            newSessionRef.child("Language").setValue(session.getLanguage());
            newSessionRef.child("PhotoID").setValue(session.getPhotoID());

            // Asetetaan arvot väripeliin
            newSessionRef.child("ColourGame").child("Attempts").setValue(session.getColourGame().Attempts);
            newSessionRef.child("ColourGame").child("CorrectAnswers").setValue(session.getColourGame().CorrectAnswers);
            newSessionRef.child("ColourGame").child("WrongAnswers").setValue(session.getColourGame().WrongAnswers);
            newSessionRef.child("ColourGame").child("ColourGameID").setValue(session.getColourGame().ColourGameID);

            // Asetetaan arvot eläinpeliin
            newSessionRef.child("AnimalGame").child("Attempts").setValue(session.getAnimalGame().Attempts);
            newSessionRef.child("AnimalGame").child("CorrectAnswers").setValue(session.getAnimalGame().CorrectAnswers);
            newSessionRef.child("AnimalGame").child("WrongAnswers").setValue(session.getAnimalGame().WrongAnswers);
            newSessionRef.child("AnimalGame").child("AnimalGameID").setValue(session.getAnimalGame().AnimalGameID);

        }
    }

}
