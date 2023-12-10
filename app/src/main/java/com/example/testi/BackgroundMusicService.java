package com.example.testi;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    private static MediaPlayer UIbtn;
    // AudioManager kontrolloi äänenvoimakkuutta
    AudioManager audioManager;
    private boolean isPrepared = false;
    private static int bgmVol = 100;
    private static int sfxVol = 100;

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
        UIbtn = MediaPlayer.create(this, R.raw.selection_sound);


        // Musiikit loopataan, ääniefektejä ei
        player.setLooping(true);
        playerGame.setLooping(true);
        effectPlayerR.setLooping(false);
        effectPlayerW.setLooping(false);
        UIbtn.setLooping(false);

        // Äänenvoimmakkuus on oletuksena max
        setSoundEffectsVolume(sfxVol);
        setMusicVolume(bgmVol);
        prepareMediaPlayerListeners();

        // Haetaan äänenvoimakkuus SharedPreferencesista
        SharedPreferences sharedPref = getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        int defaultVolume = 100;
        bgmVol = sharedPref.getInt("bgmVolume", defaultVolume);
        sfxVol = sharedPref.getInt("sfxVolume", defaultVolume);

        // Asetetaan tallennettu äänenvoimakkuus kun BGMService luodaan
        setMusicVolume(bgmVol);
        setSoundEffectsVolume(sfxVol);
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

    // Taustamusiikin toistaminen
    public void playBackgroundMusic() {
        if (playerGame != null && playerGame.isPlaying()) {
            playerGame.stop();
            playerGame.release();
            playerGame = null;
        }
        if (player == null) {
            player = MediaPlayer.create(this, R.raw.playing_in_color);
            player.setLooping(true);
        }
        if (player != null && !player.isPlaying()) {
            updateMusicVolume();
            player.start();
        }
    }

    // Pelimusiikin toistaminen
    public void playGameMusic() {
        if (player != null && player.isPlaying()) {
            player.stop();
            player.release();
            player = null;
        }
        if (playerGame == null) {
            playerGame = MediaPlayer.create(this, R.raw.morning_funny_beat);
            playerGame.setLooping(true);
        }
        if (playerGame != null && !playerGame.isPlaying()) {
            updateMusicVolume();
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

    // Ääniefekti UI-napille
    public void playUIbtnSound() {
        if (UIbtn != null) {
            UIbtn.start();
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
        public BackgroundMusicService getService() {
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

    // Ääniefektien äänenvoimakkuuden asetus
    public void setSoundEffectsVolume(int vol) {
        float volume = vol / 100.0f;
        if (effectPlayerR != null) {
            effectPlayerR.setVolume(volume, volume);
        }
        if (effectPlayerW != null) {
            effectPlayerW.setVolume(volume, volume);
        }
        if (UIbtn != null) {
            UIbtn.setVolume(volume, volume);
        }
    }

    // Taustamusiikin äänenvoimakkuuden asetus
    public void setMusicVolume(int vol) {
        float volume = vol / 100.0f;
        if (player != null) {
            player.setVolume(volume, volume);
        }
        if (playerGame != null) {
            playerGame.setVolume(volume, volume);
        }
    }

    private void updateMusicVolume() {
        SharedPreferences sharedPref = getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        int savedBgmVol = sharedPref.getInt("bgmVolume", 100);
        float volume = savedBgmVol / 100.0f;

        if (player != null) {
            player.setVolume(volume, volume);
        }
        if (playerGame != null) {
            playerGame.setVolume(volume, volume);
        }
    }

    public void pauseBackgroundMusic() {
        if (player != null && player.isPlaying()) {
            player.pause();
        }
    }

    public void pauseGameMusic() {
        if (playerGame != null && playerGame.isPlaying()) {
            playerGame.pause();
        }
    }

    // Haetaan playerissa soivan musiikin äänenvoimakkuus
    public static int getBGMvolume() {
        return bgmVol;
    }

    public static int getSFXvolume() {
        return sfxVol;
    }
}