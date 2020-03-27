package com.example.minitwitter.common;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesManager {

    private static final String APP_SETTING_FILE = "APP_SETTINGS";

    private SharedPreferencesManager() {

    }

    private static SharedPreferences getSharedPreferences() {
        return MyApp.getContext().getSharedPreferences(APP_SETTING_FILE, Context.MODE_PRIVATE);
    }

    public static void writeStringValue(String key, String valueData) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, valueData);
        editor.commit();
    }

    public static void writeBooleanValue(String key, boolean valueData) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(key, valueData);
        editor.commit();
    }

    public static String readStringValue(String key) {
        return getSharedPreferences().getString(key, null);
    }

    public static boolean readBooleanValue(String key) {
        return getSharedPreferences().getBoolean(key, false);
    }
}
