package com.example.testi;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Session {

    // Määritellään jokaisen sessionin ominaisuudet/nodet (Node on siis ominaisuus/entity joka voi "sisältää" lisää ominaisuuksia)
    // Tässä tapauksessa meidän pelit ovat nodeja.

    public int Age;
    public int XP;
    public int Level;
    public String Username;
    //tän pitää olla PhotoID, koska tällä nimellä se kenttä löytyy firebasesta
    public int PhotoID;

    //TODO: comment this
    private String sessionUniqueKey;

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
    public Session(int key, int age, String username, int XP, int level, int photoID) { // Konstruktori saa parametrina keyn
        this.Age = age;
        this.Username = username;
        this.XP = XP;
        this.Level = level;
        this.PhotoID = photoID;

        // Täällä alustetaan pelien ominaisuudet
        ColourGame.Attempts = 0;
        ColourGame.CorrectAnswers = 0;
        ColourGame.WrongAnswers = 0;
        ColourGame.ColourGameID = 0;

        AnimalGame.Attempts = 0;
        AnimalGame.CorrectAnswers = 0;
        AnimalGame.WrongAnswers = 0;
        AnimalGame.AnimalGameID = 0;

        // Key muutetaan stringiksi
        String keyString = "" + key;

        // Lopuksi kutsutaan pushToFirebase että muutokset menevät tietokantaan asti aina kun luodaan uuden sessionin
        // Tämä tietenkin tule muuttumaan nyt tää on "kovakoodattu" että testataan toimivuuden
        pushToFirebase(keyString);

    }

    public void pushToFirebase(String keyString) {
        // Haetaan firebasen instanssi ja tallennetaan sen muuttujaan
        FirebaseDatabase FirebaseDatabase = com.google.firebase.database.FirebaseDatabase.getInstance();

        // Mennään roottiin (tässä tapauksessa https://kieliappi-default-rtdb.europe-west1.firebasedatabase.app/ on meidän root) ja tallennetaan sen muuttujaan
        DatabaseReference rootRef = FirebaseDatabase.getReference();

        // Mennään Sessions nodeen ja tallennetaan se referenssi muuttujaan
        DatabaseReference sessionsRef = rootRef.child("Sessions");

        // Luodaan sessionille key, joka on arvoltaan 1 tai enemmän (riippuen mikä keyn arvo on konstruktorin parametrina)
        DatabaseReference newSessionRef = sessionsRef.child(keyString);

        //Haetaan session avaimen firebasesta
        sessionUniqueKey = newSessionRef.getKey();

        // Asetetaan Age ominaisuuteen arvon
        newSessionRef.child("Age").setValue(Age);

        // Asetetaan Username ominaisuuteen arvon
        newSessionRef.child("Username").setValue(Username);

        // Asetetaan XP ominaisuuteen arvon
        newSessionRef.child("XP").setValue(XP);

        // Asetetaan Level ominaisuuteen arvon
        newSessionRef.child("Level").setValue(Level);

        // Asetetaan ColourGame ominaisuuteen arvon
        newSessionRef.child("ColourGame").setValue(ColourGame);

        // Asetetaan AnimalGame ominaisuuteen arvon
        newSessionRef.child("AnimalGame").setValue(AnimalGame);

        // Asetetaan PhotoID ominaisuuteen arvon
        newSessionRef.child("PhotoID").setValue(PhotoID);
    }

    //Metodi palauttaa session uniikki avaimen
    public String getSessionUniqueKey() {
        return sessionUniqueKey;
    }
}
