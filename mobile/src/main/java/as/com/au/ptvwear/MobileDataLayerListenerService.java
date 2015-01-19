package as.com.au.ptvwear;

import android.content.Intent;

import as.com.au.common.DataLayerListenerService;

/**
 * Created by Anita on 19/01/2015.
 */
public class MobileDataLayerListenerService extends DataLayerListenerService {

    public void launchMainActivity() {
        Intent startIntent = new Intent(this, MainActivity.class);
        startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startIntent);
    }
}
