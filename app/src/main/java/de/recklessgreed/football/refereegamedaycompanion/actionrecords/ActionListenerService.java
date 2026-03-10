package de.recklessgreed.football.refereegamedaycompanion.actionrecords;

import android.util.Log;

import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.WearableListenerService;

public class ActionListenerService extends WearableListenerService {


    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("DL_PHONE", "SERVICE CREATED");
    }


    @Override
    public void onDataChanged(DataEventBuffer events) {
        Log.d("DL_PHONE", "onDataChanged count=" + events.getCount());
        ActionRepository repo = new ActionRepository(this);

        for (DataEvent event : events) {

            Log.d("DL_PHONE", "EVENT path=" +
                    event.getDataItem().getUri().getPath());

            if (event.getType() != DataEvent.TYPE_CHANGED) continue;

            String path = event.getDataItem().getUri().getPath();
            if (!path.startsWith(ActionContract.PATH_ACTIONS)) continue;

            ActionRecord record =
                    ActionRecord.fromDataMap(
                            DataMapItem.fromDataItem(event.getDataItem()).getDataMap()
                    );

            repo.insert(record);
        }
    }
}
