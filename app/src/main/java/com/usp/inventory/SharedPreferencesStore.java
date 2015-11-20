package com.usp.inventory;

import android.content.SharedPreferences;

/**
 * Created by umasankar on 11/14/15.
 */
public class SharedPreferencesStore {

    private static final String ACCOUNT_KEY = "accountName";

    private static final String UID_KEY = "uid";

    private static final String DISPLAY_NAME_KEY = "display_name";

    private SharedPreferences sharedPreferences;

    public SharedPreferencesStore(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public String getAccountName() {
        return sharedPreferences.getString(ACCOUNT_KEY, "");
    }

    public void setAccountName(String accountName) {
        sharedPreferences.edit().putString(ACCOUNT_KEY, accountName).apply();
    }

    public String getUid() {
        return sharedPreferences.getString(UID_KEY, "");
    }

    public void setUid(String uid) {
        sharedPreferences.edit().putString(UID_KEY, uid).apply();
    }

    public String getDisplayName() {
        return sharedPreferences.getString(DISPLAY_NAME_KEY, "");
    }

    public void setDisplayName(String displayName) {
        sharedPreferences.edit().putString(DISPLAY_NAME_KEY, displayName).apply();
    }
}
