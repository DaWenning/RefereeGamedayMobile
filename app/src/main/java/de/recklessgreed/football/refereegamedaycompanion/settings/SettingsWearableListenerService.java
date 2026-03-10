package de.recklessgreed.football.refereegamedaycompanion.settings;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class SettingsWearableListenerService extends WearableListenerService {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("SWLS", "SERVICE CREATED");
    }

    @Override
    public void onDataChanged(@NonNull DataEventBuffer dataEvents) {
        SettingsStorage storage = new SettingsStorage(getApplicationContext());
        MatchSettings local = storage.load();

        for (DataEvent event : dataEvents) {
            if (event.getType() != DataEvent.TYPE_CHANGED) continue;

            String path = event.getDataItem().getUri().getPath();
            if (!SettingsContract.PATH_MATCH_SETTINGS.equals(path)) continue;

            MatchSettings incoming = MatchSettings.fromDataMap(
                    DataMapItem.fromDataItem(event.getDataItem()).getDataMap()
            );

            // Konfliktlösung: nur übernehmen, wenn incoming neuer ist.
            if (MatchSettings.isIncomingNewer(local, incoming)) {
                storage.save(incoming);
                local = incoming;

                // UI informieren (Activity kann Receiver registrieren)
                Intent i = new Intent(SettingsContract.ACTION_SETTINGS_UPDATED);
                i.setPackage(getPackageName());
                sendBroadcast(i);
            }
        }
    }
}