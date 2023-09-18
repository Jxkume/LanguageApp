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

        // Haetaan firebasen instanssi ja tallennetaan sen muuttujaan
        //FirebaseDatabase FirebaseDatabase = com.google.firebase.database.FirebaseDatabase.getInstance();

        // Mennään roottiin (tässä tapauksessa https://kieliappi-default-rtdb.europe-west1.firebasedatabase.app/ on meidän root) ja tallennetaan sen muuttujaan
        //DatabaseReference rootRef = FirebaseDatabase.getReference();

        // Etsitään Age kenttä tietokannasta
        //DatabaseReference AgeChild = rootRef.child("Session 1").child("Age");

        // Asetetaan arvo joka tallentuu Age kenttään
        //Integer dataToWrite = 10;

        // Kirjoitetaan dataa Age kenttään
        //AgeChild.setValue(dataToWrite);

        // Etsitään colourgameAttempts tietokannasta
        //DatabaseReference ColourgameAttempts = rootRef.child("Session 1").child("Colourgame").child("Attempts");

        // Asetetaan arvo joka tallentuu colourgameAttempts kenttään
        //Integer dataToWriteToColourgame = 30;

        // Kirjoitetaan dataa colourgameAttemptiin kenttään
        //ColourgameAttempts.setValue(dataToWriteToColourgame);

        // Luodaan uusi session :D
        Session session = new Session(2, 6, "Pedro" , 0, 1);
    }
}