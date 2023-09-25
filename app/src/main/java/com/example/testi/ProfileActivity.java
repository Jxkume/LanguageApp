package com.example.testi;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity{
    // Asetetaan PopUpWindow muuttujan
    private PopupWindow popupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);

        // Haetaan aktiviteetin alanapit ja profiilikuva/profiilikuvanbackground
        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView settingsIcon = findViewById(R.id.settingsIcon);
        ImageView profilePictureBackground = findViewById(R.id.profilePictureBackground);
        ImageView profilePicture = findViewById(R.id.profilePicture);

        // Luodaan popUp olio
        popupWindow = createPopupWindow(this);

        // Kun käyttäjä painaa profile iconista, se siirtyy popupiin
        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!popupWindow.isShowing()){
                    int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
                    int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
                    int popupWidth = popupWindow.getWidth();
                    int popupHeight = popupWindow.getHeight();

                    int centerX = (screenWidth - popupWidth) / 2;
                    int centerY = (screenHeight - popupHeight) / 2;

                    // Laitetaan popUpin keskelle näyttöä esille
                    popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, centerX, centerY);
                }
            }
        });
        // Tässä jos käyttäjä painaa iconin backgroundilta, popuppi aukee
        profilePictureBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!popupWindow.isShowing()){
                    int screenWidth = getWindowManager().getDefaultDisplay().getWidth();
                    int screenHeight = getWindowManager().getDefaultDisplay().getHeight();
                    int popupWidth = popupWindow.getWidth();
                    int popupHeight = popupWindow.getHeight();

                    int centerX = (screenWidth - popupWidth) / 2;
                    int centerY = (screenHeight - popupHeight) / 2;

                    // Asetetaan popuppi keskellä näyttöä
                    popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, centerX, centerY);
                }
            }
        });

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



    }
    // Luodaan popupwindowin ja asetetaan sen width ja heightin
    private PopupWindow createPopupWindow(Context context) {
        PopupWindow popupWindow = new PopupWindow(context);
        View popupView = LayoutInflater.from(context).inflate(R.layout.popup_layout, null);
        popupWindow.setContentView(popupView);

        popupWindow.setWidth(950);
        popupWindow.setHeight(2000);
        popupWindow.setFocusable(true);

        return popupWindow;
    }

}
