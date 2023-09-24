package com.example.testi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactivity);

        // Haetaan aktiviteetin alanapit
        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView profileIcon = findViewById(R.id.profileIcon);

        // Lisätään nappeihin klikkaustoiminnallisuus
        homeIcon.setOnClickListener(v -> {
            // Siirrytään profiiliaktiviteettiin
            Intent home = new Intent(SettingsActivity.this, HomeActivity.class);
            startActivity(home);
            overridePendingTransition(0, 0);
        });

        profileIcon.setOnClickListener(v -> {
            // Siirrytään profiiliaktiviteettiin
            Intent profile = new Intent(SettingsActivity.this, ProfileActivity.class);
            startActivity(profile);
            overridePendingTransition(0, 0);
        });

    }

}
