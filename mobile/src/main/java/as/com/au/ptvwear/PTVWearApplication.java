package as.com.au.ptvwear;

import android.app.Application;
import android.content.Context;

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
    }

    public static Context getContext() {
        return instance;
    }
}
