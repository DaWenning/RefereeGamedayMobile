package de.recklessgreed.football.refereegamedaycompanion.settings;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SettingsStorage {

    private final SharedPreferences prefs;

    public SettingsStorage(@NonNull Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(SettingsContract.PREFS_NAME, Context.MODE_PRIVATE);
    }

    public MatchSettings load() {
        MatchSettings s = new MatchSettings();

        s.homeName = prefs.getString(SettingsContract.KEY_HOME_NAME, s.homeName);
        s.homeColorArgb = prefs.getInt(SettingsContract.KEY_HOME_COLOR, s.homeColorArgb);

        s.guestName = prefs.getString(SettingsContract.KEY_GUEST_NAME, s.guestName);
        s.guestColorArgb = prefs.getInt(SettingsContract.KEY_GUEST_COLOR, s.guestColorArgb);

        s.periodLengthSec = prefs.getInt(SettingsContract.KEY_PERIOD_LENGTH_SEC, s.periodLengthSec);
        s.periodCount = prefs.getInt(SettingsContract.KEY_PERIOD_COUNT, s.periodCount);
        s.timeoutsPerHalf = prefs.getInt(SettingsContract.KEY_TIMEOUTS_PER_HALF, s.timeoutsPerHalf);
        s.halfTimeLengthSec = prefs.getInt(SettingsContract.KEY_HALFTIME_LENGTH_SEC, s.halfTimeLengthSec);

        s.updatedAt = prefs.getLong(SettingsContract.KEY_UPDATED_AT, 0L);
        s.updatedBy = prefs.getString(SettingsContract.KEY_UPDATED_BY, "");

        return s;
    }

    public void save(@NonNull MatchSettings s) {
        prefs.edit()
                .putString(SettingsContract.KEY_HOME_NAME, s.homeName)
                .putInt(SettingsContract.KEY_HOME_COLOR, s.homeColorArgb)

                .putString(SettingsContract.KEY_GUEST_NAME, s.guestName)
                .putInt(SettingsContract.KEY_GUEST_COLOR, s.guestColorArgb)

                .putInt(SettingsContract.KEY_PERIOD_LENGTH_SEC, s.periodLengthSec)
                .putInt(SettingsContract.KEY_PERIOD_COUNT, s.periodCount)
                .putInt(SettingsContract.KEY_TIMEOUTS_PER_HALF, s.timeoutsPerHalf)
                .putInt(SettingsContract.KEY_HALFTIME_LENGTH_SEC, s.halfTimeLengthSec)

                .putLong(SettingsContract.KEY_UPDATED_AT, s.updatedAt)
                .putString(SettingsContract.KEY_UPDATED_BY, s.updatedBy)
                .apply();
    }
}

