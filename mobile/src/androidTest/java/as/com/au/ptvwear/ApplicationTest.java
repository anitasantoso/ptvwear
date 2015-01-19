package as.com.au.ptvwear;

import android.test.InstrumentationTestCase;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import as.com.au.common.model.Departure;
import as.com.au.common.model.Stop;
import as.com.au.ptvwear.service.NetworkService;
import as.com.au.ptvwear.service.ResponseHandler;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends InstrumentationTestCase {

    public static final String TAG = "ApplicationTest";
    int stopCount;

    public void testHealth() throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                NetworkService.getInstance().healthCheck(new ResponseHandler<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        latch.countDown();
                        assertTrue(true);
                    }

                    @Override
                    public void onError(String error) {
                        latch.countDown();
                        fail();
                    }
                });
            }
        });
        await(latch);
    }

    public void testNearbyStops() throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {

                // current location
                // home -37.865300, 144.994785
                // flinders st -37.818178, 144.966880
                NetworkService.getInstance().getNearbyStops(-37.818178, 144.966880, new ResponseHandler<List<Stop>>() {

                    @Override
                    public void onSuccess(final List<Stop> stops) {
                        assertFalse(stops.isEmpty());
                        stopCount = 0;

                        for (Stop stop : stops) {
                            Log.d(TAG, stop.toString());

                            NetworkService.getInstance().getBroadDepartures(stop, new ResponseHandler<List<Departure>>() {
                                @Override
                                public void onSuccess(List<Departure> result) {

                                    stopCount++;
                                    for (Departure dep : result) {
                                        Log.d(TAG, dep.toString());
                                    }
                                    if (stopCount == stops.size()) {
                                        latch.countDown();
                                    }
                                }

                                @Override
                                public void onError(String error) {
                                    latch.countDown();
                                    fail();
                                }
                            });
                        }
                    }

                    @Override
                    public void onError(String error) {
                        latch.countDown();
                        fail();
                    }
                });
            }
        });
        await(latch);
    }

    public void testSearch() throws Throwable {
        final CountDownLatch latch = new CountDownLatch(1);

        runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                NetworkService.getInstance().search("Balaclava", new ResponseHandler<List<Stop>>() {
                    @Override
                    public void onSuccess(List<Stop> result) {
                        latch.countDown();
                    }

                    @Override
                    public void onError(String error) {
                        latch.countDown();
                        fail();
                    }
                });
            }
        });

        await(latch);
    }

    private void await(final CountDownLatch latch) {
        try {
            latch.await(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}