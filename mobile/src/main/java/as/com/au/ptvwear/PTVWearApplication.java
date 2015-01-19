package as.com.au.ptvwear;

import android.app.Application;
import android.content.Context;

import as.com.au.ptvwear.utils.LocationService;
import as.com.au.ptvwear.utils.MobileDataLayerClient;

/**
 * Created by Anita on 19/01/2015.
 */
public class PTVWearApplication extends Application {

    private static PTVWearApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        MobileDataLayerClient.init(this);
        MobileDataLayerClient.getInstance().connect();

        LocationService.init(this);
    }

    public static Context getContext() {
        return instance;
    }
}
