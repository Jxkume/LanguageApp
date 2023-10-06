package com.example.testi;

import android.content.Intent;
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

//Setting userpic to be added
public class ProfileActivity extends AppCompatActivity{

    private DatabaseReference databaseReference;
    private TextView usernameTextView;
    private TextView currentXPTextView;
    private TextView goalXPTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);

        //This line is for debugging
        SessionKey.setSessionKey("-NforVBEGbNWtbHbAH52");

        // Haetaan aktiviteetin UI elementit
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

        loadUserInformationFromDatabase();

    }

    private void loadUserInformationFromDatabase() {
        // Polku käynnissä olevan sessionin tietoihin
        String path = "Sessions/" + SessionKey.getSessionKey();

        // Haetaan sessionin tiedot tietokannasta kyseisen sessionin kohdasta ja asetetaan nämä tiedot UI elementteihin
        databaseReference = FirebaseDatabase.getInstance().getReference(path);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Session session = snapshot.getValue(Session.class);
                if (session != null) {
                    usernameTextView.setText(session.Username);
                    currentXPTextView.setText(String.valueOf(session.XP));
                } else {
                    usernameTextView.setText("User not found");
                    currentXPTextView.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Printataan error jos sellainen tulee vastaan
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

}
