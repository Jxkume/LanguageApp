package com.example.testi.games;

import android.util.Log;

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
public class GameLogicSecondOption {
    private String gameName;
    private DatabaseReference databaseReference;
    private List<String> words;
    private int score;
    private String correctAnswer;
    private int currentQuestionIndex = 0;
    private int sessionID;
    private int progressBarProgress = 50;
    private int lvl;
    private int roundsToPlay;
    private WordGameSecondActivity gameActivity;

    public GameLogicSecondOption(String game, int sessionID, WordGameSecondActivity activity, int score) {
        loadUserLevel();
        gameName = game;
        this.sessionID = sessionID;
        databaseReference = FirebaseDatabase.getInstance().getReference("Words/" + game);
        gameActivity = activity;
        words = new ArrayList<>();
        this.score = score;
    }

    private void loadUserLevel() {
        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");

        sessionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Debug", "WTF");
                if(snapshot.exists()){
                    for(DataSnapshot sessionSnapshot : snapshot.getChildren()) {
                        String sessionKey = sessionSnapshot.getKey();
                        Long sessionIDLong = sessionSnapshot.child("SessionID").getValue(Long.class);
                        if(sessionKey != null) {
                            if (sessionIDLong != null && sessionIDLong == sessionID) {
                                lvl = sessionSnapshot.child("Level").getValue(Integer.class);
                                roundsToPlay = 5 * lvl;
                                loadWordsAndSetUpGame();
                                Log.d("Debug", "Level in the method: " + lvl);
                                Log.d("Debug", "Rounds in the method: " + roundsToPlay);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Debug", "Error fetching data: " + error.getMessage());
            }
        });
    }

    private void loadWordsAndSetUpGame() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Log.d("Debug", "WTF2");
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    if(dataSnapshot.getKey() != null) {
                        if (dataSnapshot.child("Level").getValue(Integer.class) <= lvl) {
                            String word = dataSnapshot.getKey();
                            words.add(word);
                        }
                    }
                }
                Collections.shuffle(words);
                showNextQuestion();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Debug", "Error fetching data: " + error.getMessage());
            }
        });
    }

    private void showNextQuestion() {
        if(currentQuestionIndex < roundsToPlay) {
            correctAnswer = words.get(currentQuestionIndex);
            String corrAnswerWithNoSpacesAndSpecCharacters = correctAnswer.replace("'", "");
            corrAnswerWithNoSpacesAndSpecCharacters = corrAnswerWithNoSpacesAndSpecCharacters.replace(" ", "");
            int questionImageResource = gameActivity.getResources().getIdentifier(corrAnswerWithNoSpacesAndSpecCharacters.toLowerCase() + gameName.toLowerCase(),
                    "drawable", gameActivity.getPackageName());
            gameActivity.setQuestionImg(questionImageResource, correctAnswer.toLowerCase());
            List<Integer> incorrectAnswers = getRandomIncorrectAnswers(3);
            int correctAnswerPosition = new Random().nextInt(4);
            boolean[] assignedOptions = new boolean[4];
            for (int i = 0; i < 4; i++) {
                assignedOptions[i] = false;
            }

            assignedOptions[correctAnswerPosition] = true;
            gameActivity.setCorrectAnswerText(correctAnswerPosition, correctAnswer);

            for(int i = 0; i < 4; i++) {
                if(!assignedOptions[i]) {
                    int incorrectIndex = incorrectAnswers.remove(0);
                    String incorrectAnswer = words.get(incorrectIndex);
                    gameActivity.setIncorrectAnswerText(i, incorrectAnswer);
                }
            }
        } else {
            sendXPtoDatabase();
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

    public void checkAnswer(String selected) {
        if (selected.equalsIgnoreCase(correctAnswer)){
            score ++;
            gameActivity.showCorrectToast();
        } else {
            gameActivity.showIncorrectToast();
        }

        currentQuestionIndex ++;
        showNextQuestion();

        progressBarProgress += Math.ceil((1.0 / (roundsToPlay * 2) * 100));
        Log.d("Debug", "Progress " + progressBarProgress);
        gameActivity.setProgressBarProgress(progressBarProgress);
    }

    private void sendXPtoDatabase() {
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
                                // Haetaan käyttäjän taso ja xp tietokannasta
                                int xp = sessionSnapshot.child("XP").getValue(Integer.class);
                                int lvl = sessionSnapshot.child("Level").getValue(Integer.class);

                                //Kutsutaan checkLevelUp metodia, joka tarkistaa kasvaako käyttäjän taso pelin jälkeen
                                if (checkLevelUp(xp, score, lvl)) {
                                    //Jos metodi palauttaa true-arvon, päivitetään käyttäjän tasoa
                                    sessionSnapshot.child("Level").getRef().setValue(lvl + 1);
                                }
                                //Lopuksi päivitetään käyttäjän xp
                                sessionSnapshot.child("XP").getRef().setValue(score + xp);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Printataan error jos sellainen tulee vastaan
                System.err.println("Error: " + error.getMessage());
            }
        });
    }

    private boolean checkLevelUp(int xp, int earnedPoints, int currentLevel) {
        if (currentLevel == 1) {
            if (xp < 100 && xp + earnedPoints >= 100) {
                return true;
            }
        } else if (currentLevel == 2) {
            if (xp < 200 && xp + earnedPoints >= 200) {
                return true;
            }
        } else if (currentLevel == 3) {
            if (xp < 300 && xp + earnedPoints >= 300) {
                return true;
            }
        } else if(currentLevel == 4) {
            if (xp < 400 && xp + earnedPoints >= 400) {
                return true;
            }
        } else if(currentLevel == 5) {
            if (xp < 500 && xp + earnedPoints >= 500) {
                return true;
            }
        }
        return false;
    }
}
