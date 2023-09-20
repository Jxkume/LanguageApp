package com.example.testi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Session {

    // Määritellään jokaisen sessionin ominaisuudet/nodet (Node on siis ominaisuus/entity joka "sisältää" lisää ominaisuuksia)
    // Tässä tapauksessa meidän pelit ovat nodeja.
    public int SessionID;
    public int Age;
    public int XP;
    public int Level;
    public String Username;

    // Määritellään pelin/pelien ominaisuudet
    public static class ColourGame{
        public int Attempts;
        public int CorrectAnswers;
        public int WrongAnswers;
        public int ColourGameID;
    }

    // Alustetaan/luodaan pelin olio
    public ColourGame ColourGame = new ColourGame();

    // Tehdään Sessionin konstruktori JA alustetaan pelin ominaisuudet (Vähän niinku tehtäis Hash-Map)
    public Session(int sessionID, int age, String username, int XP, int level) {
        this.SessionID = sessionID;
        this.Age = age;
        this.Username = username;
        this.XP = XP;
        this.Level = level;

        // Täällä alustetaan ColourGamen ominaisuudet
        ColourGame.Attempts = 0;
        ColourGame.CorrectAnswers = 0;
        ColourGame.WrongAnswers = 0;
        ColourGame.ColourGameID = 0;

        // Lopuksi kutsutaan pushToFirebase että muutokset menevät tietokantaan asti aina kun luodaan uuden sessionin
        // Tämä tietenkin tule muuttumaan nyt tää on "kovakoodattu" että testataan toimivuuden
        pushToFirebase();
    }

    public void pushToFirebase() {
        // Haetaan firebasen instanssi ja tallennetaan sen muuttujaan
        FirebaseDatabase FirebaseDatabase = com.google.firebase.database.FirebaseDatabase.getInstance();

        // Mennään roottiin (tässä tapauksessa https://kieliappi-default-rtdb.europe-west1.firebasedatabase.app/ on meidän root) ja tallennetaan sen muuttujaan
        DatabaseReference rootRef = FirebaseDatabase.getReference();

        // Mennään Sessions nodeen ja tallennetaan se referenssi muuttujaan
        DatabaseReference sessionsRef = rootRef.child("Sessions");

        // .push tekee sen että aina kun luodaan uusi session, se laittaa ylimmäksi nodeksi uniikki key (random numeroita ja kirjaimia)
        // joilla voidaan erotella sessionit JA tärkeimpänä sessionit eivät overwrittaa toisiaan vaan joka kertaa kun luodaan uusi session
        // se luo vaan uuden uniikki key eli noden ja sitten samat ominaisuudet kuin muissa sessionissa

        // Toivottavasti saatte edes jotain selvää tästä selityksestä, jos ette ymmärrä kattokaa ihan firebasen sivuilta :D
        DatabaseReference newSessionRef = sessionsRef.push();

        // Asetetaan Age ominaisuuteen arvon
        newSessionRef.child("Age").setValue(Age);

        // Asetetaan Username ominaisuuteen arvon
        newSessionRef.child("Username").setValue(Username);

        // Asetetaan SessionID ominaisuuteen arvon
        newSessionRef.child("SessionID").setValue(SessionID);

        // Asetetaan XP ominaisuuteen arvon
        newSessionRef.child("XP").setValue(XP);

        // Asetetaan Level ominaisuuteen arvon
        newSessionRef.child("Level").setValue(Level);

        // Asetetaan ColourGame ominaisuuteen arvon
        newSessionRef.child("ColourGame").setValue(ColourGame);
    }
}
