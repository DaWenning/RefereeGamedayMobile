package de.recklessgreed.football.refereegamedaycompanion.actionrecords;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Comparator;
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
                ActionRecord r = new ActionRecord(o);
                list.add(r);
            }
        } catch (Exception ignored) {}

        // remove duplicates by UUID
        // Keep for each non-empty id only the record with the greatest timestamp.
        // Records with missing/unknown id are kept as separate entries.
        if (!list.isEmpty()) {
            java.util.Map<String, ActionRecord> dedup = new java.util.LinkedHashMap<>();
            for (ActionRecord r : list) {
                if (r == null) continue;
                String id = r.id;
                if (id == null || id.isEmpty() || "unknown".equals(id)) {
                    // use a generated unique key so these entries are preserved individually
                    String uniqueKey = java.util.UUID.randomUUID().toString() + "@" + System.identityHashCode(r);
                    dedup.put(uniqueKey, r);
                } else {
                    ActionRecord existing = dedup.get(id);
                    if (existing == null || r.timestamp > existing.timestamp) {
                        dedup.put(id, r);
                    }
                }
            }
            list = new ArrayList<>(dedup.values());
        }

        list.sort(Comparator.comparingLong(a -> a.timestamp));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            list = list.reversed();
        }

        return list;
    }

    public synchronized void clearAll() {
        // remove the stored list key or save an empty array
        prefs.edit().remove(KEY).apply();
    }

    private void saveAll(List<ActionRecord> list) {
        JSONArray arr = new JSONArray();
        for (ActionRecord r : list) {
            try {
                arr.put(r.toJSON());
            } catch (Exception ignored) {}
        }
        prefs.edit().putString(KEY, arr.toString()).apply();
    }
}
