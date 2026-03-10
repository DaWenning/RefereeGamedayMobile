package de.recklessgreed.football.refereegamedaycompanion;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.wearable.DataClient;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.Wearable;

import de.recklessgreed.football.refereegamedaycompanion.actionrecords.ActionContract;
import de.recklessgreed.football.refereegamedaycompanion.actionrecords.ActionRecord;
import de.recklessgreed.football.refereegamedaycompanion.actionrecords.ActionRepository;

/**
 * Application class to register Wearable Data Listeners at app startup.
 * This ensures listeners are active immediately when the app starts.
 */
public class CompanionApplication extends Application {

    private static final String TAG = "CompanionApp";
    private DataClient.OnDataChangedListener actionDataListener;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Application onCreate - registering wearable listeners");

        // Create and register the action data listener
        actionDataListener = (@NonNull DataEventBuffer dataEvents) -> {
            Log.d("DL_PHONE", "onDataChanged count=" + dataEvents.getCount());
            ActionRepository repo = new ActionRepository(CompanionApplication.this);

            for (DataEvent event : dataEvents) {
                String path = event.getDataItem().getUri().getPath();
                Log.d("DL_PHONE", "EVENT path=" + path);

                if (event.getType() != DataEvent.TYPE_CHANGED) continue;

                if (path != null && path.startsWith(ActionContract.PATH_ACTIONS)) {
                    ActionRecord record = ActionRecord.fromDataMap(
                            DataMapItem.fromDataItem(event.getDataItem()).getDataMap()
                    );

                    repo.insert(record);
                    Log.d("DL_PHONE", "Action record inserted: " + record.actionType);
                }
            }
        };

        // Register the listener with Google Play Services
        Wearable.getDataClient(this).addListener(actionDataListener);
        Log.d(TAG, "Wearable DataClient listener registered");
        Log.d("DL_PHONE", "SERVICE CREATED (via Application class)");
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        // Unregister listener when app terminates
        if (actionDataListener != null) {
            Wearable.getDataClient(this).removeListener(actionDataListener);
        }
    }
}

