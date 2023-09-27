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
    Resources res;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        picPlace1 = findViewById(R.id.animalGameFirstOption1);
        ImageView picPlace2 = findViewById(R.id.animalGameFirstOption2);
        ImageView picPlace3 = findViewById(R.id.animalGameFirstOption3);
        ImageView picPlace4 = findViewById(R.id.animalGameFirstOption4);
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

                /*String path1 = "R.drawable." + wordList.get(0).getPhotoID();
                String path2 = "R.drawable." + wordList.get(0).getIcorrectAnswers()[0].getPhotoID();
                String path3 = "R.drawable." + wordList.get(0).getIcorrectAnswers()[1].getPhotoID();
                String path4 = "R.drawable." + wordList.get(0).getIcorrectAnswers()[2].getPhotoID();*/

                //int img1ID = res.getIdentifier(wordList.get(0).getPhotoID(), "drawable", activity.getPackageName());
                //System.out.println("id: " + img1ID);



                // Now, wordList contains the data from Firebase
                // You can access and use it as needed
                /*for (Word word : wordList) {
                    System.out.println("English: " + word.getEnglish());
                    System.out.println("Level: " + word.getLevel());
                    System.out.println("PhotoID: " + word.getPhotoID());
                    System.out.println("WordID: " + word.getWordID());
                }*/
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
