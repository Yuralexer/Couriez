package com.yuralexer.couriez.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferencesHelper {
    private static final String PREF_NAME = "KuryezPrefs";
    private static final String KEY_IS_FIRST_LAUNCH = "isFirstLaunch";
    private static final String KEY_SELECTED_CITY = "selectedCity";
    private static final String KEY_LOGGED_IN_USER_ID = "loggedInUserId";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void setFirstLaunch(boolean isFirst) {
        editor.putBoolean(KEY_IS_FIRST_LAUNCH, isFirst);
        editor.apply();
    }

    public boolean isFirstLaunch() {
        return sharedPreferences.getBoolean(KEY_IS_FIRST_LAUNCH, true);
    }

    public void setSelectedCity(String city) {
        editor.putString(KEY_SELECTED_CITY, city);
        editor.apply();
    }

    public String getSelectedCity() {
        return sharedPreferences.getString(KEY_SELECTED_CITY, "Москва");
    }

    public void setLoggedInUserId(long userId) {
        editor.putLong(KEY_LOGGED_IN_USER_ID, userId);
        editor.apply();
    }

    public long getLoggedInUserId() {
        return sharedPreferences.getLong(KEY_LOGGED_IN_USER_ID, -1);
    }

    public void clearLoginData() {
        editor.remove(KEY_LOGGED_IN_USER_ID);
        editor.apply();
    }
}
