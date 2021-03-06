package as.com.au.common.model;

/**
 * Created by Anita on 16/01/2015.
 */
public class Stop {

    /**
     *  {
     "type": "stop",
     "result": {
     "stop_id": 3166,
     "distance": 0.00006334438,
     "lon": 144.988434,
     "location_name": "St Kilda Primary School/Brighton Rd #36 ",
     "transport_type": "tram",
     "suburb": "Balaclava",
     "lat": -37.87011
     }
     }
     */
    private int stopId;
    private String distance;
    private double lat;
    private double lon;
    private String locationName;
    private String transportType;
    private String suburb;

    public Stop() {}

    public int getStopId() {
        return stopId;
    }

    public String getDistance() {
        return distance;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getSuburb() {
        return suburb;
    }

    public TransportType getTransportType() {
        for(TransportType type : TransportType.values()) {
            if(type.identifier.equals(this.transportType)) {
                return type;
            }
        }
        return TransportType.Unknown;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Stop && getStopId() == ((Stop)o).getStopId();
    }

    @Override
    public String toString() {
        return new StringBuffer().append(getLocationName()).append(" - ").append(getSuburb()).toString();
    }
}
