package com.example.testi.games;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testi.R;

import com.example.testi.Word;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AnimalGameFirstActivity extends AppCompatActivity {

    List<Word> wordList;
    ImageView picPlace1;
    ImageView picPlace2;
    ImageView picPlace3;
    ImageView picPlace4;
    Resources res;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activity = this;
        res = this.getResources();
        wordList = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.animalgamefirstactivity);
        retrieveWords();
        //pickWrongAnswers();
    }

    // Create a method to retrieve data and save it to a List
    public void retrieveWords() {
        DatabaseReference wordsRef = FirebaseDatabase.getInstance().getReference("Words/Animalgame");

        wordsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Word word = snapshot.getValue(Word.class);
                    if (word != null) {
                        wordList.add(word);
                    }
                }
                for (Word word : wordList){
                    Word [] randomWords = new Word[3];
                    int randomIndex1 = new Random().nextInt(wordList.size());
                    int randomIndex2 = new Random().nextInt(wordList.size());
                    int randomIndex3 = new Random().nextInt(wordList.size());

                    while(randomIndex1 == wordList.indexOf(word)) {
                        randomIndex1 = new Random().nextInt(wordList.size());
                    }

                    while(randomIndex2 == randomIndex1 || randomIndex2 == wordList.indexOf(word)){
                        randomIndex2 = new Random().nextInt(wordList.size());
                    }

                    while(randomIndex3 == randomIndex1 || randomIndex3 == randomIndex2 || randomIndex3 == wordList.indexOf(word)){
                        randomIndex3 = new Random().nextInt(wordList.size());
                    }

                    randomWords[0] = wordList.get(randomIndex1);
                    randomWords[1] = wordList.get(randomIndex2);
                    randomWords[2] = wordList.get(randomIndex3);

                    word.setIncorrectAnswers(randomWords);
                }

                picPlace1 = findViewById(R.id.animalGameFirstOption1);
                picPlace2 = findViewById(R.id.animalGameFirstOption2);
                picPlace3 = findViewById(R.id.animalGameFirstOption3);
                picPlace4 = findViewById(R.id.animalGameFirstOption4);

                while(wordList.size() >= 5) {
                    int img1ID = res.getIdentifier(wordList.get(0).getPhotoID(), "drawable", getPackageName());
                    int img2ID = res.getIdentifier(wordList.get(0).getIcorrectAnswers()[0].getPhotoID(), "drawable", getPackageName());
                    int img3ID = res.getIdentifier(wordList.get(0).getIcorrectAnswers()[1].getPhotoID(), "drawable", getPackageName());
                    int img4ID = res.getIdentifier(wordList.get(0).getIcorrectAnswers()[2].getPhotoID(), "drawable", getPackageName());

                    System.out.println("Picname: " + wordList.get(0).getPhotoID());
                    System.out.println("id: " + img1ID);
                    picPlace1.setImageResource(img1ID);
                    picPlace2.setImageResource(img2ID);
                    picPlace3.setImageResource(img3ID);
                    picPlace4.setImageResource(img4ID);

                    wordList.remove(0);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle any errors that occur
                System.out.println("Error: " + databaseError.getMessage());
            }
        });
    }

    public void pickWrongAnswers() {
        System.out.println("This metod has been called");
        System.out.println("size: " + wordList.size());
        for (Word word : wordList){
            System.out.println("Does it work?");
            Word [] randomWords = new Word[3];
            int randomIndex1 = new Random().nextInt(wordList.size());
            int randomIndex2;
            int randomIndex3;
            System.out.println("Word: " + wordList.indexOf(word));
            System.out.println("First: " + randomIndex1);

            while(randomIndex1 == wordList.indexOf(word)) {
                randomIndex1 = new Random().nextInt(wordList.size());
            }
            System.out.println("Word: " + wordList.indexOf(word));
            System.out.println("First: " + randomIndex1);
        }
    }

}
