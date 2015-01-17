package as.com.au.common.model;

/**
 * Created by Anita on 16/01/2015.
 */
public class Stop {

    public static enum TransportType {
        Train("train"), Tram("tram"), Bus("bus"), Unknown(""), ;

        String desc;
        TransportType(String desc) {
            this.desc = desc;
        }

        public int getIndex() {
            return ordinal();
        }

        @Override
        public String toString() {
            return desc;
        }
    }

    private int stopId;
    private String distance;
    private double lon;
    private String locationName;
    private String transportType;
    private String suburb;

    public Stop() {}
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

    public int getStopId() {
        return stopId;
    }

    public String getDistance() {
        return distance;
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
            if(type.desc.equals(this.transportType)) {
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
