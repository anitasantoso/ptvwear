package as.com.au.ptvwear.model;

import org.joda.time.DateTime;

/**
 * Created by Anita on 16/01/2015.
 */
public class Departure {

    /***
     * {"values":[{"platform":{"stop":{"stop_id":1013,"distance":0,"lon":144.993515,"location_name":"Balaclava","transport_type":"train","suburb":"Balaclava","lat":-37.8694878},"direction":{"line":{"line_number":"Sandringham","line_name":"Sandringham","line_id":12,"transport_type":"train"},"direction_name":"City (Flinders Street)","direction_id":0,"linedir_id":28},"realtime_id":0},"flags":"","time_realtime_utc":null,"run":{"num_skipped":0,"transport_type":"train","run_id":32611,"destination_name":"Flinders Street","destination_id":1071},"time_timetable_utc":"2015-01-16T09:38:00Z"},{"platform":{"stop":{"stop_id":1013,"distance":0,"lon":144.993515,"location_name":"Balaclava","transport_type":"train","suburb":"Balaclava","lat":-37.8694878},"direction":{"line":{"line_number":"Sandringham","line_name":"Sandringham","line_id":12,"transport_type":"train"},"direction_name":"Sandringham","direction_id":11,"linedir_id":43},"realtime_id":0},"flags":"","time_realtime_utc":null,"run":{"num_skipped":0,"transport_type":"train","run_id":32415,"destination_name":"Sandringham","destination_id":1173},"time_timetable_utc":"2015-01-16T09:44:00Z"}]}
     */
    private String directionName;
    private DateTime time;

    public Departure(String directionName, DateTime time) {
        this.directionName = directionName;
        this.time = time;
    }

    public String getDirectionName() {
        return directionName;
    }

    public DateTime getTime() {
        return time;
    }

    @Override
    public String toString() {
        return new StringBuffer().append(getDirectionName()).append(" - ").append(getTime()).toString();
    }
}
