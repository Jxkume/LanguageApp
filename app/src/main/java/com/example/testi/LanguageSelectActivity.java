package com.example.testi;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
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
    BackgroundMusicService musicService;
    boolean isBound = false;

    //Haetaan taustamusiikki aktiviteettiin
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

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

            // Asetetaan valittu kieli sovellukseen
            LanguageManager.getInstance().setSelectedLanguage(getApplicationContext(), getLanguageCode(selectedFlag));

            // Siirrytään käyttäjän luontiin
            Intent userCreation = new Intent(LanguageSelectActivity.this, NewSessionActivity.class);
            userCreation.putExtra("buttonClicked", sessionID);
            userCreation.putExtra("language", getLanguageCode(selectedFlag));
            startActivity(userCreation);
            finish();
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
        // Luodaan päivitetty konteksti, jossa kieli on asetettu valitun lipun perusteella
        Context updatedContext = createUpdatedContext();

        // Haetaan käännös resursseista päivitetyn kontekstin avulla
        String buttonText = updatedContext.getString(R.string.next);

        // Asetetaan painikkeen teksti päivitetyn tekstin perusteella
        nextButtonTextView.setText(buttonText);
    }

    private Context createUpdatedContext() {

        // Haetaan valitun lipun perusteella kielen koodi
        String languageCode = getLanguageCode(selectedFlag);

        // Luodaan päivitetty kieliobjekti
        Locale updatedLocale = new Locale(languageCode);
        Locale.setDefault(updatedLocale);

        // Luodaan konfiguraatio, jossa kieli on asetettu
        Configuration configuration = new Configuration();
        configuration.setLocale(updatedLocale);

        // Luodaan päivitetty konteksti konfiguraation perusteella
        return createConfigurationContext(configuration);
    }

    private String getLanguageCode(int flag) {
        // Palauta kielen inttinä valitun lipun perusteella
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

    // Musiikki käynnistyy kun onCreate on ladannut aktiviteetin komponentit
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BackgroundMusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        if (musicService != null) {
            musicService.playBackgroundMusic();
        }
    }

    // Musiikkipalvelun yhteys vapautetaan kun aktiviteetti ei ole enää näkyvissä
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }

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


