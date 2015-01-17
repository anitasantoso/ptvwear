package as.com.au.ptvwear;

import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ViewById;

import java.util.List;

import as.com.au.ptvwear.model.Stop;
import as.com.au.ptvwear.service.NetworkService;
import as.com.au.ptvwear.service.ResponseHandler;

@EActivity(R.layout.activity_main)
public class MainActivity extends ActionBarActivity {

    @ViewById(R.id.stops_list_view)
    ListView stopsListView;

    @AfterViews
    void initViews() {}

    @Override
    public void onResume() {
        super.onResume();

        // TODO Show spinner

        NetworkService.getInstance().getNearbyStops(-37.865300, 144.994785, new ResponseHandler<List<Stop>>() {
            @Override
            public void onSuccess(List<Stop> result) {
                // update list
            }

            @Override
            public void onError(String error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
