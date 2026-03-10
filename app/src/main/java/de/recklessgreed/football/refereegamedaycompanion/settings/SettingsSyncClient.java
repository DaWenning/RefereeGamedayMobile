package de.recklessgreed.football.refereegamedaycompanion.settings;

import android.content.Context;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataItem;
import com.google.android.gms.wearable.DataItemBuffer;
import com.google.android.gms.wearable.DataMap;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

public class SettingsSyncClient {

    public interface SettingsCallback {
        void onResult(@Nullable MatchSettings settings);
    }

    private final Context appContext;
    private final DataClient dataClient;

    public SettingsSyncClient(@NonNull Context context) {
        this.appContext = context.getApplicationContext();
        this.dataClient = Wearable.getDataClient(appContext);
    }

    /** Sendet die Settings als DataItem (persistiert & synchronisiert). */
    public void pushSettings(@NonNull MatchSettings settings, boolean urgent) {
        PutDataMapRequest put = PutDataMapRequest.create(SettingsContract.PATH_MATCH_SETTINGS);
        DataMap dm = put.getDataMap();

        DataMap from = settings.toDataMap();
        dm.putAll(from);

        PutDataRequest req = put.asPutDataRequest();
        if (urgent) req.setUrgent(); // kann UI-Latenz verbessern [1](https://developer.android.com/training/wearables/data/data-items)

        dataClient.putDataItem(req);
    }

    /**
     * Holt das neueste DataItem (falls vorhanden). Praktisch beim Start.
     * (DataItems sind über URI identifiziert; wildcard möglich.) [4](https://developers.google.com/android/reference/com/google/android/gms/wearable/DataClient)
     */
    public void fetchLatestSettings(@NonNull final SettingsCallback cb) {
        Uri uri = Uri.parse("wear://*/" + SettingsContract.PATH_MATCH_SETTINGS.substring(1));
        dataClient.getDataItems(uri)
                .addOnSuccessListener(new OnSuccessListener<DataItemBuffer>() {
                    @Override public void onSuccess(DataItemBuffer dataItems) {
                        MatchSettings newest = null;
                        try {
                            for (DataItem item : dataItems) {
                                DataMap map = DataMapItem.fromDataItem(item).getDataMap();
                                MatchSettings s = MatchSettings.fromDataMap(map);
                                if (newest == null || s.updatedAt > newest.updatedAt) {
                                    newest = s;
                                }
                            }
                        } finally {
                            dataItems.release();
                        }
                        cb.onResult(newest);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull Exception e) {
                        cb.onResult(null);
                    }
                });
    }
}
