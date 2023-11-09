package com.example.testi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Session {

    // Määritellään jokaisen sessionin ominaisuudet/nodet (Node on siis ominaisuus/entity joka voi "sisältää" lisää ominaisuuksia)
    // Tässä tapauksessa meidän pelit ovat nodeja.

    public int Age;
    public int XP;
    public String language;
    public int Level;
    public String Username;

    // Tän pitää olla PhotoID, koska tällä nimellä se kenttä löytyy firebasesta
    public int PhotoID;

    private String sessionUniqueKey;
    private int sessionID;

    // Määritellään pelin/pelien ominaisuudet
    public static class ColourGame{
        public int Attempts;
        public int CorrectAnswers;
        public int WrongAnswers;
        public int ColourGameID;
    }

    public static class AnimalGame{
        public int Attempts;
        public int CorrectAnswers;
        public int WrongAnswers;
        public int AnimalGameID;
    }

    // Alustetaan/luodaan pelien oliot
    public ColourGame ColourGame = new ColourGame();
    public AnimalGame AnimalGame = new AnimalGame();

    public Session() {

    }

    // Tehdään Sessionin konstruktori JA alustetaan pelin ominaisuudet (Vähän niinku tehtäis Hash-Map)
    public Session(int sessionID, int age, String username, int XP, int level, int photoID, String language) { // Konstruktori saa parametrina keyn
        this.sessionID = sessionID;
        this.Age = age;
        this.Username = username;
        this.XP = XP;
        this.Level = level;
        this.PhotoID = photoID;
        this.language = language;

        // Täällä alustetaan pelien ominaisuudet
        ColourGame.Attempts = 0;
        ColourGame.CorrectAnswers = 0;
        ColourGame.WrongAnswers = 0;
        ColourGame.ColourGameID = 0;

        AnimalGame.Attempts = 0;
        AnimalGame.CorrectAnswers = 0;
        AnimalGame.WrongAnswers = 0;
        AnimalGame.AnimalGameID = 0;

        // Lopuksi kutsutaan pushToFirebase että muutokset menevät tietokantaan asti aina kun luodaan uuden sessionin
        pushToFirebase();

    }

    public void pushToFirebase() {
        // Haetaan firebasen instanssi ja tallennetaan sen muuttujaan
        FirebaseDatabase FirebaseDatabase = com.google.firebase.database.FirebaseDatabase.getInstance();

        // Mennään roottiin (tässä tapauksessa https://kieliappi-default-rtdb.europe-west1.firebasedatabase.app/ on meidän root) ja tallennetaan sen muuttujaan
        DatabaseReference rootRef = FirebaseDatabase.getReference();

        // Mennään Sessions nodeen ja tallennetaan se referenssi muuttujaan
        DatabaseReference sessionsRef = rootRef.child("Sessions");

        // Luodaan session, jolla on uniikki avain
        DatabaseReference newSessionRef = sessionsRef.push();

        //Haetaan session avaimen firebasesta
        sessionUniqueKey = newSessionRef.getKey();

        // Asetetaan sessionille ID
        newSessionRef.child("SessionID").setValue(sessionID);

        // Asetetaan Age ominaisuuteen arvon
        newSessionRef.child("Age").setValue(Age);

        // Asetetaan Username ominaisuuteen arvon
        newSessionRef.child("Username").setValue(Username);

        // Asetetaan XP ominaisuuteen arvon
        newSessionRef.child("XP").setValue(XP);

        // Asetetaan Level ominaisuuteen arvon
        newSessionRef.child("Level").setValue(Level);

        // Asetetaan Language ominaisuuteen arvon
        newSessionRef.child("Language").setValue(language);

        // Asetetaan ColourGame ominaisuuteen arvon
        newSessionRef.child("ColourGame").setValue(ColourGame);

        // Asetetaan AnimalGame ominaisuuteen arvon
        newSessionRef.child("AnimalGame").setValue(AnimalGame);

        // Asetetaan PhotoID ominaisuuteen arvon
        newSessionRef.child("PhotoID").setValue(PhotoID);
    }

    //Metodi palauttaa session uniikin avaimen
    public String getSessionUniqueKey() {
        return sessionUniqueKey;
    }
}
