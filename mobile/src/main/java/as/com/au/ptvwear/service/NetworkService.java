package as.com.au.ptvwear.service;

import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.Key;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import as.com.au.common.model.Departure;
import as.com.au.common.model.Line;
import as.com.au.common.model.Stop;
import as.com.au.common.model.TransportType;

/**
 * Created by Anita on 16/01/2015.
 */
public class NetworkService {

    private static final String TAG = "NetworkService";
    private static final String API_KEY = "ca03fb4c-cf30-11e3-8bed-0263a9d0b8a0";
    private static final String DEV_ID = "1000151";

    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String HMAC_SHA1_ALGORITHM = "HmacSHA1";

    private static final String BASE_URL = "http://timetableapi.ptv.vic.gov.au";
    public static final String URI_HEALTHCHECK = "/v2/healthcheck";
    public static final String URI_NEARBY_STOPS = "/v2/nearme/latitude/%s/longitude/%s";
    public static final String URI_DEPARTURE = "/v2/mode/%d/stop/%d/departures/by-destination/limit/%d";
    public static final String URI_NEXT_DEPARTURE = "/v2/mode/%d/line/%s/stop/%d/directionid/%s/departures/all/limit/%d";

    private static NetworkService instance = new NetworkService();
    private AsyncHttpClient client;

    private NetworkService() {
        client = new AsyncHttpClient();
    }

    public static NetworkService getInstance() {
        return instance;
    }

    public void healthCheck(final ResponseHandler<Void> handler) {
        String signedUrl = generateSignedUrl(URI_HEALTHCHECK);

        client.get(signedUrl, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {

                /**
                 * TODO
                 * {
                 "securityTokenOK": true,
                 "clientClockOK": false,
                 "memcacheOK": true,
                 "databaseOK": true
                 }
                 */
                Log.d(TAG, "Success");
                handler.onSuccess(null);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                handler.onError(errorResponse.toString());
            }
        });
    }

    public void getNearbyStops(double lat, double lon, final ResponseHandler<List<Stop>> handler) {
        getNearbyStops(lat, lon, handler, null);
    }

    public void getNearbyStops(double lat, double lon, final ResponseHandler<List<Stop>> handler, final TransportType... transportTypes) {
        String signedUrl = generateSignedUrl(String.format(URI_NEARBY_STOPS, String.valueOf(lat), String.valueOf(lon)));

        client.get(signedUrl, new JsonHttpResponseHandler() {
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                List<Stop> stops = new ArrayList<Stop>();
                List<TransportType> types = Arrays.asList(transportTypes);

                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i).getJSONObject("result");
                        Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).create();
                        Stop stop = gson.fromJson(obj.toString(), Stop.class);

                        // if types are specified, filter result
                        if(types == null || types != null && types.contains(stop.getTransportType())) {
                            stops.add(stop);
                        }
                    }
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
                handler.onSuccess(stops);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                handler.onError(errorResponse.toString());
            }
        });
    }

    private Departure departureFromJSONObject(Stop stop, JSONObject obj) throws JSONException {
        JSONObject dirObj = obj.getJSONObject("platform").getJSONObject("direction");
        String directionName = dirObj.getString("direction_name");
        String directionId = dirObj.getString("direction_id");

        JSONObject lineObj = dirObj.getJSONObject("line");
        String lineName = lineObj.getString("line_name");
        String lineId = lineObj.getString("line_id");
        String timeStr = obj.getString("time_timetable_utc");

        Line line = new Line(lineId, lineName, directionId, directionName);
        return new Departure(stop, line, timeStr);

    }

    // TODO get subsequent departures
    public void getNextDeparture(final Stop stop, String lineId, String directionId, final ResponseHandler<List<Departure>> handler) {
        // public static final String URI_NEXT_DEPARTURE = "/v2/mode/%d/line/%s/stop/%d/directionid/%s/departures/all/limit/%d";

        // limit to one next departure
        String signedUrl = generateSignedUrl(String.format(URI_NEXT_DEPARTURE, stop.getTransportType().getIndex(),
                lineId, stop.getStopId(), directionId, 1));

        client.get(signedUrl, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                List<Departure> departures = new ArrayList<Departure>();
                Departure dep = null;
                try {
                    JSONArray arr = response.getJSONArray("values");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        dep = departureFromJSONObject(stop, obj);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(dep != null) {
                    departures.add(dep);
                }
                handler.onSuccess(departures);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                handler.onError(errorResponse.toString());
            }
        });
    }

    public void getBroadDepartures(final Stop stop, final ResponseHandler<List<Departure>> handler) {

        // limit to one next departure
        String signedUrl = generateSignedUrl(String.format(URI_DEPARTURE, stop.getTransportType().getIndex(), stop.getStopId(), 1));
        client.get(signedUrl, new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                List<Departure> departures = new ArrayList<Departure>();
                try {
                    JSONArray arr = response.getJSONArray("values");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        departures.add(departureFromJSONObject(stop, obj));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                handler.onSuccess(departures);
            }

            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                handler.onError(errorResponse.toString());
            }
        });
    }

    private String generateSignature(final String url) {

        String signature = null;
        StringBuilder urlWithDevId = new StringBuilder();
        urlWithDevId.append(url).append(url.contains("?") ? "&" : "?").append("devid=" + DEV_ID);

        try {
            byte[] keyBytes = API_KEY.getBytes(DEFAULT_ENCODING);
            byte[] uriBytes = urlWithDevId.toString().getBytes(DEFAULT_ENCODING);
            Key signingKey = new SecretKeySpec(keyBytes, HMAC_SHA1_ALGORITHM);

            Mac mac = Mac.getInstance(HMAC_SHA1_ALGORITHM);
            mac.init(signingKey);

            byte[] signatureBytes = mac.doFinal(uriBytes);
            StringBuffer buf = new StringBuffer(signatureBytes.length * 2);
            for (byte signatureByte : signatureBytes) {
                int intVal = signatureByte & 0xff;
                if (intVal < 0x10) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(intVal));
            }
            signature = buf.toString();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return signature != null ? signature.toUpperCase() : null;
    }

    private String generateSignedUrl(final String uri) {
        String signature = generateSignature(uri);
        StringBuffer signedUrl = new StringBuffer(BASE_URL).append(uri)
                .append(uri.contains("?") ? "&" : "?")
                .append("devid=" + DEV_ID)
                .append("&signature=" + signature);
        return signedUrl.toString();
    }
}
