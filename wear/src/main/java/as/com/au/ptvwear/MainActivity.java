package as.com.au.ptvwear;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.view.WearableListView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.wearable.Wearable;

import java.util.List;

import as.com.au.common.Const;
import as.com.au.common.DataLayerClient;
import as.com.au.common.model.Stop;
import de.greenrobot.event.EventBus;

public class MainActivity extends Activity {

    TextView statusTextView;
    WearableListView stopsListView;
    FavouriteListAdapter listAdapter;

    Button button;
    DataLayerClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        statusTextView = (TextView) findViewById(R.id.status_tv);
        stopsListView = (WearableListView)findViewById(R.id.wearable_list);
        button = (Button)findViewById(R.id.button);

        stopsListView.setClickListener(new WearableListView.ClickListener() {
            @Override
            public void onClick(WearableListView.ViewHolder viewHolder) {
                int position = (int)viewHolder.itemView.getTag();
                if(listAdapter != null) {
                    Stop stop = listAdapter.items.get(position);
                    Intent intent = new Intent(MainActivity.this, TimetableActivity.class);
                    intent.putExtra(Const.EXTRA_STOP_ID, stop.getStopId());
                    startActivity(intent);
                }
            }

            @Override
            public void onTopEmptyRegionClick() {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nodeId = client.getNodeId();

                if (nodeId != null) {
                    Wearable.MessageApi.sendMessage(client.getClient(), nodeId, Const.PATH_FETCH_FAVOURITES,
                            null);
                } else {
                    Toast.makeText(MainActivity.this, "Could not find connected node", Toast.LENGTH_SHORT).show();
                }
            }
        });

        DataLayerClient.init(this);
        client = DataLayerClient.getInstance();
        client.connect();
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(DataLayerClient.NodeStateUpdateEvent event) {
        statusTextView.setText(event.found? "Connected" : "No Connection");
        if(!event.found) {
            Toast.makeText(MainActivity.this, "No connection to device", Toast.LENGTH_SHORT).show();
            return;
        }

        String nodeId = event.nodeId;
        Wearable.MessageApi.sendMessage(client.getClient(), nodeId, Const.PATH_FETCH_FAVOURITES,
                null);
    }

    public void onEventMainThread(final DataLayerClient.DataItemChangedEvent<List<Stop>> event) {
        statusTextView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        stopsListView.setVisibility(View.VISIBLE);

        if(listAdapter == null) {
            listAdapter = (new FavouriteListAdapter(this, event.item));
            stopsListView.setAdapter(listAdapter);
        } else {
            listAdapter.setItems(event.item);
            listAdapter.notifyDataSetChanged();
        }
    }
}
