package com.example.testi;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.ImageView;
import androidx.annotation.Nullable;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 6000;
    private ImageView bubble1;
    private ImageView bubble2;
    private ImageView bubble3;
    private ImageView bubble4;
    private ImageView bubble5;
    private ImageView bubble6;


    private ImageView logo;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        bubble1 = findViewById(R.id.bubble1);
        bubble2 = findViewById(R.id.bubble2);
        bubble3 = findViewById(R.id.bubble3);
        bubble4 = findViewById(R.id.bubble4);
        bubble5 = findViewById(R.id.bubble5);
        bubble6 = findViewById(R.id.bubble6);
        logo = findViewById(R.id.logo);

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

        bubble1.setAnimation(fadeInBubble1);
        bubble2.setAnimation(fadeInBubble2);
        bubble3.setAnimation(fadeInBubble3);
        bubble4.setAnimation(fadeInBubble4);
        bubble5.setAnimation(fadeInBubble5);
        bubble6.setAnimation(fadeInBubble6);
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
