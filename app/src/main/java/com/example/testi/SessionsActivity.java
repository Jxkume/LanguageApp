package com.example.testi;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Handler;
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

    private ImageView firstSessionButton, secondSessionButton, thirdSessionButton, fourthSessionButton, fifthSessionButton;
    private TextView firstSessionTextView, secondSessionTextView, thirdSessionTextView, fourthSessionTextView, fifthSessionTextView;
    private int sessionID;
    BackgroundMusicService musicService;
    boolean isBound = false;

    // Palveluyhteys taustamusiikin aktiviteettiin
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            BackgroundMusicService.LocalBinder binder = (BackgroundMusicService.LocalBinder) service;
            musicService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicService = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kutsutaan tätä jo nyt, että nappien ulkonäköä voidaan muuttaa
        setContentView(R.layout.activity_sessions);

        // Haetaan kaikki napit
        this.firstSessionButton = findViewById(R.id.session1_btn);
        this.secondSessionButton = findViewById(R.id.session2_btn);
        this.thirdSessionButton = findViewById(R.id.session3_btn);
        this.fourthSessionButton = findViewById(R.id.session4_btn);
        this.fifthSessionButton = findViewById(R.id.session5_btn);

        // Haetaan kaikkien nappien tekstit
        this.firstSessionTextView = findViewById(R.id.firstSessionUsername);
        this.secondSessionTextView = findViewById(R.id.secondSessionUsername);
        this.thirdSessionTextView = findViewById(R.id.thirdSessionUsername);
        this.fourthSessionTextView = findViewById(R.id.fourthSessionUsername);
        this.fifthSessionTextView = findViewById(R.id.fifthSessionUsername);

        // Asetetaan jokaiselle napille tägi, jotta voidaan myöhemmin tarkistaa napin ImageResource
        this.firstSessionButton.setTag(R.drawable.newsession_btn);
        this.secondSessionButton.setTag(R.drawable.newsession_btn);
        this.thirdSessionButton.setTag(R.drawable.newsession_btn);
        this.fourthSessionButton.setTag(R.drawable.newsession_btn);
        this.fifthSessionButton.setTag(R.drawable.newsession_btn);

        // Haetaan tietokannasta Sessions-node
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
                        // Haetaan sessionID
                        Long sessionID = sessionSnapshot.child("SessionID").getValue(Long.class);
                        if (sessionKey != null) {
                            if (sessionID != null) {
                                // Jos avain ja sessionID löytyvät, asetetaan nappi erilaiseksi ja laitetaan käyttäjän nimi sen sisälle, lisäksi muutetaan napin tägi
                                if (sessionID == 1) {
                                    firstSessionButton.setImageResource(R.drawable.oldsession);
                                    firstSessionTextView.setText(username);
                                    firstSessionButton.setTag(R.drawable.oldsession);
                                } else if (sessionID == 2) {
                                    secondSessionButton.setImageResource(R.drawable.oldsession);
                                    secondSessionTextView.setText(username);
                                    secondSessionButton.setTag(R.drawable.oldsession);
                                } else if (sessionID == 3) {
                                    thirdSessionButton.setImageResource(R.drawable.oldsession);
                                    thirdSessionTextView.setText(username);
                                    thirdSessionButton.setTag(R.drawable.oldsession);
                                } else if (sessionID == 4) {
                                    fourthSessionButton.setImageResource(R.drawable.oldsession);
                                    fourthSessionTextView.setText(username);
                                    fourthSessionButton.setTag(R.drawable.oldsession);
                                } else if (sessionID == 5) {
                                    fifthSessionButton.setImageResource(R.drawable.oldsession);
                                    fifthSessionTextView.setText(username);
                                    fifthSessionButton.setTag(R.drawable.oldsession);
                                }
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

        // Tarkistetaan nappien tägit ja ohjataan käyttäjä oikeaan aktiviteettiin tägin perusteella
        // Lisäksi annetaan sessionID NewSession-aktiviteettiin, jossa sitä voidaan käyttää session luonnin yhteydessä
        firstSessionButton.setOnClickListener(v -> {
            if (isBound && musicService != null) {
                musicService.playUIbtnSound();
            }
            handleSessionButtonClick(1);
        });
        secondSessionButton.setOnClickListener(v -> {
            if (isBound && musicService != null) {
                musicService.playUIbtnSound();
            }
            handleSessionButtonClick(2);
        });
        thirdSessionButton.setOnClickListener(v -> {
            if (isBound && musicService != null) {
                musicService.playUIbtnSound();
            }
            handleSessionButtonClick(3);
        });
        fourthSessionButton.setOnClickListener(v -> {
            if (isBound && musicService != null) {
                musicService.playUIbtnSound();
            }
            handleSessionButtonClick(4);
        });
        fifthSessionButton.setOnClickListener(v -> {
            if (isBound && musicService != null) {
                musicService.playUIbtnSound();
            }
            handleSessionButtonClick(5);
        });
    }

    private void handleSessionButtonClick(int sessionID) {

        if ((int) getSessionButton(sessionID).getTag() == R.drawable.newsession_btn) {
            Intent newSession = new Intent(SessionsActivity.this, LanguageSelectActivity.class);
            newSession.putExtra("buttonClicked", sessionID);
            startActivity(newSession);
            overridePendingTransition(0, 0);
            finish();
        } else {
            LanguageManager.getInstance().setSessionID(sessionID);
            LanguageManager.getInstance().getLanguageFromDatabase(SessionsActivity.this);

            Intent oldSession = new Intent(SessionsActivity.this, HomeActivity.class);
            oldSession.putExtra("sessionID", sessionID);

            // Odotetaan sekunti ennen kuin mennään kotinäkymään
            new Handler().postDelayed(() -> {
                startActivity(oldSession);
                finish();
            }, 1000);
        }
    }

    private ImageView getSessionButton(int sessionID) {
        switch (sessionID) {
            case 1:
                return firstSessionButton;
            case 2:
                return secondSessionButton;
            case 3:
                return thirdSessionButton;
            case 4:
                return fourthSessionButton;
            case 5:
                return fifthSessionButton;
            default:
                throw new IllegalArgumentException("Invalid session ID");
        }
    }

    // Musiikki käynnistyy kun onCreate on ladannut aktiviteetin komponentit
    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(this, BackgroundMusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        if (musicService != null) {
            musicService.playBackgroundMusic();
        }
    }

    // Musiikkipalvelun yhteys vapautetaan kun aktiviteetti ei ole enää näkyvissä
    @Override
    protected void onStop() {
        super.onStop();
        unbindService(serviceConnection);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPref = getSharedPreferences("GameSettings", Context.MODE_PRIVATE);
        int savedBgmVol = sharedPref.getInt("bgmVolume", 100);
        if (isBound && musicService != null) {
            musicService.setMusicVolume(savedBgmVol);
            musicService.playBackgroundMusic();
        }
    }
}
