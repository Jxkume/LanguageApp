package com.example.testi;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
public class ProfileActivity extends AppCompatActivity{

    private DatabaseReference databaseReference;
    private TextView usernameTextView;
    private TextView currentXPTextView;
    private TextView levelText;
    private TextView goalXPTextView;

    private ImageView profilePicImageView;
    private String sessionKey;

    private SharedPreferences sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);

        //Haetaan session id, joka on tallennettu preferensseihin
        sharedPrefs = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        sessionKey = sharedPrefs.getString("currentSessionKey", "1");


        // Haetaan aktiviteetin UI elementit
        levelText = findViewById(R.id.levelText);
        usernameTextView = findViewById(R.id.usernameText);
        currentXPTextView = findViewById(R.id.currentXpText);
        goalXPTextView = findViewById(R.id.xpGoalText);
        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        // Lisätään nappeihin klikkaustoiminnallisuus
        homeIcon.setOnClickListener(v -> {
            // Siirrytään kotiaktiviteettiin
            Intent home = new Intent(ProfileActivity.this, HomeActivity.class);
            startActivity(home);
            overridePendingTransition(0, 0);
        });

        settingsIcon.setOnClickListener(v -> {
            // Siirrytään asetukset-aktiviteettiin
            Intent settings = new Intent(ProfileActivity.this, SettingsActivity.class);
            startActivity(settings);
            overridePendingTransition(0, 0);
        });

        //Haetaan käyttäjän tiedot tietokannasta
        loadUserInformationFromDatabase();

    }

    private void loadUserInformationFromDatabase() {
        //polku sessionin tietoihin tietokannassa
        String path = "Sessions/" + sessionKey;

        // Haetaan sessionin tiedot tietokannasta kyseisen sessionin kohdasta ja asetetaan nämä tiedot UI elementteihin
        databaseReference = FirebaseDatabase.getInstance().getReference(path);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Session session = snapshot.getValue(Session.class);
                setProfilePicture(session.PhotoID);
                usernameTextView.setText(session.Username);
                levelText.setText("Taso: " + String.valueOf(session.Level));
                currentXPTextView.setText(String.valueOf(session.XP));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Printataan error jos sellainen tulee vastaan
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

    //asetetaan profiilikuvaa tietokannassa olevan ID:n perusteella
    public void setProfilePicture(int picID) {
        profilePicImageView = findViewById(R.id.newProfilePicture1);

        switch (picID) {
            case 1:
                profilePicImageView.setImageResource(R.drawable.foxprofilepicture);
                break;
            case 2:
                profilePicImageView.setImageResource(R.drawable.wolfprofilepicture);
                break;
            case 3:
                profilePicImageView.setImageResource(R.drawable.zebraprofilepicture);
                break;
            case 4:
                profilePicImageView.setImageResource(R.drawable.penguinprofilepicture);
                break;
            case 5:
                profilePicImageView.setImageResource(R.drawable.duckprofilepicture);
                break;
            case 6:
                profilePicImageView.setImageResource(R.drawable.tigerprofilepicture);
                break;
            case 7:
                profilePicImageView.setImageResource(R.drawable.crocodileprofilepicture);
                break;
            case 8:
                profilePicImageView.setImageResource(R.drawable.slothprofilepicture);
                break;
            case 9:
                profilePicImageView.setImageResource(R.drawable.catprofilepicture);
        }
    }

}
