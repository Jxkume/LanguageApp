package com.example.testi;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SessionsActivity extends AppCompatActivity {

    private boolean saveslotTaken = false;
    private ImageView firstSessionButton, secondSessionButton, thirdSessionButton, fourthSessionButton, fifthSessionButton;
    private TextView firstSessionTextView, secondSessionTextView, thirdSessionTextView, fourthSessionTextView, fifthSessionTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_sessions);

        this.firstSessionButton = findViewById(R.id.session1_btn);
        this.secondSessionButton = findViewById(R.id.session2_btn);
        this.thirdSessionButton = findViewById(R.id.session3_btn);
        this.fourthSessionButton = findViewById(R.id.session4_btn);
        this.fifthSessionButton = findViewById(R.id.session5_btn);

        this.firstSessionTextView = findViewById(R.id.firstSessionUsername);
        this.secondSessionTextView = findViewById(R.id.secondSessionUsername);
        this.thirdSessionTextView = findViewById(R.id.thirdSessionUsername);
        this.fourthSessionTextView = findViewById(R.id.fourthSessionUsername);
        this.fifthSessionTextView = findViewById(R.id.fifthSessionUsername);

        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference().child("Sessions");
        sessionsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                        // Haetaan sessionin avain
                        String sessionKey = sessionSnapshot.getKey();
                        // Haetaan avaimen sisällä oleva käyttäjän nimi
                        String username = sessionSnapshot.child("Username").getValue(String.class);

                        if (sessionKey != null) {
                            if (sessionKey.equals("1")) {
                                firstSessionButton.setImageResource(R.drawable.oldsession);
                                firstSessionTextView.setText(username);
                            } else if (sessionKey.equals("2")) {
                                secondSessionButton.setImageResource(R.drawable.oldsession);
                                secondSessionTextView.setText(username);
                            } else if (sessionKey.equals("3")) {
                                thirdSessionButton.setImageResource(R.drawable.oldsession);
                                thirdSessionTextView.setText(username);
                            } else if (sessionKey.equals("4")) {
                                fourthSessionButton.setImageResource(R.drawable.oldsession);
                                fourthSessionTextView.setText(username);
                            } else if (sessionKey.equals("5")) {
                                fifthSessionButton.setImageResource(R.drawable.oldsession);
                                fifthSessionTextView.setText(username);
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Session", "Sessionien latauksessa tuli virhe");
            }
        });

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
