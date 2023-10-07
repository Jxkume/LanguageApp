package com.example.testi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private SharedPreferences prefs;
    private DatabaseReference databaseReference;
    private ProgressBar progressBar;
    private TextView levelNavbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);

        // Haetaan aktiviteetin alanapit
        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        //Haetaan pelien ikonit
        ImageView animalGameIcon = findViewById(R.id.animalGameIcon);
        ImageView colourGameIcon = findViewById(R.id.colorGameIcon);

        //haetaan käyttäjän avatar tietokannasta
        loadInformationFromDatabase();

        // Lisätään nappeihin klikkaustoiminnallisuus
        profileIcon.setOnClickListener(v -> {
            // Siirrytään profiiliaktiviteettiin
            Intent profile = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(profile);
            overridePendingTransition(0, 0);
        });

        settingsIcon.setOnClickListener(v -> {
            // Siirrytään asetukset-aktiviteettiin
            Intent settings = new Intent(HomeActivity.this, SettingsActivity.class);
            startActivity(settings);
            overridePendingTransition(0, 0);
        });

        animalGameIcon.setOnClickListener(v -> {
            Intent animalGame = new Intent(HomeActivity.this, AnimalGameFirstActivity.class);
            startActivity(animalGame);
            overridePendingTransition(0, 0);
        });

        colourGameIcon.setOnClickListener(v -> {
            Intent colourGame = new Intent(HomeActivity.this, ColourGameFirstActivity.class);
            startActivity(colourGame);
            overridePendingTransition(0, 0);
        });
    }

    private void loadInformationFromDatabase() {
        //haetaan ensin session ID preferensseistä
        prefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        String sessionKey = prefs.getString("currentSessionKey", "1");

        levelNavbar = findViewById(R.id.userLevel);
        progressBar = findViewById(R.id.progressBar);

        //polku tietokannassa oleviin käyttäjän tietoihin
        String path = "Sessions/" + sessionKey;
        Log.d("Session", path);

        //luodaan yhteys tietokantaan ja haetaan käyttäjän profiilikuvan ID, taso ja xp tietokannasta
        databaseReference = FirebaseDatabase.getInstance().getReference(path);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Session session = snapshot.getValue(Session.class);
                progressBar.setProgress(session.XP);
                levelNavbar.setText(String.valueOf(session.Level));
                setNavbarprofilePic(session.PhotoID);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Printataan error jos sellainen tulee vastaan
                System.err.println("Error: " + error.getMessage());
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
