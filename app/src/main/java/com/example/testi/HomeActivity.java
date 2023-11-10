package com.example.testi;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.testi.games.AnimalGameFirstActivity;
import com.example.testi.games.ColourGameFirstActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    private ImageView profilePicNavbarImageView;
    private ProgressBar progressBar;
    private TextView levelNavbar;
    private int sessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        sessionID = intent.getIntExtra("sessionID", -1);
        LanguageManager.getInstance().setSessionID(sessionID);
        LanguageManager.getInstance().getLanguageFromDatabase(HomeActivity.this);

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        setContentView(R.layout.homeactivity);

        // Haetaan aktiviteetin alanapit
        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        // Haetaan pelien ikonit
        ImageView animalGameIcon = findViewById(R.id.animalGameIcon);
        ImageView colourGameIcon = findViewById(R.id.colorGameIcon);

        // Haetaan käyttäjän avatar, taso ja xp tietokannasta
        loadInformationFromDatabase();

        // Lisätään nappeihin klikkaustoiminnallisuus
        profileIcon.setOnClickListener(v -> {
            // Siirrytään profiiliaktiviteettiin
            Intent profile = new Intent(HomeActivity.this, ProfileActivity.class);
            profile.putExtra("sessionID", sessionID);
            startActivity(profile);
        });

        settingsIcon.setOnClickListener(v -> {
            // Siirrytään asetukset-aktiviteettiin
            Intent settings = new Intent(HomeActivity.this, SettingsActivity.class);
            settings.putExtra("sessionID", sessionID);
            startActivity(settings);
        });

        animalGameIcon.setOnClickListener(v -> {
            Intent animalGame = new Intent(HomeActivity.this, AnimalGameFirstActivity.class);
            animalGame.putExtra("sessionID", sessionID);
            startActivity(animalGame);
            overridePendingTransition(0, 0);
        });

        colourGameIcon.setOnClickListener(v -> {
            Intent colourGame = new Intent(HomeActivity.this, ColourGameFirstActivity.class);
            colourGame.putExtra("sessionID", sessionID);
            startActivity(colourGame);
            overridePendingTransition(0, 0);
        });

    }

    void loadInformationFromDatabase() {

        levelNavbar = findViewById(R.id.userLevel);
        progressBar = findViewById(R.id.progressBar);

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
                                setNavbarprofilePic(sessionSnapshot.child("PhotoID").getValue(Integer.class));
                                progressBar.setProgress(sessionSnapshot.child("XP").getValue(Integer.class));
                                levelNavbar.setText(String.valueOf(sessionSnapshot.child("Level").getValue(Integer.class)));
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

    // Haetaan käyttäjän profiilikuvan drawable-kansiosta sen id:n perusteella ja asetetaan sen yläpalkkiin
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
