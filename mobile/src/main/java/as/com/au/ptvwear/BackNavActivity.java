package as.com.au.ptvwear;

import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;

/**
 * Created by Anita on 19/01/2015.
 */
public class BackNavActivity extends ActionBarActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
