package com.example.testi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
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
    private ImageView progressBarBackground;
    private BackgroundMusicService musicService;
    AudioManager audioMngr;
    private int appVolume;
    private boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactivity);

        // AudioManagerilla saadaan äänenvoimakkuus
        audioMngr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        //audioMngr = (AudioManager) getSystemService(AUDIO_SERVICE);

        Intent intent = getIntent();
        sessionID = intent.getIntExtra("sessionID", -1);
        LanguageManager.getInstance().setSessionID(sessionID);
        LanguageManager.getInstance().getLanguageFromDatabase(SettingsActivity.this);

        progressBarBackground = findViewById(R.id.progressBarBackground);

        // Tarkistetaan onko sovelluksen kieli LTR vai RTL
        Configuration config = getResources().getConfiguration();
        if (config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
            progressBarBackground.setScaleX(-1);
        }

        // Haetaan aktiviteetin alanapit
        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView deleteUserButton = findViewById(R.id.deleteUserButton);

        // Haetaan käyttäjän avatar tietokannasta
        loadInformationFromDatabase();

        // Lisätään nappeihin klikkaustoiminnallisuus
        homeIcon.setOnClickListener(v -> {
            // Siirrytään profiiliaktiviteettiin
            Intent home = new Intent(SettingsActivity.this, HomeActivity.class);
            home.putExtra("sessionID", sessionID);
            startActivity(home);
            finish();
        });

        profileIcon.setOnClickListener(v -> {
            // Siirrytään profiiliaktiviteettiin
            Intent profile = new Intent(SettingsActivity.this, ProfileActivity.class);
            profile.putExtra("sessionID", sessionID);
            startActivity(profile);
            finish();
        });

        // Asetetaan arvot kuvien resurssien perusteella
        int finnishFlag = R.drawable.flag_fi;
        int spanishFlag = R.drawable.flag_es;
        int arabicFlag = R.drawable.flag_ar;

        // Haetaan käytetty kieli ja vaihdetaan lipun kuva sen perusteella
        String language = LanguageManager.getInstance().getLanguage(this);
        ImageView currentFlag = findViewById(R.id.flagImageView);
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

        // Käyttäjän poistaminen onClick
        deleteUserButton.setOnClickListener(v -> deleteUser());

        // Kielen pop-up onClick
        currentFlag.setOnClickListener(v -> showLanguagePopup());

       // Musiikin ja äänien SeekBarit
        SeekBar soundSeekBar = (SeekBar) findViewById(R.id.soundSettindsSlider);
        // Musiikin ja äänien SeekBarit
        SeekBar musicSeekBar = findViewById(R.id.musicSettindsSlider);

        //Haetaan mediaplayerin äänenvoimakkuuden maksimiarvo
        int maxVolume = BackgroundMusicService.getMaxVolume();
        musicSeekBar.setMax(maxVolume);

        //Haetaan äänenvoimakkuuden nykyinen arvo
        appVolume = BackgroundMusicService.getCurrentVolume();
        musicSeekBar.setProgress(appVolume);

        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                //Päivitetään MediaPlayerin äänenvoimakkuus
                BackgroundMusicService.setVolume(i);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    // Käyttäjän poistaminen
    private void deleteUser() {
        deleteSessionData();
        Intent intent2 = new Intent(SettingsActivity.this, SplashActivity.class);
        startActivity(intent2);
        finish();
    }

    // Käyttäjän poistaminen tietokannasta
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

    // Käyttäjän tietojen haku tietokannasta ja niiden asettaminen navbariin
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

    // Haetaan käyttäjän profiilikuva drawable-kansiosta sen id:n perusteella ja asetetaan sen yläpalkkiin
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

        // Määritetään dialogin attribuutit, joilla saadaan pop-upin haluttu sijainti näytölle
        if (popUp.getWindow() != null) {
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

    // Eri kielten lippujen lataus pop-upiin
    private void loadFlagsInPopUp() {

        // Asetetaan arvot kuvien resurssien perusteella
        int finnishFlag = R.drawable.flag_fi;
        int spanishFlag = R.drawable.flag_es;
        int arabicFlag = R.drawable.flag_ar;

        // Haetaan liput
        ImageView currentFlagInPopUp = popUp.findViewById(R.id.currentFlagInPopUp);
        ImageView changeLanguageOption1 = popUp.findViewById(R.id.changeLanguageOption1);
        ImageView changeLanguageOption2 = popUp.findViewById(R.id.changeLanguageOption2);

        // Haetaan käytetty kieli ja vaihdetaan lipun kuva sen perusteella
        String language = LanguageManager.getInstance().getLanguage(this);
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

        // Kielen vaihto ensimmäiseen kielivaihtoehtoon
        changeLanguageOption1.setOnClickListener(v -> changeLanguage(changeLanguageOption1));

        // Kielen vaihto toiseen kielivaihtoehtoon
        changeLanguageOption2.setOnClickListener(v -> changeLanguage(changeLanguageOption2));
    }

    // Sovelluksen kielen vaihto ja aktiviteetin uudelleenkäynnistys
    @SuppressLint("UnsafeIntentLaunch")
    private void changeLanguage(ImageView flagImageView) {

        // Haetaan uusi kieli
        String newLanguage = getLanguageCodeForFlag(flagImageView);

        // Vaihdetaan sovelluksen kieli
        LanguageManager.getInstance().setLocale(SettingsActivity.this, newLanguage);

        // Päivitetään uusi kieli tietokantaan
        LanguageManager.getInstance().setLanguageToDatabase(newLanguage);

        // Suljetaan pop-up
        popUp.dismiss();

        // Odotetaan hetki ennen kuin käynnistetään aktiviteetti uudelleen
        new Handler().postDelayed(() -> {
            startActivity(getIntent());
            finish();
        }, 100);
    }

    // Kielen haku klikatun lipun perusteella
    @SuppressLint("UseCompatLoadingForDrawables")
    private String getLanguageCodeForFlag(ImageView flagImageView) {
        if (Objects.equals(flagImageView.getDrawable().getConstantState(), getResources().getDrawable(R.drawable.flag_es).getConstantState())) {
            return "es";
        } else if (Objects.equals(flagImageView.getDrawable().getConstantState(), getResources().getDrawable(R.drawable.flag_ar).getConstantState())) {
            return "ar";
        } else {
            return "fi";
        }
    }

    //Haetaan taustamusiikki aktiviteettiin
    ServiceConnection serviceConnection = new ServiceConnection() {

        // onServiceConnected() kutsutaan kun yhteys on luotu
        @Override

        public void onServiceConnected(ComponentName name, IBinder service) {
            // Haetaan LocalBinderin avulla BackgroundMusicServicen instanssi
            BackgroundMusicService.LocalBinder binder = (BackgroundMusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;/*

            // Haetaan musiikin SeekBar
            SeekBar volumeSeekBar = findViewById(R.id.musicSettindsSlider);

            // Haetaan järjestelmän max ja nykyinen volume
            int maxVol = audioMngr.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            volumeSeekBar.setMax(maxVol);  // Äänet täysillä

            int currentVol = audioMngr.getStreamVolume(AudioManager.STREAM_MUSIC);
            volumeSeekBar.setProgress(currentVol); // Äänet nykyisellä voimakkuudella

            // Listener, joka kutsutaan kun SeekBarin arvoa muutetaan
            volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

                // Kun arvoa muutetaan, asetetaan musiikin volume
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (isBound) {
                        float volume = progress / (float) maxVol;
                        musicService.setVolume(volume);
                    }
                    // Asetetaan järjestelmän volume
                    audioMngr.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                }

                // onStarTrackingTouch() ja onStopTrackingTouch() eivät tee mitään
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {}

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {}
            });*/
        }

        // onServiceDisconnected() kutsutaan kun yhteys katkaistaan
        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };

    // Musiikki käynnistyy kun onCreate on ladannut aktiviteetin komponentit
    // onStart() kutsutaan kun aktiviteetti tulee näkyviin
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BackgroundMusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    // onStop() kutsutaan kun aktiviteetti ei ole enää näkyvissä
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }

}