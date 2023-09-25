package com.example.testi;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Luodaan uusi session :D
        // Tämä riivin pitää olla siellä missä Sessionit luodaankaan. T.Jhon
        Session session = new Session(2, 6, "Pedro" , 0, 1);
    }
}