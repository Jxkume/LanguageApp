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

public class FoodGameFirstActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<String> words;
    private ImageView questionImageView;
    private ImageView[] optionImageViews;
    private TextView questionTextView;
    private String correctAnswer;
    private int score = 0;
    private int currentQuestionIndex = 0;
    private ImageView exitButton;
    private int sessionID;
    private ProgressBar progressBar;
    private int progressBarProgress;
    private Toast toast;
    private int lvl;
    private int roundsToPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.foodgamefirstactivity);

        Intent intent = getIntent();
        sessionID = intent.getIntExtra("sessionID", -1);

        // Etsitään ja tallennetaan Words/Animalgame path databaseReference muuttujaan
        databaseReference = FirebaseDatabase.getInstance().getReference("Words/Foodgame");

        // Alustetaan UI elementit
        exitButton = findViewById(R.id.foodGameExitButton);
        questionImageView = findViewById(R.id.foodGameFirstTextBackground);
        questionTextView = findViewById(R.id.foodGameSecondText2);
        optionImageViews = new ImageView[4];
        optionImageViews[0] = findViewById(R.id.foodGameFirstOption1);
        optionImageViews[1] = findViewById(R.id.foodGameFirstOption2);
        optionImageViews[2] = findViewById(R.id.foodGameFirstOption3);
        optionImageViews[3] = findViewById(R.id.foodGameFirstOption4);

        exitButton.setOnClickListener(v -> {
            Intent home = new Intent(FoodGameFirstActivity.this, HomeActivity.class);
            home.putExtra("sessionID", sessionID);
            startActivity(home);
            overridePendingTransition(0, 0);
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
            optionImageViews[i].setOnClickListener(v -> checkAnswer(optionIndex));
        }

        // Pelin edistymispalkki
        progressBar = findViewById(R.id.foodGameProgressBar);
        progressBarProgress = 0;
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
        // Lisätään ValueEventListenerin että saadaan ladattua (tietokannasta) vain ja ainoastaan sanojen nimet (keys)!
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iteroidaan tietokannan läpi ja lisätään sanat words ArrayListiin
                for (DataSnapshot wordSnapshot : dataSnapshot.getChildren()) {
                    if(wordSnapshot.getKey() != null) {
                        //Haetaan tietokannasta vain ne sanat, joiden taso on <= käyttäjän taso
                        if (wordSnapshot.child("Level").getValue(Integer.class) <= lvl) {
                            String word = wordSnapshot.getKey();
                            words.add(word);
                        }
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

            // Aseta kysymyskuva perustuen oikeaan vastauksen (esim., "bear" -> R.drawable.bearanimalgame)
            int questionImageResource = getResources().getIdentifier(correctAnswer.toLowerCase() + "foodgame", "drawable", getPackageName());

            // Aseta kysymysTextViewin tekstiin oikeaan sanaan.
            questionTextView.setText(correctAnswer);


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
            assignedOptions[correctAnswerPosition] = true;
            optionImageViews[correctAnswerPosition].setContentDescription(correctAnswer.toLowerCase());
            optionImageViews[correctAnswerPosition].setImageResource(questionImageResource);

            // Laitetaan muut väärät vastaukset muille vapaille paikoille
            for (int i = 0; i < 4; i++) {
                if (!assignedOptions[i]) {
                    int incorrectIndex = incorrectAnswerIndices.remove(0);
                    String incorrectAnswer = words.get(incorrectIndex);
                    int incorrectImageResource = getResources().getIdentifier(incorrectAnswer.toLowerCase() + "foodgame", "drawable", getPackageName());
                    optionImageViews[i].setContentDescription(incorrectAnswer.toLowerCase());
                    optionImageViews[i].setImageResource(incorrectImageResource);
                }
            }
        } else {
            // Käyttäjä ei voi enää klikata lisää vaihtoehtoja
            for (int i = 0; i < 4; i++) {
                optionImageViews[i].setClickable(false);
            }

            // 5 kierrosta pelattu, mennään seuraavaan aktiviteettiin
            // 0.5s viive, jotta viimeinen toast tulee näkyviin
            new Handler().postDelayed(() -> {
                questionImageView.setImageResource(0);
                questionTextView.setText("");

                // Mennään seuraavaan aktiviteettiin
                Intent intent = new Intent(FoodGameFirstActivity.this, FoodGameSecondActivity.class);
                intent.putExtra("score", score);
                intent.putExtra("sessionID", sessionID);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }, 500);
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
            String selectedAnswer = optionImageViews[selectedOptionIndex].getContentDescription() != null
                    ? optionImageViews[selectedOptionIndex].getContentDescription().toString()
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

            // Päivitetään edistymispalkkia
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
}