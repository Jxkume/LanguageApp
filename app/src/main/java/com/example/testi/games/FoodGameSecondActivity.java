package com.example.testi.games;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.testi.BackgroundMusicService;
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
public class FoodGameSecondActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<String> words;
    private ImageView questionImageView;
    private TextView[] optionTextViews;
    private String correctAnswer;
    private int score;
    private int currentQuestionIndex = 0;
    private int sessionID;
    private ProgressBar progressBar;
    private int progressBarProgress;
    private Toast toast;
    private int lvl;
    private int roundsToPlay;
    private BackgroundMusicService musicService;

    //Haetaan taustamusiikki aktiviteettiin
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodgamesecondactivity);

        Intent intent = getIntent();
        sessionID = intent.getIntExtra("sessionID", -1);

        // Etsitään ja tallennetaan Words/Foodgame path databaseReference muuttujaan
        databaseReference = FirebaseDatabase.getInstance().getReference("Words/Foodgame");

        // Otetaan pisteet pelin ekasta osasta
        Intent intent2 = getIntent();
        score = intent2.getIntExtra("score", score);

        // Alustetaan UI elementit
        ImageView exitButton = findViewById(R.id.foodGameExitButton);
        questionImageView = findViewById(R.id.foodGameSecondOption);
        optionTextViews = new TextView[4];
        optionTextViews[0] = findViewById(R.id.foodGameSecondText1);
        optionTextViews[1] = findViewById(R.id.foodGameSecondText2);
        optionTextViews[2] = findViewById(R.id.foodGameSecondText3);
        optionTextViews[3] = findViewById(R.id.foodGameSecondText4);

        exitButton.setOnClickListener(v -> {
            Intent home = new Intent(FoodGameSecondActivity.this, HomeActivity.class);
            home.putExtra("sessionID", sessionID);
            startActivity(home);
            overridePendingTransition(0, 0);
            finish();
        });

        // Alustetaan words arrayList johon tulee meidän tietokannasta tulevia sanoja
        words = new ArrayList<>();

        //Otetaan käyttäjän taso tietokannasta
        loadUserLevel();
        // Ladataan sanat tietokannasta ja alustetaan pelin
        loadWordsAndSetUpGame();

        // Laitetaan clickListerenejä option ImageViewille
        for (int i = 0; i < 4; i++) {
            final int optionIndex = i;
            //TÄTÄ ON VAIHDETTU
            optionTextViews[i].setOnClickListener(v -> checkAnswer(optionIndex));
        }

        // Pelin edistymispalkki
        progressBar = findViewById(R.id.foodGameProgressBar);
        progressBarProgress = 50;
        progressBar.setProgress(progressBarProgress);
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
                                //Lasketaan paljonko kierrosta tulee peliin käyttäjän tason perusteella
                                roundsToPlay = 5 * lvl;
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

    private void loadWordsAndSetUpGame() {
        // Lisätään ValueEventListenerin että saadaan ladattua (tietokannasta) vain ja ainoastaan sanojen nimet (keys)!
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iteroidaan tietokannan läpi ja lisätään sanat words ArrayListiin
                for (DataSnapshot wordSnapshot : dataSnapshot.getChildren()) {
                    //Haetaan tietokannasta vain ne sanat, joiden taso on <= käyttäjän taso
                    if (wordSnapshot.child("Level").getValue(Integer.class) <= lvl) {
                        String word = wordSnapshot.getKey();
                        words.add(word);
                    }
                }

                // Shufflataan sanat arraynListin sisällä että randomisoidaan kysymyksien järjestys
                Collections.shuffle(words);

                // Seuraava kysymys
                showNextQuestion();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Printataan error jos sellainen tulee vastaan
                System.err.println("Error: " + databaseError.getMessage());
            }
        });
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < roundsToPlay) {
            // Otetaan oikean vastauksen nykyiseen kysymykseen
            correctAnswer = words.get(currentQuestionIndex);

            // Aseta kysymyskuva perustuen oikeaan vastauksen
            int questionImageResource = getResources().getIdentifier(correctAnswer.toLowerCase() + "foodgame", "drawable", getPackageName());

            // Aseta kysymysTextViewin tekstiin oikeaan sanaan.
            questionImageView.setContentDescription(correctAnswer.toLowerCase());
            questionImageView.setImageResource(questionImageResource);

            // Generoi satunnaisia virheellisiä vastauksia ja aseta niiden kuvat muihin paikkoihin.
            List<Integer> incorrectAnswerIndices = getRandomIncorrectAnswerIndices(3);

            // Varmistetaan, että oikea vastaus sijoitetaan yhteen vaihtoehtoon
            int correctAnswerPosition = new Random().nextInt(4);

            // Luodaan taulukkon seuraamaan, onko jokainen option valittu ja asetettu arvon
            boolean[] assignedOptions = new boolean[4];
            for (int i = 0; i < 4; i++) {
                assignedOptions[i] = false;
            }

            // Sijoitetaan oikean vastauksen random paikkaan
            optionTextViews[correctAnswerPosition].setText(correctAnswer);
            assignedOptions[correctAnswerPosition] = true;

            // Laitetaan muut väärät vastaukset muille vapaille paikoille
            for (int i = 0; i < 4; i++) {
                if (!assignedOptions[i]) {
                    int incorrectIndex = incorrectAnswerIndices.remove(0);
                    String incorrectAnswer = words.get(incorrectIndex);
                    optionTextViews[i].setText(incorrectAnswer);
                }
            }
        } else {
            // Peli päättyy
            // Päivitetään tietokantaan kokemuspisteet
            sendXPtoDatabase();

            // Käyttäjä ei voi enää klikata lisää vaihtoehtoja, koska peli päättyi
            for (int i = 0; i < 4; i++) {
                optionTextViews[i].setClickable(false);
            }

            new Handler().postDelayed(() -> {
                questionImageView.setImageResource(0);

                // Mennään seuraavaan aktiviteettiin
                Intent intent = new Intent(FoodGameSecondActivity.this, HomeActivity.class);
                intent.putExtra("sessionID", sessionID);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            }, 2500);
        }
    }

    private List<Integer> getRandomIncorrectAnswerIndices(int count) {
        // Satunnaisesti valitaan indeksejä sanaluettelosta (words arrayLististä).
        List<Integer> incorrectAnswerIndices = new ArrayList<>();
        int correctAnswerIndex = words.indexOf(correctAnswer); // Saadaan oikean vastauksen indeksi
        while (incorrectAnswerIndices.size() < count) {
            int randomIndex = new Random().nextInt(words.size());
            // Tarkistetaan, että valittu indeksi ei ole oikea vastausindeksissa eikä se ole jo väärien sanojen luettelossa.
            if (randomIndex != correctAnswerIndex && !incorrectAnswerIndices.contains(randomIndex)) {
                incorrectAnswerIndices.add(randomIndex);
            }
        }
        return incorrectAnswerIndices;
    }

    private void checkAnswer(int selectedOptionIndex) {
        if (currentQuestionIndex < words.size()) {
            // Haetaan valitun vaihtoehdon sisällön kuvaus
            String selectedAnswer = optionTextViews[selectedOptionIndex].getText() != null
                    ? optionTextViews[selectedOptionIndex].getText().toString()
                    : "";

            Log.d("Debug", "Selected Answer: " + selectedAnswer);
            Log.d("Debug", "Correct Answer: " + correctAnswer);

            // Tarkistetaan jos valittu vastaus matchaa oikeaan vastaukseen
            if (selectedAnswer.equalsIgnoreCase(correctAnswer)) {
                score++;
                showCorrectToast();
            } else {
                showIncorrectToast();
            }

            // Mennään seuraavaan kysymykseen/roudiin
            currentQuestionIndex++;
            showNextQuestion();

            // Päivitetään edistymispalkkia: lasketaan yhden kierrokseen osuus koko pelistä ja asetetaan se palkkiin
            progressBarProgress += Math.ceil((1.0 / (roundsToPlay * 2) * 100));
            progressBar.setProgress(progressBarProgress);
        }
    }

    private void showCorrectToast() {
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

    private void showIncorrectToast() {
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

                                //Kutsutaan checkLevelUp metodi, joka tarkistaa kasvaako käyttäjän taso pelin jälkeen
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

    // Musiikki käynnistyy kun onCreate on ladannut aktiviteetin komponentit
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BackgroundMusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        if (musicService != null) {
            musicService.playGameMusic();
        }
    }

    // Musiikkipalvelun yhteys vapautetaan kun aktiviteetti ei ole enää näkyvissä
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }
}
