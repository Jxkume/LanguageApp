/**
 * Profiilinäkymä (ProfileActivity) on aktiviteettiluokka, joka vastaa käyttäjän profiilisivusta.
 * Luokka sisältää toiminnallisuudet käyttäjätietojen näyttämiseen, profiilikuvan vaihtamiseen
 * ja navigointiin muiden osien välillä sovelluksessa.
 *
 * Tämä aktiviteetti vuorovaikuttaa Firebase Realtime Database -palvelun kanssa käyttäjätietojen hakemiseksi ja päivittämiseksi.
 *
 * Luokka laajentaa AppCompatActivity-luokkaa ja sisältää UI-elementit, palveluyhteyden taustamusiikkipalveluun,
 * sekä tarvittavat metodit käyttäjätietojen lataamiseen ja profiilikuvan vaihtamiseen.
 */
package com.example.testi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Profiilinäkymä (ProfileActivity) vastaa käyttäjän profiilinäkymästä sovelluksessa.
 */
public class ProfileActivity extends AppCompatActivity{

    /**
     * Käyttäjän käyttäjätunnus (näytetään käyttöliittymässä).
     */
    private TextView usernameTextView;

    /**
     * Käyttäjän nykyinen kokemuspistemäärä (näytetään käyttöliittymässä).
     */
    private TextView currentXPTextView;

    /**
     * Käyttäjän tason näyttö (näytetään käyttöliittymässä).
     */
    private TextView levelText;

    /**
     * Kuvake uutta profiilikuvaa varten (näytetään käyttöliittymässä).
     */
    private ImageView newProfilePicture;

    /**
     * Kuvake uutta profiilikuvaa varten pop-up-ikkunassa (näytetään käyttöliittymässä).
     */
    private ImageView newProfilePictureInPopUp;

    /**
     * Valitun profiilikuvan indeksi.
     */
    private int selectedProfilePicture;

    /**
     * Dialogi-ikkuna profiilikuvan valintapop-up-ikkunalle.
     */
    private Dialog popUp;

    /**
     * Käyttäjän profiilikuvan ID tietokannassa.
     */
    private int profilePictureID;

    /**
     * Käyttäjän istunnon ID.
     */
    private int sessionID;

    /**
     * Käyttäjän tason arvo.
     */
    private int lvl;

    /**
     * Musiikin haku aktiviteettiin.
     */
    BackgroundMusicService musicService;

    /**
     * Kertoo, onko aktiviteetti liitetty taustamusiikkipalveluun vai ei.
     */
    private boolean isBound = false;

