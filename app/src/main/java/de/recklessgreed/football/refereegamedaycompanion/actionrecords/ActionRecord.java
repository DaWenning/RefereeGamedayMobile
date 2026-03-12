package de.recklessgreed.football.refereegamedaycompanion.actionrecords;

import com.google.android.gms.wearable.DataMap;

import org.json.JSONObject;


public class ActionRecord {

    public String id;          // eindeutig (UUID)
    public String gameId;
    public long timestamp;     // epoch millis
    public Long teamId;        // optional, z.B. für Tore oder Timeout-Zuordnung
    public String actionType;  // z.B. "GOAL", "TIMEOUT"
    public Integer period;         // Spielzeit / Halbzeit
    public Integer remainingTimeInPeriod; // optional, z.B. für Timeouts
    public String addlText;   // optional, z.B. für Notizen oder Details

    public ActionRecord() {}

    public ActionRecord(JSONObject object) {
        this.id = object.optString("id", object.optString("id", "unknown"));
        this.gameId = object.optString("game_id", "unknown");
        this.timestamp = object.optLong("timestamp", object.optLong("time", -1));
        this.teamId = object.has("team_id") ? object.optLong("team_id") : null;
        this.actionType = object.optString("action_type", "UNKNOWN");
        this.period = object.has("period") ? object.optInt("period") : null;
        this.remainingTimeInPeriod = object.has("remaining_time_in_period") ? object.optInt("remaining_time_in_period") : null;
        this.addlText = object.optString("addl_text", null);
    }

    public DataMap toDataMap() {
        DataMap map = new DataMap();
        map.putString("id", id);
        map.putString("game_id", gameId);
        map.putLong("timestamp", timestamp);
        map.putLong("team_id", teamId);
        map.putString("action_type", actionType);
        map.putInt("period", period);
        map.putInt("remaining_time_in_period", remainingTimeInPeriod);
        map.putString("addl_text", addlText);
        return map;
    }

    public static ActionRecord fromDataMap(DataMap map) {
        ActionRecord r = new ActionRecord();
        r.id = map.getString("id");
        r.gameId = map.getString("game_id");
        r.timestamp = map.getLong("timestamp");
        r.teamId = map.getLong("team_id");
        r.actionType = map.getString("action_type");
        r.period = map.getInt("period");
        r.remainingTimeInPeriod = map.getInt("remaining_time_in_period");
        r.addlText = map.getString("addl_text");
        return r;
    }

    public JSONObject toJSON() {
        JSONObject returner = new JSONObject();
        try {
            returner.put("id", id);
            returner.put("game_id", gameId);
            returner.put("timestamp", timestamp);
            if (teamId != null) returner.put("team_id", teamId);
            returner.put("action_type", actionType);
            if (period != null) returner.put("period", period);
            if (remainingTimeInPeriod != null) returner.put("remaining_time_in_period", remainingTimeInPeriod);
            if (addlText != null) returner.put("addl_text", addlText);
        }
        catch (Exception ignored) {}
        return returner;
    }

    public String getId() {
        return id;
    }

    public String getGameId() {
        return gameId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getReadableTimestamp(boolean withDate) {
        // Optional: Formatieren des Timestamps in ein lesbares Datum/Uhrzeit

        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm:ss");
        if (withDate)
            sdf = new java.text.SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

        return sdf.format(new java.util.Date(timestamp));
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getActionType() {
        return actionType;
    }

    public Integer getPeriod() {
        return period;
    }

    public Integer getRemainingTimeInPeriod() {
        return remainingTimeInPeriod;
    }

    public String getAddlText() {
        return addlText;
    }
}
