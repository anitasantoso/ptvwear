package as.com.au.common.model;

/**
 * Created by Anita on 17/01/2015.
 */
public class FaveStop {

    private String faveId;
    private Stop stop;
    private Line line;
    private String title;

    public FaveStop() {}

    public FaveStop(Stop stop, Line line) {
        this.faveId = java.util.UUID.randomUUID().toString(); // auto gen
        this.stop = stop;
        this.line = line;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFaveId() {
        return faveId;
    }

    public Stop getStop() {
        return stop;
    }

    public Line getLine() {
        return line;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof FaveStop && getFaveId().equals(((FaveStop)o).getFaveId());
    }
}
