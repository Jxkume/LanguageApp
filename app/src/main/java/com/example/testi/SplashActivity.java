package com.example.testi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import androidx.annotation.Nullable;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 4000;
    private ImageView bubbles;
    private ImageView logo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bubbles = findViewById(R.id.bubbles);
        logo = findViewById(R.id.logo);

        Animation fadeInBubbles = new AlphaAnimation(0, 1);
        fadeInBubbles.setDuration(2000);
        fadeInBubbles.setFillAfter(true);

        Animation fadeInLogo = new AlphaAnimation(0, 1);
        fadeInLogo.setDuration(2500);
        fadeInLogo.setFillAfter(true);

        bubbles.setAnimation(fadeInBubbles);
        logo.setAnimation(fadeInLogo);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
