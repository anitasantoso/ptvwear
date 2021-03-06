package as.com.au.ptvwear;

import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import java.util.List;

import as.com.au.common.model.Departure;
import as.com.au.common.model.Stop;
import as.com.au.ptvwear.adapter.LinesListAdapter;
import as.com.au.ptvwear.network.NetworkService;
import as.com.au.ptvwear.network.ResponseHandler;
import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_stop_details)
public class StopDetailsActivity extends BackNavActivity {

    @ViewById(R.id.departures_list_view)
    ListView depListView;

    LinesListAdapter listAdapter;

    @AfterViews
    void initView() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Stop stop = EventBus.getDefault().getStickyEvent(Stop.class);
        EventBus.getDefault().removeStickyEvent(Stop.class);

        depListView.setAdapter(listAdapter = new LinesListAdapter(this));

        // now get departures
        NetworkService.getInstance().getBroadDepartures(stop,
                new ResponseHandler<List<Departure>>() {
                    @Override
                    public void onSuccess(List<Departure> result) {
                        listAdapter.setItems(result);
                        listAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(String error) {

                    }
                });
    }
}
