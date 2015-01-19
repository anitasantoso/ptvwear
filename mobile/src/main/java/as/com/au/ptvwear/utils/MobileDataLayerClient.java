package as.com.au.ptvwear.utils;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import as.com.au.common.Const;
import as.com.au.common.DataLayerClient;
import as.com.au.common.JSONSerializer;
import as.com.au.common.model.Departure;
import as.com.au.common.model.FaveStop;
import as.com.au.ptvwear.service.NetworkService;
import as.com.au.ptvwear.service.ResponseHandler;

/**
 * Created by Anita on 19/01/2015.
 */
public class MobileDataLayerClient extends DataLayerClient {

    public static final String TAG = "MobileDataLayerClient";
    FaveMgr faveMgr;
    int count = 0;

    public static void init(Context context) {
        if (instance == null) {
            instance = new MobileDataLayerClient(context);
        }
    }

    private MobileDataLayerClient(Context context) {
        super(context);
        faveMgr = FaveMgr_.getInstance_(context);
    }

    @Override
    public void processMessage(MessageEvent event) {

        String payload = new String(event.getData());
        String path = event.getPath();

        // fetch favourite list
        if (path.equals(Const.PATH_FETCH_FAVOURITES)) {

            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(Const.PATH_FAVOURITES);
            putDataMapRequest.getDataMap().putString(Const.KEY_FAVOURITES, faveMgr.favesAsJsonString());
            putDataMapRequest.getDataMap().putInt(Const.KEY_COUNT, count++); // trigger changes

            PutDataRequest request = putDataMapRequest.asPutDataRequest();
            Wearable.DataApi.putDataItem(DataLayerClient.getInstance().getClient(), request);
        }
        // get next departure
        else if (path.equals(Const.PATH_FETCH_DEPARTURE)) {

            // favourite id not connected
            if (payload == null) {
                return;
            }

            String faveId = payload;
            FaveStop fave = faveMgr.faveById(faveId);
            if (fave == null) {
                return;
            }

            // get next three departures
            NetworkService.getInstance().getNextDeparture(fave.getStop(),
                    fave.getLine().getLineId(),
                    fave.getLine().getDirectionId(), 3,
                    new ResponseHandler<List<Departure>>() {

                        @Override
                        public void onSuccess(List<Departure> result) {

                            // now send message back to watch
                            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(Const.PATH_DEPARTURE);
                            putDataMapRequest.getDataMap().putString(Const.KEY_DEPARTURE, new JSONSerializer<Departure>()
                                    .deserialize(result, new TypeToken<List<Departure>>() {
                                    }.getType()));
                            putDataMapRequest.getDataMap().putInt(Const.KEY_COUNT, count++); // trigger changes?

                            PutDataRequest request = putDataMapRequest.asPutDataRequest();
                            Wearable.DataApi.putDataItem(DataLayerClient.getInstance().getClient(), request);
                        }

                        @Override
                        public void onError(String error) {
                            Log.e(TAG, error);
                        }
                    });
        }
    }
}
