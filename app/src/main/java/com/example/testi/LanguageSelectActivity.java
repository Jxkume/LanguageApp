package com.example.testi;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public class LanguageSelectActivity extends AppCompatActivity {

    private ImageView flagFiBackground, flagEsBackground, flagArBackground;
    private ImageView nextButton;
    private TextView nextButtonTextView;
    private int selectedFlag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.languageselectactivity);

        // Haetaan edellisen aktiviteetin napin arvo (eli mitä nappia painettiin), jotta voidaan asettaa oikea sessionID seuraavassa aktiviteetissa
        Intent intent = getIntent();
        int sessionID = intent.getIntExtra("buttonClicked", -1);

        // Alustetaan komponentit
        flagFiBackground = findViewById(R.id.flag_fi_background);
        flagEsBackground = findViewById(R.id.flag_es_background);
        flagArBackground = findViewById(R.id.flag_ar_background);
        nextButton = findViewById(R.id.nextButton);
        nextButtonTextView = findViewById(R.id.nextButtonTextView);

        // Piilotetaan nappi ja sen sisällä oleva teksti
        nextButton.setVisibility(View.GONE);
        nextButtonTextView.setVisibility(View.GONE);

        flagFiBackground.setOnClickListener(v -> handleFlagClick(0));
        flagEsBackground.setOnClickListener(v -> handleFlagClick(1));
        flagArBackground.setOnClickListener(v -> handleFlagClick(2));

        nextButton.setOnClickListener(v -> {
            // Siirrytään käyttäjän luontiin
            Intent userCreation = new Intent(LanguageSelectActivity.this, NewSessionActivity.class);
            userCreation.putExtra("buttonClicked", sessionID);
            userCreation.putExtra("language", selectedFlag);
            startActivity(userCreation);
        });
    }

    // Lipun taustan klikkaus
    private void handleFlagClick(int flag) {
        // Jos klikataan samaa lippua, joka on valittuna, ei tehdä mitään
        if (selectedFlag == flag) {
            return;
        }
        selectedFlag = flag;
        updateFlagBackground();

        // Asetetaan nappi ja sen sisällä oleva teksti näkyville
        nextButton.setVisibility(View.VISIBLE);
        nextButtonTextView.setVisibility(View.VISIBLE);

        // Päivitetään oikea kieli napin tekstiin
        updateLanguageToButton();
    }

    // Lipun taustan värin päivitys
    private void updateFlagBackground() {
        int backgroundUnchecked = R.drawable.flagbackgroundunchecked;
        int backgroundChecked = R.drawable.flagbackgroundchecked;

        // Asetetaan kaikki lippujen taustat keltaisiksi
        flagFiBackground.setImageResource(backgroundUnchecked);
        flagEsBackground.setImageResource(backgroundUnchecked);
        flagArBackground.setImageResource(backgroundUnchecked);

        // Asetetaan valitun lipun tausta vihreäksi
        switch (selectedFlag) {
            case 0:
                flagFiBackground.setImageResource(backgroundChecked);
                break;
            case 1:
                flagEsBackground.setImageResource(backgroundChecked);
                break;
            case 2:
                flagArBackground.setImageResource(backgroundChecked);
                break;
        }
    }

    private void updateLanguageToButton() {
        Context updatedContext = createUpdatedContext();
        String buttonText = updatedContext.getString(R.string.next);
        nextButtonTextView.setText(buttonText);
    }

    private Context createUpdatedContext() {
        String languageCode = getLanguageCode(selectedFlag);
        Locale updatedLocale = new Locale(languageCode);
        Locale.setDefault(updatedLocale);

        Configuration configuration = new Configuration();
        configuration.setLocale(updatedLocale);

        Context updatedContext = createConfigurationContext(configuration);
        return updatedContext;
    }

    private String getLanguageCode(int flag) {
        switch (flag) {
            case 0:
                return "fi";
            case 1:
                return "es";
            case 2:
                return "ar";
            default:
                return "fi"; // Suomi on default
        }
    }

}


