package com.example.testi;


import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NewSessionActivity extends AppCompatActivity {

    // Alustetaan muuttujat
    private ImageView newProfilePicture;
    private ImageView newProfilePictureInPopUp;
    private int selectedProfilePicture;
    private Dialog popUp;
    private EditText usernameEditText;
    private EditText ageEditText;
    private SharedPreferences sharedPreferences;
    private int latestSessionID;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsessionactivity);

        // Luodaan sharePreferences nimeltään MyPrefs SessionID:tä varten
        sharedPreferences = getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        latestSessionID = sharedPreferences.getInt("latestSessionID", 1);

        // Alustetaan elementit, joihin käyttäjän valitsema profiilikuva tulee
        newProfilePicture = findViewById(R.id.newProfilePicture1);
        newProfilePictureInPopUp = findViewById(R.id.newProfilePicture2);
        usernameEditText = findViewById(R.id.usernameEditText);
        ageEditText = findViewById(R.id.ageEditText);
        ImageView imageViewValmis = findViewById(R.id.newSessionReadyButton);

        // Kun painetaan Valmis nappulasta, tiedot tallentuvat tietokantaan ja siirrytään päänäkymälle
        imageViewValmis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Muutetaan käyttäjän asettamia tietoja stringiks ja intiksi
                String username = usernameEditText.getText().toString();
                int age = Integer.parseInt(ageEditText.getText().toString());
                String ageStr = ageEditText.getText().toString();

                // Validoidaan käyttäjän kirjoittama käyttäjätunnuksen
                if (!validUsername(username)) {
                    showAlert("Virheellinen käyttäjätunnus"," Syötä käyttäjänimeen ainoastaan kirjaimia.");
                    // Tällä returnilla estetään sen että tiedot eivät pushautuu tietokantaan. Eli toisinsanoen estää onClickin toiminnallisuuden.
                    return;
                }

                // Validoidaan käyttäjän kirjoittama ikä
                if (!validIka(ageStr)){
                    showAlert("Virheellinen ikä"," Syötä ikään ainoastaan numeroita.");
                    // Tällä returnilla estetään sen että tiedot eivät pushautuu tietokantaan. Eli toisinsanoen estää onClickin toiminnallisuuden.
                    return;
                }


                // Luodaan Session joka tallennetaan tietokantaan johon tulee käyttäjän asettamia tietoja
                Session session = new Session(latestSessionID, age, username, 0, 1, selectedProfilePicture);

                latestSessionID++;

                // Editoidaan meidän äsken luomamme MyPrefs sharedPreferences ja lisätään 1 siellä olevan arvon
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("latestSessionID", latestSessionID);
                editor.apply();

                // Siirrytään seuraavaan aktiviteettiin (Päänäkymälle)
                Intent intent = new Intent(NewSessionActivity.this, HomeActivity.class);
                startActivity(intent);

                // Finish() sulkee dialogit, näppäimistö, cursorit
                finish();
            }
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

        DatabaseReference wordsRef = FirebaseDatabase.getInstance().getReference().child("Words").child("Animalgame");

        // Valitaan oikea profiilikuva id:n perusteella
        if (profilePictureId == R.id.profilePictureOption1) {
            selectedProfilePicture = R.drawable.foxprofilepicture;
            // Ei ole fox tietokannassa
        } else if (profilePictureId == R.id.profilePictureOption2) {
            selectedProfilePicture = R.drawable.wolfprofilepicture;
            wordsRef.child("Wolf").child("PhotoID").setValue(selectedProfilePicture);
        } else if (profilePictureId == R.id.profilePictureOption3) {
            selectedProfilePicture = R.drawable.zebraprofilepicture;
            // Ei ole zebra tietokannassa
        } else if (profilePictureId == R.id.profilePictureOption4) {
            selectedProfilePicture = R.drawable.penguinprofilepicture;
            // Ei ole penguin tietokannassa
        } else if (profilePictureId == R.id.profilePictureOption5) {
            selectedProfilePicture = R.drawable.duckprofilepicture;
            // Ei ole duck tietokannassa
        } else if (profilePictureId == R.id.profilePictureOption6) {
            selectedProfilePicture = R.drawable.tigerprofilepicture;
            // Ei ole tiger tietokannassa
        } else if (profilePictureId == R.id.profilePictureOption7) {
            selectedProfilePicture = R.drawable.crocodileprofilepicture;
            // Ei ole crocodile tietokannassa
        } else if (profilePictureId == R.id.profilePictureOption8) {
            selectedProfilePicture = R.drawable.slothprofilepicture;
            // Ei ole sloth tietokannassa
        } else if (profilePictureId == R.id.profilePictureOption9) {
            selectedProfilePicture = R.drawable.catprofilepicture;
            wordsRef.child("Cat").child("PhotoID").setValue(selectedProfilePicture);
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
        popUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        popUp.getWindow().setDimAmount(0.2f);

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

    // Validoidaan käyttäjätunnuksen (Vain kirjaimia)
    private boolean validUsername(String username) {
        // regex tarkoittaa regularExpression
        String regex = "[a-zA-Z]+";
        return username.matches(regex);
    }

    // Validoidaan ikä (Vain numerot)
    private boolean validIka(String ageStr) {
        String regex = "[0-9]+";
        return ageStr.matches(regex);
    }

    // Luodaan alertti ja laitetaan viestin siihen
    private void showAlert(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(NewSessionActivity.this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", null)
                .show();
    }

}