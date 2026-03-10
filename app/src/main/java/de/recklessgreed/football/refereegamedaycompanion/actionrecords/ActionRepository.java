package de.recklessgreed.football.refereegamedaycompanion.actionrecords;

import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ActionRepository {

    private static final String PREF = "actions";
    private static final String KEY = "list";

    private final SharedPreferences prefs;

    public ActionRepository(Context ctx) {
        prefs = ctx.getSharedPreferences(PREF, Context.MODE_PRIVATE);
    }

    public synchronized void insert(ActionRecord r) {
        List<ActionRecord> all = loadAll();
        all.add(r);
        saveAll(all);
    }

    public synchronized List<ActionRecord> loadAll() {
        List<ActionRecord> list = new ArrayList<>();
        try {
            JSONArray arr = new JSONArray(prefs.getString(KEY, "[]"));
            for (int i = 0; i < arr.length(); i++) {
                JSONObject o = arr.getJSONObject(i);
                ActionRecord r = new ActionRecord();
                r.id = o.getString("id");
                r.timestamp = o.getLong("timestamp");
                r.actionType = o.getString("action_type");
                r.period = o.getInt("period");
                list.add(r);
            }
        } catch (Exception ignored) {}
        return list;
    }

    private void saveAll(List<ActionRecord> list) {
        JSONArray arr = new JSONArray();
        for (ActionRecord r : list) {
            JSONObject o = new JSONObject();
            try {
                o.put("id", r.id);
                o.put("timestamp", r.timestamp);
                o.put("action_type", r.actionType);
                o.put("period", r.period);
                arr.put(o);
            } catch (Exception ignored) {}
        }
        prefs.edit().putString(KEY, arr.toString()).apply();
    }
}
