package com.example.testi.games;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
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

import com.example.testi.Word;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class ColourGameFirstActivity extends AppCompatActivity {
    private DatabaseReference databaseReference;
    private List<String> words;
    private ImageView questionImageView;
    private ImageView[] optionImageViews;
    private TextView questionTextView;
    private String correctAnswer;
    private int score = 0;
    private int currentQuestionIndex = 0;
    private ImageView exitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colourgamefirstactivity);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Words/Colourgame");

        // Initialize UI elements
        exitButton = findViewById(R.id.colourGameExitButton);
        questionImageView = findViewById(R.id.colourGameFirstTextBackground);
        questionTextView = findViewById(R.id.colourGameSecondText2);
        optionImageViews = new ImageView[4];
        optionImageViews[0] = findViewById(R.id.colourGameFirstOption1);
        optionImageViews[1] = findViewById(R.id.colourGameFirstOption2);
        optionImageViews[2] = findViewById(R.id.colourGameFirstOption3);
        optionImageViews[3] = findViewById(R.id.colourGameFirstOption4);

        exitButton.setOnClickListener(v -> {
            Intent home = new Intent(ColourGameFirstActivity.this, HomeActivity.class);
            startActivity(home);
            overridePendingTransition(0, 0);
        });

        // Initialize word list
        words = new ArrayList<>();

        // Load words from Firebase and set up the game
        loadWordsAndSetUpGame();



        // Set click listeners for option ImageViews
        for (int i = 0; i < 4; i++) {
            final int optionIndex = i;
            optionImageViews[i].setOnClickListener(new View.OnClickListener() {
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
                // Iterate through the children of Colourgame and add word names to the list
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

            // Set the question image based on the correct answer (e.g., "red" -> R.drawable.redcolourgame)
            int questionImageResource = getResources().getIdentifier(correctAnswer.toLowerCase() + "colourgame", "drawable", getPackageName());

            // Set the text of questionTextView to the correct word
            questionTextView.setText(correctAnswer);


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
            assignedOptions[correctAnswerPosition] = true;
            optionImageViews[correctAnswerPosition].setContentDescription(correctAnswer.toLowerCase());
            optionImageViews[correctAnswerPosition].setImageResource(questionImageResource);

            // Assign incorrect answers to the remaining positions
            for (int i = 0; i < 4; i++) {
                if (!assignedOptions[i]) {
                    int incorrectIndex = incorrectAnswerIndices.remove(0);
                    String incorrectAnswer = words.get(incorrectIndex);
                    int incorrectImageResource = getResources().getIdentifier(incorrectAnswer.toLowerCase() + "colourgame", "drawable", getPackageName());
                    optionImageViews[i].setContentDescription(incorrectAnswer.toLowerCase());
                    optionImageViews[i].setImageResource(incorrectImageResource);
                }
            }
        } else {
            // End of the game
            // Remove the question image
            questionImageView.setImageResource(0);
            // Clear the questionTextView
            questionTextView.setText("");

            if (currentQuestionIndex >= 10) {
                // Moving to the second part of the game
                Intent intent = new Intent(ColourGameFirstActivity.this, ColourGameSecondActivity.class);
                intent.putExtra("score", score);
                startActivity(intent);
            }
        }
    }

    private List<Integer> getRandomIncorrectAnswerIndices(int count) {
        // To keep it simple, we'll just randomly select indices from the word list
        List<Integer> incorrectAnswerIndices = new ArrayList<>();
        int correctAnswerIndex = words.indexOf(correctAnswer); // Get the index of the correct answer
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
            String selectedAnswer = optionImageViews[selectedOptionIndex].getContentDescription() != null
                    ? optionImageViews[selectedOptionIndex].getContentDescription().toString()
                    : "";

            // Debug: Log the selected and correct answers to check for any issues
            Log.d("Debug", "Selected Answer: " + selectedAnswer);
            Log.d("Debug", "Correct Answer: " + correctAnswer);

            // Check if the selected answer matches the correct answer
            if (selectedAnswer.equalsIgnoreCase(correctAnswer)) { // Use equalsIgnoreCase for case-insensitive comparison
                score++;
                Toast.makeText(this, "Oikein!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Väärin! Valitsemasi kuvalla on " + optionImageViews[selectedOptionIndex].getContentDescription().toString(), Toast.LENGTH_SHORT).show();
            }

            // Move to the next question
            currentQuestionIndex++;
            showNextQuestion();
        }
    }
}
