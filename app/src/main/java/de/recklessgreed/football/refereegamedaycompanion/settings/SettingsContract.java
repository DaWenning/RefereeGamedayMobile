package de.recklessgreed.football.refereegamedaycompanion.settings;

public final class SettingsContract {

    private SettingsContract() {}

    // Data Layer Path (muss mit "/" beginnen)
    public static final String PATH_MATCH_SETTINGS = "/match_settings";

    // Keys in der DataMap / SharedPreferences
    public static final String KEY_HOME_NAME = "home_name";
    public static final String KEY_HOME_COLOR = "home_color"; // ARGB int
    public static final String KEY_GUEST_NAME = "guest_name";
    public static final String KEY_GUEST_COLOR = "guest_color"; // ARGB int

    // In Sekunden speichern (UI kann Minuten anzeigen)
    public static final String KEY_PERIOD_LENGTH_SEC = "period_length_sec";
    public static final String KEY_PERIOD_COUNT = "period_count";
    public static final String KEY_TIMEOUTS_PER_HALF = "timeouts_per_half";

    // Konfliktlösung / Metadaten
    public static final String KEY_UPDATED_AT = "updated_at";   // long epoch millis
    public static final String KEY_UPDATED_BY = "updated_by";   // "phone" / "wear" / nodeId

    // Lokaler Speicher
    public static final String PREFS_NAME = "match_settings_prefs";

    // Broadcast für UI-Refresh (intern)
    public static final String ACTION_SETTINGS_UPDATED =
            "de.deinprojekt.ACTION_SETTINGS_UPDATED";
}