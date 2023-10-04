package com.example.testi;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;


public class SessionsActivity extends AppCompatActivity {

    private boolean saveslotTaken = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sessions);
        setupClickListener1();
        setupClickListener2();
        setupClickListener3();
        setupClickListener4();
        setupClickListener5();
    }

    private void setupClickListener1() {
        ImageView slot1 = findViewById(R.id.session1_btn);

        slot1.setOnClickListener( v ->{
            Intent newSession = new Intent(SessionsActivity.this, NewSessionActivity.class);
            startActivity(newSession);
            overridePendingTransition(0, 0);
            saveslotTaken = true;
            updateButton1();
        });
    }

    private void setupClickListener2() {
        ImageView slot2 = findViewById(R.id.session2_btn);

        slot2.setOnClickListener( v ->{
            Intent newSession = new Intent(SessionsActivity.this, NewSessionActivity.class);
            startActivity(newSession);
            overridePendingTransition(0, 0);
            saveslotTaken = true;
            updateButton2();
        });
    }

    private void setupClickListener3() {
        ImageView slot3 = findViewById(R.id.session3_btn);

        slot3.setOnClickListener( v ->{
            Intent newSession = new Intent(SessionsActivity.this, NewSessionActivity.class);
            startActivity(newSession);
            overridePendingTransition(0, 0);
            saveslotTaken = true;
            updateButton3();
        });
    }

    private void setupClickListener4() {
        ImageView slot4 = findViewById(R.id.session4_btn);

        slot4.setOnClickListener( v ->{
            Intent newSession = new Intent(SessionsActivity.this, NewSessionActivity.class);
            startActivity(newSession);
            overridePendingTransition(0, 0);
            saveslotTaken = true;
            updateButton4();
        });
    }


    private void setupClickListener5() {
        ImageView slot5 = findViewById(R.id.session5_btn);

        slot5.setOnClickListener( v ->{
            Intent newSession = new Intent(SessionsActivity.this, NewSessionActivity.class);
            startActivity(newSession);
            overridePendingTransition(0, 0);
            saveslotTaken = true;
            updateButton5();
        });
    }

    private void updateButton1() {
        if (saveslotTaken == true ) {
            ImageView slot1 = findViewById(R.id.session1_btn);
            slot1.setImageResource(R.drawable.oldsession);
        }
    }

    private void updateButton2() {
        if (saveslotTaken == true) {
            ImageView slot2 = findViewById(R.id.session2_btn);
            slot2.setImageResource(R.drawable.oldsession);
        }
    }

    private void updateButton3() {
        if (saveslotTaken == true) {
            ImageView slot3 = findViewById(R.id.session3_btn);
            slot3.setImageResource(R.drawable.oldsession);
        }
    }

    private void updateButton4() {
        if (saveslotTaken == true) {
            ImageView slot4 = findViewById(R.id.session4_btn);
            slot4.setImageResource(R.drawable.oldsession);
        }
    }

    private void updateButton5() {
        if (saveslotTaken == true) {
            ImageView slot5 = findViewById(R.id.session5_btn);
            slot5.setImageResource(R.drawable.oldsession);
        }
    }
}
