package as.com.au.ptvwear;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.wearable.Wearable;

import java.util.List;

import as.com.au.common.Const;
import as.com.au.common.DataLayerClient;
import as.com.au.common.model.Departure;
import de.greenrobot.event.EventBus;

public class TimetableActivity extends Activity {

    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        textView = (TextView) findViewById(R.id.text_view);
        int stopId = getIntent().getExtras().getInt(Const.EXTRA_STOP_ID);

        Wearable.MessageApi.sendMessage(DataLayerClient.getInstance().getClient(), DataLayerClient.getInstance().getNodeId(),
                Const.PATH_FETCH_DEPARTURE,
                String.valueOf(stopId).getBytes());
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

    public void onEventMainThread(final DataLayerClient.DataItemChangedEvent<List<Departure>> event) {
        List<Departure> times = event.item;
        if(!times.isEmpty()) {
            textView.setText(times.get(0).toString());
        }
    }
}
