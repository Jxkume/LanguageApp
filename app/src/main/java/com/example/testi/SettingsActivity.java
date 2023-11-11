package com.example.testi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity{
    private ProgressBar progressBar;
    private TextView levelNavbar;
    private Dialog popUp;
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
        loadInformationFromDatabase();

        // Lisätään nappeihin klikkaustoiminnallisuus
        homeIcon.setOnClickListener(v -> {
            // Siirrytään profiiliaktiviteettiin
            Intent home = new Intent(SettingsActivity.this, HomeActivity.class);
            home.putExtra("sessionID", sessionID);
            startActivity(home);
        });

        profileIcon.setOnClickListener(v -> {
            // Siirrytään profiiliaktiviteettiin
            Intent profile = new Intent(SettingsActivity.this, ProfileActivity.class);
            profile.putExtra("sessionID", sessionID);
            startActivity(profile);
        });

        // Haetaan kaikki liput
        ImageView currentFlag = findViewById(R.id.flagImageView);

        // Asetetaan arvot kuvien resurssien perusteella
        int finnishFlag = R.drawable.flag_fi;
        int spanishFlag = R.drawable.flag_es;
        int arabicFlag = R.drawable.flag_ar;

        // Haetaan käytetty kieli ja vaihdetaan lipun kuva sen perusteella
        String language = LanguageManager.getInstance().getLanguage();
        switch (language) {
            case "fi":
                currentFlag.setImageResource(finnishFlag);
                break;
            case "es":
                currentFlag.setImageResource(spanishFlag);
                break;
            case "ar":
                currentFlag.setImageResource(arabicFlag);
                break;
        }

        currentFlag.setOnClickListener(v -> showLanguagePopup());
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
                Log.d("Session", "Sessionien latauksessa tuli virhe");
            }
        });
    }

    private void loadInformationFromDatabase() {

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
                                // Jos avain ja sessionID löytyvät, asetetaan oikea profiilikuva navbariin
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

    //haetaan käyttäjän profiilikuvan drawable-kansiosta sen id:n perusteella ja asetetaan sen yläpalkkiin
    private void setNavbarprofilePic(int picID) {
        ImageView profilePicNavbarImageView = findViewById(R.id.newProfilePictureNavbar);

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

    // Kielen pop-up näkyville
    private void showLanguagePopup() {

        // Luodaan Dialog-olio, joka saa parametrina nykyisen aktiviteetin. Dialog = pop-up-ikkuna
        popUp = new Dialog(SettingsActivity.this);

        // Tarkistetaan, että pop-upin ikkuna on olemassa
        if (popUp.getWindow() != null) {
            // Määritetään dialogin attribuutit, joilla saadaan pop-upin haluttu sijainti näytölle
            configureDialogAttributes();
        } else {
            Log.d("Popup", "Virhe pop-upin piirtämisessä");
        }

        // Asetetaan pop-upille oikea tausta
        popUp.setContentView(R.layout.languagepopup);

        // Ladataan pop-upin komponentit
        loadFlagsInPopUp();

        // Näytetään pop-up
        popUp.show();
    }

    // Dialogin attribuuttien asetus, dialog = pop-up
    private void configureDialogAttributes() {

        // Asetetaan pop-upin ikkunan taustan väri ja himmennys
        Objects.requireNonNull(popUp.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        popUp.getWindow().setDimAmount(0.2f);

        // Haetaan ikkunan attribuutit
        WindowManager.LayoutParams params = popUp.getWindow().getAttributes();

        // Asetetaan ikkunan sijainti keskelle vaakasuunnassa ja haluttuun y-koordinaattiin pystysuunnassa
        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;

        // Asetetaan pystysuuntainen offset ikkunan yläreunasta, laskettuna näytön korkeuden perusteella
        params.y = calculateVerticalOffset();

        // Asetetaan ikkunan attribuutit
        popUp.getWindow().setAttributes(params);
    }

    // Metodi pystysuuntaisen offsetin laskemiseen näytön korkeuden perusteella
    private int calculateVerticalOffset() {

        // Haetaan näytön metriikat
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        // Haetaan puhelimen näytön korkeus
        int screenHeight = displayMetrics.heightPixels;

        // Palautetaan haluttu pystysuuntainen offset (voi säätää tarpeen mukaan), 6 on hyvä
        return screenHeight / 6;
    }

    private void loadFlagsInPopUp() {

        // Asetetaan arvot kuvien resurssien perusteella
        int finnishFlag = R.drawable.flag_fi;
        int spanishFlag = R.drawable.flag_es;
        int arabicFlag = R.drawable.flag_ar;

        // Haetaan liput
        ImageView currentFlagInPopUp = popUp.findViewById(R.id.currentFlagInPopUp);
        ImageView changeLanguageOption1 = popUp.findViewById(R.id.changeLanguageOption1);
        ImageView changeLanguageOption2 = popUp.findViewById(R.id.changeLanguageOption2);

        Log.d("ImageViews", "currentFlagInPopUp: " + currentFlagInPopUp);
        Log.d("ImageViews", "changeLanguageOption1: " + changeLanguageOption1);
        Log.d("ImageViews", "changeLanguageOption2: " + changeLanguageOption2);

        // Haetaan käytetty kieli ja vaihdetaan lipun kuva sen perusteella
        String language = LanguageManager.getInstance().getLanguage();
        switch (language) {
            case "fi":
                currentFlagInPopUp.setImageResource(finnishFlag);
                changeLanguageOption1.setImageResource(spanishFlag);
                changeLanguageOption2.setImageResource(arabicFlag);
                break;
            case "es":
                currentFlagInPopUp.setImageResource(spanishFlag);
                changeLanguageOption1.setImageResource(finnishFlag);
                changeLanguageOption2.setImageResource(arabicFlag);
                break;
            case "ar":
                currentFlagInPopUp.setImageResource(arabicFlag);
                changeLanguageOption1.setImageResource(spanishFlag);
                changeLanguageOption2.setImageResource(finnishFlag);
                break;
        }

        changeLanguageOption1.setOnClickListener(v -> changeLanguageAndDismissPopUp(changeLanguageOption1));

        // Click listener for the second language option
        changeLanguageOption2.setOnClickListener(v -> changeLanguageAndDismissPopUp(changeLanguageOption2));
    }

    // Method to change language and dismiss the pop-up based on the clicked flag
    private void changeLanguageAndDismissPopUp(ImageView flagImageView) {
        // Get the language code based on the resource associated with the clicked flag
        String newLanguage = getLanguageCodeForFlag(flagImageView);

        // Change the language using your LanguageManager
        LanguageManager.getInstance().setLocale(SettingsActivity.this, newLanguage);

        // Päivitetään kieli myös tietokantaan
        updateLanguageToDatabase(newLanguage);

        // Dismiss the pop-up after changing the language
        popUp.dismiss();

        // Käynnistetään aktiviteetti uudelleen
        @SuppressLint("UnsafeIntentLaunch") Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    // Method to retrieve language code based on the clicked flag's resource
    @SuppressLint("UseCompatLoadingForDrawables")
    private String getLanguageCodeForFlag(ImageView flagImageView) {
        // Determine the language code based on the clicked flag's resource
        if (Objects.equals(flagImageView.getDrawable().getConstantState(), getResources().getDrawable(R.drawable.flag_es).getConstantState())) {
            return "es";
        } else if (Objects.equals(flagImageView.getDrawable().getConstantState(), getResources().getDrawable(R.drawable.flag_ar).getConstantState())) {
            return "ar";
        } else {
            return "fi";
        }
    }

    private void updateLanguageToDatabase(String languageCode) {
        LanguageManager.getInstance().setLanguageToDatabase(languageCode);
    }

}

