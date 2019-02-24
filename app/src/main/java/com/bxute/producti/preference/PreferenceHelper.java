/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.producti.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.bxute.producti.application.Main;

public class PreferenceHelper {
  private static PreferenceHelper preferenceHelper;
  private final String PREF_NAME = "producti_pref";
  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor mEditor;

  public PreferenceHelper() {
    sharedPreferences = Main.context().getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    mEditor = sharedPreferences.edit();
  }

  public static PreferenceHelper getInstance() {
    if (preferenceHelper == null) {
      preferenceHelper = new PreferenceHelper();
    }
    return preferenceHelper;
  }

  public String getAuthToken() {
    return sharedPreferences.getString("auth_token", "");
  }

  public void setAuthToken(String auth_token) {
    mEditor.putString("auth_token", auth_token);
    mEditor.apply();
  }

  public void setAccountName(String accountName) {
    mEditor.putString("account_name", accountName);
    mEditor.apply();
  }

  public String getAccountName() {
    return sharedPreferences.getString("account_name", "");
  }
}
