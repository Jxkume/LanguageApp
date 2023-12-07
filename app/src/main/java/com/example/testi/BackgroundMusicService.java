package com.example.testi;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class BackgroundMusicService extends Service {

    // Tämä MediaPlayer kontrolloi taustamusiikin toistoa
    private static MediaPlayer player;
    // Tämä MediaPlayer kontrolloi taustamusiikkia peleissä
    private static MediaPlayer playerGame;
    // Tämä kontrolloi ääniefektiä kun valitaan oikein
    private static MediaPlayer effectPlayerR;
    // Tämä kontrolloi ääniefektiä kun valitaan väärin
    private static MediaPlayer effectPlayerW;
    // AudioManager kontrolloi äänenvoimakkuutta
    AudioManager audioManager;
    private boolean isPrepared = false;
    private static int currentVolume = 100;

    // onCreate() alustaa MediaPlayerin ja asettaa sille kuuntelijat
    @Override
    public void onCreate() {

        super.onCreate();

        // AudioManagerilla saadaan äänenvoimakkuus
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        player = MediaPlayer.create(this, R.raw.playing_in_color);
        playerGame = MediaPlayer.create(this, R.raw.morning_funny_beat);
        effectPlayerR = MediaPlayer.create(this, R.raw.correct_button);
        effectPlayerW = MediaPlayer.create(this, R.raw.wrong_answer);

        player.setLooping(true);
        playerGame.setLooping(true);
        effectPlayerR.setLooping(false);
        effectPlayerW.setLooping(false);

        // Äänenvoimmakkuus on oletuksena max
        setVolume(currentVolume);
        prepareMediaPlayerListeners();
    }

    // prepareMediaPlayerListeners() asettaa MediaPlayerille kuuntelijat
    private void prepareMediaPlayerListeners() {
        player.setOnPreparedListener(mp -> {
            isPrepared = true;
            mp.start();
        });

        player.setOnErrorListener((mp, what, extra) -> {
            Log.e("MediaPlayer Error", "What: " + what + " Extra: " + extra);
            stopAndReleaseMediaPlayer();
            return true;
        });
    }

    // Tausta musiikin toistaminen
    public void playBackgroundMusic() {
        if (playerGame != null && playerGame.isPlaying()) {
            playerGame.stop();
            playerGame.prepareAsync();
        }
        if (player != null && !player.isPlaying()) {
            player.start();
        }
    }

    // Pelimusiikin toistaminen
    public void playGameMusic() {
        if (player != null && player.isPlaying()) {
            player.stop();
            player.prepareAsync();
        }
        if (playerGame != null && !playerGame.isPlaying()) {
            playerGame.start();
        }
    }

    // Ääniefekti oikealle vastaukselle
    public void playCorrectSound() {
        if (effectPlayerR != null) {
            effectPlayerR.start();
        }
    }

    // Ääniefekti väärälle vastaukselle
    public void playWrongSound() {
        if (effectPlayerW != null) {
            effectPlayerW.start();
        }
    }

    // onStartCommand() kutsutaan kun palvelu käynnistetään
    // START_STICKY tarkoittaa, että palvelu käynnistetään uudelleen jos se tuhotaan
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isPrepared && !player.isPlaying()) {
            player.start();
        }
        return START_STICKY;
    }

    // stopAndReleaseMediaPlayer() pysäyttää ja vapauttaa MediaPlayerin
    private void stopAndReleaseMediaPlayer() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }
    }

    // onDestroy() pysäyttää ja vapauttaa MediaPlayerin
    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAndReleaseMediaPlayer();
    }

    // Palautetaan palvelun instanssi
    public class LocalBinder extends Binder {
        BackgroundMusicService getService() {
            return BackgroundMusicService.this;
        }
    }

    // onBind() palauttaa IBinderin
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder();
    }

    // Mediaplayerin max äänenvoimakkuus (100%)
    public static int getMaxVolume() {
        return 100;
    }

    // Muutetaan playerin äänenvoimakkuutta asettamalla sille uusi arvo
    public static void setVolume(int vol) {
        vol = Math.max(0, Math.min(100, vol));
        currentVolume = vol;
        float volume = currentVolume / 100.0f;

        if (player != null) {
            player.setVolume(volume, volume);
        }
        if (playerGame != null) {
            playerGame.setVolume(volume, volume);
        }
        if (effectPlayerR != null) {
            effectPlayerR.setVolume(volume, volume);
        }
        if (effectPlayerW != null) {
            effectPlayerW.setVolume(volume, volume);
        }
    }

    // Haetaan playerissa soivan musiikin äänenvoimakkuus
    public static int getCurrentVolume() {
        return currentVolume;
    }
}