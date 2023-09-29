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

public class AnimalGameSecondActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<String> words;
    private ImageView questionImageView;
    private TextView[] optionTextViews;
    private String correctAnswer;
    private int score;
    private int currentQuestionIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animalgamesecondactivity);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Words/Animalgame");

        //Getting score from the first part of animal game
        Intent intent = getIntent();
        score = intent.getIntExtra("score", score);

        // Initialize UI elements

        questionImageView = findViewById(R.id.animalGameSecondOption);
        optionTextViews = new TextView[4];
        optionTextViews[0] = findViewById(R.id.animalGameSecondText1);
        optionTextViews[1] = findViewById(R.id.animalGameSecondText2);
        optionTextViews[2] = findViewById(R.id.animalGameSecondText3);
        optionTextViews[3] = findViewById(R.id.animalGameSecondText4);

        // Initialize word list
        words = new ArrayList<>();

        // Load words from Firebase and set up the game
        loadWordsAndSetUpGame();

        // Set click listeners for option TextViews
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
        // Add a ValueEventListener to retrieve only the keys (names) of the words
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Iterate through the children of "Animalgame" and add word names to the list
                for (DataSnapshot wordSnapshot : dataSnapshot.getChildren()) {
                    String word = wordSnapshot.getKey();
                    words.add(word);
                }

                // Shuffle the word list to randomize the order of questions
                Collections.shuffle(words);

                // Start the game
                showNextQuestion();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors if data retrieval fails
                System.err.println("Error: " + databaseError.getMessage());
            }
        });
    }

    private void showNextQuestion() {
        if (currentQuestionIndex < words.size()) {
            // Get the correct answer (word) for the current question
            correctAnswer = words.get(currentQuestionIndex);

            // Set the question image based on the correct answer (e.g., "bear" -> R.drawable.bearanimalgame)
            int questionImageResource = getResources().getIdentifier(correctAnswer.toLowerCase() + "animalgame", "drawable", getPackageName());

            // Set the image of the correct word to questionImageView
            questionImageView.setContentDescription(correctAnswer.toLowerCase());
            questionImageView.setImageResource(questionImageResource);


            // Generate random incorrect answers and place their images in the other positions
            List<Integer> incorrectAnswerIndices = getRandomIncorrectAnswerIndices(3); // Generate 3 incorrect answers


            // Ensure that the correct answer is placed on one of the options
            int correctAnswerPosition = new Random().nextInt(4);

            // Create an array to keep track of whether each option has been assigned
            boolean[] assignedOptions = new boolean[4];
            for (int i = 0; i < 4; i++) {
                assignedOptions[i] = false;
            }

            // Assign the correct answer to a random unassigned position
            optionTextViews[correctAnswerPosition].setText(correctAnswer);
            assignedOptions[correctAnswerPosition] = true;

            // Assign incorrect answers to the remaining positions
            for (int i = 0; i < 4; i++) {
                if (!assignedOptions[i]) {
                    int incorrectIndex = incorrectAnswerIndices.remove(0);
                    String incorrectAnswer = words.get(incorrectIndex);
                    optionTextViews[i].setText(incorrectAnswer);
                }
            }
        } else {
            // End of the game
            // Remove the question image
            questionImageView.setImageResource(0);
            Toast.makeText(this, "Hyvin meni! Olet ansainnut " + score + " pistettä", Toast.LENGTH_SHORT).show();

            if (currentQuestionIndex >= 10) {
                // Mennään seuraavaan aktiviteettiin
                Intent intent = new Intent(AnimalGameSecondActivity.this, HomeActivity.class);
                //I'm not sure if we need this:
                //intent.putExtra("score", score);
                startActivity(intent);
            }
        }
    }

    private List<Integer> getRandomIncorrectAnswerIndices(int count) {
        // To keep it simple, we'll just randomly select indices from the word list
        List<Integer> incorrectAnswerIndices = new ArrayList<>();
        // Get the index of the correct answer
        int correctAnswerIndex = words.indexOf(correctAnswer);
        while (incorrectAnswerIndices.size() < count) {
            int randomIndex = new Random().nextInt(words.size());
            // Check if the selected index is not the correct answer index and is not already in the list
            if (randomIndex != correctAnswerIndex && !incorrectAnswerIndices.contains(randomIndex)) {
                incorrectAnswerIndices.add(randomIndex);
            }
        }
        return incorrectAnswerIndices;
    }

    private void checkAnswer(int selectedOptionIndex) {
        if (currentQuestionIndex < words.size()) {
            // Get the content description of the selected option
            String selectedAnswer = optionTextViews[selectedOptionIndex].getText() != null
                    ? optionTextViews[selectedOptionIndex].getText().toString()
                    : "";

            // Debug: Log the selected and correct answers to check for any issues
            Log.d("Debug", "Selected Answer: " + selectedAnswer);
            Log.d("Debug", "Correct Answer: " + correctAnswer);

            // Check if the selected answer matches the correct answer
            if (selectedAnswer.equalsIgnoreCase(correctAnswer)) { // Use equalsIgnoreCase for case-insensitive comparison
                score++;
                Toast.makeText(this, "Oikein!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Väärin! Kuvalla on " + words.get(currentQuestionIndex).toString(), Toast.LENGTH_SHORT).show();
            }

            // Move to the next question
            currentQuestionIndex++;
            showNextQuestion();
        }
    }
}
