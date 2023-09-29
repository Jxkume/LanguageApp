package com.example.testi;

//TODO: rename instance variable
public class Word {
    private String English;
    private int Level;
    private String PhotoID;
    private int WordID;
    private Word[] incorrectAnswers;

    public Word(String desc, int lvl, String pic, int id, Word[] incorrect) {
        English = desc;
        Level = lvl;
        PhotoID = pic;
        WordID = id;
        incorrectAnswers = incorrect;
    }

    public Word(){}

    public void setPhotoID(String id){
        PhotoID = id;
    }

    public void setEnglish(String word) {
        English = word;
    }

    public void setWordID(int id) {
        WordID = id;
    }

    public void setLevel(int lvl) {
        Level = lvl;
    }

    public void setIncorrectAnswers(Word[] list){
        incorrectAnswers = list;
    }

    public int getWordID() {
        return WordID;
    }

    public String getEnglish(){
        return English;
    }

    public int getLevel(){
        return Level;
    }

    public String getPhotoID() {
        return PhotoID;
    }

    public Word[] getIcorrectAnswers() {
        return incorrectAnswers;
    }
}
