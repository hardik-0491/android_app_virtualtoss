package com.hardiksenghani.letstoss;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.hardiksenghani.letstoss.controller.TossManager;
import com.hardiksenghani.letstoss.db.DBUtils;

public class LetsTossApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        DBUtils.initializeAppDatabase(getApplicationContext());

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String key = getResources().getString(R.string.pref_key_outcome_delay_time);
        String value = preferences.getString(key, "-1");

        if (value.equals("-1")) {
            value = Integer.toString(TossManager.DEFAULT_OUTCOME_DELAY_TIME);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, value);
            editor.commit();
        }

    }
}
