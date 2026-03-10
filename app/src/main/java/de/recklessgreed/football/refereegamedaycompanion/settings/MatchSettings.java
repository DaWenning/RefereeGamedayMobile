package de.recklessgreed.football.refereegamedaycompanion.settings;
import androidx.annotation.NonNull;
import com.google.android.gms.wearable.DataMap;

public class MatchSettings {

    public String homeName;
    public int homeColorArgb;

    public String guestName;
    public int guestColorArgb;

    public int periodLengthSec;
    public int periodCount;
    public int timeoutsPerHalf;

    public long updatedAt;     // epoch millis
    public String updatedBy;   // "phone"/"wear"/nodeId

    public MatchSettings() {
        // Defaults
        homeName = "Heim";
        guestName = "Gast";
        homeColorArgb = 1;   // Blau
        guestColorArgb = 3;  // Rot
        periodLengthSec = 20 * 60;    // 20 Minuten
        periodCount = 2;
        timeoutsPerHalf = 1;
        updatedAt = 0L;
        updatedBy = "";
    }

    public MatchSettings(String homeName, int homeColorArgb, String guestName, int guestColorArgb,
                         int periodLengthSec, int periodCount, int timeoutsPerHalf,
                         long updatedAt, String updatedBy) {
        this.homeName = homeName;
        this.homeColorArgb = homeColorArgb;
        this.guestName = guestName;
        this.guestColorArgb = guestColorArgb;
        this.periodLengthSec = periodLengthSec;
        this.periodCount = periodCount;
        this.timeoutsPerHalf = timeoutsPerHalf;
        this.updatedAt = updatedAt;
        this.updatedBy = updatedBy;
    }

    public MatchSettings copy() {
        MatchSettings c = new MatchSettings();
        c.homeName = homeName;
        c.homeColorArgb = homeColorArgb;
        c.guestName = guestName;
        c.guestColorArgb = guestColorArgb;
        c.periodLengthSec = periodLengthSec;
        c.periodCount = periodCount;
        c.timeoutsPerHalf = timeoutsPerHalf;
        c.updatedAt = updatedAt;
        c.updatedBy = updatedBy;
        return c;
    }

    public DataMap toDataMap() {
        DataMap m = new DataMap();
        m.putString(SettingsContract.KEY_HOME_NAME, homeName);
        m.putInt(SettingsContract.KEY_HOME_COLOR, homeColorArgb);
        m.putString(SettingsContract.KEY_GUEST_NAME, guestName);
        m.putInt(SettingsContract.KEY_GUEST_COLOR, guestColorArgb);

        m.putInt(SettingsContract.KEY_PERIOD_LENGTH_SEC, periodLengthSec);
        m.putInt(SettingsContract.KEY_PERIOD_COUNT, periodCount);
        m.putInt(SettingsContract.KEY_TIMEOUTS_PER_HALF, timeoutsPerHalf);

        m.putLong(SettingsContract.KEY_UPDATED_AT, updatedAt);
        m.putString(SettingsContract.KEY_UPDATED_BY, updatedBy);
        return m;
    }

    public static MatchSettings fromDataMap(@NonNull DataMap m) {
        MatchSettings s = new MatchSettings();
        s.homeName = m.getString(SettingsContract.KEY_HOME_NAME, s.homeName);
        s.homeColorArgb = m.getInt(SettingsContract.KEY_HOME_COLOR, s.homeColorArgb);
        s.guestName = m.getString(SettingsContract.KEY_GUEST_NAME, s.guestName);
        s.guestColorArgb = m.getInt(SettingsContract.KEY_GUEST_COLOR, s.guestColorArgb);

        s.periodLengthSec = m.getInt(SettingsContract.KEY_PERIOD_LENGTH_SEC, s.periodLengthSec);
        s.periodCount = m.getInt(SettingsContract.KEY_PERIOD_COUNT, s.periodCount);
        s.timeoutsPerHalf = m.getInt(SettingsContract.KEY_TIMEOUTS_PER_HALF, s.timeoutsPerHalf);

        s.updatedAt = m.getLong(SettingsContract.KEY_UPDATED_AT, 0L);
        s.updatedBy = m.getString(SettingsContract.KEY_UPDATED_BY, "");
        return s;
    }

    /** Last-write-wins: true wenn incoming "neuer" ist als local */
    public static boolean isIncomingNewer(MatchSettings local, MatchSettings incoming) {
        if (incoming == null) return false;
        if (local == null) return true;
        return incoming.updatedAt > local.updatedAt;
    }
}
