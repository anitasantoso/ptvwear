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

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import java.util.List;

import as.com.au.ptvwear.adapter.StopsListAdapter;
import as.com.au.ptvwear.model.Departure;
import as.com.au.ptvwear.model.Stop;
import as.com.au.ptvwear.service.NetworkService;
import as.com.au.ptvwear.service.ResponseHandler;
import as.com.au.ptvwear.utils.AlertUtils;
import as.com.au.ptvwear.utils.FaveMgr;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity implements StopsListAdapter.DatasetChangedDelegate<Stop> {

    public static final int REQ_CODE_VIEW_STOPS = 100;

    @ViewById(R.id.fave_list_view)
    ListView faveListView;

    @ViewById(R.id.tv_empty)
    TextView emptyTextView;

    @Bean
    FaveMgr faveMgr;

    StopsListAdapter listAdapter;

    @AfterViews
    void initViews() {
        faveListView.setAdapter(listAdapter = new StopsListAdapter(this));
        listAdapter.setDelegate(this);
        faveListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Stop stop = (Stop)listAdapter.getItem(position);
                NetworkService.getInstance().getNextDeparture(stop, new ResponseHandler<List<Departure>>() {
                    
                    @Override
                    public void onSuccess(List<Departure> result) {

                        StringBuffer desc = new StringBuffer();
                        for(Departure dep : result) {
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
    public void onResume() {
        super.onResume();

        List<Stop> stops = faveMgr.getFaves();
        boolean isEmpty = stops.isEmpty();
        emptyTextView.setVisibility(isEmpty? View.VISIBLE : View.GONE);
        faveListView.setVisibility(isEmpty? View.GONE : View.VISIBLE);

        if(!isEmpty) {
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
        if(id == R.id.action_add) {
            startActivityForResult(new Intent(this, StopsActivity_.class), REQ_CODE_VIEW_STOPS);
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
        listAdapter.setStops(faveMgr.getFaves());
        listAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        if(requestCode == REQ_CODE_VIEW_STOPS && resultCode == Activity.RESULT_OK) {
            reloadListView();
        }
    }
}