    /**
     * Taustamusiikkipalveluun liittyvä palveluyhteys.
     */
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BackgroundMusicService.LocalBinder binder = (BackgroundMusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;

            // Päivitä äänenvoimakkuusasetukset
            SharedPreferences sharedPref = getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
            int savedBgmVol = sharedPref.getInt("bgmVolume", 100);
            musicService.setMusicVolume(savedBgmVol);
            musicService.playBackgroundMusic();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBound = false;
        }
    };


    /**
     * Kutsutaan, kun aktiviteetti luodaan. Vastuussa käyttöliittymäkomponenttien alustamisesta,
     * klikkikuuntelijoiden asettamisesta ja käyttäjätietojen lataamisesta Firebase-tietokannasta.
     *
     * @param savedInstanceState Bundle-objekti, joka sisältää aiemmin tallennetun tilan.
     */
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.profileactivity);

        Intent intent = getIntent();
        sessionID = intent.getIntExtra("sessionID", -1);

        // Haetaan aktiviteetin UI elementit
        levelText = findViewById(R.id.levelText);
        usernameTextView = findViewById(R.id.usernameText);
        currentXPTextView = findViewById(R.id.currentXpText);
        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView settingsIcon = findViewById(R.id.settingsIcon);

        // Alustetaan elementit, joihin käyttäjän valitsema uusi profiilikuva tulee
        newProfilePicture = findViewById(R.id.newProfilePicture1);
        newProfilePictureInPopUp = findViewById(R.id.newProfilePicture2);

        // Lisätään nappeihin klikkaustoiminnallisuus
        homeIcon.setOnClickListener(v -> {
            // Siirrytään kotiaktiviteettiin
            if (isBound && musicService != null) {
                musicService.playUIbtnSound();
            }
            Intent home = new Intent(ProfileActivity.this, HomeActivity.class);
            home.putExtra("sessionID", sessionID);
            startActivity(home);
            finish();
        });

        settingsIcon.setOnClickListener(v -> {
            // Siirrytään asetukset-aktiviteettiin
            if (isBound && musicService != null) {
                musicService.playUIbtnSound();
            }
            Intent settings = new Intent(ProfileActivity.this, SettingsActivity.class);
            settings.putExtra("sessionID", sessionID);
            startActivity(settings);
            finish();
        });

        // Haetaan käyttäjän tiedot tietokannasta
        loadUserInformationFromDatabase();

        // Profiilikuvan valitseminen
        ImageView profilePictureBackground = findViewById(R.id.profilePictureBackground);
        profilePictureBackground.setOnClickListener(v -> {
            // Kutsutaan metodia, jolla saadaan profiilikuvien pop-up näkyville
            if (isBound && musicService != null) {
                musicService.playUIbtnSound();
            }
            showProfilePicturePopup();
        });
    }

    /**
     * Lataa käyttäjän tiedot Firebase Realtime Databasesta ja päivittää käyttöliittymän sen mukaisesti.
     * Tätä metodia kutsutaan aktiviteetin luonnin yhteydessä näyttämään käyttäjän profiilitiedot.
     */
    private void loadUserInformationFromDatabase() {
        // Haetaan tietokannasta Sessions-node
        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");
        sessionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
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
                                // Jos avain ja sessionID löytyvät, asetetaan oikeat tiedot
                                // Otetaan käyttäjän taso tietokannasta
                                lvl = sessionSnapshot.child("Level").getValue(Integer.class);
                                setProfilePicture(sessionSnapshot.child("PhotoID").getValue(Integer.class));
                                usernameTextView.setText("- " + sessionSnapshot.child("Username").getValue(String.class) + " -");
                                levelText.setText(getString(R.string.level) + " " + String.valueOf(lvl));
                                String xp = getString(R.string.xp);
                                // Laitetaan tekstikenttään käyttäjän xp sekä nykyisev tason tavoite, jonka saadaan kutsumalla getGoalXp-metodia
                                currentXPTextView.setText(sessionSnapshot.child("XP").getValue(Long.class) + " / " + String.valueOf(getGoalXp()) + " " + xp);
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

    /**
     * Asettaa profiilikuvan annetun kuvan ID:n perusteella.
     *
     * @param picID Valittu profiilikuvan ID.
     */
    public void setProfilePicture(int picID) {
        ImageView profilePicImageView = findViewById(R.id.newProfilePicture1);

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


    /**
     * Käsittelee klikkaustapahtuman, kun profiilikuvan vaihtoehto valitaan pop-up-ikkunassa.
     *
     * @param view Klikatun profiilikuvan vaihtoehdon näkymä.
     */
    public void onProfilePictureOptionClick(View view) {

        if (isBound && musicService != null) {
            musicService.playUIbtnSound();
        }

        // Haetaan klikatun kuvan id
        int profilePictureId = view.getId();

        // Valitaan oikea profiilikuva id:n perusteella
        if (profilePictureId == R.id.profilePictureOption1) {
            selectedProfilePicture = R.drawable.foxprofilepicture;
            profilePictureID = 1;
        } else if (profilePictureId == R.id.profilePictureOption2) {
            selectedProfilePicture = R.drawable.wolfprofilepicture;
            profilePictureID = 2;
        } else if (profilePictureId == R.id.profilePictureOption3) {
            selectedProfilePicture = R.drawable.zebraprofilepicture;
            profilePictureID = 3;
        } else if (profilePictureId == R.id.profilePictureOption4) {
            selectedProfilePicture = R.drawable.penguinprofilepicture;
            profilePictureID = 4;
        } else if (profilePictureId == R.id.profilePictureOption5) {
            selectedProfilePicture = R.drawable.duckprofilepicture;
            profilePictureID = 5;
        } else if (profilePictureId == R.id.profilePictureOption6) {
            selectedProfilePicture = R.drawable.tigerprofilepicture;
            profilePictureID = 6;
        } else if (profilePictureId == R.id.profilePictureOption7) {
            selectedProfilePicture = R.drawable.crocodileprofilepicture;
            profilePictureID = 7;
        } else if (profilePictureId == R.id.profilePictureOption8) {
            selectedProfilePicture = R.drawable.slothprofilepicture;
            profilePictureID = 8;
        } else if (profilePictureId == R.id.profilePictureOption9) {
            selectedProfilePicture = R.drawable.catprofilepicture;
            profilePictureID = 9;
        }

        // Päivitetään aktiviteetissa oleva profiilikuva valitulla kuvalla
        if (newProfilePicture != null) {
            newProfilePicture.setImageResource(selectedProfilePicture);
        }

        // Päivitetään pop-upin sisällä oleva profiilikuva valitulla kuvalla
        if (newProfilePictureInPopUp != null) {
            newProfilePictureInPopUp.setImageResource(selectedProfilePicture);
        }

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
                                // Jos avain ja sessionID löytyvät, asetetaan uusi profiilikuva
                                sessionSnapshot.child("PhotoID").getRef().setValue(profilePictureID);
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

        // Suljetaan pop-up heti uuden profiilikuvan valitsemisen jälkeen
        popUp.dismiss();
    }

    /**
     * Näyttää pop-up-ikkunan uuden profiilikuvan valitsemista varten.
     * Pop-up mahdollistaa käyttäjän valita eri profiilikuvavaihtoehdoista.
     */
    private void showProfilePicturePopup() {

        // Luodaan Dialog-olio, joka saa parametrina nykyisen aktiviteetin. Dialog = pop-up-ikkuna
        popUp = new Dialog(ProfileActivity.this);

        // Pop-upin taustan ulkonäön muokkaaminen, ilman näitä tausta olisi valkoinen ja pop-upin ulkopuolinen alue tummempi
        if (popUp.getWindow() != null) {
            popUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            popUp.getWindow().setDimAmount(0.2f);
        } else {
            Log.d("Popup", "Virhe pop-upin piirtämisessä");
        }

        // Haetaan pop-upin XML-tiedosto, joka kertoo miltä pop-upin ulkonäön tulee näyttää
        popUp.setContentView(R.layout.profilepicturepopup);

        // Haetaan pop-upin sisällä oleva ImageView, jossa on käyttäjän valitsema kuva
        // Ilman tätä pop-up ei muista minkä kuvan käyttäjä on valinnut, jos käyttäjä sulkee pop-upin ja avaa sen uudestaan
        ImageView popupProfilePicture = popUp.findViewById(R.id.newProfilePicture2);
        if (popupProfilePicture != null) {
            popupProfilePicture.setImageResource(selectedProfilePicture);
        }

        // Pop-upin asettaminen näkyviin
        popUp.show();
    }

    /**
     * Palauttaa nykyisen tason vaatiman kokemuspistemäärän.
     *
     * @return Nykyisen tason vaatima kokemuspistemäärä.
     */
    private int getGoalXp() {
        switch (lvl){
            case 1: return 100;
            case 2: return 200;
            case 3: return 300;
            case 4: return 400;
            case 5: return 500;
            default: return 0;

        }
    }

    /**
     * Kutsutaan, kun aktiviteetti tulee näkyväksi käyttäjälle.
     * Sitoo aktiviteetin taustamusiikkipalveluun musiikin toistamiseksi.
     */
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BackgroundMusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        SharedPreferences sharedPref = getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        int savedBgmVol = sharedPref.getInt("bgmVolume", 100);
    }

    /**
     * Kutsutaan, kun aktiviteetti ei ole enää näkyvissä käyttäjälle.
     * Irrottaa aktiviteetin taustamusiikkipalvelusta vapauttaakseen yhteyden.
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    /**
     * Kutsutaan, kun aktiviteetti on jälleen näkyvissä käyttäjälle.
     * Päivittää musiikin äänenvoimakkuusasetukset ja jatkaa musiikin toistamista.
     */
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        int savedBgmVol = sharedPref.getInt("bgmVolume", 100);
        if (isBound && musicService != null) {
            musicService.setMusicVolume(savedBgmVol);
            musicService.playBackgroundMusic();
        }
    }
}
