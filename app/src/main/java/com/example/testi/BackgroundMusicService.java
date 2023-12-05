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

    // MediaPlayer kontrolloi taustamusiikin toistoa
    private static MediaPlayer player;
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
        player.setLooping(true);
        // Äänenvoimmakkuus on oletuksena max
        player.setVolume(currentVolume / 100.0f, currentVolume / 100.0f);

        // Kuuntelija joka kutsutaan kun MediaPlayer on valmis
        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPrepared = true;
                mp.start();
            }
        });

        // Kuuntelija, joka kutsutaan jos MediaPlayer ei ole valmis
        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("MediaPlayer Error", "What: " + what + " Extra: " + extra);
                stopAndReleaseMediaPlayer();
                return true;
            }
        });
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

    // IBinderin avulla voidaan palauttaa palvelun instanssi
    private final IBinder binder = (IBinder) new LocalBinder();

    // Palautetaan palvelun instanssi
    public class LocalBinder extends Binder {
        BackgroundMusicService getService() {
            return BackgroundMusicService.this;
        }
    }

    // setVolume() asettaa äänenvoimakkuuden
    public void setVolume(float volume) {
        if (player != null) {
            player.setVolume(volume, volume);
        }
    }

    // onBind() palauttaa IBinderin
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //Mediaplayerin max äänenvoimakkuus (100%)
    public static int getMaxVolume() {
        return 100;
    }

    //Muutetaan playerin äänenvoimakkuutta asettamalla sille uusi arvo
    public static void setVolume(int vol) {
        vol = Math.max(0, Math.min(100, vol));
        currentVolume = vol;

        player.setVolume(currentVolume / 100.0f, currentVolume / 100.0f);
    }

    //Haetaan playerissa soivan musiikin äänenvoimakkuus
    public static int getCurrentVolume() {
        return currentVolume;
    }
}