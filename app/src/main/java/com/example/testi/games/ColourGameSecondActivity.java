package com.example.testi.games;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
public class ColourGameSecondActivity extends WordGameSecondActivity {
    private ImageView questionImageView;
    private TextView[] optionTextViews;
    private int score;
    private int sessionID;
    private ProgressBar progressBar;
    private Toast toast;
    private GameLogicSecondOption logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colourgamesecondactivity);

        Intent intent = getIntent();
        sessionID = intent.getIntExtra("sessionID", -1);

        // Otetaan pisteet colourgamen ekasta osasta
        Intent intent2 = getIntent();
        score = intent2.getIntExtra("score", score);

        // Alustetaan UI elementit
        ImageView exitButton = findViewById(R.id.colourGameExitButton);
        questionImageView = findViewById(R.id.colourGameSecondOption);
        optionTextViews = new TextView[4];
        optionTextViews[0] = findViewById(R.id.colourGameSecondText1);
        optionTextViews[1] = findViewById(R.id.colourGameSecondText2);
        optionTextViews[2] = findViewById(R.id.colourGameSecondText3);
        optionTextViews[3] = findViewById(R.id.colourGameSecondText4);

        exitButton.setOnClickListener(v -> {
            Intent home = new Intent(ColourGameSecondActivity.this, HomeActivity.class);
            home.putExtra("sessionID", sessionID);
            startActivity(home);
            overridePendingTransition(0, 0);
            finish();
        });

        // Laitetaan clickListerenejä option ImageViewille (Eli siis värien kuville)
        for (int i = 0; i < 4; i++) {
            final int optionIndex = i;
            //TÄTÄ ON VAIHDETTU
            optionTextViews[i].setOnClickListener(v -> checkAnswer(optionIndex));
        }

        // Pelin edistymispalkki
        progressBar = findViewById(R.id.colourGameProgressBar);
        progressBar.setProgress(50);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeGameLogic();
    }

    private void initializeGameLogic(){
        logic = new GameLogicSecondOption("Colourgame", sessionID, this, score);
    }

    private void checkAnswer(int selectedOptionIndex) {
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
            Intent intent = new Intent(ColourGameSecondActivity.this, HomeActivity.class);
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

        toast.show();
    }

    @Override
    void setProgressBarProgress(int progress) {
        progressBar.setProgress(progress);
    }
}
