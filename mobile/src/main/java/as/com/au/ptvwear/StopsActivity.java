package as.com.au.ptvwear;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import as.com.au.common.model.Stop;
import as.com.au.ptvwear.adapter.StopsListAdapter;
import as.com.au.ptvwear.service.NetworkService;
import as.com.au.ptvwear.service.ResponseHandler;
import as.com.au.ptvwear.utils.AlertUtils;
import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_stops)
public class StopsActivity extends ActionBarActivity {

    @ViewById(R.id.stops_list_view)
    ListView stopsListView;

    StopsListAdapter listAdapter;

    @AfterViews
    void initViews() {
        stopsListView.setAdapter(listAdapter = new StopsListAdapter(this));
        stopsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // pass Stop object
                EventBus.getDefault().postSticky(listAdapter.getItem(position));

                Intent intent = new Intent(StopsActivity.this, StopDetailsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        // TODO location manager
        // TODO Show spinner
        // home -37.865300, 144.994785
        // flinders st -37.818178, 144.966880
        NetworkService.getInstance().getNearbyStops(-37.818178, 144.966880, new ResponseHandler<List<Stop>>() {
            @Override
            public void onSuccess(List<Stop> result) {

                Collections.sort(result, new Comparator<Stop>() {
                    @Override
                    public int compare(Stop lhs, Stop rhs) {
                        return lhs.getTransportType() == Stop.TransportType.Train ? -1 :
                                lhs.getTransportType() == Stop.TransportType.Tram ? 0 : 1;
                    }
                });
                // update list
                listAdapter.setStops(result);
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String error) {
                AlertUtils.showError(StopsActivity.this, error);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stops, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if(id == R.id.action_done) {
            setResult(Activity.RESULT_OK);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
