package as.com.au.common;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataMapItem;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.reflect.TypeToken;

import java.util.List;
import java.util.concurrent.TimeUnit;

import as.com.au.common.model.Departure;
import as.com.au.common.model.FaveStop;
import de.greenrobot.event.EventBus;

/**
 * Created by Anita on 17/01/2015.
 */
public class DataLayerClient implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, DataApi.DataListener, MessageApi.MessageListener,
        NodeApi.NodeListener {

    private static final String TAG = "DataLayerClient";

    protected static DataLayerClient instance;
    protected static Context context;
    protected GoogleApiClient mGoogleApiClient;
    protected boolean isConnected;

    private String nodeId;

    protected DataLayerClient(Context ctx) {
        context = ctx;
    }

    public static void init(Context context) {
        if(instance == null) {
            instance = new DataLayerClient(context);
        }
    }

    // default instance
    public static DataLayerClient getInstance() {
        if(instance == null) {
            throw new IllegalStateException("DataLayerClient hasn't been initialised!");
        }
        return instance;
    }

    public void connect() {
        if (isConnected) {
            return;
        }
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    public boolean isConnected() {
        return isConnected && nodeId != null;
    }

    public boolean reconnect() {
        if (!mGoogleApiClient.isConnected()) {
            ConnectionResult connectionResult = mGoogleApiClient
                    .blockingConnect(30, TimeUnit.SECONDS);
            return connectionResult.isSuccess();
        }
        return true;
    }

    public GoogleApiClient getClient() {
        return mGoogleApiClient;
    }

    public void getNodes() {

        final PendingResult<NodeApi.GetConnectedNodesResult> pendingResult =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient);
        pendingResult.setResultCallback(new ResultCallback<NodeApi.GetConnectedNodesResult>() {

            @Override
            public void onResult(NodeApi.GetConnectedNodesResult nodesResult) {
                nodeId = null;
                ConnectionStateUpdatedEvent event = null;

                if (nodesResult != null) {
                    List<Node> nodes = nodesResult.getNodes();

                    // get first node
                    if (!nodes.isEmpty()) {
                        nodeId = nodes.get(0).getId();
                    }
                    event = new ConnectionStateUpdatedEvent(true, nodeId);
                }
                if(event == null) {
                    event = new ConnectionStateUpdatedEvent(false, null);
                }
                EventBus.getDefault().post(event);
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        isConnected = false;
        // TODO reconnect?
    }

    @Override
    public void onConnected(Bundle bundle) {
        isConnected = true;
        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.NodeApi.addListener(mGoogleApiClient, this);

        getNodes();
    }

    @Override
    public void onConnectionSuspended(int i) {
        isConnected = false;
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();

        for (DataEvent event : events) {
            if (event.getType() == DataEvent.TYPE_CHANGED) {
                Log.d(TAG, "Type changed");

                String path = event.getDataItem().getUri().getPath();
                DataMapItem dataMapItem = DataMapItem.fromDataItem(event.getDataItem());

                List<?> items = null;

                // favourite list
                if(path.equals(Const.PATH_FAVOURITES)) {
                    String dataStr = dataMapItem.getDataMap().get(Const.KEY_FAVOURITES);
                    items = new JSONSerializer<FaveStop>().serialize(dataStr, new TypeToken<List<FaveStop>>(){}.getType());

                } else if(path.equals(Const.PATH_DEPARTURE)) {
                    String dataStr = dataMapItem.getDataMap().get(Const.KEY_DEPARTURE);
                    items = new JSONSerializer<Departure>().serialize(dataStr, new TypeToken<List<Departure>>(){}.getType());
                }

                // broadcast items
                if(items != null) {
                    EventBus.getDefault().post(new DataItemChangedEvent(items));
                }
            } else if (event.getType() == DataEvent.TYPE_DELETED) {
                Log.d(TAG, "Type deleted");
            } else {
                Log.d(TAG, "Type unknown");
            }
        }
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d(TAG, "onMessageReceived: " + messageEvent);

        processMessage(messageEvent);
//        EventBus.getDefault().post(new MessageReceivedEvent(messageEvent.getPath(), new String(messageEvent.getData())));
    }

    @Override
    public void onPeerConnected(Node node) {

    }

    @Override
    public void onPeerDisconnected(Node node) {
        // TODO
    }

    public String getNodeId() {
        return nodeId;
    }

    public void processMessage(MessageEvent event) {
        // do nothing
    }

    public static class DataItemChangedEvent<T> {
        public T item;
        public DataItemChangedEvent(T item) {
            this.item = item;
        }
    }

    public static class ConnectionStateUpdatedEvent {
        public String nodeId;
        public boolean connected;

        public ConnectionStateUpdatedEvent(boolean connected, String nodeId) {
            this.connected = connected;
            this.nodeId = nodeId;
        }
    }

    public static class MessageReceivedEvent {
        public String path;
        public String payload;

        public MessageReceivedEvent(String path, String payload) {
            this.path = path;
            this.payload = payload;
        }
    }
}
