package as.com.au.ptvwear;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.wearable.Wearable;

import org.joda.time.DateTime;
import org.joda.time.Minutes;
import org.joda.time.format.DateTimeFormat;

import java.util.List;

import as.com.au.common.Const;
import as.com.au.common.DataLayerClient;
import as.com.au.common.model.Departure;
import de.greenrobot.event.EventBus;

public class TimetableActivity extends Activity {

    TextView textView;
    TextView durationTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        textView = (TextView) findViewById(R.id.text_view);
        durationTextView = (TextView)findViewById(R.id.duration_tv);

        String faveId = getIntent().getExtras().getString(Const.EXTRA_FAVE_ID);

        Wearable.MessageApi.sendMessage(DataLayerClient.getInstance().getClient(), DataLayerClient.getInstance().getNodeId(),
                Const.PATH_FETCH_DEPARTURE,
                String.valueOf(faveId).getBytes());
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
        List<Departure> departures = event.item;

        if(!departures.isEmpty()) {
            Departure dep = departures.get(0);

            DateTime time = dep.getTime();
            Minutes waitTimeInMins = Minutes.minutesBetween(new DateTime(), time);

            durationTextView.setText(String.format("(in %s mins)",
                    String.valueOf(waitTimeInMins.getMinutes())));
            textView.setText(DateTimeFormat.forStyle("MS").print(dep.getTime()));
        }
    }
}
