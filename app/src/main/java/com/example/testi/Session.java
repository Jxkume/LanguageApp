package com.example.testi;

public class Session {

    private static Session instance;

    // Määritellään jokaisen sessionin ominaisuudet/nodet (Node on siis ominaisuus/entity joka voi "sisältää" lisää ominaisuuksia)
    // Tässä tapauksessa meidän pelit ovat nodeja.

    private int sessionID;
    private int age;
    private String username;
    private int xp;
    private int level;
    private int photoID;
    private String language;
    private String sessionUniqueKey;

    // Määritellään pelin/pelien ominaisuudet
    public static class ColourGame {
        public int Attempts;
        public int CorrectAnswers;
        public int WrongAnswers;
        public int ColourGameID;
    }

    public static class AnimalGame {
        public int Attempts;
        public int CorrectAnswers;
        public int WrongAnswers;
        public int AnimalGameID;
    }

    // Alustetaan/luodaan pelien oliot
    public ColourGame ColourGame = new ColourGame();
    public AnimalGame AnimalGame = new AnimalGame();

    private Session() {
        // Private konstruktori, jotta sessioita voi olla vain yksi per sovellus
    }

    // Tehdään Sessionin konstruktori JA alustetaan pelin ominaisuudet (Vähän niinku tehtäis Hash-Map)
    public Session(int sessionID, int age, String username, int xp, int level, int photoID, String language) { // Konstruktori saa parametrina keyn
        this.sessionID = sessionID;
        this.age = age;
        this.username = username;
        this.xp = xp;
        this.level = level;
        this.photoID = photoID;
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
    }

    public static synchronized Session getInstance() {
        if (instance == null) {
            instance = new Session();
        }
        return instance;
    }

    public void setSessionUniqueKey(String key) {
        this.sessionUniqueKey = key;
    }

    //Metodi palauttaa session uniikin avaimen
    public String getSessionUniqueKey() {
        return sessionUniqueKey;
    }

    public int getSessionID() {
        return sessionID;
    }

    public int getAge() {
        return age;
    }

    public String getUsername() {
        return username;
    }

    public int getXp() {
        return xp;
    }

    public int getLevel() {
        return level;
    }

    public int getPhotoID() {
        return photoID;
    }

    public String getLanguage() {
        return language;
    }

    public ColourGame getColourGame() {
        return ColourGame;
    }

    public AnimalGame getAnimalGame() {
        return AnimalGame;
    }

}
