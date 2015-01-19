package as.com.au.ptvwear.utils;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by Anita on 19/01/2015.
 */
public class LocationService {

    static LocationListener listener;
    static Location lastKnownLocation;
    static LocationManager locMgr;

    // TODO show spinner while loading fine location
    // TODO need stop location updates
    // locMgr.removeUpdates(listener);

    public static void init(Context context) {

        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                lastKnownLocation = location;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };

        int intervalInMillis = 60*60;
        int distanceInMeter = 10;

        locMgr = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        locMgr.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, intervalInMillis, distanceInMeter, listener);

        // TODO
        // locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, listener);
    }

    public static Location getLastKnownLocation() {
        return lastKnownLocation;
    }

    public class LocationUpdateEvent {
        Location location;
        public LocationUpdateEvent(Location location) {
            this.location = location;
        }
    }
}
