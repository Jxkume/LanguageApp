package com.example.testi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.testi.games.AnimalGameFirstActivity;
import com.example.testi.games.ColourGameFirstActivity;
import com.example.testi.games.FoodGameFirstActivity;
import com.example.testi.games.TimeGameFirstActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class HomeActivity extends AppCompatActivity {

    private ImageView profilePicNavbarImageView, progressBarBackground;
    private ProgressBar progressBar;
    private TextView levelNavbar;
    private int sessionID;

    // Haetaan taustamusiikki aktiviteettiin
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        sessionID = intent.getIntExtra("sessionID", -1);

        setContentView(R.layout.homeactivity);

        progressBarBackground = findViewById(R.id.progressBarBackground);

        // Haetaan aktiviteetin alanapit
        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        // Haetaan pelien ikonit
        ImageView animalGameIcon = findViewById(R.id.animalGameIcon);
        ImageView colourGameIcon = findViewById(R.id.colorGameIcon);
        ImageView foodGameIcon = findViewById(R.id.foodGameIcon);
        ImageView timeGameIcon = findViewById(R.id.timeGameIcon);

        // Haetaan käyttäjän avatar, taso ja xp tietokannasta
        loadInformationFromDatabase();

        // Lisätään nappeihin klikkaustoiminnallisuus
        profileIcon.setOnClickListener(v -> {
            // Siirrytään profiiliaktiviteettiin
            Intent profile = new Intent(HomeActivity.this, ProfileActivity.class);
            profile.putExtra("sessionID", sessionID);
            startActivity(profile);
            finish();
        });

        settingsIcon.setOnClickListener(v -> {
            // Siirrytään asetukset-aktiviteettiin
            Intent settings = new Intent(HomeActivity.this, SettingsActivity.class);
            settings.putExtra("sessionID", sessionID);
            startActivity(settings);
            finish();
        });

        animalGameIcon.setOnClickListener(v -> {
            // Siirrytään eläinpeli-aktiviteettiin
            Intent animalGame = new Intent(HomeActivity.this, AnimalGameFirstActivity.class);
            animalGame.putExtra("sessionID", sessionID);
            startActivity(animalGame);
            overridePendingTransition(0, 0);
            finish();
        });

        colourGameIcon.setOnClickListener(v -> {
            // Siirrytään väripeli-aktiviteettiin
            Intent colourGame = new Intent(HomeActivity.this, ColourGameFirstActivity.class);
            colourGame.putExtra("sessionID", sessionID);
            startActivity(colourGame);
            overridePendingTransition(0, 0);
            finish();
        });

        foodGameIcon.setOnClickListener(v -> {
            // Siirrytään ruokapeli-aktiviteettiin
            Intent foodGame = new Intent(HomeActivity.this, FoodGameFirstActivity.class);
            foodGame.putExtra("sessionID", sessionID);
            startActivity(foodGame);
            overridePendingTransition(0, 0);
            finish();
        });

        timeGameIcon.setOnClickListener(v -> {
            // Siirrytään aikapeli-aktiviteettiin
            Intent foodGame = new Intent(HomeActivity.this, TimeGameFirstActivity.class);
            foodGame.putExtra("sessionID", sessionID);
            startActivity(foodGame);
            overridePendingTransition(0, 0);
            finish();
        });

        // Tarkistetaan onko sovelluksen kieli LTR vai RTL
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            progressBarBackground.setScaleX(-1);
        }
    }

    void loadInformationFromDatabase() {

        levelNavbar = findViewById(R.id.userLevel);
        progressBar = findViewById(R.id.progressBar);

        // Haetaan tietokannasta Sessions-node
        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");
        sessionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("Language", "onDataChange() called");
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
                                progressBar.setProgress(sessionSnapshot.child("XP").getValue(Integer.class) % 100);
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

    // Musiikki käynnistyy kun onCreate on ladannut aktiviteetin komponentit
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BackgroundMusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    // Musiikkipalvelun yhteys vapautetaan kun aktiviteetti ei ole enää näkyvissä
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }

}
