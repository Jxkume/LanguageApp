package com.example.testi;

import android.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.HashMap;

public class NewSessionActivity extends AppCompatActivity {

    private ImageView newProfilePicture;
    private ImageView newProfilePictureInPopUp;
    private int selectedProfilePicture;
    private Dialog popUp;
    private EditText usernameEditText;
    private EditText ageEditText;
    private boolean isProfilePictureSelected = false;
    private int profilePictureID;
    private HashMap buttonSessionPairs;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsessionactivity);

        // Haetaan edellisen aktiviteetin napin arvo (eli mitä nappia painettiin), jotta voidaan asettaa oikea sessionID
        Intent intent = getIntent();
        int sessionID = intent.getIntExtra("buttonClicked", -1);

        // Alustetaan elementit, joihin käyttäjän valitsema profiilikuva tulee
        newProfilePicture = findViewById(R.id.newProfilePicture1);
        newProfilePictureInPopUp = findViewById(R.id.newProfilePicture2);

        usernameEditText = findViewById(R.id.usernameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        ImageView imageViewValmis = findViewById(R.id.newSessionReadyButton);

        // Kun painetaan Valmis nappulasta, tiedot tallentuvat tietokantaan ja siirrytään päänäkymälle
        imageViewValmis.setOnClickListener(v -> {

            String username = usernameEditText.getText().toString();
            String ageStr = ageEditText.getText().toString();

            // Jos kaikki kentät on tyhjiä heitetään alert
            if (username.trim().isEmpty() && ageStr.trim().isEmpty() && !isProfilePictureSelected) {
                showAlert("Täytä kaikki kentät", "Täytä kaikki tiedot");
                return;
            }

            // Jos nimi ja ikä kentät ovat tyhjiä heitetään alert
            if (username.trim().isEmpty() && ageStr.trim().isEmpty()){
                showAlert("Täytä kaikki kentät", "Ole hyvä ja täytä nimi- ja ikäkentät jatkaaksesi");
                return;
            }

            // Jos nimi kenttä on tyhjä ja profiilikuva ei ole valittu heitetään alert
            if (username.trim().isEmpty() && !isProfilePictureSelected){
                showAlert("Täytä kaikki kentät", "Ole hyvä ja valitse profiilikuva sekä täytä nimikenttä jatkaaksesi");
                return;
            }

            // Jos ikä kenttä on tyhjä ja profiilikuva ei ole valittu heitetään alert
            if (ageStr.trim().isEmpty() && !isProfilePictureSelected){
                showAlert("Täytä kaikki kentät", "Ole hyvä ja valitse profiilikuva sekä täytä ikäkenttä jatkaaksesi");
                return;
            }

            // Jos ainoastaan nimi kenttä on tyhjä heitetään alert
            if (username.trim().isEmpty()){
                showAlert("Täytä nimikenttä", "Täytä nimikenttä jatkaaksesi");
                return;
            }

            // Jos ainoastaan ikä kenttä on tyhjä heitetään alert
            if (ageStr.trim().isEmpty()) {
                showAlert("Täytä ikäkenttä", "Täytä ikäkenttä jatkaaksesi");
                return;
            }

            // Jos ainoastaa profiilikuva ei ole valittu heitetään alert
            if (!isProfilePictureSelected){
                showAlert("Valitse profiilikuva", "Valitse profiilikuva jatkaaksesi");
                return;
            }

            // Validoidaan käyttäjän kirjoittama käyttäjätunnuksen
            if (!validUsername(username)) {
                showAlert("Virheellinen käyttäjätunnus","Syötä käyttäjänimeen ainoastaan kirjaimia.");
                return;
            }

            // Luodaan sessio vain silloin, kun session arvo ollaan saatu edellisestä aktiviteetista (jos arvoa ei olla saatu, arvo on defaulttina -1)
            if (sessionID != -1) {
                int age = Integer.parseInt(ageStr);
                Session session = new Session(sessionID, age, username, 0, 1, profilePictureID);
            }

            Intent home = new Intent(NewSessionActivity.this, HomeActivity.class);
            home.putExtra("sessionID", sessionID);
            startActivity(home);

            finish();
        });

        // Profiilikuvan valitseminen
        ImageView profilePictureBackground = findViewById(R.id.newProfilePictureBackground1);
        profilePictureBackground.setOnClickListener(v -> {
            // Kutsutaan metodia, jolla saadaan profiilikuvien pop-up näkyville
            showProfilePicturePopup();
        });
    }

    // Pop-upin sisällä tapahtuva profiilikuvan onClick-metodi
    public void onProfilePictureOptionClick(View view) {

        // Haetaan klikatun kuvan id
        int profilePictureId = view.getId();

        // Valitaan oikea profiilikuva id:n perusteella
        if (profilePictureId == R.id.profilePictureOption1) {
            selectedProfilePicture = R.drawable.foxprofilepicture;
            isProfilePictureSelected = true;
            profilePictureID = 1;
        } else if (profilePictureId == R.id.profilePictureOption2) {
            selectedProfilePicture = R.drawable.wolfprofilepicture;
            isProfilePictureSelected = true;
            profilePictureID = 2;
        } else if (profilePictureId == R.id.profilePictureOption3) {
            selectedProfilePicture = R.drawable.zebraprofilepicture;
            isProfilePictureSelected = true;
            profilePictureID = 3;
        } else if (profilePictureId == R.id.profilePictureOption4) {
            selectedProfilePicture = R.drawable.penguinprofilepicture;
            isProfilePictureSelected = true;
            profilePictureID = 4;
        } else if (profilePictureId == R.id.profilePictureOption5) {
            selectedProfilePicture = R.drawable.duckprofilepicture;
            isProfilePictureSelected = true;
            profilePictureID = 5;
        } else if (profilePictureId == R.id.profilePictureOption6) {
            selectedProfilePicture = R.drawable.tigerprofilepicture;
            isProfilePictureSelected = true;
            profilePictureID = 6;
        } else if (profilePictureId == R.id.profilePictureOption7) {
            selectedProfilePicture = R.drawable.crocodileprofilepicture;
            isProfilePictureSelected = true;
            profilePictureID = 7;
        } else if (profilePictureId == R.id.profilePictureOption8) {
            selectedProfilePicture = R.drawable.slothprofilepicture;
            isProfilePictureSelected = true;
            profilePictureID = 8;
        } else if (profilePictureId == R.id.profilePictureOption9) {
            selectedProfilePicture = R.drawable.catprofilepicture;
            isProfilePictureSelected = true;
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
    }

    // Pop-up näkyville
    private void showProfilePicturePopup() {

        // Luodaan Dialog-olio, joka saa parametrina nykyisen aktiviteetin. Dialog = pop-up-ikkuna
        popUp = new Dialog(NewSessionActivity.this);

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

    // Validoidaan käyttäjätunnuksen (vain kirjaimia)
    private boolean validUsername(String username) {
        // regex tarkoittaa regularExpression
        String regex = "[a-zA-Z]+";
        return username.matches(regex);
    }

    // Luodaan alertti ja laitetaan siihen viesti
    private void showAlert(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSessionActivity.this);
        builder.setTitle(title).setMessage(message).setPositiveButton("OK", null).show();
    }
}