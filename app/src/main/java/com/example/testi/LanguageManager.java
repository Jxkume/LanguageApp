package com.example.testi;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class LanguageManager {

    private static LanguageManager instance;
    private String selectedLanguage;
    private int sessionID;
    private String language;

    private LanguageManager() {
        // Private constructor to prevent instantiation
    }

    public static LanguageManager getInstance() {
        if (instance == null) {
            instance = new LanguageManager();
        }
        return instance;
    }

    public String getSelectedLanguage() {
        return selectedLanguage != null ? selectedLanguage : "fi"; // Default to "fi" if not set
    }

    public void setSelectedLanguage(Context context, String languageCode) {
        selectedLanguage = languageCode;
        setLocale(context, languageCode);
    }

    // Kielen asettaminen sovellukseen
    public void setLocale(Context context, String languageCode) {
        Configuration configuration = context.getResources().getConfiguration();
        Locale newLocale = new Locale(languageCode);
        configuration.setLocale(newLocale);
        context.getResources().updateConfiguration(configuration, context.getResources().getDisplayMetrics());
    }

    // Kielen asettaminen tietokantaan
    public void setLanguageToDatabase(String languageCode) {
        // Haetaan tietokannasta Sessions-node
        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");
        sessionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                        // Haetaan sessionin avain
                        String sessionKey = sessionSnapshot.getKey();
                        // Haetaan sessionID
                        Long sessionIDLong = sessionSnapshot.child("SessionID").getValue(Long.class);
                        if (sessionKey != null) {
                            if (sessionIDLong != null && sessionIDLong == sessionID) {
                                // Jos avain ja sessionID löytyvät, asetetaan vaihdettu kieli
                                sessionSnapshot.child("Language").getRef().setValue(languageCode);
                                language = languageCode;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Session", "Kielen tallentamisessa tapahtui virhe.");
            }
        });

    }

    // Hakee tietokannasta kielen, jota käyttäjä käyttää
    public void getLanguageFromDatabase(Context context) {

        // Haetaan tietokannasta Sessions-node
        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");
        sessionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                        // Haetaan sessionin avain
                        String sessionKey = sessionSnapshot.getKey();
                        // Haetaan sessionID
                        Long sessionIDLong = sessionSnapshot.child("SessionID").getValue(Long.class);
                        if (sessionKey != null) {
                            if (sessionIDLong != null && sessionIDLong == sessionID) {
                                // Jos avain ja sessionID löytyvät, asetetaan oikea profiilikuva, xp ja taso navbariin
                                language = sessionSnapshot.child("Language").getValue(String.class);
                                // Asetetaan sovellukseen oikea kieli, joka saadaan tietokannasta
                                setLocale(context, language);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void setSessionID(int sessionID) {
        this.sessionID = sessionID;
    }

    public String getLanguage() {
        return language;
    }

}