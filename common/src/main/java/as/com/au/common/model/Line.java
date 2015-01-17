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

    @Override
    public String toString() {
        return new StringBuffer().append(lineName).append(" towards ").append(directionName).toString();
    }

}
