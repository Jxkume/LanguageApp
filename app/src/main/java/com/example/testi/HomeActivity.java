package com.example.testi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homeactivity);

        // Haetaan aktiviteetin alanapit
        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView settingsIcon = findViewById(R.id.settingsIcon);

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

    }


}
