package as.com.au.ptvwear;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.GridViewPager;

import com.google.android.gms.wearable.Wearable;

import java.util.List;

import as.com.au.common.Const;
import as.com.au.common.DataLayerClient;
import as.com.au.common.model.Departure;
import de.greenrobot.event.EventBus;

public class TimetablePagerActivity extends Activity {

    GridViewPager pager;
    GridPagerAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_pager);
        pager = (GridViewPager)findViewById(R.id.pager);
        pager.setAdapter(pagerAdapter = new GridPagerAdapter(this, getFragmentManager()));
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
            pagerAdapter.setItems(departures);
            pagerAdapter.notifyDataSetChanged();
        }
    }
}
