package com.example.testi.games;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
public class ColourGameSecondActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<String> words;
    private ImageView questionImageView;
    private TextView[] optionTextViews;
    private String correctAnswer;
    private int score;
    private int currentQuestionIndex = 0;
    private ImageView exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colourgamesecondactivity);

        // Etsitään ja tallennetaan Words/Colourgame path databaseReference muuttujaan
        databaseReference = FirebaseDatabase.getInstance().getReference("Words/Colourgame");

        // Otetaan pisteet colourgamen ekasta osasta
        Intent intent = getIntent();
        score = intent.getIntExtra("score", score);

        // Alustetaan UI elementit
        exitButton = findViewById(R.id.colourGameExitButton);
        questionImageView = findViewById(R.id.colourGameSecondOption);
        optionTextViews = new TextView[4];
        optionTextViews[0] = findViewById(R.id.colourGameSecondText1);
        optionTextViews[1] = findViewById(R.id.colourGameSecondText2);
        optionTextViews[2] = findViewById(R.id.colourGameSecondText3);
        optionTextViews[3] = findViewById(R.id.colourGameSecondText4);

        exitButton.setOnClickListener(v -> {
            Intent home = new Intent(ColourGameSecondActivity.this, HomeActivity.class);
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
        if (currentQuestionIndex < words.size()) {
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
            questionImageView.setImageResource(0);
            Toast.makeText(this, "Hyvin meni! Olet ansainnut " + score + " pistettä", Toast.LENGTH_SHORT).show();

            if (currentQuestionIndex >= 10) {
                // Mennään seuraavaan aktiviteettiin
                Intent intent = new Intent(ColourGameSecondActivity.this, HomeActivity.class);
                //intent.putExtra("score", score); //wat is dis T.JHON
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
                Toast.makeText(this, "Oikein!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Väärin! Kuvalla on " + words.get(currentQuestionIndex).toString(), Toast.LENGTH_SHORT).show();
            }

            // Mennään seuraavaan kysymykseen/roudiin
            currentQuestionIndex++;
            showNextQuestion();
        }
    }
}
