package com.example.testi.games;

import androidx.appcompat.app.AppCompatActivity;

public abstract class WordGame extends AppCompatActivity {
    abstract void setQuestionText(String correctAnswer);
    abstract void setCorrectAnswerImage(int imgViewIndex, int imgResource, String contentDescription);
    abstract void setIncorrectAnswerImage(int imgViewIndex, int imgResource, String contentDescription);
    abstract void goToTheNextActivity();
}
