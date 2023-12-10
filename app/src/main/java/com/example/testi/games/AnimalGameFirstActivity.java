package com.example.testi.games;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.testi.HomeActivity;
import com.example.testi.R;
import com.example.testi.BackgroundMusicService;

public class AnimalGameFirstActivity extends WordGameFirstActivity {
    private ImageView questionImageView;
    private ImageView[] optionImageViews;
    private TextView questionTextView;
    private ImageView exitButton;
    private int sessionID;
    private ProgressBar progressBar;
    private Toast toast;
    private GameLogicFirstOption logic;
    private BackgroundMusicService musicService;
    private boolean isBound = false;

    //Haetaan taustamusiikki aktiviteettiin
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
        setContentView(R.layout.animalgamefirstactivity);

        Intent intent = getIntent();
        sessionID = intent.getIntExtra("sessionID", -1);

        // Alustetaan UI elementit
        exitButton = findViewById(R.id.animalGameExitButton);
        questionImageView = findViewById(R.id.animalGameFirstTextBackground);
        questionTextView = findViewById(R.id.animalGameSecondText2);
        optionImageViews = new ImageView[4];
        optionImageViews[0] = findViewById(R.id.animalGameFirstOption1);
        optionImageViews[1] = findViewById(R.id.animalGameFirstOption2);
        optionImageViews[2] = findViewById(R.id.animalGameFirstOption3);
        optionImageViews[3] = findViewById(R.id.animalGameFirstOption4);

        exitButton.setOnClickListener(v -> {
            Intent home = new Intent(AnimalGameFirstActivity.this, HomeActivity.class);
            home.putExtra("sessionID", sessionID);
            startActivity(home);
            overridePendingTransition(0, 0);
            finish();
        });

        // Laitetaan clickListerenejä option ImageViewille (Eli siis eläimen kuville)
        for (int i = 0; i < 4; i++) {
            final int optionIndex = i;
            optionImageViews[i].setOnClickListener(v -> checkAnswer(optionIndex));
        }

        // Pelin edistymispalkki
        progressBar = findViewById(R.id.animalGameProgressBar);
        progressBar.setProgress(0);
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

    @Override
    protected void onResume(){
        super.onResume();
        initializeGameLogic();
        updateVolumeSettings();
    }

    private void initializeGameLogic() {
        logic = new GameLogicFirstOption("Animalgame", sessionID, this);
    }

    private void checkAnswer(int selectedOptionIndex) {
        // Haetaan valitun vaihtoehdon sisällön kuvaus
        String selectedAnswer = optionImageViews[selectedOptionIndex].getContentDescription() != null
                ? optionImageViews[selectedOptionIndex].getContentDescription().toString()
                : "";

        logic.checkAnswer(selectedAnswer);
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
    public void setQuestionText(String correctAnswer) {
        questionTextView.setText(correctAnswer);
    }

    @Override
    public void setCorrectAnswerImage(int imgViewIndex, int imgResource, String contentDescription) {
        optionImageViews[imgViewIndex].setContentDescription(contentDescription);
        optionImageViews[imgViewIndex].setImageResource(imgResource);
    }

    @Override
    public void setIncorrectAnswerImage(int imgViewIndex, int imgResource, String contentDescription) {
        optionImageViews[imgViewIndex].setContentDescription(contentDescription);
        optionImageViews[imgViewIndex].setImageResource(imgResource);
    }

    @Override
    public void goToTheNextActivity(int score) {
        for (int i = 0; i < 4; i++) {
            optionImageViews[i].setClickable(false);
        }

        new Handler().postDelayed(() -> {
            questionImageView.setImageResource(0);
            questionTextView.setText("");

            Intent intent = new Intent(AnimalGameFirstActivity.this, AnimalGameSecondActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("sessionID", sessionID);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }, 500);
    }

    @Override
    public void setProgressBarProgress(int progress) {
        progressBar.setProgress(progress);
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
}