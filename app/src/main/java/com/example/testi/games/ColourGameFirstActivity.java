package com.example.testi.games;

import android.content.Intent;
import android.os.Bundle;
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

public class ColourGameFirstActivity extends WordGame {
    private ImageView questionImageView;
    private ImageView[] optionImageViews;
    private TextView questionTextView;
    private ImageView exitButton;
    private int sessionID;
    private ProgressBar progressBar;
    private Toast toast;
    private GameLogic logic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colourgamefirstactivity);

        Intent intent = getIntent();
        sessionID = intent.getIntExtra("sessionID", -1);

        // Alustetaan UI elementit
        exitButton = findViewById(R.id.colourGameExitButton);
        questionImageView = findViewById(R.id.colourGameFirstTextBackground);
        questionTextView = findViewById(R.id.colourGameSecondText2);
        optionImageViews = new ImageView[4];
        optionImageViews[0] = findViewById(R.id.colourGameFirstOption1);
        optionImageViews[1] = findViewById(R.id.colourGameFirstOption2);
        optionImageViews[2] = findViewById(R.id.colourGameFirstOption3);
        optionImageViews[3] = findViewById(R.id.colourGameFirstOption4);

        exitButton.setOnClickListener(v -> {
            Intent home = new Intent(ColourGameFirstActivity.this, HomeActivity.class);
            home.putExtra("sessionID", sessionID);
            startActivity(home);
            overridePendingTransition(0, 0);
            finish();
        });

        // Laitetaan clickListerenejä option ImageViewille (Eli siis värien kuville)
        for (int i = 0; i < 4; i++) {
            final int optionIndex = i;
            optionImageViews[i].setOnClickListener(v -> checkAnswer(optionIndex));
        }

        // Pelin edistymispalkki
        progressBar = findViewById(R.id.colourGameProgressBar);
        progressBar.setProgress(0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeGameLogic();
    }

    private void initializeGameLogic(){
        logic = new GameLogic("Colourgame", sessionID, this);
    }

    private void checkAnswer(int selectedOptionIndex) {
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
    public void setQuestionText(String correctAnswerText) {
        questionTextView.setText(correctAnswerText);
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
    public void goToTheNextActivity(int score){
        for (int i = 0; i < 4; i++) {
            optionImageViews[i].setClickable(false);
        }

        new Handler().postDelayed(() -> {
            questionImageView.setImageResource(0);
            questionTextView.setText("");

            // Mennään seuraavaan aktiviteettiin
            Intent intent = new Intent(ColourGameFirstActivity.this, ColourGameSecondActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("sessionID", sessionID);
            startActivity(intent);
            overridePendingTransition(0, 0);
            finish();
        }, 500);
    }

    @Override
    public void setProgressBarProgress(int progress){
        progressBar.setProgress(progress);
    }
}
