package com.example.testi;

public class SessionKey {
    private static String key;

    public static void setSessionKey(String newKey) {
        key = newKey;
    }

    public static String getSessionKey(){
        return key;
    }
}
