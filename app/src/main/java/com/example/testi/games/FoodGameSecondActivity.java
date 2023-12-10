package com.example.testi.games;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import com.example.testi.HomeActivity;
import com.example.testi.R;
import com.example.testi.BackgroundMusicService;
public class FoodGameSecondActivity extends WordGameSecondActivity {
    private ImageView questionImageView;
    private TextView[] optionTextViews;
    private int score;
    private int sessionID;
    private ProgressBar progressBar;
    private Toast toast;
    private GameLogicSecondOption logic;
    private BackgroundMusicService musicService;
    private boolean isBound = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BackgroundMusicService.LocalBinder binder = (BackgroundMusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;
            updateVolumeSettings();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
            isBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodgamesecondactivity);

        Intent intent = getIntent();
        sessionID = intent.getIntExtra("sessionID", -1);

        // Otetaan pisteet pelin ekasta osasta
        Intent intent2 = getIntent();
        score = intent2.getIntExtra("score", score);

        // Alustetaan UI elementit
        ImageView exitButton = findViewById(R.id.foodGameExitButton);
        questionImageView = findViewById(R.id.foodGameSecondOption);
        optionTextViews = new TextView[4];
        optionTextViews[0] = findViewById(R.id.foodGameSecondText1);
        optionTextViews[1] = findViewById(R.id.foodGameSecondText2);
        optionTextViews[2] = findViewById(R.id.foodGameSecondText3);
        optionTextViews[3] = findViewById(R.id.foodGameSecondText4);

        exitButton.setOnClickListener(v -> {
            Intent home = new Intent(FoodGameSecondActivity.this, HomeActivity.class);
            home.putExtra("sessionID", sessionID);
            startActivity(home);
            overridePendingTransition(0, 0);
            finish();
        });

        // Laitetaan clickListerenejä option ImageViewille
        for (int i = 0; i < 4; i++) {
            final int optionIndex = i;
            optionTextViews[i].setOnClickListener(v -> checkAnswer(optionIndex));
        }

        // Pelin edistymispalkki
        progressBar = findViewById(R.id.foodGameProgressBar);
        progressBar.setProgress(50);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BackgroundMusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        updateVolumeSettings();
    }

    // Musiikkipalvelun yhteys vapautetaan kun aktiviteetti ei ole enää näkyvissä
    @Override
    protected void onStop() {
        super.onStop();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    private void updateVolumeSettings() {
        SharedPreferences sharedPref = getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        int savedBgmVol = sharedPref.getInt("bgmVolume", 100);
        int savedSfxVol = sharedPref.getInt("sfxVolume", 100);
        if (musicService != null) {
            musicService.setMusicVolume(savedBgmVol);
            musicService.setSoundEffectsVolume(savedSfxVol);
            musicService.playGameMusic();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeGameLogic();
        updateVolumeSettings();
    }

    private void initializeGameLogic(){
        logic = new GameLogicSecondOption("Foodgame", sessionID, this, score);
    }

    private void checkAnswer(int selectedOptionIndex){
        String selectedAnswer = optionTextViews[selectedOptionIndex].getText() != null
                ? optionTextViews[selectedOptionIndex].getText().toString()
                : "";
        logic.checkAnswer(selectedAnswer);
    }

    @Override
    void setQuestionImg(int imgResource, String contentDescription) {
        questionImageView.setContentDescription(contentDescription);
        questionImageView.setImageResource(imgResource);
    }

    @Override
    void setCorrectAnswerText(int textViewIndex, String text) {
        optionTextViews[textViewIndex].setText(text);
    }

    @Override
    void setIncorrectAnswerText(int textViewIndex, String text) {
        optionTextViews[textViewIndex].setText(text);
    }

    @Override
    void goToTheNextActivity() {
        for (int i = 0; i < 4; i++) {
            optionTextViews[i].setClickable(false);
        }

        new Handler().postDelayed(() -> {
            questionImageView.setImageResource(0);

            // Mennään seuraavaan aktiviteettiin
            Intent intent = new Intent(FoodGameSecondActivity.this, HomeActivity.class);
            intent.putExtra("sessionID", sessionID);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }, 2500);
    }

    public void showCorrectToast() {
        // Toast peruutetaan, jos se on jo olemassa (vältetään toastien kasautuminen jonoon)
        if (toast != null) {
            toast.cancel();
        }
        LayoutInflater inflater = getLayoutInflater();
        View corr_toast = inflater.inflate(R.layout.toast_layout_correct, (ViewGroup) findViewById(R.id.toast_layout_correct));

        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(corr_toast);

        musicService.playCorrectSound();
        toast.show();
    }

    public void showIncorrectToast() {
        // Toast peruutetaan, jos se on jo olemassa (vältetään toastien kasautuminen jonoon)
        if (toast != null) {
            toast.cancel();
        }
        LayoutInflater inflater = getLayoutInflater();
        View incorr_toast = inflater.inflate(R.layout.toast_layout_incorrect, (ViewGroup) findViewById(R.id.toast_layout_incorrect));

        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(incorr_toast);

        musicService.playWrongSound();
        toast.show();
    }

    @Override
    void setProgressBarProgress(int progress) {
        progressBar.setProgress(progress);
    }
}
