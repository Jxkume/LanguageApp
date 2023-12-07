package com.example.testi.games;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

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
    private String correctAnswer;
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
            correctAnswer = words.get(currentQuestionIndex);
            int answerImageResource = gameActivity.getResources().getIdentifier(correctAnswer.toLowerCase() + gameName.toLowerCase(),
                    "drawable", gameActivity.getPackageName());
            gameActivity.setQuestionText(correctAnswer);
            List<Integer> incorrectAnswers = getRandomIncorrectAnswers(3);
            int correctAnswerPosition = new Random().nextInt(4);
            boolean[] assignedOptions = new boolean[4];
            for (int i = 0; i < 4; i++) {
                assignedOptions[i] = false;
            }

            assignedOptions[correctAnswerPosition] = true;
            gameActivity.setCorrectAnswerImage(correctAnswerPosition, answerImageResource, correctAnswer.toLowerCase());

            for(int i = 0; i < 4; i++) {
                if(!assignedOptions[i]) {
                    int incorrectIndex = incorrectAnswers.remove(0);
                    String incorrectAnswer = words.get(incorrectIndex);
                    int incorrectImageResource = gameActivity.getResources().getIdentifier(incorrectAnswer.toLowerCase() + gameName.toLowerCase(),
                            "drawable", gameActivity.getPackageName());
                    gameActivity.setIncorrectAnswerImage(i, incorrectImageResource, incorrectAnswer.toLowerCase());
                }
            }
        } else {
            gameActivity.goToTheNextActivity();
        }
    }

    private List<Integer> getRandomIncorrectAnswers(int count) {
        List<Integer> indexes = new ArrayList<>();
        int correctAnswerIndex = words.indexOf(correctAnswer);

        while(indexes.size() < count){
            int randomIndex = new Random().nextInt(words.size());

            if(randomIndex != correctAnswerIndex && !indexes.contains(randomIndex)) {
                indexes.add(randomIndex);
            }
        }

        return indexes;
    }

    //NOT SURE ABOUT THIS METHOD, I WAS ALMOST ASLEEP WHEN WRITING IT!!!
    private boolean checkAnswer(String selected) {
        //Maybe I'll need to put if (currentQuestionIndex < words.size()) here
        if(selected.equalsIgnoreCase(correctAnswer)){
            score ++;
            return true;
        }
        return false;
    }
}
