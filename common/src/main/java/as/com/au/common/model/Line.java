package as.com.au.common.model;

/**
 * Created by Anita on 17/01/2015.
 */
public class Line {

    private String directionName;
    private String lineName;
    private String directionId;
    private String lineId;

    public Line() {}

    public Line(String lineId, String lineName, String directionId, String directionName) {
        this.directionName = directionName;
        this.lineName = lineName;
        this.directionId = directionId;
        this.lineId = lineId;
    }

    public String getDirectionName() {
        return directionName;
    }

    public String getLineName() {
        return lineName;
    }

    public String getDirectionId() {
        return directionId;
    }

    public String getLineId() {
        return lineId;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Line) {
            Line other = (Line)o;
            return getLineId().equals(other.getLineId()) && getDirectionId().equals(other.getDirectionId());
        }
        return false;
    }

    @Override
    public String toString() {
        return new StringBuffer().append(lineName).append(" towards ").append(directionName).toString();
    }
}
