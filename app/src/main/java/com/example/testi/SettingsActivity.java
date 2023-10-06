package com.example.testi;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingsActivity extends AppCompatActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settingsactivity);

        // Haetaan aktiviteetin alanapit
        ImageView homeIcon = findViewById(R.id.homeIcon);
        ImageView profileIcon = findViewById(R.id.profileIcon);
        ImageView deleteUserButton = findViewById(R.id.deleteUserButton);


        deleteUserButton.setOnClickListener(v -> {
        deleteSessionData();
        // Sit kun sessions näkymä on done pitää laittaa tähän if else jnejnejne.
        Intent intent = new Intent(SettingsActivity.this, NewSessionActivity.class);
        startActivity(intent);
    });

        // Lisätään nappeihin klikkaustoiminnallisuus
        homeIcon.setOnClickListener(v -> {
            // Siirrytään profiiliaktiviteettiin
            Intent home = new Intent(SettingsActivity.this, HomeActivity.class);
            startActivity(home);
            overridePendingTransition(0, 0);
        });

        profileIcon.setOnClickListener(v -> {
            // Siirrytään profiiliaktiviteettiin
            Intent profile = new Intent(SettingsActivity.this, ProfileActivity.class);
            startActivity(profile);
            overridePendingTransition(0, 0);
        });

    }

    private void deleteSessionData() {
        DatabaseReference sessionsRef = FirebaseDatabase.getInstance().getReference("Sessions");
        sessionsRef.limitToLast(1).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot sessionSnapshot : dataSnapshot.getChildren()) {
                    // Saadaan viimeisen session keyn
                    String lastSessionKey = sessionSnapshot.getKey();

                    if (lastSessionKey != null) {
                        // Poistetaan viimeinen session keyn perusteella
                        sessionsRef.child(lastSessionKey).removeValue();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
