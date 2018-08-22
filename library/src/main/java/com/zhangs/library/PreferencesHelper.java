package com.zhangs.library;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class PreferencesHelper {
    private final static String PREFERENCES_NAME = "SP_KEYSTORE";
    private static volatile PreferencesHelper instances;
    private static SharedPreferences preferences;

    private PreferencesHelper(@NonNull Context context) {
        preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static PreferencesHelper instance(@NonNull Context context) {
        if (instances == null) {
            synchronized (PreferencesHelper.class) {
                if (instances == null) {
                    instances = new PreferencesHelper(context.getApplicationContext());
                }
            }
        }
        return instances;
    }

    public  void save(String key, String value) {
        preferences.edit().putString(key,value).apply();
    }

    public  String get(String key) {
        return preferences.getString(key,"");
    }
}
