package com.example.testi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import androidx.annotation.Nullable;

public class SplashActivity extends AppCompatActivity {

    // Määritellään näytön näyttämisen kuluvan ajan raja
    private static final int SPLASH_DURATION = 6000;

    // Kuvakkeet (kuplat ja logo)
    private ImageView bubble1;
    private ImageView bubble2;
    private ImageView bubble3;
    private ImageView bubble4;
    private ImageView bubble5;
    private ImageView bubble6;
    private ImageView logo;
    BackgroundMusicService musicService;
    boolean isBound = false;

    // Palveluyhteys taustamusiikin aktiviteettiin
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("Yhteys", "Palveluyhteys muodostettu");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("Yhteys", "Palveluyhteys katkaistu");
        }
    };

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Asetetaan näyttöaktiviteetin näkymä resurssista
        setContentView(R.layout.activity_splash);

        // Etsitään jokaisen kuplan ja logon oikea ID
        bubble1 = findViewById(R.id.bubble1);
        bubble2 = findViewById(R.id.bubble2);
        bubble3 = findViewById(R.id.bubble3);
        bubble4 = findViewById(R.id.bubble4);
        bubble5 = findViewById(R.id.bubble5);
        bubble6 = findViewById(R.id.bubble6);
        logo = findViewById(R.id.logo);

        // Luodaan kukin kuplalle ja logolle oman animaation
        Animation fadeInBubble1 = new AlphaAnimation(0, 1);
        fadeInBubble1.setStartOffset(1500);
        fadeInBubble1.setDuration(2000);
        fadeInBubble1.setFillAfter(true);

        Animation fadeInBubble2 = new AlphaAnimation(0, 1);
        fadeInBubble2.setStartOffset(500);
        fadeInBubble2.setDuration(1000);
        fadeInBubble2.setFillAfter(true);

        Animation fadeInBubble3 = new AlphaAnimation(0, 1);
        fadeInBubble3.setStartOffset(500);
        fadeInBubble3.setDuration(1500);
        fadeInBubble3.setFillAfter(true);

        Animation fadeInBubble4 = new AlphaAnimation(0, 1);
        fadeInBubble4.setStartOffset(2000);
        fadeInBubble4.setDuration(3000);
        fadeInBubble4.setFillAfter(true);

        Animation fadeInBubble5 = new AlphaAnimation(0, 1);
        fadeInBubble5.setStartOffset(2000);
        fadeInBubble5.setDuration(500);
        fadeInBubble5.setFillAfter(true);

        Animation fadeInBubble6 = new AlphaAnimation(0, 1);
        fadeInBubble6.setStartOffset(1500);
        fadeInBubble6.setDuration(500);
        fadeInBubble6.setFillAfter(true);

        Animation fadeInLogo = new AlphaAnimation(0, 1);
        fadeInLogo.setStartOffset(3500);
        fadeInLogo.setDuration(1000);
        fadeInLogo.setFillAfter(true);

        // Asetetaan animaation kullekin kuplalle ja logolle
        bubble1.setAnimation(fadeInBubble1);
        bubble2.setAnimation(fadeInBubble2);
        bubble3.setAnimation(fadeInBubble3);
        bubble4.setAnimation(fadeInBubble4);
        bubble5.setAnimation(fadeInBubble5);
        bubble6.setAnimation(fadeInBubble6);
        logo.setAnimation(fadeInLogo);


        // Asetetaan viiveen, että näyttö vaihtuu
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, SessionsActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
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
