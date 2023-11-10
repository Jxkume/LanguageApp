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
public class ColourGameSecondActivity extends AppCompatActivity {
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colourgamesecondactivity);

        Intent intent = getIntent();
        sessionID = intent.getIntExtra("sessionID", -1);

        // Etsitään ja tallennetaan Words/Colourgame path databaseReference muuttujaan
        databaseReference = FirebaseDatabase.getInstance().getReference("Words/Colourgame");

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
        });

        // Alustetaan words arrayList johon tulee meidän tietokannasta tulevia sanoja
        words = new ArrayList<>();

        // Ladataan sanat tietokannasta ja alustetaan pelin
        loadWordsAndSetUpGame();

        // Laitetaan clickListerenejä option ImageViewille (Eli siis värien kuville)
        for (int i = 0; i < 4; i++) {
            final int optionIndex = i;
            //TÄTÄ ON VAIHDETTU
            optionTextViews[i].setOnClickListener(v -> checkAnswer(optionIndex));
        }

        // Pelin edistymispalkki
        progressBar = findViewById(R.id.colourGameProgressBar);
        progressBarProgress = 50;
        progressBar.setProgress(progressBarProgress);
    }

    private void loadWordsAndSetUpGame() {
        // Lisätään ValueEventListenerin että saadaan ladattua (tietokannasta) vain ja ainoastaan sanojen nimet (keys)!
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iteroidaan Colourgamen läpi ja lisätään sanat words ArrayListiin
                for (DataSnapshot wordSnapshot : dataSnapshot.getChildren()) {
                    String word = wordSnapshot.getKey();
                    words.add(word);
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
        if (currentQuestionIndex < 5) {
            // Otetaan oikean vastauksen nykyiseen kysymykseen
            correctAnswer = words.get(currentQuestionIndex);

            // Aseta kysymyskuva perustuen oikeaan vastauksen (e.g., "red" -> R.drawable.redcolourgame)
            int questionImageResource = getResources().getIdentifier(correctAnswer.toLowerCase() + "colourgame", "drawable", getPackageName());

            // Aseta kysymysTextViewin tekstiin oikeaan sanaan.
            questionImageView.setContentDescription(correctAnswer.toLowerCase());
            questionImageView.setImageResource(questionImageResource);

            // Generoi satunnaisia virheellisiä vastauksia ja aseta niiden kuvat muihin paikkoihin.
            List<Integer> incorrectAnswerIndices = getRandomIncorrectAnswerIndices(3); // Generate 3 incorrect answers


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
                //Toast.makeText(this, "Hyvin meni! Olet ansainnut " + score + " pistettä", Toast.LENGTH_SHORT).show();

                // Mennään seuraavaan aktiviteettiin
                Intent intent = new Intent(ColourGameSecondActivity.this, HomeActivity.class);
                finish();
                intent.putExtra("sessionID", sessionID);
                startActivity(intent);
                overridePendingTransition(0, 0);
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

            // Lisätään edistymispalkkia
            progressBarProgress += 10;
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
}
