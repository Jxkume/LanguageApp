package com.example.testi;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
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

public class ProfileActivity extends AppCompatActivity{

    private DatabaseReference databaseReference;
    private TextView usernameTextView;
    private TextView currentXPTextView;
    private TextView levelText;
    private TextView goalXPTextView;

    private ImageView profilePicImageView;
    private String sessionKey;

    private SharedPreferences sharedPrefs;
    private ImageView newProfilePicture;
    private ImageView newProfilePictureInPopUp;
    private int selectedProfilePicture;
    private Dialog popUp;
    private int profilePictureID;

    @SuppressLint("MissingInflatedId")
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

        // Alustetaan elementit, joihin käyttäjän valitsema uusi profiilikuva tulee
        newProfilePicture = findViewById(R.id.newProfilePicture1);
        newProfilePictureInPopUp = findViewById(R.id.newProfilePicture2);

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

        // Profiilikuvan valitseminen
        ImageView profilePictureBackground = findViewById(R.id.profilePictureBackground);
        profilePictureBackground.setOnClickListener(v -> {
            // Kutsutaan metodia, jolla saadaan profiilikuvien pop-up näkyville
            showProfilePicturePopup();
        });
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

                if (session != null) {
                    setProfilePicture(session.PhotoID);
                    usernameTextView.setText(session.Username);
                    levelText.setText("Taso: " + session.Level);
                    currentXPTextView.setText(String.valueOf(session.XP));
                } else {
                    Log.d("Session", "Session tietojen asettamisessa virhe");
                }
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

    // Pop-upin sisällä tapahtuva profiilikuvan onClick-metodi
    public void onProfilePictureOptionClick(View view) {

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

        // Suljetaan pop-up heti uuden profiilikuvan valitsemisen jälkeen
        popUp.dismiss();

        // Tallennetaan uusi profiilikuva tietokantaan
        databaseReference.child("PhotoID").setValue(profilePictureID);
    }

    // Pop-up näkyville
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

}
