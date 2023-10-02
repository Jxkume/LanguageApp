package com.example.testi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;

public class NewSessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newsessionactivity);

        // Haetaan profiilikuvan tausta pop-uppia varten
        ImageView profilePictureBackground = findViewById(R.id.newProfilePictureBackground1);

        // Tällä saadaan pop-up näkyville, kun klikataan profiilikuvan taustaa
        profilePictureBackground.setOnClickListener(v -> {

            final Dialog popUp = new Dialog(NewSessionActivity.this);

            popUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            popUp.getWindow().setDimAmount(0.2f);

            popUp.requestWindowFeature(Window.FEATURE_NO_TITLE);
            popUp.setContentView(R.layout.profilepicturepopup);
            popUp.show();

        });

    }

}