package com.example.testi;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class BackgroundMusicService extends Service {

    private MediaPlayer player;
    private boolean isPrepared = false;

    @Override
    public void onCreate() {
        super.onCreate();
        player = MediaPlayer.create(this, R.raw.playing_in_color);
        player.setLooping(true);
        player.setVolume(1.0f, 1.0f);

        player.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                isPrepared = true;
                mp.start();
            }
        });

        player.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Log.e("MediaPlayer Error", "What: " + what + " Extra: " + extra);
                stopAndReleaseMediaPlayer();
                return true;
            }
        });
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        if (isPrepared && !player.isPlaying()) {
            player.start();
        }
        return START_STICKY;
    }

    private void stopAndReleaseMediaPlayer() {
        if (player != null) {
            if (player.isPlaying()) {
                player.stop();
            }
            player.release();
            player = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAndReleaseMediaPlayer();
    }

    private final IBinder binder = (IBinder) new LocalBinder();

    public class LocalBinder extends Binder {
        BackgroundMusicService getService() {
            return BackgroundMusicService.this;
        }
    }

    public void setVolume(float volume) {
        if (player != null) {
            player.setVolume(volume, volume);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}