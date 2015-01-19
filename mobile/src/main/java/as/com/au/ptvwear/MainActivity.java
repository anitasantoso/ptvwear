package as.com.au.ptvwear;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;
import com.mobeta.android.dslv.DragSortListView;

import java.util.List;

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

    @ViewById(R.id.fave_list_view)
    DragSortListView faveListView;

    @ViewById(R.id.tv_empty)
    TextView emptyTextView;

    @Bean
    FaveMgr faveMgr;

    FaveStopsListAdapter listAdapter;

    @AfterViews
    void initViews() {

        getSupportActionBar().setHomeButtonEnabled(true);

        faveListView.setAdapter(listAdapter = new FaveStopsListAdapter(this));
        listAdapter.setDelegate(this);
        faveListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FaveStop fave = (FaveStop) listAdapter.getItem(position);
                EventBus.getDefault().postSticky(fave);

                Intent intent = new Intent(MainActivity.this, TimetableActivity_.class);
                startActivity(intent);
            }
        });
        faveListView.setDropListener(new DragSortListView.DropListener() {
            @Override
            public void drop(int from, int to) {
                listAdapter.swapItem(from, to);
                listAdapter.notifyDataSetChanged();
                faveMgr.setFaves(listAdapter.getItems());
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        performHealthCheck();
    }

    @Override
    public void onStop() {
//        EventBus.getDefault().unregister(this);
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
        switch (id) {
            case R.id.action_add:
                startActivityForResult(new Intent(this, StopsActivity_.class), REQUEST_VIEW_STOPS);
                break;
            case R.id.action_watch:
                // TODO
                break;
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

    private void performHealthCheck() {
        NetworkService.getInstance().healthCheck(new ResponseHandler<Void>() {
            @Override
            public void onSuccess(Void result) {
                Log.d(TAG, "Healthcheck status is normal");
            }

            @Override
            public void onError(String error) {
                AlertUtils.showErrorDialog(MainActivity.this, error);
            }
        });
    }
}
