package com.example.testi.games;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

public class AnimalGameSecondActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<String> words;
    private ImageView questionImageView;
    private TextView[] optionTextViews;
    private String correctAnswer;
    private int score;
    private int currentQuestionIndex = 0;
    private ImageView exitButton;

    private int sessionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animalgamesecondactivity);

        Intent intent = getIntent();
        sessionID = intent.getIntExtra("sessionID", -1);

        // Etsitään ja tallennetaan Words/Animalgame path databaseReference muuttujaan
        databaseReference = FirebaseDatabase.getInstance().getReference("Words/Animalgame");

        // Otetaan pisteet eläinpelin ekasta osasta
        Intent intent2 = getIntent();
        score = intent2.getIntExtra("score", score);

        // Alustetaan UI elementit
        exitButton = findViewById(R.id.animalGameExitButton);
        questionImageView = findViewById(R.id.animalGameSecondOption);
        optionTextViews = new TextView[4];
        optionTextViews[0] = findViewById(R.id.animalGameSecondText1);
        optionTextViews[1] = findViewById(R.id.animalGameSecondText2);
        optionTextViews[2] = findViewById(R.id.animalGameSecondText3);
        optionTextViews[3] = findViewById(R.id.animalGameSecondText4);

        exitButton.setOnClickListener(v -> {
            Intent home = new Intent(AnimalGameSecondActivity.this, HomeActivity.class);
            home.putExtra("sessionID", sessionID);
            startActivity(home);
            overridePendingTransition(0, 0);
        });

        // Alustetaan words arrayList johon tulee meidän tietokannasta tulevia sanoja
        words = new ArrayList<>();

        // Ladataan sanat tietokannasta ja alustetaan pelin
        loadWordsAndSetUpGame();

        // Laitetaan clickListerenejä option ImageViewille (Eli siis eläimen kuville)
        for (int i = 0; i < 4; i++) {
            final int optionIndex = i;
            optionTextViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkAnswer(optionIndex);
                }
            });
        }
    }

    private void loadWordsAndSetUpGame() {
        // Lisätään ValueEventListenerin että saadaan ladattua (tietokannasta) vain ja ainoastaan sanojen nimet (keys)!
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iteroidaan AnimalGamen läpi ja lisätään sanat words ArrayListiin
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
        if (currentQuestionIndex < words.size()) {
            // Otetaan oikean vastauksen nykyiseen kysymykseen
            correctAnswer = words.get(currentQuestionIndex);

            // Aseta kysymyskuva perustuen oikeaan vastauksen (esim., "bear" -> R.drawable.bearanimalgame)
            int questionImageResource = getResources().getIdentifier(correctAnswer.toLowerCase() + "animalgame", "drawable", getPackageName());

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
            questionImageView.setImageResource(0);
            Toast.makeText(this, "Hyvin meni! Olet ansainnut " + score + " pistettä", Toast.LENGTH_SHORT).show();

            if (currentQuestionIndex >= 10) {
                // Mennään seuraavaan aktiviteettiin
                Intent intent = new Intent(AnimalGameSecondActivity.this, HomeActivity.class);
                //intent.putExtra("score", score); //wat is dis T.JHON
                intent.putExtra("sessionID", sessionID);
                startActivity(intent);
            }
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
        }
    }

    private void showCorrectToast() {
        LayoutInflater inflater = getLayoutInflater();
        View corr_toast = inflater.inflate(R.layout.toast_layout_correct, (ViewGroup) findViewById(R.id.toast_layout_correct));

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(corr_toast);

        toast.show();
    }

    private void showIncorrectToast() {
        LayoutInflater inflater = getLayoutInflater();
        View incorr_toast = inflater.inflate(R.layout.toast_layout_incorrect, (ViewGroup) findViewById(R.id.toast_layout_incorrect));

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(incorr_toast);

        toast.show();
    }
}
