package com.example.testi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity{
    private ImageView profilePicNavbarImageView;
    private int sessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactivity);

        Intent intent = getIntent();
        sessionID = intent.getIntExtra("sessionID", -1);

        // Haetaan aktiviteetin alanapit
        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView deleteUserButton = findViewById(R.id.deleteUserButton);

        deleteUserButton.setOnClickListener(v -> {
        deleteSessionData();
        // Sit kun sessions näkymä on done pitää laittaa tähän if else jnejnejne.
        Intent intent2 = new Intent(SettingsActivity.this, SplashActivity.class);
        intent2.putExtra("sessionID", sessionID);
        startActivity(intent2);
    });

        //haetaan käyttäjän avatar tietokannasta
        loadProfilePicFromDatabase();

        // Lisätään nappeihin klikkaustoiminnallisuus
        homeIcon.setOnClickListener(v -> {
            // Siirrytään profiiliaktiviteettiin
            Intent home = new Intent(SettingsActivity.this, HomeActivity.class);
            home.putExtra("sessionID", sessionID);
            startActivity(home);
            overridePendingTransition(0, 0);
        });

        profileIcon.setOnClickListener(v -> {
            // Siirrytään profiiliaktiviteettiin
            Intent profile = new Intent(SettingsActivity.this, ProfileActivity.class);
            profile.putExtra("sessionID", sessionID);
            startActivity(profile);
            overridePendingTransition(0, 0);
        });

    }

    private void deleteSessionData() {
        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference("Sessions");
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
                                // Jos avain ja sessionID löytyvät, poistetaan sessio
                                sessionsRef.child(sessionKey).removeValue();
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadProfilePicFromDatabase() {
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
                                // Jos avain ja sessionID löytyvät, asetetaan oikea profiilikuva navbariin
                                setNavbarprofilePic(sessionSnapshot.child("PhotoID").getValue(Integer.class));
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Session", "Sessionien latauksessa tuli virhe");
            }
        });
    }

    //haetaan käyttäjän profiilikuvan drawable-kansiosta sen id:n perusteella ja asetetaan sen yläpalkkiin
    private void setNavbarprofilePic(int picID) {
        profilePicNavbarImageView = findViewById(R.id.newProfilePictureNavbar);

        switch (picID) {
            case 1:
                profilePicNavbarImageView.setImageResource(R.drawable.foxprofilepicture);
                break;
            case 2:
                profilePicNavbarImageView.setImageResource(R.drawable.wolfprofilepicture);
                break;
            case 3:
                profilePicNavbarImageView.setImageResource(R.drawable.zebraprofilepicture);
                break;
            case 4:
                profilePicNavbarImageView.setImageResource(R.drawable.penguinprofilepicture);
                break;
            case 5:
                profilePicNavbarImageView.setImageResource(R.drawable.duckprofilepicture);
                break;
            case 6:
                profilePicNavbarImageView.setImageResource(R.drawable.tigerprofilepicture);
                break;
            case 7:
                profilePicNavbarImageView.setImageResource(R.drawable.crocodileprofilepicture);
                break;
            case 8:
                profilePicNavbarImageView.setImageResource(R.drawable.slothprofilepicture);
                break;
            case 9:
                profilePicNavbarImageView.setImageResource(R.drawable.catprofilepicture);
        }
    }

}
