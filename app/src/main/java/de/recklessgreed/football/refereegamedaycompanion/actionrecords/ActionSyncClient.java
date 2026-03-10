package de.recklessgreed.football.refereegamedaycompanion.actionrecords;

import android.content.Context;

import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.UUID;

public class ActionSyncClient {

    private final Context context;

    public ActionSyncClient(Context context) {
        this.context = context.getApplicationContext();
    }

    public void sendAction(String actionType, int period) {
        ActionRecord record = new ActionRecord();
        record.id = UUID.randomUUID().toString();
        record.timestamp = System.currentTimeMillis();
        record.actionType = actionType;
        record.period = period;

        String path = ActionContract.PATH_ACTIONS + "/" + record.id;

        PutDataMapRequest put = PutDataMapRequest.create(path);
        put.getDataMap().putAll(record.toDataMap());

        PutDataRequest req = put.asPutDataRequest();
        req.setUrgent();

        Wearable.getDataClient(context).putDataItem(req);
    }
}
