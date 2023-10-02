package com.example.testi;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);

        // Haetaan aktiviteetin alanapit ja profiilikuva/profiilikuvanbackground
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

        // Haetaan profiilikuvan tausta pop-uppia varten
        ImageView profilePictureBackground = findViewById(R.id.profilePictureBackground);

        // Tällä saadaan pop-up näkyville, kun klikataan profiilikuvan taustaa
        profilePictureBackground.setOnClickListener(v -> {

            final Dialog popUp = new Dialog(ProfileActivity.this);

            popUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            popUp.getWindow().setDimAmount(0.2f);

            popUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
            popUp.setContentView(R.layout.profilepicturepopup);
            popUp.show();

        });

    }

}
