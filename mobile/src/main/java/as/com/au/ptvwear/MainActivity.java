package as.com.au.ptvwear;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.gson.reflect.TypeToken;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import java.util.List;

import as.com.au.common.Const;
import as.com.au.common.DataLayerClient;
import as.com.au.common.JSONSerializer;
import as.com.au.common.model.Departure;
import as.com.au.common.model.FaveStop;
import as.com.au.common.model.Stop;
import as.com.au.ptvwear.adapter.FaveStopsListAdapter;
import as.com.au.ptvwear.service.NetworkService;
import as.com.au.ptvwear.service.ResponseHandler;
import as.com.au.ptvwear.utils.AlertUtils;
import as.com.au.ptvwear.utils.FaveMgr;
import de.greenrobot.event.EventBus;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity implements FaveStopsListAdapter.DatasetChangedDelegate<Stop> {

    private static final String TAG = "MainActivity";
    private static final int REQUEST_VIEW_STOPS = 100;
    private static final int REQUEST_RESOLVE_ERROR = 1000;

    @ViewById(R.id.fave_list_view)
    ListView faveListView;

    @ViewById(R.id.tv_empty)
    TextView emptyTextView;

    @Bean
    FaveMgr faveMgr;

    FaveStopsListAdapter listAdapter;
    DataLayerClient client;

    @AfterInject
    void init() {
        client = new DataLayerClient(this);
        client.connect();
    }

    @AfterViews
    void initViews() {
        faveListView.setAdapter(listAdapter = new FaveStopsListAdapter(this));
        listAdapter.setDelegate(this);
        faveListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FaveStop fave = (FaveStop) listAdapter.getItem(position);
                NetworkService.getInstance().getBroadDepartures(fave.getStop(),
                        new ResponseHandler<List<Departure>>() {

                            @Override
                            public void onSuccess(List<Departure> result) {

                                StringBuffer desc = new StringBuffer();
                                for (Departure dep : result) {
                                    desc.append(dep.toString()).append("\n\n");
                                }
                                AlertUtils.showError(MainActivity.this, desc.toString());
                            }

                            @Override
                            public void onError(String error) {
                                AlertUtils.showError(MainActivity.this, error);
                            }
                        });
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        List<FaveStop> faves = faveMgr.getFaves();
        boolean isEmpty = faves.isEmpty();
        emptyTextView.setVisibility(isEmpty ? View.VISIBLE : View.GONE);
        faveListView.setVisibility(isEmpty ? View.GONE : View.VISIBLE);

        if (!isEmpty) {
            reloadListView();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.action_add) {
            startActivityForResult(new Intent(this, StopsActivity_.class), REQUEST_VIEW_STOPS);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void itemRemoved(Stop item) {
        reloadListView();
    }

    @Override
    public void itemAdded(Stop item) {
        reloadListView();
    }

    private void reloadListView() {
        listAdapter.setItems(faveMgr.getFaves());
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_VIEW_STOPS && resultCode == Activity.RESULT_OK) {
            reloadListView();
        }
    }

    static int count = 0;

    // TODO move this into service class
    public void onEventMainThread(DataLayerClient.MessageReceivedEvent event) {

        // show favourites
        if (event.path.equals(Const.PATH_FETCH_FAVOURITES)) {

            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(Const.PATH_FAVOURITES);
            putDataMapRequest.getDataMap().putString(Const.KEY_FAVOURITES, faveMgr.favesAsJsonString());
            putDataMapRequest.getDataMap().putInt(Const.KEY_COUNT, count++); // trigger changes

            PutDataRequest request = putDataMapRequest.asPutDataRequest();
            Wearable.DataApi.putDataItem(client.getClient(), request);

        } else if (event.path.equals(Const.PATH_FETCH_DEPARTURE) && event.payload != null) {

            String faveId = event.payload;
            FaveStop fave = faveMgr.faveById(faveId);
            if (fave == null) {
                return;
            }
            NetworkService.getInstance().getNextDeparture(fave.getStop(), fave.getLine().getLineId(), fave.getLine().getDirectionId(),
                    new ResponseHandler<List<Departure>>() {
                        @Override
                        public void onSuccess(List<Departure> result) {
                            // now back at the watch

                            PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(Const.PATH_DEPARTURE);
                            putDataMapRequest.getDataMap().putString(Const.KEY_DEPARTURE, new JSONSerializer<Departure>()
                                    .deserialize(result, new TypeToken<List<Departure>>() {
                                    }.getType()));
                            putDataMapRequest.getDataMap().putInt(Const.KEY_COUNT, count++); // trigger changes?

                            PutDataRequest request = putDataMapRequest.asPutDataRequest();
                            Wearable.DataApi.putDataItem(client.getClient(), request);
                        }

                        @Override
                        public void onError(String error) {

                        }
                    });
        }
    }
}
