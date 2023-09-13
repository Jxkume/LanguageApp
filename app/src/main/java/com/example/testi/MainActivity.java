package com.example.testi;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;


import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Kirjoitetaan firebaseen dataa
        FirebaseDatabase FirebaseDatabase = com.google.firebase.database.FirebaseDatabase.getInstance();
        DatabaseReference rootRef = FirebaseDatabase.getReference();

        DatabaseReference AgeChild = rootRef.child("Session 1").child("Age");

        Integer dataToWrite = 6;
        AgeChild.setValue(dataToWrite);


        DatabaseReference ColourgameAttempts = rootRef.child("Session 1").child("Colourgame").child("Attempts");

        Integer dataToWriteToColourgame = 30;
        ColourgameAttempts.setValue(dataToWriteToColourgame);
    }
}