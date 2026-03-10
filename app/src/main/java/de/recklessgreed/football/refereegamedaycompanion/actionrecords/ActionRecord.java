package de.recklessgreed.football.refereegamedaycompanion.actionrecords;

import com.google.android.gms.wearable.DataMap;

public class ActionRecord {

    public String id;          // eindeutig (UUID)
    public long timestamp;     // epoch millis
    public String actionType;  // z.B. "GOAL", "TIMEOUT"
    public int period;         // Spielzeit / Halbzeit

    public ActionRecord() {}

    public DataMap toDataMap() {
        DataMap map = new DataMap();
        map.putString("id", id);
        map.putLong("timestamp", timestamp);
        map.putString("action_type", actionType);
        map.putInt("period", period);
        return map;
    }

    public static ActionRecord fromDataMap(DataMap map) {
        ActionRecord r = new ActionRecord();
        r.id = map.getString("id");
        r.timestamp = map.getLong("timestamp");
        r.actionType = map.getString("action_type");
        r.period = map.getInt("period");
        return r;
    }
}
