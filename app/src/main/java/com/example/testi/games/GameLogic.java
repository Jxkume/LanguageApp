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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testi.HomeActivity;
import com.example.testi.R;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class GameLogic {
    private String gameName;
    private DatabaseReference databaseReference;
    private List<String> words;
    private ImageView questionImageView;
    private ImageView[] optionImageViews;
    private TextView questionTextView;
    private int score = 0;
    private int currentQuestionIndex = 0;
    private int sessionID;
    private ProgressBar progressBar;
    private int progressBarProgress;
    private Toast toast;
    private int lvl;
    private int roundsToPlay;
    private WordGame gameActivity;

    public GameLogic(String game, int sessionID, WordGame activity) {
        gameName = game;
        this.sessionID = sessionID;
        databaseReference = FirebaseDatabase.getInstance().getReference("Words/" + game);
        gameActivity = activity;
        words = new ArrayList<>();
        loadUserLevel();
        loadWordsAndSetUpGame();
    }

    private void loadUserLevel() {
        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");

        sessionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    for(DataSnapshot sessionSnapshot : snapshot.getChildren()) {
                        String sessionKey = sessionSnapshot.getKey();
                        Long sessionIDLong = sessionSnapshot.child("SessionID").getValue(Long.class);
                        if(sessionKey != null) {
                            if (sessionIDLong != null && sessionIDLong == sessionID) {
                                lvl = sessionSnapshot.child("Level").getValue(Integer.class);
                                //Lasketaan montako kierrosta tulee peliin käyttäjän tason perusteella
                                roundsToPlay = 5 * lvl;
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

    private void loadWordsAndSetUpGame() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot.getKey() != null) {
                        if (dataSnapshot.child("Level").getValue(Integer.class) <= lvl) {
                            String word = dataSnapshot.getKey();
                            words.add(word);
                        }
                    }
                }
                Collections.shuffle(words);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showNextQuestion(){
        if(currentQuestionIndex < roundsToPlay) {
            String correctAnswer = words.get(currentQuestionIndex);
            int questionImageResource = gameActivity.getResources().getIdentifier(correctAnswer.toLowerCase() + gameName.toLowerCase(),
                    "drawable", gameActivity.getPackageName());
            gameActivity.setQuestionText(correctAnswer);
            int[] incorrectAnswers = getRandomIncorrectAnswers(3);
            int correctAnswerPosition = new Random().nextInt(4);
            boolean[] assignedOptions = new boolean[4];
            for (int i = 0; i < 4; i++) {
                assignedOptions[i] = false;
            }
        }
    }

    private int[] getRandomIncorrectAnswers(int count) {
        int[] answers = new int[count];

        return answers;
    }
}
