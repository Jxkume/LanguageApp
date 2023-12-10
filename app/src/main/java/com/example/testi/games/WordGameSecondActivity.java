package com.example.testi.games;

import androidx.appcompat.app.AppCompatActivity;

public abstract class WordGameSecondActivity extends AppCompatActivity {
    abstract void setQuestionImg(int imgResource, String contentDescription);
    abstract void setCorrectAnswerText(int textViewIndex, String text);
    abstract void setIncorrectAnswerText(int textViewIndex, String text);
    abstract void goToTheNextActivity();
    abstract void showCorrectToast();
    abstract void showIncorrectToast();
    abstract void setProgressBarProgress(int progress);
}
